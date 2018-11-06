package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.ActivityPattern;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.ActivityPatternRepository;
import com.peploleum.insight.repository.search.ActivityPatternSearchRepository;
import com.peploleum.insight.service.ActivityPatternService;
import com.peploleum.insight.service.dto.ActivityPatternDTO;
import com.peploleum.insight.service.mapper.ActivityPatternMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.ActivityPatternCriteria;
import com.peploleum.insight.service.ActivityPatternQueryService;

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
 * Test class for the ActivityPatternResource REST controller.
 *
 * @see ActivityPatternResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class ActivityPatternResourceIntTest {

    private static final String DEFAULT_MODELE = "AAAAAAAAAA";
    private static final String UPDATED_MODELE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_VALIDE_A_PARTIR_DE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALIDE_A_PARTIR_DE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ActivityPatternRepository activityPatternRepository;


    @Autowired
    private ActivityPatternMapper activityPatternMapper;
    

    @Autowired
    private ActivityPatternService activityPatternService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.ActivityPatternSearchRepositoryMockConfiguration
     */
    @Autowired
    private ActivityPatternSearchRepository mockActivityPatternSearchRepository;

    @Autowired
    private ActivityPatternQueryService activityPatternQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityPatternMockMvc;

    private ActivityPattern activityPattern;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityPatternResource activityPatternResource = new ActivityPatternResource(activityPatternService, activityPatternQueryService);
        this.restActivityPatternMockMvc = MockMvcBuilders.standaloneSetup(activityPatternResource)
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
    public static ActivityPattern createEntity(EntityManager em) {
        ActivityPattern activityPattern = new ActivityPattern()
            .modele(DEFAULT_MODELE)
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .valideAPartirDe(DEFAULT_VALIDE_A_PARTIR_DE);
        return activityPattern;
    }

    @Before
    public void initTest() {
        activityPattern = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityPattern() throws Exception {
        int databaseSizeBeforeCreate = activityPatternRepository.findAll().size();

        // Create the ActivityPattern
        ActivityPatternDTO activityPatternDTO = activityPatternMapper.toDto(activityPattern);
        restActivityPatternMockMvc.perform(post("/api/activity-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityPatternDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityPattern in the database
        List<ActivityPattern> activityPatternList = activityPatternRepository.findAll();
        assertThat(activityPatternList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityPattern testActivityPattern = activityPatternList.get(activityPatternList.size() - 1);
        assertThat(testActivityPattern.getModele()).isEqualTo(DEFAULT_MODELE);
        assertThat(testActivityPattern.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testActivityPattern.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActivityPattern.getValideAPartirDe()).isEqualTo(DEFAULT_VALIDE_A_PARTIR_DE);

        // Validate the ActivityPattern in Elasticsearch
        verify(mockActivityPatternSearchRepository, times(1)).save(testActivityPattern);
    }

    @Test
    @Transactional
    public void createActivityPatternWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityPatternRepository.findAll().size();

        // Create the ActivityPattern with an existing ID
        activityPattern.setId(1L);
        ActivityPatternDTO activityPatternDTO = activityPatternMapper.toDto(activityPattern);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityPatternMockMvc.perform(post("/api/activity-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityPatternDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityPattern in the database
        List<ActivityPattern> activityPatternList = activityPatternRepository.findAll();
        assertThat(activityPatternList).hasSize(databaseSizeBeforeCreate);

        // Validate the ActivityPattern in Elasticsearch
        verify(mockActivityPatternSearchRepository, times(0)).save(activityPattern);
    }

    @Test
    @Transactional
    public void getAllActivityPatterns() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList
        restActivityPatternMockMvc.perform(get("/api/activity-patterns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].valideAPartirDe").value(hasItem(sameInstant(DEFAULT_VALIDE_A_PARTIR_DE))));
    }
    

    @Test
    @Transactional
    public void getActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get the activityPattern
        restActivityPatternMockMvc.perform(get("/api/activity-patterns/{id}", activityPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityPattern.getId().intValue()))
            .andExpect(jsonPath("$.modele").value(DEFAULT_MODELE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.valideAPartirDe").value(sameInstant(DEFAULT_VALIDE_A_PARTIR_DE)));
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByModeleIsEqualToSomething() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where modele equals to DEFAULT_MODELE
        defaultActivityPatternShouldBeFound("modele.equals=" + DEFAULT_MODELE);

        // Get all the activityPatternList where modele equals to UPDATED_MODELE
        defaultActivityPatternShouldNotBeFound("modele.equals=" + UPDATED_MODELE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByModeleIsInShouldWork() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where modele in DEFAULT_MODELE or UPDATED_MODELE
        defaultActivityPatternShouldBeFound("modele.in=" + DEFAULT_MODELE + "," + UPDATED_MODELE);

        // Get all the activityPatternList where modele equals to UPDATED_MODELE
        defaultActivityPatternShouldNotBeFound("modele.in=" + UPDATED_MODELE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByModeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where modele is not null
        defaultActivityPatternShouldBeFound("modele.specified=true");

        // Get all the activityPatternList where modele is null
        defaultActivityPatternShouldNotBeFound("modele.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where nom equals to DEFAULT_NOM
        defaultActivityPatternShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the activityPatternList where nom equals to UPDATED_NOM
        defaultActivityPatternShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultActivityPatternShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the activityPatternList where nom equals to UPDATED_NOM
        defaultActivityPatternShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where nom is not null
        defaultActivityPatternShouldBeFound("nom.specified=true");

        // Get all the activityPatternList where nom is null
        defaultActivityPatternShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where type equals to DEFAULT_TYPE
        defaultActivityPatternShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the activityPatternList where type equals to UPDATED_TYPE
        defaultActivityPatternShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultActivityPatternShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the activityPatternList where type equals to UPDATED_TYPE
        defaultActivityPatternShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where type is not null
        defaultActivityPatternShouldBeFound("type.specified=true");

        // Get all the activityPatternList where type is null
        defaultActivityPatternShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByValideAPartirDeIsEqualToSomething() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where valideAPartirDe equals to DEFAULT_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldBeFound("valideAPartirDe.equals=" + DEFAULT_VALIDE_A_PARTIR_DE);

        // Get all the activityPatternList where valideAPartirDe equals to UPDATED_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldNotBeFound("valideAPartirDe.equals=" + UPDATED_VALIDE_A_PARTIR_DE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByValideAPartirDeIsInShouldWork() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where valideAPartirDe in DEFAULT_VALIDE_A_PARTIR_DE or UPDATED_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldBeFound("valideAPartirDe.in=" + DEFAULT_VALIDE_A_PARTIR_DE + "," + UPDATED_VALIDE_A_PARTIR_DE);

        // Get all the activityPatternList where valideAPartirDe equals to UPDATED_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldNotBeFound("valideAPartirDe.in=" + UPDATED_VALIDE_A_PARTIR_DE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByValideAPartirDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where valideAPartirDe is not null
        defaultActivityPatternShouldBeFound("valideAPartirDe.specified=true");

        // Get all the activityPatternList where valideAPartirDe is null
        defaultActivityPatternShouldNotBeFound("valideAPartirDe.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByValideAPartirDeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where valideAPartirDe greater than or equals to DEFAULT_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldBeFound("valideAPartirDe.greaterOrEqualThan=" + DEFAULT_VALIDE_A_PARTIR_DE);

        // Get all the activityPatternList where valideAPartirDe greater than or equals to UPDATED_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldNotBeFound("valideAPartirDe.greaterOrEqualThan=" + UPDATED_VALIDE_A_PARTIR_DE);
    }

    @Test
    @Transactional
    public void getAllActivityPatternsByValideAPartirDeIsLessThanSomething() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatternList where valideAPartirDe less than or equals to DEFAULT_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldNotBeFound("valideAPartirDe.lessThan=" + DEFAULT_VALIDE_A_PARTIR_DE);

        // Get all the activityPatternList where valideAPartirDe less than or equals to UPDATED_VALIDE_A_PARTIR_DE
        defaultActivityPatternShouldBeFound("valideAPartirDe.lessThan=" + UPDATED_VALIDE_A_PARTIR_DE);
    }


    @Test
    @Transactional
    public void getAllActivityPatternsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        activityPattern.setLinkOf(linkOf);
        activityPatternRepository.saveAndFlush(activityPattern);
        Long linkOfId = linkOf.getId();

        // Get all the activityPatternList where linkOf equals to linkOfId
        defaultActivityPatternShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the activityPatternList where linkOf equals to linkOfId + 1
        defaultActivityPatternShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultActivityPatternShouldBeFound(String filter) throws Exception {
        restActivityPatternMockMvc.perform(get("/api/activity-patterns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].valideAPartirDe").value(hasItem(sameInstant(DEFAULT_VALIDE_A_PARTIR_DE))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultActivityPatternShouldNotBeFound(String filter) throws Exception {
        restActivityPatternMockMvc.perform(get("/api/activity-patterns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingActivityPattern() throws Exception {
        // Get the activityPattern
        restActivityPatternMockMvc.perform(get("/api/activity-patterns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        int databaseSizeBeforeUpdate = activityPatternRepository.findAll().size();

        // Update the activityPattern
        ActivityPattern updatedActivityPattern = activityPatternRepository.findById(activityPattern.getId()).get();
        // Disconnect from session so that the updates on updatedActivityPattern are not directly saved in db
        em.detach(updatedActivityPattern);
        updatedActivityPattern
            .modele(UPDATED_MODELE)
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .valideAPartirDe(UPDATED_VALIDE_A_PARTIR_DE);
        ActivityPatternDTO activityPatternDTO = activityPatternMapper.toDto(updatedActivityPattern);

        restActivityPatternMockMvc.perform(put("/api/activity-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityPatternDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityPattern in the database
        List<ActivityPattern> activityPatternList = activityPatternRepository.findAll();
        assertThat(activityPatternList).hasSize(databaseSizeBeforeUpdate);
        ActivityPattern testActivityPattern = activityPatternList.get(activityPatternList.size() - 1);
        assertThat(testActivityPattern.getModele()).isEqualTo(UPDATED_MODELE);
        assertThat(testActivityPattern.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testActivityPattern.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActivityPattern.getValideAPartirDe()).isEqualTo(UPDATED_VALIDE_A_PARTIR_DE);

        // Validate the ActivityPattern in Elasticsearch
        verify(mockActivityPatternSearchRepository, times(1)).save(testActivityPattern);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityPattern() throws Exception {
        int databaseSizeBeforeUpdate = activityPatternRepository.findAll().size();

        // Create the ActivityPattern
        ActivityPatternDTO activityPatternDTO = activityPatternMapper.toDto(activityPattern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restActivityPatternMockMvc.perform(put("/api/activity-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityPatternDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityPattern in the database
        List<ActivityPattern> activityPatternList = activityPatternRepository.findAll();
        assertThat(activityPatternList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ActivityPattern in Elasticsearch
        verify(mockActivityPatternSearchRepository, times(0)).save(activityPattern);
    }

    @Test
    @Transactional
    public void deleteActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        int databaseSizeBeforeDelete = activityPatternRepository.findAll().size();

        // Get the activityPattern
        restActivityPatternMockMvc.perform(delete("/api/activity-patterns/{id}", activityPattern.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityPattern> activityPatternList = activityPatternRepository.findAll();
        assertThat(activityPatternList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ActivityPattern in Elasticsearch
        verify(mockActivityPatternSearchRepository, times(1)).deleteById(activityPattern.getId());
    }

    @Test
    @Transactional
    public void searchActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);
        when(mockActivityPatternSearchRepository.search(queryStringQuery("id:" + activityPattern.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(activityPattern), PageRequest.of(0, 1), 1));
        // Search the activityPattern
        restActivityPatternMockMvc.perform(get("/api/_search/activity-patterns?query=id:" + activityPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].valideAPartirDe").value(hasItem(sameInstant(DEFAULT_VALIDE_A_PARTIR_DE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityPattern.class);
        ActivityPattern activityPattern1 = new ActivityPattern();
        activityPattern1.setId(1L);
        ActivityPattern activityPattern2 = new ActivityPattern();
        activityPattern2.setId(activityPattern1.getId());
        assertThat(activityPattern1).isEqualTo(activityPattern2);
        activityPattern2.setId(2L);
        assertThat(activityPattern1).isNotEqualTo(activityPattern2);
        activityPattern1.setId(null);
        assertThat(activityPattern1).isNotEqualTo(activityPattern2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityPatternDTO.class);
        ActivityPatternDTO activityPatternDTO1 = new ActivityPatternDTO();
        activityPatternDTO1.setId(1L);
        ActivityPatternDTO activityPatternDTO2 = new ActivityPatternDTO();
        assertThat(activityPatternDTO1).isNotEqualTo(activityPatternDTO2);
        activityPatternDTO2.setId(activityPatternDTO1.getId());
        assertThat(activityPatternDTO1).isEqualTo(activityPatternDTO2);
        activityPatternDTO2.setId(2L);
        assertThat(activityPatternDTO1).isNotEqualTo(activityPatternDTO2);
        activityPatternDTO1.setId(null);
        assertThat(activityPatternDTO1).isNotEqualTo(activityPatternDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityPatternMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityPatternMapper.fromId(null)).isNull();
    }
}
