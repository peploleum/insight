package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.domain.Event;
import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.domain.Location;
import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.repository.BiographicsRepository;
import com.peploleum.insight.repository.search.BiographicsSearchRepository;
import com.peploleum.insight.service.BiographicsService;
import com.peploleum.insight.service.dto.BiographicsDTO;
import com.peploleum.insight.service.mapper.BiographicsMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.BiographicsQueryService;

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
import org.springframework.util.Base64Utils;

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

import com.peploleum.insight.domain.enumeration.Gender;
/**
 * Test class for the BiographicsResource REST controller.
 *
 * @see BiographicsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class BiographicsResourceIntTest {

    private static final String DEFAULT_BIOGRAPHICS_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHICS_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIOGRAPHICS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHICS_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_BIOGRAPHICS_AGE = 1;
    private static final Integer UPDATED_BIOGRAPHICS_AGE = 2;

    private static final Gender DEFAULT_BIOGRAPHICS_GENDER = Gender.MALE;
    private static final Gender UPDATED_BIOGRAPHICS_GENDER = Gender.FEMALE;

    private static final byte[] DEFAULT_BIOGRAPHICS_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BIOGRAPHICS_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BIOGRAPHICS_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BIOGRAPHICS_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHICS_COORDINATES = "BBBBBBBBBB";

    @Autowired
    private BiographicsRepository biographicsRepository;
    @Mock
    private BiographicsRepository biographicsRepositoryMock;

    @Autowired
    private BiographicsMapper biographicsMapper;
    
    @Mock
    private BiographicsService biographicsServiceMock;

    @Autowired
    private BiographicsService biographicsService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.BiographicsSearchRepositoryMockConfiguration
     */
    @Autowired
    private BiographicsSearchRepository mockBiographicsSearchRepository;

    @Autowired
    private BiographicsQueryService biographicsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBiographicsMockMvc;

    private Biographics biographics;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BiographicsResource biographicsResource = new BiographicsResource(biographicsService, biographicsQueryService, null);
        this.restBiographicsMockMvc = MockMvcBuilders.standaloneSetup(biographicsResource)
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
    public static Biographics createEntity(EntityManager em) {
        Biographics biographics = new Biographics()
            .biographicsFirstname(DEFAULT_BIOGRAPHICS_FIRSTNAME)
            .biographicsName(DEFAULT_BIOGRAPHICS_NAME)
            .biographicsAge(DEFAULT_BIOGRAPHICS_AGE)
            .biographicsGender(DEFAULT_BIOGRAPHICS_GENDER)
            .biographicsPhoto(DEFAULT_BIOGRAPHICS_PHOTO)
            .biographicsPhotoContentType(DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE)
            .biographicsCoordinates(DEFAULT_BIOGRAPHICS_COORDINATES);
        return biographics;
    }

    @Before
    public void initTest() {
        biographics = createEntity(em);
    }

    @Test
    @Transactional
    public void createBiographics() throws Exception {
        int databaseSizeBeforeCreate = biographicsRepository.findAll().size();

        // Create the Biographics
        BiographicsDTO biographicsDTO = biographicsMapper.toDto(biographics);
        restBiographicsMockMvc.perform(post("/api/biographics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(biographicsDTO)))
            .andExpect(status().isCreated());

        // Validate the Biographics in the database
        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeCreate + 1);
        Biographics testBiographics = biographicsList.get(biographicsList.size() - 1);
        assertThat(testBiographics.getBiographicsFirstname()).isEqualTo(DEFAULT_BIOGRAPHICS_FIRSTNAME);
        assertThat(testBiographics.getBiographicsName()).isEqualTo(DEFAULT_BIOGRAPHICS_NAME);
        assertThat(testBiographics.getBiographicsAge()).isEqualTo(DEFAULT_BIOGRAPHICS_AGE);
        assertThat(testBiographics.getBiographicsGender()).isEqualTo(DEFAULT_BIOGRAPHICS_GENDER);
        assertThat(testBiographics.getBiographicsPhoto()).isEqualTo(DEFAULT_BIOGRAPHICS_PHOTO);
        assertThat(testBiographics.getBiographicsPhotoContentType()).isEqualTo(DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE);
        assertThat(testBiographics.getBiographicsCoordinates()).isEqualTo(DEFAULT_BIOGRAPHICS_COORDINATES);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(1)).save(testBiographics);
    }

    @Test
    @Transactional
    public void createBiographicsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = biographicsRepository.findAll().size();

        // Create the Biographics with an existing ID
        biographics.setId(1L);
        BiographicsDTO biographicsDTO = biographicsMapper.toDto(biographics);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBiographicsMockMvc.perform(post("/api/biographics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(biographicsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Biographics in the database
        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(0)).save(biographics);
    }

    @Test
    @Transactional
    public void checkBiographicsFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = biographicsRepository.findAll().size();
        // set the field null
        biographics.setBiographicsFirstname(null);

        // Create the Biographics, which fails.
        BiographicsDTO biographicsDTO = biographicsMapper.toDto(biographics);

        restBiographicsMockMvc.perform(post("/api/biographics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(biographicsDTO)))
            .andExpect(status().isBadRequest());

        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBiographicsNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = biographicsRepository.findAll().size();
        // set the field null
        biographics.setBiographicsName(null);

        // Create the Biographics, which fails.
        BiographicsDTO biographicsDTO = biographicsMapper.toDto(biographics);

        restBiographicsMockMvc.perform(post("/api/biographics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(biographicsDTO)))
            .andExpect(status().isBadRequest());

        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList
        restBiographicsMockMvc.perform(get("/api/biographics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(biographics.getId().intValue())))
            .andExpect(jsonPath("$.[*].biographicsFirstname").value(hasItem(DEFAULT_BIOGRAPHICS_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsName").value(hasItem(DEFAULT_BIOGRAPHICS_NAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsAge").value(hasItem(DEFAULT_BIOGRAPHICS_AGE)))
            .andExpect(jsonPath("$.[*].biographicsGender").value(hasItem(DEFAULT_BIOGRAPHICS_GENDER.toString())))
            .andExpect(jsonPath("$.[*].biographicsPhotoContentType").value(hasItem(DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].biographicsPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_PHOTO))))
            .andExpect(jsonPath("$.[*].biographicsCoordinates").value(hasItem(DEFAULT_BIOGRAPHICS_COORDINATES.toString())));
    }
    
    public void getAllBiographicsWithEagerRelationshipsIsEnabled() throws Exception {
        BiographicsResource biographicsResource = new BiographicsResource(biographicsServiceMock, biographicsQueryService, null);
        when(biographicsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restBiographicsMockMvc = MockMvcBuilders.standaloneSetup(biographicsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restBiographicsMockMvc.perform(get("/api/biographics?eagerload=true"))
        .andExpect(status().isOk());

        verify(biographicsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllBiographicsWithEagerRelationshipsIsNotEnabled() throws Exception {
        BiographicsResource biographicsResource = new BiographicsResource(biographicsServiceMock, biographicsQueryService, null);
            when(biographicsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restBiographicsMockMvc = MockMvcBuilders.standaloneSetup(biographicsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restBiographicsMockMvc.perform(get("/api/biographics?eagerload=true"))
        .andExpect(status().isOk());

            verify(biographicsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get the biographics
        restBiographicsMockMvc.perform(get("/api/biographics/{id}", biographics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(biographics.getId().intValue()))
            .andExpect(jsonPath("$.biographicsFirstname").value(DEFAULT_BIOGRAPHICS_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.biographicsName").value(DEFAULT_BIOGRAPHICS_NAME.toString()))
            .andExpect(jsonPath("$.biographicsAge").value(DEFAULT_BIOGRAPHICS_AGE))
            .andExpect(jsonPath("$.biographicsGender").value(DEFAULT_BIOGRAPHICS_GENDER.toString()))
            .andExpect(jsonPath("$.biographicsPhotoContentType").value(DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.biographicsPhoto").value(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_PHOTO)))
            .andExpect(jsonPath("$.biographicsCoordinates").value(DEFAULT_BIOGRAPHICS_COORDINATES.toString()));
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsFirstname equals to DEFAULT_BIOGRAPHICS_FIRSTNAME
        defaultBiographicsShouldBeFound("biographicsFirstname.equals=" + DEFAULT_BIOGRAPHICS_FIRSTNAME);

        // Get all the biographicsList where biographicsFirstname equals to UPDATED_BIOGRAPHICS_FIRSTNAME
        defaultBiographicsShouldNotBeFound("biographicsFirstname.equals=" + UPDATED_BIOGRAPHICS_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsFirstname in DEFAULT_BIOGRAPHICS_FIRSTNAME or UPDATED_BIOGRAPHICS_FIRSTNAME
        defaultBiographicsShouldBeFound("biographicsFirstname.in=" + DEFAULT_BIOGRAPHICS_FIRSTNAME + "," + UPDATED_BIOGRAPHICS_FIRSTNAME);

        // Get all the biographicsList where biographicsFirstname equals to UPDATED_BIOGRAPHICS_FIRSTNAME
        defaultBiographicsShouldNotBeFound("biographicsFirstname.in=" + UPDATED_BIOGRAPHICS_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsFirstname is not null
        defaultBiographicsShouldBeFound("biographicsFirstname.specified=true");

        // Get all the biographicsList where biographicsFirstname is null
        defaultBiographicsShouldNotBeFound("biographicsFirstname.specified=false");
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsNameIsEqualToSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsName equals to DEFAULT_BIOGRAPHICS_NAME
        defaultBiographicsShouldBeFound("biographicsName.equals=" + DEFAULT_BIOGRAPHICS_NAME);

        // Get all the biographicsList where biographicsName equals to UPDATED_BIOGRAPHICS_NAME
        defaultBiographicsShouldNotBeFound("biographicsName.equals=" + UPDATED_BIOGRAPHICS_NAME);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsNameIsInShouldWork() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsName in DEFAULT_BIOGRAPHICS_NAME or UPDATED_BIOGRAPHICS_NAME
        defaultBiographicsShouldBeFound("biographicsName.in=" + DEFAULT_BIOGRAPHICS_NAME + "," + UPDATED_BIOGRAPHICS_NAME);

        // Get all the biographicsList where biographicsName equals to UPDATED_BIOGRAPHICS_NAME
        defaultBiographicsShouldNotBeFound("biographicsName.in=" + UPDATED_BIOGRAPHICS_NAME);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsName is not null
        defaultBiographicsShouldBeFound("biographicsName.specified=true");

        // Get all the biographicsList where biographicsName is null
        defaultBiographicsShouldNotBeFound("biographicsName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsAge equals to DEFAULT_BIOGRAPHICS_AGE
        defaultBiographicsShouldBeFound("biographicsAge.equals=" + DEFAULT_BIOGRAPHICS_AGE);

        // Get all the biographicsList where biographicsAge equals to UPDATED_BIOGRAPHICS_AGE
        defaultBiographicsShouldNotBeFound("biographicsAge.equals=" + UPDATED_BIOGRAPHICS_AGE);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsAgeIsInShouldWork() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsAge in DEFAULT_BIOGRAPHICS_AGE or UPDATED_BIOGRAPHICS_AGE
        defaultBiographicsShouldBeFound("biographicsAge.in=" + DEFAULT_BIOGRAPHICS_AGE + "," + UPDATED_BIOGRAPHICS_AGE);

        // Get all the biographicsList where biographicsAge equals to UPDATED_BIOGRAPHICS_AGE
        defaultBiographicsShouldNotBeFound("biographicsAge.in=" + UPDATED_BIOGRAPHICS_AGE);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsAge is not null
        defaultBiographicsShouldBeFound("biographicsAge.specified=true");

        // Get all the biographicsList where biographicsAge is null
        defaultBiographicsShouldNotBeFound("biographicsAge.specified=false");
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsAge greater than or equals to DEFAULT_BIOGRAPHICS_AGE
        defaultBiographicsShouldBeFound("biographicsAge.greaterOrEqualThan=" + DEFAULT_BIOGRAPHICS_AGE);

        // Get all the biographicsList where biographicsAge greater than or equals to UPDATED_BIOGRAPHICS_AGE
        defaultBiographicsShouldNotBeFound("biographicsAge.greaterOrEqualThan=" + UPDATED_BIOGRAPHICS_AGE);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsAge less than or equals to DEFAULT_BIOGRAPHICS_AGE
        defaultBiographicsShouldNotBeFound("biographicsAge.lessThan=" + DEFAULT_BIOGRAPHICS_AGE);

        // Get all the biographicsList where biographicsAge less than or equals to UPDATED_BIOGRAPHICS_AGE
        defaultBiographicsShouldBeFound("biographicsAge.lessThan=" + UPDATED_BIOGRAPHICS_AGE);
    }


    @Test
    @Transactional
    public void getAllBiographicsByBiographicsGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsGender equals to DEFAULT_BIOGRAPHICS_GENDER
        defaultBiographicsShouldBeFound("biographicsGender.equals=" + DEFAULT_BIOGRAPHICS_GENDER);

        // Get all the biographicsList where biographicsGender equals to UPDATED_BIOGRAPHICS_GENDER
        defaultBiographicsShouldNotBeFound("biographicsGender.equals=" + UPDATED_BIOGRAPHICS_GENDER);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsGenderIsInShouldWork() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsGender in DEFAULT_BIOGRAPHICS_GENDER or UPDATED_BIOGRAPHICS_GENDER
        defaultBiographicsShouldBeFound("biographicsGender.in=" + DEFAULT_BIOGRAPHICS_GENDER + "," + UPDATED_BIOGRAPHICS_GENDER);

        // Get all the biographicsList where biographicsGender equals to UPDATED_BIOGRAPHICS_GENDER
        defaultBiographicsShouldNotBeFound("biographicsGender.in=" + UPDATED_BIOGRAPHICS_GENDER);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsGender is not null
        defaultBiographicsShouldBeFound("biographicsGender.specified=true");

        // Get all the biographicsList where biographicsGender is null
        defaultBiographicsShouldNotBeFound("biographicsGender.specified=false");
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsCoordinatesIsEqualToSomething() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsCoordinates equals to DEFAULT_BIOGRAPHICS_COORDINATES
        defaultBiographicsShouldBeFound("biographicsCoordinates.equals=" + DEFAULT_BIOGRAPHICS_COORDINATES);

        // Get all the biographicsList where biographicsCoordinates equals to UPDATED_BIOGRAPHICS_COORDINATES
        defaultBiographicsShouldNotBeFound("biographicsCoordinates.equals=" + UPDATED_BIOGRAPHICS_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsCoordinatesIsInShouldWork() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsCoordinates in DEFAULT_BIOGRAPHICS_COORDINATES or UPDATED_BIOGRAPHICS_COORDINATES
        defaultBiographicsShouldBeFound("biographicsCoordinates.in=" + DEFAULT_BIOGRAPHICS_COORDINATES + "," + UPDATED_BIOGRAPHICS_COORDINATES);

        // Get all the biographicsList where biographicsCoordinates equals to UPDATED_BIOGRAPHICS_COORDINATES
        defaultBiographicsShouldNotBeFound("biographicsCoordinates.in=" + UPDATED_BIOGRAPHICS_COORDINATES);
    }

    @Test
    @Transactional
    public void getAllBiographicsByBiographicsCoordinatesIsNullOrNotNull() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        // Get all the biographicsList where biographicsCoordinates is not null
        defaultBiographicsShouldBeFound("biographicsCoordinates.specified=true");

        // Get all the biographicsList where biographicsCoordinates is null
        defaultBiographicsShouldNotBeFound("biographicsCoordinates.specified=false");
    }

    @Test
    @Transactional
    public void getAllBiographicsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        Event event = EventResourceIntTest.createEntity(em);
        em.persist(event);
        em.flush();
        biographics.addEvent(event);
        biographicsRepository.saveAndFlush(biographics);
        Long eventId = event.getId();

        // Get all the biographicsList where event equals to eventId
        defaultBiographicsShouldBeFound("eventId.equals=" + eventId);

        // Get all the biographicsList where event equals to eventId + 1
        defaultBiographicsShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }


    @Test
    @Transactional
    public void getAllBiographicsByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        biographics.addEquipment(equipment);
        biographicsRepository.saveAndFlush(biographics);
        Long equipmentId = equipment.getId();

        // Get all the biographicsList where equipment equals to equipmentId
        defaultBiographicsShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the biographicsList where equipment equals to equipmentId + 1
        defaultBiographicsShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }


    @Test
    @Transactional
    public void getAllBiographicsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        biographics.addLocation(location);
        biographicsRepository.saveAndFlush(biographics);
        Long locationId = location.getId();

        // Get all the biographicsList where location equals to locationId
        defaultBiographicsShouldBeFound("locationId.equals=" + locationId);

        // Get all the biographicsList where location equals to locationId + 1
        defaultBiographicsShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllBiographicsByOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        Organisation organisation = OrganisationResourceIntTest.createEntity(em);
        em.persist(organisation);
        em.flush();
        biographics.addOrganisation(organisation);
        biographicsRepository.saveAndFlush(biographics);
        Long organisationId = organisation.getId();

        // Get all the biographicsList where organisation equals to organisationId
        defaultBiographicsShouldBeFound("organisationId.equals=" + organisationId);

        // Get all the biographicsList where organisation equals to organisationId + 1
        defaultBiographicsShouldNotBeFound("organisationId.equals=" + (organisationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBiographicsShouldBeFound(String filter) throws Exception {
        restBiographicsMockMvc.perform(get("/api/biographics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(biographics.getId().intValue())))
            .andExpect(jsonPath("$.[*].biographicsFirstname").value(hasItem(DEFAULT_BIOGRAPHICS_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsName").value(hasItem(DEFAULT_BIOGRAPHICS_NAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsAge").value(hasItem(DEFAULT_BIOGRAPHICS_AGE)))
            .andExpect(jsonPath("$.[*].biographicsGender").value(hasItem(DEFAULT_BIOGRAPHICS_GENDER.toString())))
            .andExpect(jsonPath("$.[*].biographicsPhotoContentType").value(hasItem(DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].biographicsPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_PHOTO))))
            .andExpect(jsonPath("$.[*].biographicsCoordinates").value(hasItem(DEFAULT_BIOGRAPHICS_COORDINATES.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBiographicsShouldNotBeFound(String filter) throws Exception {
        restBiographicsMockMvc.perform(get("/api/biographics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingBiographics() throws Exception {
        // Get the biographics
        restBiographicsMockMvc.perform(get("/api/biographics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        int databaseSizeBeforeUpdate = biographicsRepository.findAll().size();

        // Update the biographics
        Biographics updatedBiographics = biographicsRepository.findById(biographics.getId()).get();
        // Disconnect from session so that the updates on updatedBiographics are not directly saved in db
        em.detach(updatedBiographics);
        updatedBiographics
            .biographicsFirstname(UPDATED_BIOGRAPHICS_FIRSTNAME)
            .biographicsName(UPDATED_BIOGRAPHICS_NAME)
            .biographicsAge(UPDATED_BIOGRAPHICS_AGE)
            .biographicsGender(UPDATED_BIOGRAPHICS_GENDER)
            .biographicsPhoto(UPDATED_BIOGRAPHICS_PHOTO)
            .biographicsPhotoContentType(UPDATED_BIOGRAPHICS_PHOTO_CONTENT_TYPE)
            .biographicsCoordinates(UPDATED_BIOGRAPHICS_COORDINATES);
        BiographicsDTO biographicsDTO = biographicsMapper.toDto(updatedBiographics);

        restBiographicsMockMvc.perform(put("/api/biographics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(biographicsDTO)))
            .andExpect(status().isOk());

        // Validate the Biographics in the database
        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeUpdate);
        Biographics testBiographics = biographicsList.get(biographicsList.size() - 1);
        assertThat(testBiographics.getBiographicsFirstname()).isEqualTo(UPDATED_BIOGRAPHICS_FIRSTNAME);
        assertThat(testBiographics.getBiographicsName()).isEqualTo(UPDATED_BIOGRAPHICS_NAME);
        assertThat(testBiographics.getBiographicsAge()).isEqualTo(UPDATED_BIOGRAPHICS_AGE);
        assertThat(testBiographics.getBiographicsGender()).isEqualTo(UPDATED_BIOGRAPHICS_GENDER);
        assertThat(testBiographics.getBiographicsPhoto()).isEqualTo(UPDATED_BIOGRAPHICS_PHOTO);
        assertThat(testBiographics.getBiographicsPhotoContentType()).isEqualTo(UPDATED_BIOGRAPHICS_PHOTO_CONTENT_TYPE);
        assertThat(testBiographics.getBiographicsCoordinates()).isEqualTo(UPDATED_BIOGRAPHICS_COORDINATES);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(1)).save(testBiographics);
    }

    @Test
    @Transactional
    public void updateNonExistingBiographics() throws Exception {
        int databaseSizeBeforeUpdate = biographicsRepository.findAll().size();

        // Create the Biographics
        BiographicsDTO biographicsDTO = biographicsMapper.toDto(biographics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restBiographicsMockMvc.perform(put("/api/biographics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(biographicsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Biographics in the database
        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(0)).save(biographics);
    }

    @Test
    @Transactional
    public void deleteBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);

        int databaseSizeBeforeDelete = biographicsRepository.findAll().size();

        // Get the biographics
        restBiographicsMockMvc.perform(delete("/api/biographics/{id}", biographics.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Biographics> biographicsList = biographicsRepository.findAll();
        assertThat(biographicsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(1)).deleteById(biographics.getId());
    }

    @Test
    @Transactional
    public void searchBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.saveAndFlush(biographics);
        when(mockBiographicsSearchRepository.search(queryStringQuery("id:" + biographics.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(biographics), PageRequest.of(0, 1), 1));
        // Search the biographics
        restBiographicsMockMvc.perform(get("/api/_search/biographics?query=id:" + biographics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(biographics.getId().intValue())))
            .andExpect(jsonPath("$.[*].biographicsFirstname").value(hasItem(DEFAULT_BIOGRAPHICS_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsName").value(hasItem(DEFAULT_BIOGRAPHICS_NAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsAge").value(hasItem(DEFAULT_BIOGRAPHICS_AGE)))
            .andExpect(jsonPath("$.[*].biographicsGender").value(hasItem(DEFAULT_BIOGRAPHICS_GENDER.toString())))
            .andExpect(jsonPath("$.[*].biographicsPhotoContentType").value(hasItem(DEFAULT_BIOGRAPHICS_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].biographicsPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_PHOTO))))
            .andExpect(jsonPath("$.[*].biographicsCoordinates").value(hasItem(DEFAULT_BIOGRAPHICS_COORDINATES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Biographics.class);
        Biographics biographics1 = new Biographics();
        biographics1.setId(1L);
        Biographics biographics2 = new Biographics();
        biographics2.setId(biographics1.getId());
        assertThat(biographics1).isEqualTo(biographics2);
        biographics2.setId(2L);
        assertThat(biographics1).isNotEqualTo(biographics2);
        biographics1.setId(null);
        assertThat(biographics1).isNotEqualTo(biographics2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BiographicsDTO.class);
        BiographicsDTO biographicsDTO1 = new BiographicsDTO();
        biographicsDTO1.setId(1L);
        BiographicsDTO biographicsDTO2 = new BiographicsDTO();
        assertThat(biographicsDTO1).isNotEqualTo(biographicsDTO2);
        biographicsDTO2.setId(biographicsDTO1.getId());
        assertThat(biographicsDTO1).isEqualTo(biographicsDTO2);
        biographicsDTO2.setId(2L);
        assertThat(biographicsDTO1).isNotEqualTo(biographicsDTO2);
        biographicsDTO1.setId(null);
        assertThat(biographicsDTO1).isNotEqualTo(biographicsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(biographicsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(biographicsMapper.fromId(null)).isNull();
    }
}
