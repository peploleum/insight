package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.ObservedData;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.ObservedDataRepository;
import com.peploleum.insight.repository.search.ObservedDataSearchRepository;
import com.peploleum.insight.service.ObservedDataService;
import com.peploleum.insight.service.dto.ObservedDataDTO;
import com.peploleum.insight.service.mapper.ObservedDataMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.ObservedDataCriteria;
import com.peploleum.insight.service.ObservedDataQueryService;

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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.peploleum.insight.web.rest.TestUtil.sameInstant;
import static com.peploleum.insight.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ObservedDataResource REST controller.
 *
 * @see ObservedDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class ObservedDataResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJETS_OBSERVES = "AAAAAAAAAA";
    private static final String UPDATED_OBJETS_OBSERVES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_NOMBRE_JOURS = 1;
    private static final Integer UPDATED_NOMBRE_JOURS = 2;

    @Autowired
    private ObservedDataRepository observedDataRepository;


    @Autowired
    private ObservedDataMapper observedDataMapper;
    

    @Autowired
    private ObservedDataService observedDataService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.ObservedDataSearchRepositoryMockConfiguration
     */
    @Autowired
    private ObservedDataSearchRepository mockObservedDataSearchRepository;

    @Autowired
    private ObservedDataQueryService observedDataQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restObservedDataMockMvc;

    private ObservedData observedData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ObservedDataResource observedDataResource = new ObservedDataResource(observedDataService, observedDataQueryService);
        this.restObservedDataMockMvc = MockMvcBuilders.standaloneSetup(observedDataResource)
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
    public static ObservedData createEntity(EntityManager em) {
        ObservedData observedData = new ObservedData()
            .type(DEFAULT_TYPE)
            .objetsObserves(DEFAULT_OBJETS_OBSERVES)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .nombreJours(DEFAULT_NOMBRE_JOURS);
        return observedData;
    }

    @Before
    public void initTest() {
        observedData = createEntity(em);
    }

    @Test
    @Transactional
    public void createObservedData() throws Exception {
        int databaseSizeBeforeCreate = observedDataRepository.findAll().size();

        // Create the ObservedData
        ObservedDataDTO observedDataDTO = observedDataMapper.toDto(observedData);
        restObservedDataMockMvc.perform(post("/api/observed-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observedDataDTO)))
            .andExpect(status().isCreated());

        // Validate the ObservedData in the database
        List<ObservedData> observedDataList = observedDataRepository.findAll();
        assertThat(observedDataList).hasSize(databaseSizeBeforeCreate + 1);
        ObservedData testObservedData = observedDataList.get(observedDataList.size() - 1);
        assertThat(testObservedData.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testObservedData.getObjetsObserves()).isEqualTo(DEFAULT_OBJETS_OBSERVES);
        assertThat(testObservedData.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testObservedData.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testObservedData.getNombreJours()).isEqualTo(DEFAULT_NOMBRE_JOURS);

        // Validate the ObservedData in Elasticsearch
        verify(mockObservedDataSearchRepository, times(1)).save(testObservedData);
    }

    @Test
    @Transactional
    public void createObservedDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = observedDataRepository.findAll().size();

        // Create the ObservedData with an existing ID
        observedData.setId(1L);
        ObservedDataDTO observedDataDTO = observedDataMapper.toDto(observedData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restObservedDataMockMvc.perform(post("/api/observed-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observedDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ObservedData in the database
        List<ObservedData> observedDataList = observedDataRepository.findAll();
        assertThat(observedDataList).hasSize(databaseSizeBeforeCreate);

        // Validate the ObservedData in Elasticsearch
        verify(mockObservedDataSearchRepository, times(0)).save(observedData);
    }

    @Test
    @Transactional
    public void getAllObservedData() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList
        restObservedDataMockMvc.perform(get("/api/observed-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(observedData.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objetsObserves").value(hasItem(DEFAULT_OBJETS_OBSERVES.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].nombreJours").value(hasItem(DEFAULT_NOMBRE_JOURS)));
    }
    

    @Test
    @Transactional
    public void getObservedData() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get the observedData
        restObservedDataMockMvc.perform(get("/api/observed-data/{id}", observedData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(observedData.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.objetsObserves").value(DEFAULT_OBJETS_OBSERVES.toString()))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.nombreJours").value(DEFAULT_NOMBRE_JOURS));
    }

    @Test
    @Transactional
    public void getAllObservedDataByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where type equals to DEFAULT_TYPE
        defaultObservedDataShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the observedDataList where type equals to UPDATED_TYPE
        defaultObservedDataShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllObservedDataByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultObservedDataShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the observedDataList where type equals to UPDATED_TYPE
        defaultObservedDataShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllObservedDataByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where type is not null
        defaultObservedDataShouldBeFound("type.specified=true");

        // Get all the observedDataList where type is null
        defaultObservedDataShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservedDataByObjetsObservesIsEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where objetsObserves equals to DEFAULT_OBJETS_OBSERVES
        defaultObservedDataShouldBeFound("objetsObserves.equals=" + DEFAULT_OBJETS_OBSERVES);

        // Get all the observedDataList where objetsObserves equals to UPDATED_OBJETS_OBSERVES
        defaultObservedDataShouldNotBeFound("objetsObserves.equals=" + UPDATED_OBJETS_OBSERVES);
    }

    @Test
    @Transactional
    public void getAllObservedDataByObjetsObservesIsInShouldWork() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where objetsObserves in DEFAULT_OBJETS_OBSERVES or UPDATED_OBJETS_OBSERVES
        defaultObservedDataShouldBeFound("objetsObserves.in=" + DEFAULT_OBJETS_OBSERVES + "," + UPDATED_OBJETS_OBSERVES);

        // Get all the observedDataList where objetsObserves equals to UPDATED_OBJETS_OBSERVES
        defaultObservedDataShouldNotBeFound("objetsObserves.in=" + UPDATED_OBJETS_OBSERVES);
    }

    @Test
    @Transactional
    public void getAllObservedDataByObjetsObservesIsNullOrNotNull() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where objetsObserves is not null
        defaultObservedDataShouldBeFound("objetsObserves.specified=true");

        // Get all the observedDataList where objetsObserves is null
        defaultObservedDataShouldNotBeFound("objetsObserves.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultObservedDataShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the observedDataList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultObservedDataShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultObservedDataShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the observedDataList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultObservedDataShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateDebut is not null
        defaultObservedDataShouldBeFound("dateDebut.specified=true");

        // Get all the observedDataList where dateDebut is null
        defaultObservedDataShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateDebut greater than or equals to DEFAULT_DATE_DEBUT
        defaultObservedDataShouldBeFound("dateDebut.greaterOrEqualThan=" + DEFAULT_DATE_DEBUT);

        // Get all the observedDataList where dateDebut greater than or equals to UPDATED_DATE_DEBUT
        defaultObservedDataShouldNotBeFound("dateDebut.greaterOrEqualThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateDebut less than or equals to DEFAULT_DATE_DEBUT
        defaultObservedDataShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the observedDataList where dateDebut less than or equals to UPDATED_DATE_DEBUT
        defaultObservedDataShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllObservedDataByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateFin equals to DEFAULT_DATE_FIN
        defaultObservedDataShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the observedDataList where dateFin equals to UPDATED_DATE_FIN
        defaultObservedDataShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultObservedDataShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the observedDataList where dateFin equals to UPDATED_DATE_FIN
        defaultObservedDataShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateFin is not null
        defaultObservedDataShouldBeFound("dateFin.specified=true");

        // Get all the observedDataList where dateFin is null
        defaultObservedDataShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateFin greater than or equals to DEFAULT_DATE_FIN
        defaultObservedDataShouldBeFound("dateFin.greaterOrEqualThan=" + DEFAULT_DATE_FIN);

        // Get all the observedDataList where dateFin greater than or equals to UPDATED_DATE_FIN
        defaultObservedDataShouldNotBeFound("dateFin.greaterOrEqualThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllObservedDataByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where dateFin less than or equals to DEFAULT_DATE_FIN
        defaultObservedDataShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the observedDataList where dateFin less than or equals to UPDATED_DATE_FIN
        defaultObservedDataShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }


    @Test
    @Transactional
    public void getAllObservedDataByNombreJoursIsEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where nombreJours equals to DEFAULT_NOMBRE_JOURS
        defaultObservedDataShouldBeFound("nombreJours.equals=" + DEFAULT_NOMBRE_JOURS);

        // Get all the observedDataList where nombreJours equals to UPDATED_NOMBRE_JOURS
        defaultObservedDataShouldNotBeFound("nombreJours.equals=" + UPDATED_NOMBRE_JOURS);
    }

    @Test
    @Transactional
    public void getAllObservedDataByNombreJoursIsInShouldWork() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where nombreJours in DEFAULT_NOMBRE_JOURS or UPDATED_NOMBRE_JOURS
        defaultObservedDataShouldBeFound("nombreJours.in=" + DEFAULT_NOMBRE_JOURS + "," + UPDATED_NOMBRE_JOURS);

        // Get all the observedDataList where nombreJours equals to UPDATED_NOMBRE_JOURS
        defaultObservedDataShouldNotBeFound("nombreJours.in=" + UPDATED_NOMBRE_JOURS);
    }

    @Test
    @Transactional
    public void getAllObservedDataByNombreJoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where nombreJours is not null
        defaultObservedDataShouldBeFound("nombreJours.specified=true");

        // Get all the observedDataList where nombreJours is null
        defaultObservedDataShouldNotBeFound("nombreJours.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservedDataByNombreJoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where nombreJours greater than or equals to DEFAULT_NOMBRE_JOURS
        defaultObservedDataShouldBeFound("nombreJours.greaterOrEqualThan=" + DEFAULT_NOMBRE_JOURS);

        // Get all the observedDataList where nombreJours greater than or equals to UPDATED_NOMBRE_JOURS
        defaultObservedDataShouldNotBeFound("nombreJours.greaterOrEqualThan=" + UPDATED_NOMBRE_JOURS);
    }

    @Test
    @Transactional
    public void getAllObservedDataByNombreJoursIsLessThanSomething() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        // Get all the observedDataList where nombreJours less than or equals to DEFAULT_NOMBRE_JOURS
        defaultObservedDataShouldNotBeFound("nombreJours.lessThan=" + DEFAULT_NOMBRE_JOURS);

        // Get all the observedDataList where nombreJours less than or equals to UPDATED_NOMBRE_JOURS
        defaultObservedDataShouldBeFound("nombreJours.lessThan=" + UPDATED_NOMBRE_JOURS);
    }


    @Test
    @Transactional
    public void getAllObservedDataByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        observedData.setLinkOf(linkOf);
        observedDataRepository.saveAndFlush(observedData);
        Long linkOfId = linkOf.getId();

        // Get all the observedDataList where linkOf equals to linkOfId
        defaultObservedDataShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the observedDataList where linkOf equals to linkOfId + 1
        defaultObservedDataShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultObservedDataShouldBeFound(String filter) throws Exception {
        restObservedDataMockMvc.perform(get("/api/observed-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(observedData.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objetsObserves").value(hasItem(DEFAULT_OBJETS_OBSERVES.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].nombreJours").value(hasItem(DEFAULT_NOMBRE_JOURS)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultObservedDataShouldNotBeFound(String filter) throws Exception {
        restObservedDataMockMvc.perform(get("/api/observed-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingObservedData() throws Exception {
        // Get the observedData
        restObservedDataMockMvc.perform(get("/api/observed-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObservedData() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        int databaseSizeBeforeUpdate = observedDataRepository.findAll().size();

        // Update the observedData
        ObservedData updatedObservedData = observedDataRepository.findById(observedData.getId()).get();
        // Disconnect from session so that the updates on updatedObservedData are not directly saved in db
        em.detach(updatedObservedData);
        updatedObservedData
            .type(UPDATED_TYPE)
            .objetsObserves(UPDATED_OBJETS_OBSERVES)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .nombreJours(UPDATED_NOMBRE_JOURS);
        ObservedDataDTO observedDataDTO = observedDataMapper.toDto(updatedObservedData);

        restObservedDataMockMvc.perform(put("/api/observed-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observedDataDTO)))
            .andExpect(status().isOk());

        // Validate the ObservedData in the database
        List<ObservedData> observedDataList = observedDataRepository.findAll();
        assertThat(observedDataList).hasSize(databaseSizeBeforeUpdate);
        ObservedData testObservedData = observedDataList.get(observedDataList.size() - 1);
        assertThat(testObservedData.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testObservedData.getObjetsObserves()).isEqualTo(UPDATED_OBJETS_OBSERVES);
        assertThat(testObservedData.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testObservedData.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testObservedData.getNombreJours()).isEqualTo(UPDATED_NOMBRE_JOURS);

        // Validate the ObservedData in Elasticsearch
        verify(mockObservedDataSearchRepository, times(1)).save(testObservedData);
    }

    @Test
    @Transactional
    public void updateNonExistingObservedData() throws Exception {
        int databaseSizeBeforeUpdate = observedDataRepository.findAll().size();

        // Create the ObservedData
        ObservedDataDTO observedDataDTO = observedDataMapper.toDto(observedData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restObservedDataMockMvc.perform(put("/api/observed-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observedDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ObservedData in the database
        List<ObservedData> observedDataList = observedDataRepository.findAll();
        assertThat(observedDataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ObservedData in Elasticsearch
        verify(mockObservedDataSearchRepository, times(0)).save(observedData);
    }

    @Test
    @Transactional
    public void deleteObservedData() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);

        int databaseSizeBeforeDelete = observedDataRepository.findAll().size();

        // Get the observedData
        restObservedDataMockMvc.perform(delete("/api/observed-data/{id}", observedData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ObservedData> observedDataList = observedDataRepository.findAll();
        assertThat(observedDataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ObservedData in Elasticsearch
        verify(mockObservedDataSearchRepository, times(1)).deleteById(observedData.getId());
    }

    @Test
    @Transactional
    public void searchObservedData() throws Exception {
        // Initialize the database
        observedDataRepository.saveAndFlush(observedData);
        when(mockObservedDataSearchRepository.search(queryStringQuery("id:" + observedData.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(observedData), PageRequest.of(0, 1), 1));
        // Search the observedData
        restObservedDataMockMvc.perform(get("/api/_search/observed-data?query=id:" + observedData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(observedData.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objetsObserves").value(hasItem(DEFAULT_OBJETS_OBSERVES.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].nombreJours").value(hasItem(DEFAULT_NOMBRE_JOURS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObservedData.class);
        ObservedData observedData1 = new ObservedData();
        observedData1.setId(1L);
        ObservedData observedData2 = new ObservedData();
        observedData2.setId(observedData1.getId());
        assertThat(observedData1).isEqualTo(observedData2);
        observedData2.setId(2L);
        assertThat(observedData1).isNotEqualTo(observedData2);
        observedData1.setId(null);
        assertThat(observedData1).isNotEqualTo(observedData2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObservedDataDTO.class);
        ObservedDataDTO observedDataDTO1 = new ObservedDataDTO();
        observedDataDTO1.setId(1L);
        ObservedDataDTO observedDataDTO2 = new ObservedDataDTO();
        assertThat(observedDataDTO1).isNotEqualTo(observedDataDTO2);
        observedDataDTO2.setId(observedDataDTO1.getId());
        assertThat(observedDataDTO1).isEqualTo(observedDataDTO2);
        observedDataDTO2.setId(2L);
        assertThat(observedDataDTO1).isNotEqualTo(observedDataDTO2);
        observedDataDTO1.setId(null);
        assertThat(observedDataDTO1).isNotEqualTo(observedDataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(observedDataMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(observedDataMapper.fromId(null)).isNull();
    }
}
