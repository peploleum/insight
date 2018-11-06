package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Campaign;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.CampaignRepository;
import com.peploleum.insight.repository.search.CampaignSearchRepository;
import com.peploleum.insight.service.CampaignService;
import com.peploleum.insight.service.dto.CampaignDTO;
import com.peploleum.insight.service.mapper.CampaignMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.CampaignCriteria;
import com.peploleum.insight.service.CampaignQueryService;

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
 * Test class for the CampaignResource REST controller.
 *
 * @see CampaignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class CampaignResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIF = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF = "BBBBBBBBBB";

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private CampaignRepository campaignRepository;


    @Autowired
    private CampaignMapper campaignMapper;
    

    @Autowired
    private CampaignService campaignService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.CampaignSearchRepositoryMockConfiguration
     */
    @Autowired
    private CampaignSearchRepository mockCampaignSearchRepository;

    @Autowired
    private CampaignQueryService campaignQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCampaignMockMvc;

    private Campaign campaign;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampaignResource campaignResource = new CampaignResource(campaignService, campaignQueryService);
        this.restCampaignMockMvc = MockMvcBuilders.standaloneSetup(campaignResource)
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
    public static Campaign createEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .description(DEFAULT_DESCRIPTION)
            .nom(DEFAULT_NOM)
            .objectif(DEFAULT_OBJECTIF)
            .alias(DEFAULT_ALIAS)
            .type(DEFAULT_TYPE);
        return campaign;
    }

    @Before
    public void initTest() {
        campaign = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);
        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCampaign.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCampaign.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testCampaign.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testCampaign.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Campaign in Elasticsearch
        verify(mockCampaignSearchRepository, times(1)).save(testCampaign);
    }

    @Test
    @Transactional
    public void createCampaignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign with an existing ID
        campaign.setId(1L);
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate);

        // Validate the Campaign in Elasticsearch
        verify(mockCampaignSearchRepository, times(0)).save(campaign);
    }

    @Test
    @Transactional
    public void getAllCampaigns() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campaign.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllCampaignsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where description equals to DEFAULT_DESCRIPTION
        defaultCampaignShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the campaignList where description equals to UPDATED_DESCRIPTION
        defaultCampaignShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCampaignsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCampaignShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the campaignList where description equals to UPDATED_DESCRIPTION
        defaultCampaignShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCampaignsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where description is not null
        defaultCampaignShouldBeFound("description.specified=true");

        // Get all the campaignList where description is null
        defaultCampaignShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where nom equals to DEFAULT_NOM
        defaultCampaignShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the campaignList where nom equals to UPDATED_NOM
        defaultCampaignShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCampaignsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultCampaignShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the campaignList where nom equals to UPDATED_NOM
        defaultCampaignShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCampaignsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where nom is not null
        defaultCampaignShouldBeFound("nom.specified=true");

        // Get all the campaignList where nom is null
        defaultCampaignShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByObjectifIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where objectif equals to DEFAULT_OBJECTIF
        defaultCampaignShouldBeFound("objectif.equals=" + DEFAULT_OBJECTIF);

        // Get all the campaignList where objectif equals to UPDATED_OBJECTIF
        defaultCampaignShouldNotBeFound("objectif.equals=" + UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    public void getAllCampaignsByObjectifIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where objectif in DEFAULT_OBJECTIF or UPDATED_OBJECTIF
        defaultCampaignShouldBeFound("objectif.in=" + DEFAULT_OBJECTIF + "," + UPDATED_OBJECTIF);

        // Get all the campaignList where objectif equals to UPDATED_OBJECTIF
        defaultCampaignShouldNotBeFound("objectif.in=" + UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    public void getAllCampaignsByObjectifIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where objectif is not null
        defaultCampaignShouldBeFound("objectif.specified=true");

        // Get all the campaignList where objectif is null
        defaultCampaignShouldNotBeFound("objectif.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByAliasIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where alias equals to DEFAULT_ALIAS
        defaultCampaignShouldBeFound("alias.equals=" + DEFAULT_ALIAS);

        // Get all the campaignList where alias equals to UPDATED_ALIAS
        defaultCampaignShouldNotBeFound("alias.equals=" + UPDATED_ALIAS);
    }

    @Test
    @Transactional
    public void getAllCampaignsByAliasIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where alias in DEFAULT_ALIAS or UPDATED_ALIAS
        defaultCampaignShouldBeFound("alias.in=" + DEFAULT_ALIAS + "," + UPDATED_ALIAS);

        // Get all the campaignList where alias equals to UPDATED_ALIAS
        defaultCampaignShouldNotBeFound("alias.in=" + UPDATED_ALIAS);
    }

    @Test
    @Transactional
    public void getAllCampaignsByAliasIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where alias is not null
        defaultCampaignShouldBeFound("alias.specified=true");

        // Get all the campaignList where alias is null
        defaultCampaignShouldNotBeFound("alias.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where type equals to DEFAULT_TYPE
        defaultCampaignShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the campaignList where type equals to UPDATED_TYPE
        defaultCampaignShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCampaignsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCampaignShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the campaignList where type equals to UPDATED_TYPE
        defaultCampaignShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCampaignsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where type is not null
        defaultCampaignShouldBeFound("type.specified=true");

        // Get all the campaignList where type is null
        defaultCampaignShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        campaign.setLinkOf(linkOf);
        campaignRepository.saveAndFlush(campaign);
        Long linkOfId = linkOf.getId();

        // Get all the campaignList where linkOf equals to linkOfId
        defaultCampaignShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the campaignList where linkOf equals to linkOfId + 1
        defaultCampaignShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCampaignShouldBeFound(String filter) throws Exception {
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCampaignShouldNotBeFound(String filter) throws Exception {
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCampaign() throws Exception {
        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findById(campaign.getId()).get();
        // Disconnect from session so that the updates on updatedCampaign are not directly saved in db
        em.detach(updatedCampaign);
        updatedCampaign
            .description(UPDATED_DESCRIPTION)
            .nom(UPDATED_NOM)
            .objectif(UPDATED_OBJECTIF)
            .alias(UPDATED_ALIAS)
            .type(UPDATED_TYPE);
        CampaignDTO campaignDTO = campaignMapper.toDto(updatedCampaign);

        restCampaignMockMvc.perform(put("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCampaign.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCampaign.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testCampaign.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testCampaign.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Campaign in Elasticsearch
        verify(mockCampaignSearchRepository, times(1)).save(testCampaign);
    }

    @Test
    @Transactional
    public void updateNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCampaignMockMvc.perform(put("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Campaign in Elasticsearch
        verify(mockCampaignSearchRepository, times(0)).save(campaign);
    }

    @Test
    @Transactional
    public void deleteCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        int databaseSizeBeforeDelete = campaignRepository.findAll().size();

        // Get the campaign
        restCampaignMockMvc.perform(delete("/api/campaigns/{id}", campaign.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Campaign in Elasticsearch
        verify(mockCampaignSearchRepository, times(1)).deleteById(campaign.getId());
    }

    @Test
    @Transactional
    public void searchCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);
        when(mockCampaignSearchRepository.search(queryStringQuery("id:" + campaign.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(campaign), PageRequest.of(0, 1), 1));
        // Search the campaign
        restCampaignMockMvc.perform(get("/api/_search/campaigns?query=id:" + campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campaign.class);
        Campaign campaign1 = new Campaign();
        campaign1.setId(1L);
        Campaign campaign2 = new Campaign();
        campaign2.setId(campaign1.getId());
        assertThat(campaign1).isEqualTo(campaign2);
        campaign2.setId(2L);
        assertThat(campaign1).isNotEqualTo(campaign2);
        campaign1.setId(null);
        assertThat(campaign1).isNotEqualTo(campaign2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignDTO.class);
        CampaignDTO campaignDTO1 = new CampaignDTO();
        campaignDTO1.setId(1L);
        CampaignDTO campaignDTO2 = new CampaignDTO();
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
        campaignDTO2.setId(campaignDTO1.getId());
        assertThat(campaignDTO1).isEqualTo(campaignDTO2);
        campaignDTO2.setId(2L);
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
        campaignDTO1.setId(null);
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(campaignMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(campaignMapper.fromId(null)).isNull();
    }
}
