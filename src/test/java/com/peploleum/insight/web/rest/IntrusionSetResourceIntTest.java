package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.IntrusionSet;
import com.peploleum.insight.domain.Tool;
import com.peploleum.insight.domain.Actor;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.IntrusionSetRepository;
import com.peploleum.insight.repository.search.IntrusionSetSearchRepository;
import com.peploleum.insight.service.IntrusionSetService;
import com.peploleum.insight.service.dto.IntrusionSetDTO;
import com.peploleum.insight.service.mapper.IntrusionSetMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.IntrusionSetCriteria;
import com.peploleum.insight.service.IntrusionSetQueryService;

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
 * Test class for the IntrusionSetResource REST controller.
 *
 * @see IntrusionSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class IntrusionSetResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIF = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU_RESSOURCE = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU_RESSOURCE = "BBBBBBBBBB";

    @Autowired
    private IntrusionSetRepository intrusionSetRepository;


    @Autowired
    private IntrusionSetMapper intrusionSetMapper;
    

    @Autowired
    private IntrusionSetService intrusionSetService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.IntrusionSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private IntrusionSetSearchRepository mockIntrusionSetSearchRepository;

    @Autowired
    private IntrusionSetQueryService intrusionSetQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIntrusionSetMockMvc;

    private IntrusionSet intrusionSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IntrusionSetResource intrusionSetResource = new IntrusionSetResource(intrusionSetService, intrusionSetQueryService);
        this.restIntrusionSetMockMvc = MockMvcBuilders.standaloneSetup(intrusionSetResource)
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
    public static IntrusionSet createEntity(EntityManager em) {
        IntrusionSet intrusionSet = new IntrusionSet()
            .description(DEFAULT_DESCRIPTION)
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .objectif(DEFAULT_OBJECTIF)
            .niveauRessource(DEFAULT_NIVEAU_RESSOURCE);
        return intrusionSet;
    }

    @Before
    public void initTest() {
        intrusionSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntrusionSet() throws Exception {
        int databaseSizeBeforeCreate = intrusionSetRepository.findAll().size();

        // Create the IntrusionSet
        IntrusionSetDTO intrusionSetDTO = intrusionSetMapper.toDto(intrusionSet);
        restIntrusionSetMockMvc.perform(post("/api/intrusion-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intrusionSetDTO)))
            .andExpect(status().isCreated());

        // Validate the IntrusionSet in the database
        List<IntrusionSet> intrusionSetList = intrusionSetRepository.findAll();
        assertThat(intrusionSetList).hasSize(databaseSizeBeforeCreate + 1);
        IntrusionSet testIntrusionSet = intrusionSetList.get(intrusionSetList.size() - 1);
        assertThat(testIntrusionSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIntrusionSet.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testIntrusionSet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIntrusionSet.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testIntrusionSet.getNiveauRessource()).isEqualTo(DEFAULT_NIVEAU_RESSOURCE);

        // Validate the IntrusionSet in Elasticsearch
        verify(mockIntrusionSetSearchRepository, times(1)).save(testIntrusionSet);
    }

    @Test
    @Transactional
    public void createIntrusionSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = intrusionSetRepository.findAll().size();

        // Create the IntrusionSet with an existing ID
        intrusionSet.setId(1L);
        IntrusionSetDTO intrusionSetDTO = intrusionSetMapper.toDto(intrusionSet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntrusionSetMockMvc.perform(post("/api/intrusion-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intrusionSetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IntrusionSet in the database
        List<IntrusionSet> intrusionSetList = intrusionSetRepository.findAll();
        assertThat(intrusionSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the IntrusionSet in Elasticsearch
        verify(mockIntrusionSetSearchRepository, times(0)).save(intrusionSet);
    }

    @Test
    @Transactional
    public void getAllIntrusionSets() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList
        restIntrusionSetMockMvc.perform(get("/api/intrusion-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intrusionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].niveauRessource").value(hasItem(DEFAULT_NIVEAU_RESSOURCE.toString())));
    }
    

    @Test
    @Transactional
    public void getIntrusionSet() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get the intrusionSet
        restIntrusionSetMockMvc.perform(get("/api/intrusion-sets/{id}", intrusionSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(intrusionSet.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.toString()))
            .andExpect(jsonPath("$.niveauRessource").value(DEFAULT_NIVEAU_RESSOURCE.toString()));
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where description equals to DEFAULT_DESCRIPTION
        defaultIntrusionSetShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the intrusionSetList where description equals to UPDATED_DESCRIPTION
        defaultIntrusionSetShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultIntrusionSetShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the intrusionSetList where description equals to UPDATED_DESCRIPTION
        defaultIntrusionSetShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where description is not null
        defaultIntrusionSetShouldBeFound("description.specified=true");

        // Get all the intrusionSetList where description is null
        defaultIntrusionSetShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where nom equals to DEFAULT_NOM
        defaultIntrusionSetShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the intrusionSetList where nom equals to UPDATED_NOM
        defaultIntrusionSetShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultIntrusionSetShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the intrusionSetList where nom equals to UPDATED_NOM
        defaultIntrusionSetShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where nom is not null
        defaultIntrusionSetShouldBeFound("nom.specified=true");

        // Get all the intrusionSetList where nom is null
        defaultIntrusionSetShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where type equals to DEFAULT_TYPE
        defaultIntrusionSetShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the intrusionSetList where type equals to UPDATED_TYPE
        defaultIntrusionSetShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultIntrusionSetShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the intrusionSetList where type equals to UPDATED_TYPE
        defaultIntrusionSetShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where type is not null
        defaultIntrusionSetShouldBeFound("type.specified=true");

        // Get all the intrusionSetList where type is null
        defaultIntrusionSetShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByObjectifIsEqualToSomething() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where objectif equals to DEFAULT_OBJECTIF
        defaultIntrusionSetShouldBeFound("objectif.equals=" + DEFAULT_OBJECTIF);

        // Get all the intrusionSetList where objectif equals to UPDATED_OBJECTIF
        defaultIntrusionSetShouldNotBeFound("objectif.equals=" + UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByObjectifIsInShouldWork() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where objectif in DEFAULT_OBJECTIF or UPDATED_OBJECTIF
        defaultIntrusionSetShouldBeFound("objectif.in=" + DEFAULT_OBJECTIF + "," + UPDATED_OBJECTIF);

        // Get all the intrusionSetList where objectif equals to UPDATED_OBJECTIF
        defaultIntrusionSetShouldNotBeFound("objectif.in=" + UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByObjectifIsNullOrNotNull() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where objectif is not null
        defaultIntrusionSetShouldBeFound("objectif.specified=true");

        // Get all the intrusionSetList where objectif is null
        defaultIntrusionSetShouldNotBeFound("objectif.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByNiveauRessourceIsEqualToSomething() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where niveauRessource equals to DEFAULT_NIVEAU_RESSOURCE
        defaultIntrusionSetShouldBeFound("niveauRessource.equals=" + DEFAULT_NIVEAU_RESSOURCE);

        // Get all the intrusionSetList where niveauRessource equals to UPDATED_NIVEAU_RESSOURCE
        defaultIntrusionSetShouldNotBeFound("niveauRessource.equals=" + UPDATED_NIVEAU_RESSOURCE);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByNiveauRessourceIsInShouldWork() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where niveauRessource in DEFAULT_NIVEAU_RESSOURCE or UPDATED_NIVEAU_RESSOURCE
        defaultIntrusionSetShouldBeFound("niveauRessource.in=" + DEFAULT_NIVEAU_RESSOURCE + "," + UPDATED_NIVEAU_RESSOURCE);

        // Get all the intrusionSetList where niveauRessource equals to UPDATED_NIVEAU_RESSOURCE
        defaultIntrusionSetShouldNotBeFound("niveauRessource.in=" + UPDATED_NIVEAU_RESSOURCE);
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByNiveauRessourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        // Get all the intrusionSetList where niveauRessource is not null
        defaultIntrusionSetShouldBeFound("niveauRessource.specified=true");

        // Get all the intrusionSetList where niveauRessource is null
        defaultIntrusionSetShouldNotBeFound("niveauRessource.specified=false");
    }

    @Test
    @Transactional
    public void getAllIntrusionSetsByIsUsesIntrusionSetToToolIsEqualToSomething() throws Exception {
        // Initialize the database
        Tool isUsesIntrusionSetToTool = ToolResourceIntTest.createEntity(em);
        em.persist(isUsesIntrusionSetToTool);
        em.flush();
        intrusionSet.addIsUsesIntrusionSetToTool(isUsesIntrusionSetToTool);
        intrusionSetRepository.saveAndFlush(intrusionSet);
        Long isUsesIntrusionSetToToolId = isUsesIntrusionSetToTool.getId();

        // Get all the intrusionSetList where isUsesIntrusionSetToTool equals to isUsesIntrusionSetToToolId
        defaultIntrusionSetShouldBeFound("isUsesIntrusionSetToToolId.equals=" + isUsesIntrusionSetToToolId);

        // Get all the intrusionSetList where isUsesIntrusionSetToTool equals to isUsesIntrusionSetToToolId + 1
        defaultIntrusionSetShouldNotBeFound("isUsesIntrusionSetToToolId.equals=" + (isUsesIntrusionSetToToolId + 1));
    }


    @Test
    @Transactional
    public void getAllIntrusionSetsByIsTargetsIntrusionSetToActorIsEqualToSomething() throws Exception {
        // Initialize the database
        Actor isTargetsIntrusionSetToActor = ActorResourceIntTest.createEntity(em);
        em.persist(isTargetsIntrusionSetToActor);
        em.flush();
        intrusionSet.setIsTargetsIntrusionSetToActor(isTargetsIntrusionSetToActor);
        intrusionSetRepository.saveAndFlush(intrusionSet);
        Long isTargetsIntrusionSetToActorId = isTargetsIntrusionSetToActor.getId();

        // Get all the intrusionSetList where isTargetsIntrusionSetToActor equals to isTargetsIntrusionSetToActorId
        defaultIntrusionSetShouldBeFound("isTargetsIntrusionSetToActorId.equals=" + isTargetsIntrusionSetToActorId);

        // Get all the intrusionSetList where isTargetsIntrusionSetToActor equals to isTargetsIntrusionSetToActorId + 1
        defaultIntrusionSetShouldNotBeFound("isTargetsIntrusionSetToActorId.equals=" + (isTargetsIntrusionSetToActorId + 1));
    }


    @Test
    @Transactional
    public void getAllIntrusionSetsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        intrusionSet.setLinkOf(linkOf);
        intrusionSetRepository.saveAndFlush(intrusionSet);
        Long linkOfId = linkOf.getId();

        // Get all the intrusionSetList where linkOf equals to linkOfId
        defaultIntrusionSetShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the intrusionSetList where linkOf equals to linkOfId + 1
        defaultIntrusionSetShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIntrusionSetShouldBeFound(String filter) throws Exception {
        restIntrusionSetMockMvc.perform(get("/api/intrusion-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intrusionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].niveauRessource").value(hasItem(DEFAULT_NIVEAU_RESSOURCE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIntrusionSetShouldNotBeFound(String filter) throws Exception {
        restIntrusionSetMockMvc.perform(get("/api/intrusion-sets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingIntrusionSet() throws Exception {
        // Get the intrusionSet
        restIntrusionSetMockMvc.perform(get("/api/intrusion-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntrusionSet() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        int databaseSizeBeforeUpdate = intrusionSetRepository.findAll().size();

        // Update the intrusionSet
        IntrusionSet updatedIntrusionSet = intrusionSetRepository.findById(intrusionSet.getId()).get();
        // Disconnect from session so that the updates on updatedIntrusionSet are not directly saved in db
        em.detach(updatedIntrusionSet);
        updatedIntrusionSet
            .description(UPDATED_DESCRIPTION)
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .objectif(UPDATED_OBJECTIF)
            .niveauRessource(UPDATED_NIVEAU_RESSOURCE);
        IntrusionSetDTO intrusionSetDTO = intrusionSetMapper.toDto(updatedIntrusionSet);

        restIntrusionSetMockMvc.perform(put("/api/intrusion-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intrusionSetDTO)))
            .andExpect(status().isOk());

        // Validate the IntrusionSet in the database
        List<IntrusionSet> intrusionSetList = intrusionSetRepository.findAll();
        assertThat(intrusionSetList).hasSize(databaseSizeBeforeUpdate);
        IntrusionSet testIntrusionSet = intrusionSetList.get(intrusionSetList.size() - 1);
        assertThat(testIntrusionSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIntrusionSet.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testIntrusionSet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIntrusionSet.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testIntrusionSet.getNiveauRessource()).isEqualTo(UPDATED_NIVEAU_RESSOURCE);

        // Validate the IntrusionSet in Elasticsearch
        verify(mockIntrusionSetSearchRepository, times(1)).save(testIntrusionSet);
    }

    @Test
    @Transactional
    public void updateNonExistingIntrusionSet() throws Exception {
        int databaseSizeBeforeUpdate = intrusionSetRepository.findAll().size();

        // Create the IntrusionSet
        IntrusionSetDTO intrusionSetDTO = intrusionSetMapper.toDto(intrusionSet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restIntrusionSetMockMvc.perform(put("/api/intrusion-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intrusionSetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IntrusionSet in the database
        List<IntrusionSet> intrusionSetList = intrusionSetRepository.findAll();
        assertThat(intrusionSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IntrusionSet in Elasticsearch
        verify(mockIntrusionSetSearchRepository, times(0)).save(intrusionSet);
    }

    @Test
    @Transactional
    public void deleteIntrusionSet() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);

        int databaseSizeBeforeDelete = intrusionSetRepository.findAll().size();

        // Get the intrusionSet
        restIntrusionSetMockMvc.perform(delete("/api/intrusion-sets/{id}", intrusionSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IntrusionSet> intrusionSetList = intrusionSetRepository.findAll();
        assertThat(intrusionSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IntrusionSet in Elasticsearch
        verify(mockIntrusionSetSearchRepository, times(1)).deleteById(intrusionSet.getId());
    }

    @Test
    @Transactional
    public void searchIntrusionSet() throws Exception {
        // Initialize the database
        intrusionSetRepository.saveAndFlush(intrusionSet);
        when(mockIntrusionSetSearchRepository.search(queryStringQuery("id:" + intrusionSet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(intrusionSet), PageRequest.of(0, 1), 1));
        // Search the intrusionSet
        restIntrusionSetMockMvc.perform(get("/api/_search/intrusion-sets?query=id:" + intrusionSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intrusionSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].niveauRessource").value(hasItem(DEFAULT_NIVEAU_RESSOURCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntrusionSet.class);
        IntrusionSet intrusionSet1 = new IntrusionSet();
        intrusionSet1.setId(1L);
        IntrusionSet intrusionSet2 = new IntrusionSet();
        intrusionSet2.setId(intrusionSet1.getId());
        assertThat(intrusionSet1).isEqualTo(intrusionSet2);
        intrusionSet2.setId(2L);
        assertThat(intrusionSet1).isNotEqualTo(intrusionSet2);
        intrusionSet1.setId(null);
        assertThat(intrusionSet1).isNotEqualTo(intrusionSet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntrusionSetDTO.class);
        IntrusionSetDTO intrusionSetDTO1 = new IntrusionSetDTO();
        intrusionSetDTO1.setId(1L);
        IntrusionSetDTO intrusionSetDTO2 = new IntrusionSetDTO();
        assertThat(intrusionSetDTO1).isNotEqualTo(intrusionSetDTO2);
        intrusionSetDTO2.setId(intrusionSetDTO1.getId());
        assertThat(intrusionSetDTO1).isEqualTo(intrusionSetDTO2);
        intrusionSetDTO2.setId(2L);
        assertThat(intrusionSetDTO1).isNotEqualTo(intrusionSetDTO2);
        intrusionSetDTO1.setId(null);
        assertThat(intrusionSetDTO1).isNotEqualTo(intrusionSetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(intrusionSetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(intrusionSetMapper.fromId(null)).isNull();
    }
}
