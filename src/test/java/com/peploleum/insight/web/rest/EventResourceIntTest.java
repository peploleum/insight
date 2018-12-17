package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Event;
import com.peploleum.insight.repository.EventRepository;
import com.peploleum.insight.repository.search.EventSearchRepository;
import com.peploleum.insight.service.EventService;
import com.peploleum.insight.service.dto.EventDTO;
import com.peploleum.insight.service.mapper.EventMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.ZoneId;
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

    private static final LocalDate DEFAULT_EVENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EVENT_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_COORDINATES = "BBBBBBBBBB";

    private static final byte[] DEFAULT_EVENT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_EVENT_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_EVENT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_EVENT_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_EVENT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_SYMBOL = "BBBBBBBBBB";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

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
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEventMockMvc;

    private Event event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventService);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity() {
        Event event = new Event()
            .eventName(DEFAULT_EVENT_NAME)
            .eventDescription(DEFAULT_EVENT_DESCRIPTION)
            .eventType(DEFAULT_EVENT_TYPE)
            .eventDate(DEFAULT_EVENT_DATE)
            .eventCoordinates(DEFAULT_EVENT_COORDINATES)
            .eventImage(DEFAULT_EVENT_IMAGE)
            .eventImageContentType(DEFAULT_EVENT_IMAGE_CONTENT_TYPE)
            .eventSymbol(DEFAULT_EVENT_SYMBOL);
        return event;
    }

    @Before
    public void initTest() {
        eventRepository.deleteAll();
        event = createEntity();
    }

    @Test
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
        assertThat(testEvent.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEvent.getEventCoordinates()).isEqualTo(DEFAULT_EVENT_COORDINATES);
        assertThat(testEvent.getEventImage()).isEqualTo(DEFAULT_EVENT_IMAGE);
        assertThat(testEvent.getEventImageContentType()).isEqualTo(DEFAULT_EVENT_IMAGE_CONTENT_TYPE);
        assertThat(testEvent.getEventSymbol()).isEqualTo(DEFAULT_EVENT_SYMBOL);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).save(testEvent);
    }

    @Test
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId("existing_id");
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
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.save(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventCoordinates").value(hasItem(DEFAULT_EVENT_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].eventImageContentType").value(hasItem(DEFAULT_EVENT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_EVENT_IMAGE))))
            .andExpect(jsonPath("$.[*].eventSymbol").value(hasItem(DEFAULT_EVENT_SYMBOL.toString())));
    }
    
    @Test
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.save(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
            .andExpect(jsonPath("$.eventDescription").value(DEFAULT_EVENT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.eventCoordinates").value(DEFAULT_EVENT_COORDINATES.toString()))
            .andExpect(jsonPath("$.eventImageContentType").value(DEFAULT_EVENT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.eventImage").value(Base64Utils.encodeToString(DEFAULT_EVENT_IMAGE)))
            .andExpect(jsonPath("$.eventSymbol").value(DEFAULT_EVENT_SYMBOL.toString()));
    }

    @Test
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.save(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        updatedEvent
            .eventName(UPDATED_EVENT_NAME)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .eventType(UPDATED_EVENT_TYPE)
            .eventDate(UPDATED_EVENT_DATE)
            .eventCoordinates(UPDATED_EVENT_COORDINATES)
            .eventImage(UPDATED_EVENT_IMAGE)
            .eventImageContentType(UPDATED_EVENT_IMAGE_CONTENT_TYPE)
            .eventSymbol(UPDATED_EVENT_SYMBOL);
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
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvent.getEventCoordinates()).isEqualTo(UPDATED_EVENT_COORDINATES);
        assertThat(testEvent.getEventImage()).isEqualTo(UPDATED_EVENT_IMAGE);
        assertThat(testEvent.getEventImageContentType()).isEqualTo(UPDATED_EVENT_IMAGE_CONTENT_TYPE);
        assertThat(testEvent.getEventSymbol()).isEqualTo(UPDATED_EVENT_SYMBOL);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).save(testEvent);
    }

    @Test
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
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.save(event);

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
    public void searchEvent() throws Exception {
        // Initialize the database
        eventRepository.save(event);
        when(mockEventSearchRepository.search(queryStringQuery("id:" + event.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(event), PageRequest.of(0, 1), 1));
        // Search the event
        restEventMockMvc.perform(get("/api/_search/events?query=id:" + event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventCoordinates").value(hasItem(DEFAULT_EVENT_COORDINATES)))
            .andExpect(jsonPath("$.[*].eventImageContentType").value(hasItem(DEFAULT_EVENT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_EVENT_IMAGE))))
            .andExpect(jsonPath("$.[*].eventSymbol").value(hasItem(DEFAULT_EVENT_SYMBOL)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId("id1");
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId("id2");
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDTO.class);
        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setId("id1");
        EventDTO eventDTO2 = new EventDTO();
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO2.setId(eventDTO1.getId());
        assertThat(eventDTO1).isEqualTo(eventDTO2);
        eventDTO2.setId("id2");
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO1.setId(null);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
    }
}
