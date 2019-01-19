package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.RawData;
import com.peploleum.insight.repository.RawDataRepository;
import com.peploleum.insight.repository.search.RawDataSearchRepository;
import com.peploleum.insight.service.RawDataService;
import com.peploleum.insight.service.dto.RawDataDTO;
import com.peploleum.insight.service.mapper.RawDataMapper;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.peploleum.insight.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RawDataResource REST controller.
 *
 * @see RawDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class RawDataResourceIntTest {

    private static final String DEFAULT_RAW_DATA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_SUB_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_SUB_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_SOURCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_SOURCE_URI = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_SOURCE_URI = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_SOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_SOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_RAW_DATA_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RAW_DATA_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RAW_DATA_EXTRACTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RAW_DATA_EXTRACTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RAW_DATA_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_SYMBOL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_RAW_DATA_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RAW_DATA_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RAW_DATA_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RAW_DATA_DATA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_RAW_DATA_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_COORDINATES = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_DATA_ANNOTATIONS = "AAAAAAAAAA";
    private static final String UPDATED_RAW_DATA_ANNOTATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    @Autowired
    private RawDataRepository rawDataRepository;

    @Autowired
    private RawDataMapper rawDataMapper;

    @Autowired
    private RawDataService rawDataService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.RawDataSearchRepositoryMockConfiguration
     */
    @Autowired
    private RawDataSearchRepository mockRawDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restRawDataMockMvc;

    private RawData rawData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RawDataResource rawDataResource = new RawDataResource(rawDataService);
        this.restRawDataMockMvc = MockMvcBuilders.standaloneSetup(rawDataResource)
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
    public static RawData createEntity() {
        RawData rawData = new RawData()
            .rawDataName(DEFAULT_RAW_DATA_NAME)
            .rawDataType(DEFAULT_RAW_DATA_TYPE)
            .rawDataSubType(DEFAULT_RAW_DATA_SUB_TYPE)
            .rawDataSourceName(DEFAULT_RAW_DATA_SOURCE_NAME)
            .rawDataSourceUri(DEFAULT_RAW_DATA_SOURCE_URI)
            .rawDataSourceType(DEFAULT_RAW_DATA_SOURCE_TYPE)
            .rawDataContent(DEFAULT_RAW_DATA_CONTENT)
            .rawDataCreationDate(DEFAULT_RAW_DATA_CREATION_DATE)
            .rawDataExtractedDate(DEFAULT_RAW_DATA_EXTRACTED_DATE)
            .rawDataSymbol(DEFAULT_RAW_DATA_SYMBOL)
            .rawDataData(DEFAULT_RAW_DATA_DATA)
            .rawDataDataContentType(DEFAULT_RAW_DATA_DATA_CONTENT_TYPE)
            .rawDataCoordinates(DEFAULT_RAW_DATA_COORDINATES)
            .rawDataAnnotations(DEFAULT_RAW_DATA_ANNOTATIONS)
            .externalId(DEFAULT_EXTERNAL_ID);
        return rawData;
    }

    @Before
    public void initTest() {
        rawDataRepository.deleteAll();
        rawData = createEntity();
    }

    @Test
    public void createRawData() throws Exception {
        int databaseSizeBeforeCreate = rawDataRepository.findAll().size();

        // Create the RawData
        RawDataDTO rawDataDTO = rawDataMapper.toDto(rawData);
        restRawDataMockMvc.perform(post("/api/raw-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawDataDTO)))
            .andExpect(status().isCreated());

        // Validate the RawData in the database
        List<RawData> rawDataList = rawDataRepository.findAll();
        assertThat(rawDataList).hasSize(databaseSizeBeforeCreate + 1);
        RawData testRawData = rawDataList.get(rawDataList.size() - 1);
        assertThat(testRawData.getRawDataName()).isEqualTo(DEFAULT_RAW_DATA_NAME);
        assertThat(testRawData.getRawDataType()).isEqualTo(DEFAULT_RAW_DATA_TYPE);
        assertThat(testRawData.getRawDataSubType()).isEqualTo(DEFAULT_RAW_DATA_SUB_TYPE);
        assertThat(testRawData.getRawDataSourceName()).isEqualTo(DEFAULT_RAW_DATA_SOURCE_NAME);
        assertThat(testRawData.getRawDataSourceUri()).isEqualTo(DEFAULT_RAW_DATA_SOURCE_URI);
        assertThat(testRawData.getRawDataSourceType()).isEqualTo(DEFAULT_RAW_DATA_SOURCE_TYPE);
        assertThat(testRawData.getRawDataContent()).isEqualTo(DEFAULT_RAW_DATA_CONTENT);
        assertThat(testRawData.getRawDataCreationDate()).isEqualTo(DEFAULT_RAW_DATA_CREATION_DATE);
        assertThat(testRawData.getRawDataExtractedDate()).isEqualTo(DEFAULT_RAW_DATA_EXTRACTED_DATE);
        assertThat(testRawData.getRawDataSymbol()).isEqualTo(DEFAULT_RAW_DATA_SYMBOL);
        assertThat(testRawData.getRawDataData()).isEqualTo(DEFAULT_RAW_DATA_DATA);
        assertThat(testRawData.getRawDataDataContentType()).isEqualTo(DEFAULT_RAW_DATA_DATA_CONTENT_TYPE);
        assertThat(testRawData.getRawDataCoordinates()).isEqualTo(DEFAULT_RAW_DATA_COORDINATES);
        assertThat(testRawData.getRawDataAnnotations()).isEqualTo(DEFAULT_RAW_DATA_ANNOTATIONS);
        assertThat(testRawData.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);

        // Validate the RawData in Elasticsearch
        verify(mockRawDataSearchRepository, times(1)).save(testRawData);
    }

    @Test
    public void createRawDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rawDataRepository.findAll().size();

        // Create the RawData with an existing ID
        rawData.setId("existing_id");
        RawDataDTO rawDataDTO = rawDataMapper.toDto(rawData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRawDataMockMvc.perform(post("/api/raw-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RawData in the database
        List<RawData> rawDataList = rawDataRepository.findAll();
        assertThat(rawDataList).hasSize(databaseSizeBeforeCreate);

        // Validate the RawData in Elasticsearch
        verify(mockRawDataSearchRepository, times(0)).save(rawData);
    }

    @Test
    public void checkRawDataNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rawDataRepository.findAll().size();
        // set the field null
        rawData.setRawDataName(null);

        // Create the RawData, which fails.
        RawDataDTO rawDataDTO = rawDataMapper.toDto(rawData);

        restRawDataMockMvc.perform(post("/api/raw-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawDataDTO)))
            .andExpect(status().isBadRequest());

        List<RawData> rawDataList = rawDataRepository.findAll();
        assertThat(rawDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllRawData() throws Exception {
        // Initialize the database
        rawDataRepository.save(rawData);

        // Get all the rawDataList
        restRawDataMockMvc.perform(get("/api/raw-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rawData.getId())))
            .andExpect(jsonPath("$.[*].rawDataName").value(hasItem(DEFAULT_RAW_DATA_NAME.toString())))
            .andExpect(jsonPath("$.[*].rawDataType").value(hasItem(DEFAULT_RAW_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rawDataSubType").value(hasItem(DEFAULT_RAW_DATA_SUB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rawDataSourceName").value(hasItem(DEFAULT_RAW_DATA_SOURCE_NAME.toString())))
            .andExpect(jsonPath("$.[*].rawDataSourceUri").value(hasItem(DEFAULT_RAW_DATA_SOURCE_URI.toString())))
            .andExpect(jsonPath("$.[*].rawDataSourceType").value(hasItem(DEFAULT_RAW_DATA_SOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rawDataContent").value(hasItem(DEFAULT_RAW_DATA_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].rawDataCreationDate").value(hasItem(DEFAULT_RAW_DATA_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].rawDataExtractedDate").value(hasItem(DEFAULT_RAW_DATA_EXTRACTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rawDataSymbol").value(hasItem(DEFAULT_RAW_DATA_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].rawDataDataContentType").value(hasItem(DEFAULT_RAW_DATA_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].rawDataData").value(hasItem(Base64Utils.encodeToString(DEFAULT_RAW_DATA_DATA))))
            .andExpect(jsonPath("$.[*].rawDataCoordinates").value(hasItem(DEFAULT_RAW_DATA_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].rawDataAnnotations").value(hasItem(DEFAULT_RAW_DATA_ANNOTATIONS.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID.toString())));
    }
    
    @Test
    public void getRawData() throws Exception {
        // Initialize the database
        rawDataRepository.save(rawData);

        // Get the rawData
        restRawDataMockMvc.perform(get("/api/raw-data/{id}", rawData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rawData.getId()))
            .andExpect(jsonPath("$.rawDataName").value(DEFAULT_RAW_DATA_NAME.toString()))
            .andExpect(jsonPath("$.rawDataType").value(DEFAULT_RAW_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.rawDataSubType").value(DEFAULT_RAW_DATA_SUB_TYPE.toString()))
            .andExpect(jsonPath("$.rawDataSourceName").value(DEFAULT_RAW_DATA_SOURCE_NAME.toString()))
            .andExpect(jsonPath("$.rawDataSourceUri").value(DEFAULT_RAW_DATA_SOURCE_URI.toString()))
            .andExpect(jsonPath("$.rawDataSourceType").value(DEFAULT_RAW_DATA_SOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.rawDataContent").value(DEFAULT_RAW_DATA_CONTENT.toString()))
            .andExpect(jsonPath("$.rawDataCreationDate").value(DEFAULT_RAW_DATA_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.rawDataExtractedDate").value(DEFAULT_RAW_DATA_EXTRACTED_DATE.toString()))
            .andExpect(jsonPath("$.rawDataSymbol").value(DEFAULT_RAW_DATA_SYMBOL.toString()))
            .andExpect(jsonPath("$.rawDataDataContentType").value(DEFAULT_RAW_DATA_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.rawDataData").value(Base64Utils.encodeToString(DEFAULT_RAW_DATA_DATA)))
            .andExpect(jsonPath("$.rawDataCoordinates").value(DEFAULT_RAW_DATA_COORDINATES.toString()))
            .andExpect(jsonPath("$.rawDataAnnotations").value(DEFAULT_RAW_DATA_ANNOTATIONS.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.toString()));
    }

    @Test
    public void getNonExistingRawData() throws Exception {
        // Get the rawData
        restRawDataMockMvc.perform(get("/api/raw-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRawData() throws Exception {
        // Initialize the database
        rawDataRepository.save(rawData);

        int databaseSizeBeforeUpdate = rawDataRepository.findAll().size();

        // Update the rawData
        RawData updatedRawData = rawDataRepository.findById(rawData.getId()).get();
        updatedRawData
            .rawDataName(UPDATED_RAW_DATA_NAME)
            .rawDataType(UPDATED_RAW_DATA_TYPE)
            .rawDataSubType(UPDATED_RAW_DATA_SUB_TYPE)
            .rawDataSourceName(UPDATED_RAW_DATA_SOURCE_NAME)
            .rawDataSourceUri(UPDATED_RAW_DATA_SOURCE_URI)
            .rawDataSourceType(UPDATED_RAW_DATA_SOURCE_TYPE)
            .rawDataContent(UPDATED_RAW_DATA_CONTENT)
            .rawDataCreationDate(UPDATED_RAW_DATA_CREATION_DATE)
            .rawDataExtractedDate(UPDATED_RAW_DATA_EXTRACTED_DATE)
            .rawDataSymbol(UPDATED_RAW_DATA_SYMBOL)
            .rawDataData(UPDATED_RAW_DATA_DATA)
            .rawDataDataContentType(UPDATED_RAW_DATA_DATA_CONTENT_TYPE)
            .rawDataCoordinates(UPDATED_RAW_DATA_COORDINATES)
            .rawDataAnnotations(UPDATED_RAW_DATA_ANNOTATIONS)
            .externalId(UPDATED_EXTERNAL_ID);
        RawDataDTO rawDataDTO = rawDataMapper.toDto(updatedRawData);

        restRawDataMockMvc.perform(put("/api/raw-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawDataDTO)))
            .andExpect(status().isOk());

        // Validate the RawData in the database
        List<RawData> rawDataList = rawDataRepository.findAll();
        assertThat(rawDataList).hasSize(databaseSizeBeforeUpdate);
        RawData testRawData = rawDataList.get(rawDataList.size() - 1);
        assertThat(testRawData.getRawDataName()).isEqualTo(UPDATED_RAW_DATA_NAME);
        assertThat(testRawData.getRawDataType()).isEqualTo(UPDATED_RAW_DATA_TYPE);
        assertThat(testRawData.getRawDataSubType()).isEqualTo(UPDATED_RAW_DATA_SUB_TYPE);
        assertThat(testRawData.getRawDataSourceName()).isEqualTo(UPDATED_RAW_DATA_SOURCE_NAME);
        assertThat(testRawData.getRawDataSourceUri()).isEqualTo(UPDATED_RAW_DATA_SOURCE_URI);
        assertThat(testRawData.getRawDataSourceType()).isEqualTo(UPDATED_RAW_DATA_SOURCE_TYPE);
        assertThat(testRawData.getRawDataContent()).isEqualTo(UPDATED_RAW_DATA_CONTENT);
        assertThat(testRawData.getRawDataCreationDate()).isEqualTo(UPDATED_RAW_DATA_CREATION_DATE);
        assertThat(testRawData.getRawDataExtractedDate()).isEqualTo(UPDATED_RAW_DATA_EXTRACTED_DATE);
        assertThat(testRawData.getRawDataSymbol()).isEqualTo(UPDATED_RAW_DATA_SYMBOL);
        assertThat(testRawData.getRawDataData()).isEqualTo(UPDATED_RAW_DATA_DATA);
        assertThat(testRawData.getRawDataDataContentType()).isEqualTo(UPDATED_RAW_DATA_DATA_CONTENT_TYPE);
        assertThat(testRawData.getRawDataCoordinates()).isEqualTo(UPDATED_RAW_DATA_COORDINATES);
        assertThat(testRawData.getRawDataAnnotations()).isEqualTo(UPDATED_RAW_DATA_ANNOTATIONS);
        assertThat(testRawData.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);

        // Validate the RawData in Elasticsearch
        verify(mockRawDataSearchRepository, times(1)).save(testRawData);
    }

    @Test
    public void updateNonExistingRawData() throws Exception {
        int databaseSizeBeforeUpdate = rawDataRepository.findAll().size();

        // Create the RawData
        RawDataDTO rawDataDTO = rawDataMapper.toDto(rawData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRawDataMockMvc.perform(put("/api/raw-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RawData in the database
        List<RawData> rawDataList = rawDataRepository.findAll();
        assertThat(rawDataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RawData in Elasticsearch
        verify(mockRawDataSearchRepository, times(0)).save(rawData);
    }

    @Test
    public void deleteRawData() throws Exception {
        // Initialize the database
        rawDataRepository.save(rawData);

        int databaseSizeBeforeDelete = rawDataRepository.findAll().size();

        // Get the rawData
        restRawDataMockMvc.perform(delete("/api/raw-data/{id}", rawData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RawData> rawDataList = rawDataRepository.findAll();
        assertThat(rawDataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RawData in Elasticsearch
        verify(mockRawDataSearchRepository, times(1)).deleteById(rawData.getId());
    }

    @Test
    public void searchRawData() throws Exception {
        // Initialize the database
        rawDataRepository.save(rawData);
        when(mockRawDataSearchRepository.search(queryStringQuery("id:" + rawData.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rawData), PageRequest.of(0, 1), 1));
        // Search the rawData
        restRawDataMockMvc.perform(get("/api/_search/raw-data?query=id:" + rawData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rawData.getId())))
            .andExpect(jsonPath("$.[*].rawDataName").value(hasItem(DEFAULT_RAW_DATA_NAME)))
            .andExpect(jsonPath("$.[*].rawDataType").value(hasItem(DEFAULT_RAW_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].rawDataSubType").value(hasItem(DEFAULT_RAW_DATA_SUB_TYPE)))
            .andExpect(jsonPath("$.[*].rawDataSourceName").value(hasItem(DEFAULT_RAW_DATA_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].rawDataSourceUri").value(hasItem(DEFAULT_RAW_DATA_SOURCE_URI)))
            .andExpect(jsonPath("$.[*].rawDataSourceType").value(hasItem(DEFAULT_RAW_DATA_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].rawDataContent").value(hasItem(DEFAULT_RAW_DATA_CONTENT)))
            .andExpect(jsonPath("$.[*].rawDataCreationDate").value(hasItem(DEFAULT_RAW_DATA_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].rawDataExtractedDate").value(hasItem(DEFAULT_RAW_DATA_EXTRACTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rawDataSymbol").value(hasItem(DEFAULT_RAW_DATA_SYMBOL)))
            .andExpect(jsonPath("$.[*].rawDataDataContentType").value(hasItem(DEFAULT_RAW_DATA_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].rawDataData").value(hasItem(Base64Utils.encodeToString(DEFAULT_RAW_DATA_DATA))))
            .andExpect(jsonPath("$.[*].rawDataCoordinates").value(hasItem(DEFAULT_RAW_DATA_COORDINATES)))
            .andExpect(jsonPath("$.[*].rawDataAnnotations").value(hasItem(DEFAULT_RAW_DATA_ANNOTATIONS)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawData.class);
        RawData rawData1 = new RawData();
        rawData1.setId("id1");
        RawData rawData2 = new RawData();
        rawData2.setId(rawData1.getId());
        assertThat(rawData1).isEqualTo(rawData2);
        rawData2.setId("id2");
        assertThat(rawData1).isNotEqualTo(rawData2);
        rawData1.setId(null);
        assertThat(rawData1).isNotEqualTo(rawData2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawDataDTO.class);
        RawDataDTO rawDataDTO1 = new RawDataDTO();
        rawDataDTO1.setId("id1");
        RawDataDTO rawDataDTO2 = new RawDataDTO();
        assertThat(rawDataDTO1).isNotEqualTo(rawDataDTO2);
        rawDataDTO2.setId(rawDataDTO1.getId());
        assertThat(rawDataDTO1).isEqualTo(rawDataDTO2);
        rawDataDTO2.setId("id2");
        assertThat(rawDataDTO1).isNotEqualTo(rawDataDTO2);
        rawDataDTO1.setId(null);
        assertThat(rawDataDTO1).isNotEqualTo(rawDataDTO2);
    }
}
