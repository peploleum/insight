package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.repository.BiographicsRepository;
import com.peploleum.insight.repository.search.BiographicsSearchRepository;
import com.peploleum.insight.service.BiographicsService;
import com.peploleum.insight.service.dto.BiographicsDTO;
import com.peploleum.insight.service.mapper.BiographicsMapper;
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

    private static final byte[] DEFAULT_BIOGRAPHICS_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BIOGRAPHICS_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BIOGRAPHICS_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BIOGRAPHICS_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BIOGRAPHICS_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHICS_COORDINATES = "BBBBBBBBBB";

    private static final String DEFAULT_BIOGRAPHICS_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHICS_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    @Autowired
    private BiographicsRepository biographicsRepository;

    @Autowired
    private BiographicsMapper biographicsMapper;

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
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restBiographicsMockMvc;

    private Biographics biographics;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BiographicsResource biographicsResource = new BiographicsResource(biographicsService);
        this.restBiographicsMockMvc = MockMvcBuilders.standaloneSetup(biographicsResource)
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
    public static Biographics createEntity() {
        Biographics biographics = new Biographics()
            .biographicsFirstname(DEFAULT_BIOGRAPHICS_FIRSTNAME)
            .biographicsName(DEFAULT_BIOGRAPHICS_NAME)
            .biographicsAge(DEFAULT_BIOGRAPHICS_AGE)
            .biographicsGender(DEFAULT_BIOGRAPHICS_GENDER)
            .biographicsImage(DEFAULT_BIOGRAPHICS_IMAGE)
            .biographicsImageContentType(DEFAULT_BIOGRAPHICS_IMAGE_CONTENT_TYPE)
            .biographicsCoordinates(DEFAULT_BIOGRAPHICS_COORDINATES)
            .biographicsSymbol(DEFAULT_BIOGRAPHICS_SYMBOL)
            .externalId(DEFAULT_EXTERNAL_ID);
        return biographics;
    }

    @Before
    public void initTest() {
        biographicsRepository.deleteAll();
        biographics = createEntity();
    }

    @Test
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
        assertThat(testBiographics.getBiographicsImage()).isEqualTo(DEFAULT_BIOGRAPHICS_IMAGE);
        assertThat(testBiographics.getBiographicsImageContentType()).isEqualTo(DEFAULT_BIOGRAPHICS_IMAGE_CONTENT_TYPE);
        assertThat(testBiographics.getBiographicsCoordinates()).isEqualTo(DEFAULT_BIOGRAPHICS_COORDINATES);
        assertThat(testBiographics.getBiographicsSymbol()).isEqualTo(DEFAULT_BIOGRAPHICS_SYMBOL);
        assertThat(testBiographics.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(1)).save(testBiographics);
    }

    @Test
    public void createBiographicsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = biographicsRepository.findAll().size();

        // Create the Biographics with an existing ID
        biographics.setId("existing_id");
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
    public void getAllBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.save(biographics);

        // Get all the biographicsList
        restBiographicsMockMvc.perform(get("/api/biographics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(biographics.getId())))
            .andExpect(jsonPath("$.[*].biographicsFirstname").value(hasItem(DEFAULT_BIOGRAPHICS_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsName").value(hasItem(DEFAULT_BIOGRAPHICS_NAME.toString())))
            .andExpect(jsonPath("$.[*].biographicsAge").value(hasItem(DEFAULT_BIOGRAPHICS_AGE)))
            .andExpect(jsonPath("$.[*].biographicsGender").value(hasItem(DEFAULT_BIOGRAPHICS_GENDER.toString())))
            .andExpect(jsonPath("$.[*].biographicsImageContentType").value(hasItem(DEFAULT_BIOGRAPHICS_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].biographicsImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_IMAGE))))
            .andExpect(jsonPath("$.[*].biographicsCoordinates").value(hasItem(DEFAULT_BIOGRAPHICS_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].biographicsSymbol").value(hasItem(DEFAULT_BIOGRAPHICS_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID.toString())));
    }
    
    @Test
    public void getBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.save(biographics);

        // Get the biographics
        restBiographicsMockMvc.perform(get("/api/biographics/{id}", biographics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(biographics.getId()))
            .andExpect(jsonPath("$.biographicsFirstname").value(DEFAULT_BIOGRAPHICS_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.biographicsName").value(DEFAULT_BIOGRAPHICS_NAME.toString()))
            .andExpect(jsonPath("$.biographicsAge").value(DEFAULT_BIOGRAPHICS_AGE))
            .andExpect(jsonPath("$.biographicsGender").value(DEFAULT_BIOGRAPHICS_GENDER.toString()))
            .andExpect(jsonPath("$.biographicsImageContentType").value(DEFAULT_BIOGRAPHICS_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.biographicsImage").value(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_IMAGE)))
            .andExpect(jsonPath("$.biographicsCoordinates").value(DEFAULT_BIOGRAPHICS_COORDINATES.toString()))
            .andExpect(jsonPath("$.biographicsSymbol").value(DEFAULT_BIOGRAPHICS_SYMBOL.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.toString()));
    }

    @Test
    public void getNonExistingBiographics() throws Exception {
        // Get the biographics
        restBiographicsMockMvc.perform(get("/api/biographics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.save(biographics);

        int databaseSizeBeforeUpdate = biographicsRepository.findAll().size();

        // Update the biographics
        Biographics updatedBiographics = biographicsRepository.findById(biographics.getId()).get();
        updatedBiographics
            .biographicsFirstname(UPDATED_BIOGRAPHICS_FIRSTNAME)
            .biographicsName(UPDATED_BIOGRAPHICS_NAME)
            .biographicsAge(UPDATED_BIOGRAPHICS_AGE)
            .biographicsGender(UPDATED_BIOGRAPHICS_GENDER)
            .biographicsImage(UPDATED_BIOGRAPHICS_IMAGE)
            .biographicsImageContentType(UPDATED_BIOGRAPHICS_IMAGE_CONTENT_TYPE)
            .biographicsCoordinates(UPDATED_BIOGRAPHICS_COORDINATES)
            .biographicsSymbol(UPDATED_BIOGRAPHICS_SYMBOL)
            .externalId(UPDATED_EXTERNAL_ID);
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
        assertThat(testBiographics.getBiographicsImage()).isEqualTo(UPDATED_BIOGRAPHICS_IMAGE);
        assertThat(testBiographics.getBiographicsImageContentType()).isEqualTo(UPDATED_BIOGRAPHICS_IMAGE_CONTENT_TYPE);
        assertThat(testBiographics.getBiographicsCoordinates()).isEqualTo(UPDATED_BIOGRAPHICS_COORDINATES);
        assertThat(testBiographics.getBiographicsSymbol()).isEqualTo(UPDATED_BIOGRAPHICS_SYMBOL);
        assertThat(testBiographics.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);

        // Validate the Biographics in Elasticsearch
        verify(mockBiographicsSearchRepository, times(1)).save(testBiographics);
    }

    @Test
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
    public void deleteBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.save(biographics);

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
    public void searchBiographics() throws Exception {
        // Initialize the database
        biographicsRepository.save(biographics);
        when(mockBiographicsSearchRepository.search(queryStringQuery("id:" + biographics.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(biographics), PageRequest.of(0, 1), 1));
        // Search the biographics
        restBiographicsMockMvc.perform(get("/api/_search/biographics?query=id:" + biographics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(biographics.getId())))
            .andExpect(jsonPath("$.[*].biographicsFirstname").value(hasItem(DEFAULT_BIOGRAPHICS_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].biographicsName").value(hasItem(DEFAULT_BIOGRAPHICS_NAME)))
            .andExpect(jsonPath("$.[*].biographicsAge").value(hasItem(DEFAULT_BIOGRAPHICS_AGE)))
            .andExpect(jsonPath("$.[*].biographicsGender").value(hasItem(DEFAULT_BIOGRAPHICS_GENDER.toString())))
            .andExpect(jsonPath("$.[*].biographicsImageContentType").value(hasItem(DEFAULT_BIOGRAPHICS_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].biographicsImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_BIOGRAPHICS_IMAGE))))
            .andExpect(jsonPath("$.[*].biographicsCoordinates").value(hasItem(DEFAULT_BIOGRAPHICS_COORDINATES)))
            .andExpect(jsonPath("$.[*].biographicsSymbol").value(hasItem(DEFAULT_BIOGRAPHICS_SYMBOL)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Biographics.class);
        Biographics biographics1 = new Biographics();
        biographics1.setId("id1");
        Biographics biographics2 = new Biographics();
        biographics2.setId(biographics1.getId());
        assertThat(biographics1).isEqualTo(biographics2);
        biographics2.setId("id2");
        assertThat(biographics1).isNotEqualTo(biographics2);
        biographics1.setId(null);
        assertThat(biographics1).isNotEqualTo(biographics2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BiographicsDTO.class);
        BiographicsDTO biographicsDTO1 = new BiographicsDTO();
        biographicsDTO1.setId("id1");
        BiographicsDTO biographicsDTO2 = new BiographicsDTO();
        assertThat(biographicsDTO1).isNotEqualTo(biographicsDTO2);
        biographicsDTO2.setId(biographicsDTO1.getId());
        assertThat(biographicsDTO1).isEqualTo(biographicsDTO2);
        biographicsDTO2.setId("id2");
        assertThat(biographicsDTO1).isNotEqualTo(biographicsDTO2);
        biographicsDTO1.setId(null);
        assertThat(biographicsDTO1).isNotEqualTo(biographicsDTO2);
    }
}
