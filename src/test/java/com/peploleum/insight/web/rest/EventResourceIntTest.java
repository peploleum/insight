package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Event;
import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.domain.Location;
import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.repository.EventRepository;
import com.peploleum.insight.repository.search.EventSearchRepository;
import com.peploleum.insight.service.EventService;
import com.peploleum.insight.service.dto.EventDTO;
import com.peploleum.insight.service.mapper.EventMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.EventCriteria;
import com.peploleum.insight.service.EventQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.peploleum.insight.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peploleum.insight.domain.enumeration.EventType;
/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class EventResourceIntTest {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DESCRIPTION = "BBBBBBBBBB";

    private static final EventType DEFAULT_EVENT_TYPE = EventType.POLITICAL;
    private static final EventType UPDATED_EVENT_TYPE = EventType.DOMESTIC;

    private static final String DEFAULT_EVENT_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_COORDINATES = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EventRepository eventRepository;
    @Mock
    private EventRepository eventRepositoryMock;

    @Autowired
    private EventMapper eventMapper;
    
    @Mock
    private EventService eventServiceMock;

    @Autowired
    private EventService eventService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.EventSearchRepositoryMockConfiguration
     */
    @Autowired
    private EventSearchRepository mockEventSearchRepository;

    @Autowired
    private EventQueryService eventQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMockMvc;

    private Event event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventService, eventQueryService);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .eventName(DEFAULT_EVENT_NAME)
            .eventDescription(DEFAULT_EVENT_DESCRIPTION)
            .eventType(DEFAULT_EVENT_TYPE)
            .eventCoordinates(DEFAULT_EVENT_COORDINATES)
            .eventDate(DEFAULT_EVENT_DATE);
        return event;
    }

    @Before
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testEvent.getEventDescription()).isEqualTo(DEFAULT_EVENT_DESCRIPTION);
        assertThat(testEvent.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testEvent.getEventCoordinates()).isEqualTo(DEFAULT_EVENT_COORDINATES);
        assertThat(testEvent.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).save(testEvent);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(0)).save(event);
    }

    @Test
    @Transactional
    public void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventName(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventCoordinates").value(hasItem(DEFAULT_EVENT_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())));
    }
    
    public void getAllEventsWithEagerRelationshipsIsEnabled() throws Exception {
        EventResource eventResource = new EventResource(eventServiceMock, eventQueryService);
        when(eventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEventMockMvc.perform(get("/api/events?eagerload=true"))
        .andExpect(status().isOk());

        verify(eventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        EventResource eventResource = new EventResource(eventServiceMock, eventQueryService);
            when(eventServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEventMockMvc.perform(get("/api/events?eagerload=true"))
        .andExpect(status().isOk());

            verify(eventServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
            .andExpect(jsonPath("$.eventDescription").value(DEFAULT_EVENT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()))
            .andExpect(jsonPath("$.eventCoordinates").value(DEFAULT_EVENT_COORDINATES.toString()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllEventsByEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName equals to DEFAULT_EVENT_NAME
        defaultEventShouldBeFound("eventName.equals=" + DEFAULT_EVENT_NAME);

        // Get all the eventList where eventName equals to UPDATED_EVENT_NAME
        defaultEventShouldNotBeFound("eventName.equals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName in DEFAULT_EVENT_NAME or UPDATED_EVENT_NAME
        defaultEventShouldBeFound("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME);

        // Get all the eventList where eventName equals to UPDATED_EVENT_NAME
        defaultEventShouldNotBeFound("eventName.in=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventName is not null
        defaultEventShouldBeFound("eventName.specified=true");

        // Get all the eventList where eventName is null
        defaultEventShouldNotBeFound("eventName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEventDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDescription equals to DEFAULT_EVENT_DESCRIPTION
        defaultEventShouldBeFound("eventDescription.equals=" + DEFAULT_EVENT_DESCRIPTION);

        // Get all the eventList where eventDescription equals to UPDATED_EVENT_DESCRIPTION
        defaultEventShouldNotBeFound("eventDescription.equals=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEventsByEventDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDescription in DEFAULT_EVENT_DESCRIPTION or UPDATED_EVENT_DESCRIPTION
        defaultEventShouldBeFound("eventDescription.in=" + DEFAULT_EVENT_DESCRIPTION + "," + UPDATED_EVENT_DESCRIPTION);

        // Get all the eventList where eventDescription equals to UPDATED_EVENT_DESCRIPTION
        defaultEventShouldNotBeFound("eventDescription.in=" + UPDATED_EVENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEventsByEventDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDescription is not null
        defaultEventShouldBeFound("eventDescription.specified=true");

        // Get all the eventList where eventDescription is null
        defaultEventShouldNotBeFound("eventDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType equals to DEFAULT_EVENT_TYPE
        defaultEventShouldBeFound("eventType.equals=" + DEFAULT_EVENT_TYPE);

        // Get all the eventList where eventType equals to UPDATED_EVENT_TYPE
        defaultEventShouldNotBeFound("eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllEventsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType in DEFAULT_EVENT_TYPE or UPDATED_EVENT_TYPE
        defaultEventShouldBeFound("eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE);

        // Get all the eventList where eventType equals to UPDATED_EVENT_TYPE
        defaultEventShouldNotBeFound("eventType.in=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllEventsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType is not null
        defaultEventShouldBeFound("eventType.specified=true");

        // Get all the eventList where eventType is null
        defaultEventShouldNotBeFound("eventType.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEventCoordinatesIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventCoordinates equals to DEFAULT_EVENT_COORDINATES
        defaultEventShouldBeFound("eventCoordinates.equals=" + DEFAULT_EVENT_COORDINATES);

        // Get all the eventList where eventCoordinates equals to UPDATED_EVENT_COORDINATES
        defaultEventShouldNotBeFound("eventCoordinates.equals=" + UPDATED_EVENT_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllEventsByEventCoordinatesIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventCoordinates in DEFAULT_EVENT_COORDINATES or UPDATED_EVENT_COORDINATES
        defaultEventShouldBeFound("eventCoordinates.in=" + DEFAULT_EVENT_COORDINATES + "," + UPDATED_EVENT_COORDINATES);

        // Get all the eventList where eventCoordinates equals to UPDATED_EVENT_COORDINATES
        defaultEventShouldNotBeFound("eventCoordinates.in=" + UPDATED_EVENT_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllEventsByEventCoordinatesIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventCoordinates is not null
        defaultEventShouldBeFound("eventCoordinates.specified=true");

        // Get all the eventList where eventCoordinates is null
        defaultEventShouldNotBeFound("eventCoordinates.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate equals to DEFAULT_EVENT_DATE
        defaultEventShouldBeFound("eventDate.equals=" + DEFAULT_EVENT_DATE);

        // Get all the eventList where eventDate equals to UPDATED_EVENT_DATE
        defaultEventShouldNotBeFound("eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate in DEFAULT_EVENT_DATE or UPDATED_EVENT_DATE
        defaultEventShouldBeFound("eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE);

        // Get all the eventList where eventDate equals to UPDATED_EVENT_DATE
        defaultEventShouldNotBeFound("eventDate.in=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventDate is not null
        defaultEventShouldBeFound("eventDate.specified=true");

        // Get all the eventList where eventDate is null
        defaultEventShouldNotBeFound("eventDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        event.addEquipment(equipment);
        eventRepository.saveAndFlush(event);
        Long equipmentId = equipment.getId();

        // Get all the eventList where equipment equals to equipmentId
        defaultEventShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the eventList where equipment equals to equipmentId + 1
        defaultEventShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }


    @Test
    @Transactional
    public void getAllEventsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        event.addLocation(location);
        eventRepository.saveAndFlush(event);
        Long locationId = location.getId();

        // Get all the eventList where location equals to locationId
        defaultEventShouldBeFound("locationId.equals=" + locationId);

        // Get all the eventList where location equals to locationId + 1
        defaultEventShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllEventsByOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        Organisation organisation = OrganisationResourceIntTest.createEntity(em);
        em.persist(organisation);
        em.flush();
        event.addOrganisation(organisation);
        eventRepository.saveAndFlush(event);
        Long organisationId = organisation.getId();

        // Get all the eventList where organisation equals to organisationId
        defaultEventShouldBeFound("organisationId.equals=" + organisationId);

        // Get all the eventList where organisation equals to organisationId + 1
        defaultEventShouldNotBeFound("organisationId.equals=" + (organisationId + 1));
    }


    @Test
    @Transactional
    public void getAllEventsByBiographicsIsEqualToSomething() throws Exception {
        // Initialize the database
        Biographics biographics = BiographicsResourceIntTest.createEntity(em);
        em.persist(biographics);
        em.flush();
        event.addBiographics(biographics);
        eventRepository.saveAndFlush(event);
        Long biographicsId = biographics.getId();

        // Get all the eventList where biographics equals to biographicsId
        defaultEventShouldBeFound("biographicsId.equals=" + biographicsId);

        // Get all the eventList where biographics equals to biographicsId + 1
        defaultEventShouldNotBeFound("biographicsId.equals=" + (biographicsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventCoordinates").value(hasItem(DEFAULT_EVENT_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .eventType(UPDATED_EVENT_TYPE)
            .eventCoordinates(UPDATED_EVENT_COORDINATES)
            .eventDate(UPDATED_EVENT_DATE);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEvent.getEventDescription()).isEqualTo(UPDATED_EVENT_DESCRIPTION);
        assertThat(testEvent.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testEvent.getEventCoordinates()).isEqualTo(UPDATED_EVENT_COORDINATES);
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).save(testEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(0)).save(event);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).deleteById(event.getId());
    }

    @Test
    @Transactional
    public void searchEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        when(mockEventSearchRepository.search(queryStringQuery("id:" + event.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(event), PageRequest.of(0, 1), 1));
        // Search the event
        restEventMockMvc.perform(get("/api/_search/events?query=id:" + event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventCoordinates").value(hasItem(DEFAULT_EVENT_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId(2L);
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDTO.class);
        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setId(1L);
        EventDTO eventDTO2 = new EventDTO();
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO2.setId(eventDTO1.getId());
        assertThat(eventDTO1).isEqualTo(eventDTO2);
        eventDTO2.setId(2L);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO1.setId(null);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eventMapper.fromId(null)).isNull();
    }
}
