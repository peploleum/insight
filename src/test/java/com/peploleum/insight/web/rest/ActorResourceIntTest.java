package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Actor;
import com.peploleum.insight.domain.IntrusionSet;
import com.peploleum.insight.domain.Malware;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.ActorRepository;
import com.peploleum.insight.repository.search.ActorSearchRepository;
import com.peploleum.insight.service.ActorService;
import com.peploleum.insight.service.dto.ActorDTO;
import com.peploleum.insight.service.mapper.ActorMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.ActorCriteria;
import com.peploleum.insight.service.ActorQueryService;

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
 * Test class for the ActorResource REST controller.
 *
 * @see ActorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class ActorResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASSE_IDENTITE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE_IDENTITE = "BBBBBBBBBB";

    @Autowired
    private ActorRepository actorRepository;


    @Autowired
    private ActorMapper actorMapper;
    

    @Autowired
    private ActorService actorService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.ActorSearchRepositoryMockConfiguration
     */
    @Autowired
    private ActorSearchRepository mockActorSearchRepository;

    @Autowired
    private ActorQueryService actorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActorMockMvc;

    private Actor actor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActorResource actorResource = new ActorResource(actorService, actorQueryService);
        this.restActorMockMvc = MockMvcBuilders.standaloneSetup(actorResource)
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
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor()
            .description(DEFAULT_DESCRIPTION)
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .libelle(DEFAULT_LIBELLE)
            .classeIdentite(DEFAULT_CLASSE_IDENTITE);
        return actor;
    }

    @Before
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);
        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testActor.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testActor.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActor.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testActor.getClasseIdentite()).isEqualTo(DEFAULT_CLASSE_IDENTITE);

        // Validate the Actor in Elasticsearch
        verify(mockActorSearchRepository, times(1)).save(testActor);
    }

    @Test
    @Transactional
    public void createActorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor with an existing ID
        actor.setId(1L);
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Actor in Elasticsearch
        verify(mockActorSearchRepository, times(0)).save(actor);
    }

    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].classeIdentite").value(hasItem(DEFAULT_CLASSE_IDENTITE.toString())));
    }
    

    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.classeIdentite").value(DEFAULT_CLASSE_IDENTITE.toString()));
    }

    @Test
    @Transactional
    public void getAllActorsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where description equals to DEFAULT_DESCRIPTION
        defaultActorShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the actorList where description equals to UPDATED_DESCRIPTION
        defaultActorShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllActorsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultActorShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the actorList where description equals to UPDATED_DESCRIPTION
        defaultActorShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllActorsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where description is not null
        defaultActorShouldBeFound("description.specified=true");

        // Get all the actorList where description is null
        defaultActorShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllActorsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where nom equals to DEFAULT_NOM
        defaultActorShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the actorList where nom equals to UPDATED_NOM
        defaultActorShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActorsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultActorShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the actorList where nom equals to UPDATED_NOM
        defaultActorShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActorsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where nom is not null
        defaultActorShouldBeFound("nom.specified=true");

        // Get all the actorList where nom is null
        defaultActorShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllActorsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where type equals to DEFAULT_TYPE
        defaultActorShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the actorList where type equals to UPDATED_TYPE
        defaultActorShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllActorsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultActorShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the actorList where type equals to UPDATED_TYPE
        defaultActorShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllActorsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where type is not null
        defaultActorShouldBeFound("type.specified=true");

        // Get all the actorList where type is null
        defaultActorShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllActorsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where libelle equals to DEFAULT_LIBELLE
        defaultActorShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the actorList where libelle equals to UPDATED_LIBELLE
        defaultActorShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllActorsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultActorShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the actorList where libelle equals to UPDATED_LIBELLE
        defaultActorShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllActorsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where libelle is not null
        defaultActorShouldBeFound("libelle.specified=true");

        // Get all the actorList where libelle is null
        defaultActorShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllActorsByClasseIdentiteIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where classeIdentite equals to DEFAULT_CLASSE_IDENTITE
        defaultActorShouldBeFound("classeIdentite.equals=" + DEFAULT_CLASSE_IDENTITE);

        // Get all the actorList where classeIdentite equals to UPDATED_CLASSE_IDENTITE
        defaultActorShouldNotBeFound("classeIdentite.equals=" + UPDATED_CLASSE_IDENTITE);
    }

    @Test
    @Transactional
    public void getAllActorsByClasseIdentiteIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where classeIdentite in DEFAULT_CLASSE_IDENTITE or UPDATED_CLASSE_IDENTITE
        defaultActorShouldBeFound("classeIdentite.in=" + DEFAULT_CLASSE_IDENTITE + "," + UPDATED_CLASSE_IDENTITE);

        // Get all the actorList where classeIdentite equals to UPDATED_CLASSE_IDENTITE
        defaultActorShouldNotBeFound("classeIdentite.in=" + UPDATED_CLASSE_IDENTITE);
    }

    @Test
    @Transactional
    public void getAllActorsByClasseIdentiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where classeIdentite is not null
        defaultActorShouldBeFound("classeIdentite.specified=true");

        // Get all the actorList where classeIdentite is null
        defaultActorShouldNotBeFound("classeIdentite.specified=false");
    }

    @Test
    @Transactional
    public void getAllActorsByTargetsActorToIntrusionSetIsEqualToSomething() throws Exception {
        // Initialize the database
        IntrusionSet targetsActorToIntrusionSet = IntrusionSetResourceIntTest.createEntity(em);
        em.persist(targetsActorToIntrusionSet);
        em.flush();
        actor.addTargetsActorToIntrusionSet(targetsActorToIntrusionSet);
        actorRepository.saveAndFlush(actor);
        Long targetsActorToIntrusionSetId = targetsActorToIntrusionSet.getId();

        // Get all the actorList where targetsActorToIntrusionSet equals to targetsActorToIntrusionSetId
        defaultActorShouldBeFound("targetsActorToIntrusionSetId.equals=" + targetsActorToIntrusionSetId);

        // Get all the actorList where targetsActorToIntrusionSet equals to targetsActorToIntrusionSetId + 1
        defaultActorShouldNotBeFound("targetsActorToIntrusionSetId.equals=" + (targetsActorToIntrusionSetId + 1));
    }


    @Test
    @Transactional
    public void getAllActorsByTargetsActorToMalwareIsEqualToSomething() throws Exception {
        // Initialize the database
        Malware targetsActorToMalware = MalwareResourceIntTest.createEntity(em);
        em.persist(targetsActorToMalware);
        em.flush();
        actor.addTargetsActorToMalware(targetsActorToMalware);
        actorRepository.saveAndFlush(actor);
        Long targetsActorToMalwareId = targetsActorToMalware.getId();

        // Get all the actorList where targetsActorToMalware equals to targetsActorToMalwareId
        defaultActorShouldBeFound("targetsActorToMalwareId.equals=" + targetsActorToMalwareId);

        // Get all the actorList where targetsActorToMalware equals to targetsActorToMalwareId + 1
        defaultActorShouldNotBeFound("targetsActorToMalwareId.equals=" + (targetsActorToMalwareId + 1));
    }


    @Test
    @Transactional
    public void getAllActorsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        actor.setLinkOf(linkOf);
        actorRepository.saveAndFlush(actor);
        Long linkOfId = linkOf.getId();

        // Get all the actorList where linkOf equals to linkOfId
        defaultActorShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the actorList where linkOf equals to linkOfId + 1
        defaultActorShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultActorShouldBeFound(String filter) throws Exception {
        restActorMockMvc.perform(get("/api/actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].classeIdentite").value(hasItem(DEFAULT_CLASSE_IDENTITE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultActorShouldNotBeFound(String filter) throws Exception {
        restActorMockMvc.perform(get("/api/actors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findById(actor.getId()).get();
        // Disconnect from session so that the updates on updatedActor are not directly saved in db
        em.detach(updatedActor);
        updatedActor
            .description(UPDATED_DESCRIPTION)
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .libelle(UPDATED_LIBELLE)
            .classeIdentite(UPDATED_CLASSE_IDENTITE);
        ActorDTO actorDTO = actorMapper.toDto(updatedActor);

        restActorMockMvc.perform(put("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testActor.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testActor.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActor.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testActor.getClasseIdentite()).isEqualTo(UPDATED_CLASSE_IDENTITE);

        // Validate the Actor in Elasticsearch
        verify(mockActorSearchRepository, times(1)).save(testActor);
    }

    @Test
    @Transactional
    public void updateNonExistingActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restActorMockMvc.perform(put("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Actor in Elasticsearch
        verify(mockActorSearchRepository, times(0)).save(actor);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Get the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Actor in Elasticsearch
        verify(mockActorSearchRepository, times(1)).deleteById(actor.getId());
    }

    @Test
    @Transactional
    public void searchActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);
        when(mockActorSearchRepository.search(queryStringQuery("id:" + actor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(actor), PageRequest.of(0, 1), 1));
        // Search the actor
        restActorMockMvc.perform(get("/api/_search/actors?query=id:" + actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].classeIdentite").value(hasItem(DEFAULT_CLASSE_IDENTITE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actor.class);
        Actor actor1 = new Actor();
        actor1.setId(1L);
        Actor actor2 = new Actor();
        actor2.setId(actor1.getId());
        assertThat(actor1).isEqualTo(actor2);
        actor2.setId(2L);
        assertThat(actor1).isNotEqualTo(actor2);
        actor1.setId(null);
        assertThat(actor1).isNotEqualTo(actor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActorDTO.class);
        ActorDTO actorDTO1 = new ActorDTO();
        actorDTO1.setId(1L);
        ActorDTO actorDTO2 = new ActorDTO();
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
        actorDTO2.setId(actorDTO1.getId());
        assertThat(actorDTO1).isEqualTo(actorDTO2);
        actorDTO2.setId(2L);
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
        actorDTO1.setId(null);
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(actorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(actorMapper.fromId(null)).isNull();
    }
}
