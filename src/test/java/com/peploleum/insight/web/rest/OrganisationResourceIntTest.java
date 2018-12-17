package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.repository.OrganisationRepository;
import com.peploleum.insight.repository.search.OrganisationSearchRepository;
import com.peploleum.insight.service.OrganisationService;
import com.peploleum.insight.service.dto.OrganisationDTO;
import com.peploleum.insight.service.mapper.OrganisationMapper;
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

    private static final String DEFAULT_ORGANISATION_DESCRPTION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_DESCRPTION = "BBBBBBBBBB";

    private static final Size DEFAULT_ORGANISATION_SIZE = Size.SMALL;
    private static final Size UPDATED_ORGANISATION_SIZE = Size.MEDIUM;

    private static final String DEFAULT_ORGANISATION_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_COORDINATES = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ORGANISATION_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ORGANISATION_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ORGANISATION_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ORGANISATION_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ORGANISATION_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION_SYMBOL = "BBBBBBBBBB";

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private OrganisationMapper organisationMapper;

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
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restOrganisationMockMvc;

    private Organisation organisation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganisationResource organisationResource = new OrganisationResource(organisationService);
        this.restOrganisationMockMvc = MockMvcBuilders.standaloneSetup(organisationResource)
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
    public static Organisation createEntity() {
        Organisation organisation = new Organisation()
            .organisationName(DEFAULT_ORGANISATION_NAME)
            .organisationDescrption(DEFAULT_ORGANISATION_DESCRPTION)
            .organisationSize(DEFAULT_ORGANISATION_SIZE)
            .organisationCoordinates(DEFAULT_ORGANISATION_COORDINATES)
            .organisationImage(DEFAULT_ORGANISATION_IMAGE)
            .organisationImageContentType(DEFAULT_ORGANISATION_IMAGE_CONTENT_TYPE)
            .organisationSymbol(DEFAULT_ORGANISATION_SYMBOL);
        return organisation;
    }

    @Before
    public void initTest() {
        organisationRepository.deleteAll();
        organisation = createEntity();
    }

    @Test
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
        assertThat(testOrganisation.getOrganisationDescrption()).isEqualTo(DEFAULT_ORGANISATION_DESCRPTION);
        assertThat(testOrganisation.getOrganisationSize()).isEqualTo(DEFAULT_ORGANISATION_SIZE);
        assertThat(testOrganisation.getOrganisationCoordinates()).isEqualTo(DEFAULT_ORGANISATION_COORDINATES);
        assertThat(testOrganisation.getOrganisationImage()).isEqualTo(DEFAULT_ORGANISATION_IMAGE);
        assertThat(testOrganisation.getOrganisationImageContentType()).isEqualTo(DEFAULT_ORGANISATION_IMAGE_CONTENT_TYPE);
        assertThat(testOrganisation.getOrganisationSymbol()).isEqualTo(DEFAULT_ORGANISATION_SYMBOL);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(1)).save(testOrganisation);
    }

    @Test
    public void createOrganisationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organisationRepository.findAll().size();

        // Create the Organisation with an existing ID
        organisation.setId("existing_id");
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
    public void getAllOrganisations() throws Exception {
        // Initialize the database
        organisationRepository.save(organisation);

        // Get all the organisationList
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId())))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].organisationDescrption").value(hasItem(DEFAULT_ORGANISATION_DESCRPTION.toString())))
            .andExpect(jsonPath("$.[*].organisationSize").value(hasItem(DEFAULT_ORGANISATION_SIZE.toString())))
            .andExpect(jsonPath("$.[*].organisationCoordinates").value(hasItem(DEFAULT_ORGANISATION_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].organisationImageContentType").value(hasItem(DEFAULT_ORGANISATION_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].organisationImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ORGANISATION_IMAGE))))
            .andExpect(jsonPath("$.[*].organisationSymbol").value(hasItem(DEFAULT_ORGANISATION_SYMBOL.toString())));
    }
    
    @Test
    public void getOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.save(organisation);

        // Get the organisation
        restOrganisationMockMvc.perform(get("/api/organisations/{id}", organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organisation.getId()))
            .andExpect(jsonPath("$.organisationName").value(DEFAULT_ORGANISATION_NAME.toString()))
            .andExpect(jsonPath("$.organisationDescrption").value(DEFAULT_ORGANISATION_DESCRPTION.toString()))
            .andExpect(jsonPath("$.organisationSize").value(DEFAULT_ORGANISATION_SIZE.toString()))
            .andExpect(jsonPath("$.organisationCoordinates").value(DEFAULT_ORGANISATION_COORDINATES.toString()))
            .andExpect(jsonPath("$.organisationImageContentType").value(DEFAULT_ORGANISATION_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.organisationImage").value(Base64Utils.encodeToString(DEFAULT_ORGANISATION_IMAGE)))
            .andExpect(jsonPath("$.organisationSymbol").value(DEFAULT_ORGANISATION_SYMBOL.toString()));
    }

    @Test
    public void getNonExistingOrganisation() throws Exception {
        // Get the organisation
        restOrganisationMockMvc.perform(get("/api/organisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.save(organisation);

        int databaseSizeBeforeUpdate = organisationRepository.findAll().size();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findById(organisation.getId()).get();
        updatedOrganisation
            .organisationName(UPDATED_ORGANISATION_NAME)
            .organisationDescrption(UPDATED_ORGANISATION_DESCRPTION)
            .organisationSize(UPDATED_ORGANISATION_SIZE)
            .organisationCoordinates(UPDATED_ORGANISATION_COORDINATES)
            .organisationImage(UPDATED_ORGANISATION_IMAGE)
            .organisationImageContentType(UPDATED_ORGANISATION_IMAGE_CONTENT_TYPE)
            .organisationSymbol(UPDATED_ORGANISATION_SYMBOL);
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
        assertThat(testOrganisation.getOrganisationDescrption()).isEqualTo(UPDATED_ORGANISATION_DESCRPTION);
        assertThat(testOrganisation.getOrganisationSize()).isEqualTo(UPDATED_ORGANISATION_SIZE);
        assertThat(testOrganisation.getOrganisationCoordinates()).isEqualTo(UPDATED_ORGANISATION_COORDINATES);
        assertThat(testOrganisation.getOrganisationImage()).isEqualTo(UPDATED_ORGANISATION_IMAGE);
        assertThat(testOrganisation.getOrganisationImageContentType()).isEqualTo(UPDATED_ORGANISATION_IMAGE_CONTENT_TYPE);
        assertThat(testOrganisation.getOrganisationSymbol()).isEqualTo(UPDATED_ORGANISATION_SYMBOL);

        // Validate the Organisation in Elasticsearch
        verify(mockOrganisationSearchRepository, times(1)).save(testOrganisation);
    }

    @Test
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
    public void deleteOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.save(organisation);

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
    public void searchOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.save(organisation);
        when(mockOrganisationSearchRepository.search(queryStringQuery("id:" + organisation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(organisation), PageRequest.of(0, 1), 1));
        // Search the organisation
        restOrganisationMockMvc.perform(get("/api/_search/organisations?query=id:" + organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId())))
            .andExpect(jsonPath("$.[*].organisationName").value(hasItem(DEFAULT_ORGANISATION_NAME)))
            .andExpect(jsonPath("$.[*].organisationDescrption").value(hasItem(DEFAULT_ORGANISATION_DESCRPTION)))
            .andExpect(jsonPath("$.[*].organisationSize").value(hasItem(DEFAULT_ORGANISATION_SIZE.toString())))
            .andExpect(jsonPath("$.[*].organisationCoordinates").value(hasItem(DEFAULT_ORGANISATION_COORDINATES)))
            .andExpect(jsonPath("$.[*].organisationImageContentType").value(hasItem(DEFAULT_ORGANISATION_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].organisationImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ORGANISATION_IMAGE))))
            .andExpect(jsonPath("$.[*].organisationSymbol").value(hasItem(DEFAULT_ORGANISATION_SYMBOL)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = new Organisation();
        organisation1.setId("id1");
        Organisation organisation2 = new Organisation();
        organisation2.setId(organisation1.getId());
        assertThat(organisation1).isEqualTo(organisation2);
        organisation2.setId("id2");
        assertThat(organisation1).isNotEqualTo(organisation2);
        organisation1.setId(null);
        assertThat(organisation1).isNotEqualTo(organisation2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationDTO.class);
        OrganisationDTO organisationDTO1 = new OrganisationDTO();
        organisationDTO1.setId("id1");
        OrganisationDTO organisationDTO2 = new OrganisationDTO();
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO2.setId(organisationDTO1.getId());
        assertThat(organisationDTO1).isEqualTo(organisationDTO2);
        organisationDTO2.setId("id2");
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO1.setId(null);
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
    }
}
