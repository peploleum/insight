package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.ThreatActor;
import com.peploleum.insight.domain.Vulnerability;
import com.peploleum.insight.domain.Tool;
import com.peploleum.insight.domain.Malware;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.ThreatActorRepository;
import com.peploleum.insight.repository.search.ThreatActorSearchRepository;
import com.peploleum.insight.service.ThreatActorService;
import com.peploleum.insight.service.dto.ThreatActorDTO;
import com.peploleum.insight.service.mapper.ThreatActorMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.ThreatActorCriteria;
import com.peploleum.insight.service.ThreatActorQueryService;

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
 * Test class for the ThreatActorResource REST controller.
 *
 * @see ThreatActorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class ThreatActorResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    @Autowired
    private ThreatActorRepository threatActorRepository;


    @Autowired
    private ThreatActorMapper threatActorMapper;
    

    @Autowired
    private ThreatActorService threatActorService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.ThreatActorSearchRepositoryMockConfiguration
     */
    @Autowired
    private ThreatActorSearchRepository mockThreatActorSearchRepository;

    @Autowired
    private ThreatActorQueryService threatActorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restThreatActorMockMvc;

    private ThreatActor threatActor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ThreatActorResource threatActorResource = new ThreatActorResource(threatActorService, threatActorQueryService);
        this.restThreatActorMockMvc = MockMvcBuilders.standaloneSetup(threatActorResource)
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
    public static ThreatActor createEntity(EntityManager em) {
        ThreatActor threatActor = new ThreatActor()
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .libelle(DEFAULT_LIBELLE)
            .specification(DEFAULT_SPECIFICATION)
            .role(DEFAULT_ROLE);
        return threatActor;
    }

    @Before
    public void initTest() {
        threatActor = createEntity(em);
    }

    @Test
    @Transactional
    public void createThreatActor() throws Exception {
        int databaseSizeBeforeCreate = threatActorRepository.findAll().size();

        // Create the ThreatActor
        ThreatActorDTO threatActorDTO = threatActorMapper.toDto(threatActor);
        restThreatActorMockMvc.perform(post("/api/threat-actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(threatActorDTO)))
            .andExpect(status().isCreated());

        // Validate the ThreatActor in the database
        List<ThreatActor> threatActorList = threatActorRepository.findAll();
        assertThat(threatActorList).hasSize(databaseSizeBeforeCreate + 1);
        ThreatActor testThreatActor = threatActorList.get(threatActorList.size() - 1);
        assertThat(testThreatActor.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testThreatActor.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testThreatActor.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testThreatActor.getSpecification()).isEqualTo(DEFAULT_SPECIFICATION);
        assertThat(testThreatActor.getRole()).isEqualTo(DEFAULT_ROLE);

        // Validate the ThreatActor in Elasticsearch
        verify(mockThreatActorSearchRepository, times(1)).save(testThreatActor);
    }

    @Test
    @Transactional
    public void createThreatActorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = threatActorRepository.findAll().size();

        // Create the ThreatActor with an existing ID
        threatActor.setId(1L);
        ThreatActorDTO threatActorDTO = threatActorMapper.toDto(threatActor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThreatActorMockMvc.perform(post("/api/threat-actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(threatActorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ThreatActor in the database
        List<ThreatActor> threatActorList = threatActorRepository.findAll();
        assertThat(threatActorList).hasSize(databaseSizeBeforeCreate);

        // Validate the ThreatActor in Elasticsearch
        verify(mockThreatActorSearchRepository, times(0)).save(threatActor);
    }

    @Test
    @Transactional
    public void getAllThreatActors() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList
        restThreatActorMockMvc.perform(get("/api/threat-actors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(threatActor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }
    

    @Test
    @Transactional
    public void getThreatActor() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get the threatActor
        restThreatActorMockMvc.perform(get("/api/threat-actors/{id}", threatActor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(threatActor.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.specification").value(DEFAULT_SPECIFICATION.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getAllThreatActorsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where nom equals to DEFAULT_NOM
        defaultThreatActorShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the threatActorList where nom equals to UPDATED_NOM
        defaultThreatActorShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultThreatActorShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the threatActorList where nom equals to UPDATED_NOM
        defaultThreatActorShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where nom is not null
        defaultThreatActorShouldBeFound("nom.specified=true");

        // Get all the threatActorList where nom is null
        defaultThreatActorShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllThreatActorsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where type equals to DEFAULT_TYPE
        defaultThreatActorShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the threatActorList where type equals to UPDATED_TYPE
        defaultThreatActorShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultThreatActorShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the threatActorList where type equals to UPDATED_TYPE
        defaultThreatActorShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where type is not null
        defaultThreatActorShouldBeFound("type.specified=true");

        // Get all the threatActorList where type is null
        defaultThreatActorShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllThreatActorsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where libelle equals to DEFAULT_LIBELLE
        defaultThreatActorShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the threatActorList where libelle equals to UPDATED_LIBELLE
        defaultThreatActorShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultThreatActorShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the threatActorList where libelle equals to UPDATED_LIBELLE
        defaultThreatActorShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where libelle is not null
        defaultThreatActorShouldBeFound("libelle.specified=true");

        // Get all the threatActorList where libelle is null
        defaultThreatActorShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllThreatActorsBySpecificationIsEqualToSomething() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where specification equals to DEFAULT_SPECIFICATION
        defaultThreatActorShouldBeFound("specification.equals=" + DEFAULT_SPECIFICATION);

        // Get all the threatActorList where specification equals to UPDATED_SPECIFICATION
        defaultThreatActorShouldNotBeFound("specification.equals=" + UPDATED_SPECIFICATION);
    }

    @Test
    @Transactional
    public void getAllThreatActorsBySpecificationIsInShouldWork() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where specification in DEFAULT_SPECIFICATION or UPDATED_SPECIFICATION
        defaultThreatActorShouldBeFound("specification.in=" + DEFAULT_SPECIFICATION + "," + UPDATED_SPECIFICATION);

        // Get all the threatActorList where specification equals to UPDATED_SPECIFICATION
        defaultThreatActorShouldNotBeFound("specification.in=" + UPDATED_SPECIFICATION);
    }

    @Test
    @Transactional
    public void getAllThreatActorsBySpecificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where specification is not null
        defaultThreatActorShouldBeFound("specification.specified=true");

        // Get all the threatActorList where specification is null
        defaultThreatActorShouldNotBeFound("specification.specified=false");
    }

    @Test
    @Transactional
    public void getAllThreatActorsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where role equals to DEFAULT_ROLE
        defaultThreatActorShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the threatActorList where role equals to UPDATED_ROLE
        defaultThreatActorShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultThreatActorShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the threatActorList where role equals to UPDATED_ROLE
        defaultThreatActorShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllThreatActorsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        // Get all the threatActorList where role is not null
        defaultThreatActorShouldBeFound("role.specified=true");

        // Get all the threatActorList where role is null
        defaultThreatActorShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    public void getAllThreatActorsByIsTargetsThreatActorToVulnerabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        Vulnerability isTargetsThreatActorToVulnerability = VulnerabilityResourceIntTest.createEntity(em);
        em.persist(isTargetsThreatActorToVulnerability);
        em.flush();
        threatActor.addIsTargetsThreatActorToVulnerability(isTargetsThreatActorToVulnerability);
        threatActorRepository.saveAndFlush(threatActor);
        Long isTargetsThreatActorToVulnerabilityId = isTargetsThreatActorToVulnerability.getId();

        // Get all the threatActorList where isTargetsThreatActorToVulnerability equals to isTargetsThreatActorToVulnerabilityId
        defaultThreatActorShouldBeFound("isTargetsThreatActorToVulnerabilityId.equals=" + isTargetsThreatActorToVulnerabilityId);

        // Get all the threatActorList where isTargetsThreatActorToVulnerability equals to isTargetsThreatActorToVulnerabilityId + 1
        defaultThreatActorShouldNotBeFound("isTargetsThreatActorToVulnerabilityId.equals=" + (isTargetsThreatActorToVulnerabilityId + 1));
    }


    @Test
    @Transactional
    public void getAllThreatActorsByIsUsesThreatActorToToolIsEqualToSomething() throws Exception {
        // Initialize the database
        Tool isUsesThreatActorToTool = ToolResourceIntTest.createEntity(em);
        em.persist(isUsesThreatActorToTool);
        em.flush();
        threatActor.addIsUsesThreatActorToTool(isUsesThreatActorToTool);
        threatActorRepository.saveAndFlush(threatActor);
        Long isUsesThreatActorToToolId = isUsesThreatActorToTool.getId();

        // Get all the threatActorList where isUsesThreatActorToTool equals to isUsesThreatActorToToolId
        defaultThreatActorShouldBeFound("isUsesThreatActorToToolId.equals=" + isUsesThreatActorToToolId);

        // Get all the threatActorList where isUsesThreatActorToTool equals to isUsesThreatActorToToolId + 1
        defaultThreatActorShouldNotBeFound("isUsesThreatActorToToolId.equals=" + (isUsesThreatActorToToolId + 1));
    }


    @Test
    @Transactional
    public void getAllThreatActorsByIsUsesThreatActorToMalwareIsEqualToSomething() throws Exception {
        // Initialize the database
        Malware isUsesThreatActorToMalware = MalwareResourceIntTest.createEntity(em);
        em.persist(isUsesThreatActorToMalware);
        em.flush();
        threatActor.setIsUsesThreatActorToMalware(isUsesThreatActorToMalware);
        threatActorRepository.saveAndFlush(threatActor);
        Long isUsesThreatActorToMalwareId = isUsesThreatActorToMalware.getId();

        // Get all the threatActorList where isUsesThreatActorToMalware equals to isUsesThreatActorToMalwareId
        defaultThreatActorShouldBeFound("isUsesThreatActorToMalwareId.equals=" + isUsesThreatActorToMalwareId);

        // Get all the threatActorList where isUsesThreatActorToMalware equals to isUsesThreatActorToMalwareId + 1
        defaultThreatActorShouldNotBeFound("isUsesThreatActorToMalwareId.equals=" + (isUsesThreatActorToMalwareId + 1));
    }


    @Test
    @Transactional
    public void getAllThreatActorsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        threatActor.setLinkOf(linkOf);
        threatActorRepository.saveAndFlush(threatActor);
        Long linkOfId = linkOf.getId();

        // Get all the threatActorList where linkOf equals to linkOfId
        defaultThreatActorShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the threatActorList where linkOf equals to linkOfId + 1
        defaultThreatActorShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultThreatActorShouldBeFound(String filter) throws Exception {
        restThreatActorMockMvc.perform(get("/api/threat-actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(threatActor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultThreatActorShouldNotBeFound(String filter) throws Exception {
        restThreatActorMockMvc.perform(get("/api/threat-actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingThreatActor() throws Exception {
        // Get the threatActor
        restThreatActorMockMvc.perform(get("/api/threat-actors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThreatActor() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        int databaseSizeBeforeUpdate = threatActorRepository.findAll().size();

        // Update the threatActor
        ThreatActor updatedThreatActor = threatActorRepository.findById(threatActor.getId()).get();
        // Disconnect from session so that the updates on updatedThreatActor are not directly saved in db
        em.detach(updatedThreatActor);
        updatedThreatActor
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .libelle(UPDATED_LIBELLE)
            .specification(UPDATED_SPECIFICATION)
            .role(UPDATED_ROLE);
        ThreatActorDTO threatActorDTO = threatActorMapper.toDto(updatedThreatActor);

        restThreatActorMockMvc.perform(put("/api/threat-actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(threatActorDTO)))
            .andExpect(status().isOk());

        // Validate the ThreatActor in the database
        List<ThreatActor> threatActorList = threatActorRepository.findAll();
        assertThat(threatActorList).hasSize(databaseSizeBeforeUpdate);
        ThreatActor testThreatActor = threatActorList.get(threatActorList.size() - 1);
        assertThat(testThreatActor.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testThreatActor.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testThreatActor.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testThreatActor.getSpecification()).isEqualTo(UPDATED_SPECIFICATION);
        assertThat(testThreatActor.getRole()).isEqualTo(UPDATED_ROLE);

        // Validate the ThreatActor in Elasticsearch
        verify(mockThreatActorSearchRepository, times(1)).save(testThreatActor);
    }

    @Test
    @Transactional
    public void updateNonExistingThreatActor() throws Exception {
        int databaseSizeBeforeUpdate = threatActorRepository.findAll().size();

        // Create the ThreatActor
        ThreatActorDTO threatActorDTO = threatActorMapper.toDto(threatActor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restThreatActorMockMvc.perform(put("/api/threat-actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(threatActorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ThreatActor in the database
        List<ThreatActor> threatActorList = threatActorRepository.findAll();
        assertThat(threatActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ThreatActor in Elasticsearch
        verify(mockThreatActorSearchRepository, times(0)).save(threatActor);
    }

    @Test
    @Transactional
    public void deleteThreatActor() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);

        int databaseSizeBeforeDelete = threatActorRepository.findAll().size();

        // Get the threatActor
        restThreatActorMockMvc.perform(delete("/api/threat-actors/{id}", threatActor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ThreatActor> threatActorList = threatActorRepository.findAll();
        assertThat(threatActorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ThreatActor in Elasticsearch
        verify(mockThreatActorSearchRepository, times(1)).deleteById(threatActor.getId());
    }

    @Test
    @Transactional
    public void searchThreatActor() throws Exception {
        // Initialize the database
        threatActorRepository.saveAndFlush(threatActor);
        when(mockThreatActorSearchRepository.search(queryStringQuery("id:" + threatActor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(threatActor), PageRequest.of(0, 1), 1));
        // Search the threatActor
        restThreatActorMockMvc.perform(get("/api/_search/threat-actors?query=id:" + threatActor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(threatActor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThreatActor.class);
        ThreatActor threatActor1 = new ThreatActor();
        threatActor1.setId(1L);
        ThreatActor threatActor2 = new ThreatActor();
        threatActor2.setId(threatActor1.getId());
        assertThat(threatActor1).isEqualTo(threatActor2);
        threatActor2.setId(2L);
        assertThat(threatActor1).isNotEqualTo(threatActor2);
        threatActor1.setId(null);
        assertThat(threatActor1).isNotEqualTo(threatActor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThreatActorDTO.class);
        ThreatActorDTO threatActorDTO1 = new ThreatActorDTO();
        threatActorDTO1.setId(1L);
        ThreatActorDTO threatActorDTO2 = new ThreatActorDTO();
        assertThat(threatActorDTO1).isNotEqualTo(threatActorDTO2);
        threatActorDTO2.setId(threatActorDTO1.getId());
        assertThat(threatActorDTO1).isEqualTo(threatActorDTO2);
        threatActorDTO2.setId(2L);
        assertThat(threatActorDTO1).isNotEqualTo(threatActorDTO2);
        threatActorDTO1.setId(null);
        assertThat(threatActorDTO1).isNotEqualTo(threatActorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(threatActorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(threatActorMapper.fromId(null)).isNull();
    }
}
