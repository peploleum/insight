package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.domain.Location;
import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.domain.Event;
import com.peploleum.insight.repository.EquipmentRepository;
import com.peploleum.insight.repository.search.EquipmentSearchRepository;
import com.peploleum.insight.service.EquipmentService;
import com.peploleum.insight.service.dto.EquipmentDTO;
import com.peploleum.insight.service.mapper.EquipmentMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.EquipmentCriteria;
import com.peploleum.insight.service.EquipmentQueryService;

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

import com.peploleum.insight.domain.enumeration.EquipmentType;
/**
 * Test class for the EquipmentResource REST controller.
 *
 * @see EquipmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class EquipmentResourceIntTest {

    private static final String DEFAULT_EQUIPMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final EquipmentType DEFAULT_EQUIPMENT_TYPE = EquipmentType.WEAPON;
    private static final EquipmentType UPDATED_EQUIPMENT_TYPE = EquipmentType.TOOL;

    private static final String DEFAULT_EQUIPMENT_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_COORDINATES = "BBBBBBBBBB";

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Mock
    private EquipmentRepository equipmentRepositoryMock;

    @Autowired
    private EquipmentMapper equipmentMapper;
    
    @Mock
    private EquipmentService equipmentServiceMock;

    @Autowired
    private EquipmentService equipmentService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.EquipmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private EquipmentSearchRepository mockEquipmentSearchRepository;

    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipmentResource equipmentResource = new EquipmentResource(equipmentService, equipmentQueryService);
        this.restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
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
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .equipmentName(DEFAULT_EQUIPMENT_NAME)
            .equipmentDescription(DEFAULT_EQUIPMENT_DESCRIPTION)
            .equipmentType(DEFAULT_EQUIPMENT_TYPE)
            .equipmentCoordinates(DEFAULT_EQUIPMENT_COORDINATES);
        return equipment;
    }

    @Before
    public void initTest() {
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentName()).isEqualTo(DEFAULT_EQUIPMENT_NAME);
        assertThat(testEquipment.getEquipmentDescription()).isEqualTo(DEFAULT_EQUIPMENT_DESCRIPTION);
        assertThat(testEquipment.getEquipmentType()).isEqualTo(DEFAULT_EQUIPMENT_TYPE);
        assertThat(testEquipment.getEquipmentCoordinates()).isEqualTo(DEFAULT_EQUIPMENT_COORDINATES);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(1)).save(testEquipment);
    }

    @Test
    @Transactional
    public void createEquipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment with an existing ID
        equipment.setId(1L);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(0)).save(equipment);
    }

    @Test
    @Transactional
    public void checkEquipmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].equipmentCoordinates").value(hasItem(DEFAULT_EQUIPMENT_COORDINATES.toString())));
    }
    
    public void getAllEquipmentWithEagerRelationshipsIsEnabled() throws Exception {
        EquipmentResource equipmentResource = new EquipmentResource(equipmentServiceMock, equipmentQueryService);
        when(equipmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEquipmentMockMvc.perform(get("/api/equipment?eagerload=true"))
        .andExpect(status().isOk());

        verify(equipmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllEquipmentWithEagerRelationshipsIsNotEnabled() throws Exception {
        EquipmentResource equipmentResource = new EquipmentResource(equipmentServiceMock, equipmentQueryService);
            when(equipmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEquipmentMockMvc.perform(get("/api/equipment?eagerload=true"))
        .andExpect(status().isOk());

            verify(equipmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.equipmentName").value(DEFAULT_EQUIPMENT_NAME.toString()))
            .andExpect(jsonPath("$.equipmentDescription").value(DEFAULT_EQUIPMENT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.equipmentType").value(DEFAULT_EQUIPMENT_TYPE.toString()))
            .andExpect(jsonPath("$.equipmentCoordinates").value(DEFAULT_EQUIPMENT_COORDINATES.toString()));
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentName equals to DEFAULT_EQUIPMENT_NAME
        defaultEquipmentShouldBeFound("equipmentName.equals=" + DEFAULT_EQUIPMENT_NAME);

        // Get all the equipmentList where equipmentName equals to UPDATED_EQUIPMENT_NAME
        defaultEquipmentShouldNotBeFound("equipmentName.equals=" + UPDATED_EQUIPMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentName in DEFAULT_EQUIPMENT_NAME or UPDATED_EQUIPMENT_NAME
        defaultEquipmentShouldBeFound("equipmentName.in=" + DEFAULT_EQUIPMENT_NAME + "," + UPDATED_EQUIPMENT_NAME);

        // Get all the equipmentList where equipmentName equals to UPDATED_EQUIPMENT_NAME
        defaultEquipmentShouldNotBeFound("equipmentName.in=" + UPDATED_EQUIPMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentName is not null
        defaultEquipmentShouldBeFound("equipmentName.specified=true");

        // Get all the equipmentList where equipmentName is null
        defaultEquipmentShouldNotBeFound("equipmentName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentDescription equals to DEFAULT_EQUIPMENT_DESCRIPTION
        defaultEquipmentShouldBeFound("equipmentDescription.equals=" + DEFAULT_EQUIPMENT_DESCRIPTION);

        // Get all the equipmentList where equipmentDescription equals to UPDATED_EQUIPMENT_DESCRIPTION
        defaultEquipmentShouldNotBeFound("equipmentDescription.equals=" + UPDATED_EQUIPMENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentDescription in DEFAULT_EQUIPMENT_DESCRIPTION or UPDATED_EQUIPMENT_DESCRIPTION
        defaultEquipmentShouldBeFound("equipmentDescription.in=" + DEFAULT_EQUIPMENT_DESCRIPTION + "," + UPDATED_EQUIPMENT_DESCRIPTION);

        // Get all the equipmentList where equipmentDescription equals to UPDATED_EQUIPMENT_DESCRIPTION
        defaultEquipmentShouldNotBeFound("equipmentDescription.in=" + UPDATED_EQUIPMENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentDescription is not null
        defaultEquipmentShouldBeFound("equipmentDescription.specified=true");

        // Get all the equipmentList where equipmentDescription is null
        defaultEquipmentShouldNotBeFound("equipmentDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentType equals to DEFAULT_EQUIPMENT_TYPE
        defaultEquipmentShouldBeFound("equipmentType.equals=" + DEFAULT_EQUIPMENT_TYPE);

        // Get all the equipmentList where equipmentType equals to UPDATED_EQUIPMENT_TYPE
        defaultEquipmentShouldNotBeFound("equipmentType.equals=" + UPDATED_EQUIPMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentType in DEFAULT_EQUIPMENT_TYPE or UPDATED_EQUIPMENT_TYPE
        defaultEquipmentShouldBeFound("equipmentType.in=" + DEFAULT_EQUIPMENT_TYPE + "," + UPDATED_EQUIPMENT_TYPE);

        // Get all the equipmentList where equipmentType equals to UPDATED_EQUIPMENT_TYPE
        defaultEquipmentShouldNotBeFound("equipmentType.in=" + UPDATED_EQUIPMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentType is not null
        defaultEquipmentShouldBeFound("equipmentType.specified=true");

        // Get all the equipmentList where equipmentType is null
        defaultEquipmentShouldNotBeFound("equipmentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentCoordinatesIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentCoordinates equals to DEFAULT_EQUIPMENT_COORDINATES
        defaultEquipmentShouldBeFound("equipmentCoordinates.equals=" + DEFAULT_EQUIPMENT_COORDINATES);

        // Get all the equipmentList where equipmentCoordinates equals to UPDATED_EQUIPMENT_COORDINATES
        defaultEquipmentShouldNotBeFound("equipmentCoordinates.equals=" + UPDATED_EQUIPMENT_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentCoordinatesIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentCoordinates in DEFAULT_EQUIPMENT_COORDINATES or UPDATED_EQUIPMENT_COORDINATES
        defaultEquipmentShouldBeFound("equipmentCoordinates.in=" + DEFAULT_EQUIPMENT_COORDINATES + "," + UPDATED_EQUIPMENT_COORDINATES);

        // Get all the equipmentList where equipmentCoordinates equals to UPDATED_EQUIPMENT_COORDINATES
        defaultEquipmentShouldNotBeFound("equipmentCoordinates.in=" + UPDATED_EQUIPMENT_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentCoordinatesIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where equipmentCoordinates is not null
        defaultEquipmentShouldBeFound("equipmentCoordinates.specified=true");

        // Get all the equipmentList where equipmentCoordinates is null
        defaultEquipmentShouldNotBeFound("equipmentCoordinates.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        equipment.addLocation(location);
        equipmentRepository.saveAndFlush(equipment);
        Long locationId = location.getId();

        // Get all the equipmentList where location equals to locationId
        defaultEquipmentShouldBeFound("locationId.equals=" + locationId);

        // Get all the equipmentList where location equals to locationId + 1
        defaultEquipmentShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        Organisation organisation = OrganisationResourceIntTest.createEntity(em);
        em.persist(organisation);
        em.flush();
        equipment.addOrganisation(organisation);
        equipmentRepository.saveAndFlush(equipment);
        Long organisationId = organisation.getId();

        // Get all the equipmentList where organisation equals to organisationId
        defaultEquipmentShouldBeFound("organisationId.equals=" + organisationId);

        // Get all the equipmentList where organisation equals to organisationId + 1
        defaultEquipmentShouldNotBeFound("organisationId.equals=" + (organisationId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByBiographicsIsEqualToSomething() throws Exception {
        // Initialize the database
        Biographics biographics = BiographicsResourceIntTest.createEntity(em);
        em.persist(biographics);
        em.flush();
        equipment.addBiographics(biographics);
        equipmentRepository.saveAndFlush(equipment);
        Long biographicsId = biographics.getId();

        // Get all the equipmentList where biographics equals to biographicsId
        defaultEquipmentShouldBeFound("biographicsId.equals=" + biographicsId);

        // Get all the equipmentList where biographics equals to biographicsId + 1
        defaultEquipmentShouldNotBeFound("biographicsId.equals=" + (biographicsId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        Event event = EventResourceIntTest.createEntity(em);
        em.persist(event);
        em.flush();
        equipment.addEvent(event);
        equipmentRepository.saveAndFlush(equipment);
        Long eventId = event.getId();

        // Get all the equipmentList where event equals to eventId
        defaultEquipmentShouldBeFound("eventId.equals=" + eventId);

        // Get all the equipmentList where event equals to eventId + 1
        defaultEquipmentShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEquipmentShouldBeFound(String filter) throws Exception {
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].equipmentCoordinates").value(hasItem(DEFAULT_EQUIPMENT_COORDINATES.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEquipmentShouldNotBeFound(String filter) throws Exception {
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).get();
        // Disconnect from session so that the updates on updatedEquipment are not directly saved in db
        em.detach(updatedEquipment);
        updatedEquipment
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .equipmentDescription(UPDATED_EQUIPMENT_DESCRIPTION)
            .equipmentType(UPDATED_EQUIPMENT_TYPE)
            .equipmentCoordinates(UPDATED_EQUIPMENT_COORDINATES);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentName()).isEqualTo(UPDATED_EQUIPMENT_NAME);
        assertThat(testEquipment.getEquipmentDescription()).isEqualTo(UPDATED_EQUIPMENT_DESCRIPTION);
        assertThat(testEquipment.getEquipmentType()).isEqualTo(UPDATED_EQUIPMENT_TYPE);
        assertThat(testEquipment.getEquipmentCoordinates()).isEqualTo(UPDATED_EQUIPMENT_COORDINATES);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(1)).save(testEquipment);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(0)).save(equipment);
    }

    @Test
    @Transactional
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Get the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(1)).deleteById(equipment.getId());
    }

    @Test
    @Transactional
    public void searchEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        when(mockEquipmentSearchRepository.search(queryStringQuery("id:" + equipment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(equipment), PageRequest.of(0, 1), 1));
        // Search the equipment
        restEquipmentMockMvc.perform(get("/api/_search/equipment?query=id:" + equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].equipmentCoordinates").value(hasItem(DEFAULT_EQUIPMENT_COORDINATES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
        Equipment equipment1 = new Equipment();
        equipment1.setId(1L);
        Equipment equipment2 = new Equipment();
        equipment2.setId(equipment1.getId());
        assertThat(equipment1).isEqualTo(equipment2);
        equipment2.setId(2L);
        assertThat(equipment1).isNotEqualTo(equipment2);
        equipment1.setId(null);
        assertThat(equipment1).isNotEqualTo(equipment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentDTO.class);
        EquipmentDTO equipmentDTO1 = new EquipmentDTO();
        equipmentDTO1.setId(1L);
        EquipmentDTO equipmentDTO2 = new EquipmentDTO();
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO2.setId(equipmentDTO1.getId());
        assertThat(equipmentDTO1).isEqualTo(equipmentDTO2);
        equipmentDTO2.setId(2L);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO1.setId(null);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(equipmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(equipmentMapper.fromId(null)).isNull();
    }
}
