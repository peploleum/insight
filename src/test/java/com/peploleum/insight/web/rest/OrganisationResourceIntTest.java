package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.domain.Location;
import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.domain.Event;
import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.repository.OrganisationRepository;
import com.peploleum.insight.repository.search.OrganisationSearchRepository;
import com.peploleum.insight.service.OrganisationService;
import com.peploleum.insight.service.dto.OrganisationDTO;
import com.peploleum.insight.service.mapper.OrganisationMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.OrganisationCriteria;
import com.peploleum.insight.service.OrganisationQueryService;

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

import com.peploleum.insight.domain.enumeration.Size;
/**
 * Test class for the OrganisationResource REST controller.
 *
 * @see OrganisationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class OrganisationResourceIntTest {

    private static final String DEFAULT_ORGANISATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_NAME = "BBBBBBBBBB";

    private static final Size DEFAULT_ORGANISATION_SIZE = Size.SMALL;
    private static final Size UPDATED_ORGANISATION_SIZE = Size.MEDIUM;

    private static final String DEFAULT_ORGANISATION_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_COORDINATES = "BBBBBBBBBB";

    @Autowired
    private OrganisationRepository organisationRepository;
    @Mock
    private OrganisationRepository organisationRepositoryMock;

    @Autowired
    private OrganisationMapper organisationMapper;
    
    @Mock
    private OrganisationService organisationServiceMock;

    @Autowired
    private OrganisationService organisationService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.OrganisationSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrganisationSearchRepository mockOrganisationSearchRepository;

    @Autowired
    private OrganisationQueryService organisationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganisationMockMvc;

    private Organisation organisation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganisationResource organisationResource = new OrganisationResource(organisationService, organisationQueryService);
        this.restOrganisationMockMvc = MockMvcBuilders.standaloneSetup(organisationResource)
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
    public static Organisation createEntity(EntityManager em) {
        Organisation organisation = new Organisation()
            .organisationName(DEFAULT_ORGANISATION_NAME)
            .organisationSize(DEFAULT_ORGANISATION_SIZE)
            .organisationCoordinates(DEFAULT_ORGANISATION_COORDINATES);
        return organisation;
    }

    @Before
    public void initTest() {
        organisation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganisation() throws Exception {
        int databaseSizeBeforeCreate = organisationRepository.findAll().size();

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);
        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isCreated());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeCreate + 1);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getOrganisationName()).isEqualTo(DEFAULT_ORGANISATION_NAME);
        assertThat(testOrganisation.getOrganisationSize()).isEqualTo(DEFAULT_ORGANISATION_SIZE);
        assertThat(testOrganisation.getOrganisationCoordinates()).isEqualTo(DEFAULT_ORGANISATION_COORDINATES);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(1)).save(testOrganisation);
    }

    @Test
    @Transactional
    public void createOrganisationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organisationRepository.findAll().size();

        // Create the Organisation with an existing ID
        organisation.setId(1L);
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(0)).save(organisation);
    }

    @Test
    @Transactional
    public void checkOrganisationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationRepository.findAll().size();
        // set the field null
        organisation.setOrganisationName(null);

        // Create the Organisation, which fails.
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganisations() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].organisationSize").value(hasItem(DEFAULT_ORGANISATION_SIZE.toString())))
            .andExpect(jsonPath("$.[*].organisationCoordinates").value(hasItem(DEFAULT_ORGANISATION_COORDINATES.toString())));
    }
    
    public void getAllOrganisationsWithEagerRelationshipsIsEnabled() throws Exception {
        OrganisationResource organisationResource = new OrganisationResource(organisationServiceMock, organisationQueryService);
        when(organisationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrganisationMockMvc = MockMvcBuilders.standaloneSetup(organisationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrganisationMockMvc.perform(get("/api/organisations?eagerload=true"))
        .andExpect(status().isOk());

        verify(organisationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllOrganisationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrganisationResource organisationResource = new OrganisationResource(organisationServiceMock, organisationQueryService);
            when(organisationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrganisationMockMvc = MockMvcBuilders.standaloneSetup(organisationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrganisationMockMvc.perform(get("/api/organisations?eagerload=true"))
        .andExpect(status().isOk());

            verify(organisationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get the organisation
        restOrganisationMockMvc.perform(get("/api/organisations/{id}", organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organisation.getId().intValue()))
            .andExpect(jsonPath("$.organisationName").value(DEFAULT_ORGANISATION_NAME.toString()))
            .andExpect(jsonPath("$.organisationSize").value(DEFAULT_ORGANISATION_SIZE.toString()))
            .andExpect(jsonPath("$.organisationCoordinates").value(DEFAULT_ORGANISATION_COORDINATES.toString()));
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationName equals to DEFAULT_ORGANISATION_NAME
        defaultOrganisationShouldBeFound("organisationName.equals=" + DEFAULT_ORGANISATION_NAME);

        // Get all the organisationList where organisationName equals to UPDATED_ORGANISATION_NAME
        defaultOrganisationShouldNotBeFound("organisationName.equals=" + UPDATED_ORGANISATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationNameIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationName in DEFAULT_ORGANISATION_NAME or UPDATED_ORGANISATION_NAME
        defaultOrganisationShouldBeFound("organisationName.in=" + DEFAULT_ORGANISATION_NAME + "," + UPDATED_ORGANISATION_NAME);

        // Get all the organisationList where organisationName equals to UPDATED_ORGANISATION_NAME
        defaultOrganisationShouldNotBeFound("organisationName.in=" + UPDATED_ORGANISATION_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationName is not null
        defaultOrganisationShouldBeFound("organisationName.specified=true");

        // Get all the organisationList where organisationName is null
        defaultOrganisationShouldNotBeFound("organisationName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationSize equals to DEFAULT_ORGANISATION_SIZE
        defaultOrganisationShouldBeFound("organisationSize.equals=" + DEFAULT_ORGANISATION_SIZE);

        // Get all the organisationList where organisationSize equals to UPDATED_ORGANISATION_SIZE
        defaultOrganisationShouldNotBeFound("organisationSize.equals=" + UPDATED_ORGANISATION_SIZE);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationSizeIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationSize in DEFAULT_ORGANISATION_SIZE or UPDATED_ORGANISATION_SIZE
        defaultOrganisationShouldBeFound("organisationSize.in=" + DEFAULT_ORGANISATION_SIZE + "," + UPDATED_ORGANISATION_SIZE);

        // Get all the organisationList where organisationSize equals to UPDATED_ORGANISATION_SIZE
        defaultOrganisationShouldNotBeFound("organisationSize.in=" + UPDATED_ORGANISATION_SIZE);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationSize is not null
        defaultOrganisationShouldBeFound("organisationSize.specified=true");

        // Get all the organisationList where organisationSize is null
        defaultOrganisationShouldNotBeFound("organisationSize.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationCoordinatesIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationCoordinates equals to DEFAULT_ORGANISATION_COORDINATES
        defaultOrganisationShouldBeFound("organisationCoordinates.equals=" + DEFAULT_ORGANISATION_COORDINATES);

        // Get all the organisationList where organisationCoordinates equals to UPDATED_ORGANISATION_COORDINATES
        defaultOrganisationShouldNotBeFound("organisationCoordinates.equals=" + UPDATED_ORGANISATION_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationCoordinatesIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationCoordinates in DEFAULT_ORGANISATION_COORDINATES or UPDATED_ORGANISATION_COORDINATES
        defaultOrganisationShouldBeFound("organisationCoordinates.in=" + DEFAULT_ORGANISATION_COORDINATES + "," + UPDATED_ORGANISATION_COORDINATES);

        // Get all the organisationList where organisationCoordinates equals to UPDATED_ORGANISATION_COORDINATES
        defaultOrganisationShouldNotBeFound("organisationCoordinates.in=" + UPDATED_ORGANISATION_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByOrganisationCoordinatesIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where organisationCoordinates is not null
        defaultOrganisationShouldBeFound("organisationCoordinates.specified=true");

        // Get all the organisationList where organisationCoordinates is null
        defaultOrganisationShouldNotBeFound("organisationCoordinates.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        organisation.addLocation(location);
        organisationRepository.saveAndFlush(organisation);
        Long locationId = location.getId();

        // Get all the organisationList where location equals to locationId
        defaultOrganisationShouldBeFound("locationId.equals=" + locationId);

        // Get all the organisationList where location equals to locationId + 1
        defaultOrganisationShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationsByBiographicsIsEqualToSomething() throws Exception {
        // Initialize the database
        Biographics biographics = BiographicsResourceIntTest.createEntity(em);
        em.persist(biographics);
        em.flush();
        organisation.addBiographics(biographics);
        organisationRepository.saveAndFlush(organisation);
        Long biographicsId = biographics.getId();

        // Get all the organisationList where biographics equals to biographicsId
        defaultOrganisationShouldBeFound("biographicsId.equals=" + biographicsId);

        // Get all the organisationList where biographics equals to biographicsId + 1
        defaultOrganisationShouldNotBeFound("biographicsId.equals=" + (biographicsId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        Event event = EventResourceIntTest.createEntity(em);
        em.persist(event);
        em.flush();
        organisation.addEvent(event);
        organisationRepository.saveAndFlush(organisation);
        Long eventId = event.getId();

        // Get all the organisationList where event equals to eventId
        defaultOrganisationShouldBeFound("eventId.equals=" + eventId);

        // Get all the organisationList where event equals to eventId + 1
        defaultOrganisationShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationsByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        organisation.addEquipment(equipment);
        organisationRepository.saveAndFlush(organisation);
        Long equipmentId = equipment.getId();

        // Get all the organisationList where equipment equals to equipmentId
        defaultOrganisationShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the organisationList where equipment equals to equipmentId + 1
        defaultOrganisationShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrganisationShouldBeFound(String filter) throws Exception {
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].organisationSize").value(hasItem(DEFAULT_ORGANISATION_SIZE.toString())))
            .andExpect(jsonPath("$.[*].organisationCoordinates").value(hasItem(DEFAULT_ORGANISATION_COORDINATES.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrganisationShouldNotBeFound(String filter) throws Exception {
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingOrganisation() throws Exception {
        // Get the organisation
        restOrganisationMockMvc.perform(get("/api/organisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        int databaseSizeBeforeUpdate = organisationRepository.findAll().size();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findById(organisation.getId()).get();
        // Disconnect from session so that the updates on updatedOrganisation are not directly saved in db
        em.detach(updatedOrganisation);
        updatedOrganisation
            .organisationName(UPDATED_ORGANISATION_NAME)
            .organisationSize(UPDATED_ORGANISATION_SIZE)
            .organisationCoordinates(UPDATED_ORGANISATION_COORDINATES);
        OrganisationDTO organisationDTO = organisationMapper.toDto(updatedOrganisation);

        restOrganisationMockMvc.perform(put("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isOk());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getOrganisationName()).isEqualTo(UPDATED_ORGANISATION_NAME);
        assertThat(testOrganisation.getOrganisationSize()).isEqualTo(UPDATED_ORGANISATION_SIZE);
        assertThat(testOrganisation.getOrganisationCoordinates()).isEqualTo(UPDATED_ORGANISATION_COORDINATES);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(1)).save(testOrganisation);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().size();

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOrganisationMockMvc.perform(put("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(0)).save(organisation);
    }

    @Test
    @Transactional
    public void deleteOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        int databaseSizeBeforeDelete = organisationRepository.findAll().size();

        // Get the organisation
        restOrganisationMockMvc.perform(delete("/api/organisations/{id}", organisation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(1)).deleteById(organisation.getId());
    }

    @Test
    @Transactional
    public void searchOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);
        when(mockOrganisationSearchRepository.search(queryStringQuery("id:" + organisation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(organisation), PageRequest.of(0, 1), 1));
        // Search the organisation
        restOrganisationMockMvc.perform(get("/api/_search/organisations?query=id:" + organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].organisationSize").value(hasItem(DEFAULT_ORGANISATION_SIZE.toString())))
            .andExpect(jsonPath("$.[*].organisationCoordinates").value(hasItem(DEFAULT_ORGANISATION_COORDINATES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = new Organisation();
        organisation1.setId(1L);
        Organisation organisation2 = new Organisation();
        organisation2.setId(organisation1.getId());
        assertThat(organisation1).isEqualTo(organisation2);
        organisation2.setId(2L);
        assertThat(organisation1).isNotEqualTo(organisation2);
        organisation1.setId(null);
        assertThat(organisation1).isNotEqualTo(organisation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationDTO.class);
        OrganisationDTO organisationDTO1 = new OrganisationDTO();
        organisationDTO1.setId(1L);
        OrganisationDTO organisationDTO2 = new OrganisationDTO();
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO2.setId(organisationDTO1.getId());
        assertThat(organisationDTO1).isEqualTo(organisationDTO2);
        organisationDTO2.setId(2L);
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO1.setId(null);
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(organisationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(organisationMapper.fromId(null)).isNull();
    }
}
