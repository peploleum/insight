package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.AttackPattern;
import com.peploleum.insight.domain.Malware;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.AttackPatternRepository;
import com.peploleum.insight.repository.search.AttackPatternSearchRepository;
import com.peploleum.insight.service.AttackPatternService;
import com.peploleum.insight.service.dto.AttackPatternDTO;
import com.peploleum.insight.service.mapper.AttackPatternMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.AttackPatternCriteria;
import com.peploleum.insight.service.AttackPatternQueryService;

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
 * Test class for the AttackPatternResource REST controller.
 *
 * @see AttackPatternResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class AttackPatternResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_EXTERNE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_EXTERNE = "BBBBBBBBBB";

    private static final String DEFAULT_TUEUR_PROCESSUS = "AAAAAAAAAA";
    private static final String UPDATED_TUEUR_PROCESSUS = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private AttackPatternRepository attackPatternRepository;


    @Autowired
    private AttackPatternMapper attackPatternMapper;
    

    @Autowired
    private AttackPatternService attackPatternService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.AttackPatternSearchRepositoryMockConfiguration
     */
    @Autowired
    private AttackPatternSearchRepository mockAttackPatternSearchRepository;

    @Autowired
    private AttackPatternQueryService attackPatternQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttackPatternMockMvc;

    private AttackPattern attackPattern;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttackPatternResource attackPatternResource = new AttackPatternResource(attackPatternService, attackPatternQueryService);
        this.restAttackPatternMockMvc = MockMvcBuilders.standaloneSetup(attackPatternResource)
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
    public static AttackPattern createEntity(EntityManager em) {
        AttackPattern attackPattern = new AttackPattern()
            .description(DEFAULT_DESCRIPTION)
            .nom(DEFAULT_NOM)
            .referenceExterne(DEFAULT_REFERENCE_EXTERNE)
            .tueurProcessus(DEFAULT_TUEUR_PROCESSUS)
            .type(DEFAULT_TYPE);
        return attackPattern;
    }

    @Before
    public void initTest() {
        attackPattern = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttackPattern() throws Exception {
        int databaseSizeBeforeCreate = attackPatternRepository.findAll().size();

        // Create the AttackPattern
        AttackPatternDTO attackPatternDTO = attackPatternMapper.toDto(attackPattern);
        restAttackPatternMockMvc.perform(post("/api/attack-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attackPatternDTO)))
            .andExpect(status().isCreated());

        // Validate the AttackPattern in the database
        List<AttackPattern> attackPatternList = attackPatternRepository.findAll();
        assertThat(attackPatternList).hasSize(databaseSizeBeforeCreate + 1);
        AttackPattern testAttackPattern = attackPatternList.get(attackPatternList.size() - 1);
        assertThat(testAttackPattern.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttackPattern.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAttackPattern.getReferenceExterne()).isEqualTo(DEFAULT_REFERENCE_EXTERNE);
        assertThat(testAttackPattern.getTueurProcessus()).isEqualTo(DEFAULT_TUEUR_PROCESSUS);
        assertThat(testAttackPattern.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the AttackPattern in Elasticsearch
        verify(mockAttackPatternSearchRepository, times(1)).save(testAttackPattern);
    }

    @Test
    @Transactional
    public void createAttackPatternWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attackPatternRepository.findAll().size();

        // Create the AttackPattern with an existing ID
        attackPattern.setId(1L);
        AttackPatternDTO attackPatternDTO = attackPatternMapper.toDto(attackPattern);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttackPatternMockMvc.perform(post("/api/attack-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attackPatternDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AttackPattern in the database
        List<AttackPattern> attackPatternList = attackPatternRepository.findAll();
        assertThat(attackPatternList).hasSize(databaseSizeBeforeCreate);

        // Validate the AttackPattern in Elasticsearch
        verify(mockAttackPatternSearchRepository, times(0)).save(attackPattern);
    }

    @Test
    @Transactional
    public void getAllAttackPatterns() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList
        restAttackPatternMockMvc.perform(get("/api/attack-patterns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attackPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].referenceExterne").value(hasItem(DEFAULT_REFERENCE_EXTERNE.toString())))
            .andExpect(jsonPath("$.[*].tueurProcessus").value(hasItem(DEFAULT_TUEUR_PROCESSUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getAttackPattern() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get the attackPattern
        restAttackPatternMockMvc.perform(get("/api/attack-patterns/{id}", attackPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attackPattern.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.referenceExterne").value(DEFAULT_REFERENCE_EXTERNE.toString()))
            .andExpect(jsonPath("$.tueurProcessus").value(DEFAULT_TUEUR_PROCESSUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where description equals to DEFAULT_DESCRIPTION
        defaultAttackPatternShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the attackPatternList where description equals to UPDATED_DESCRIPTION
        defaultAttackPatternShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAttackPatternShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the attackPatternList where description equals to UPDATED_DESCRIPTION
        defaultAttackPatternShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where description is not null
        defaultAttackPatternShouldBeFound("description.specified=true");

        // Get all the attackPatternList where description is null
        defaultAttackPatternShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where nom equals to DEFAULT_NOM
        defaultAttackPatternShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the attackPatternList where nom equals to UPDATED_NOM
        defaultAttackPatternShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultAttackPatternShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the attackPatternList where nom equals to UPDATED_NOM
        defaultAttackPatternShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where nom is not null
        defaultAttackPatternShouldBeFound("nom.specified=true");

        // Get all the attackPatternList where nom is null
        defaultAttackPatternShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByReferenceExterneIsEqualToSomething() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where referenceExterne equals to DEFAULT_REFERENCE_EXTERNE
        defaultAttackPatternShouldBeFound("referenceExterne.equals=" + DEFAULT_REFERENCE_EXTERNE);

        // Get all the attackPatternList where referenceExterne equals to UPDATED_REFERENCE_EXTERNE
        defaultAttackPatternShouldNotBeFound("referenceExterne.equals=" + UPDATED_REFERENCE_EXTERNE);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByReferenceExterneIsInShouldWork() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where referenceExterne in DEFAULT_REFERENCE_EXTERNE or UPDATED_REFERENCE_EXTERNE
        defaultAttackPatternShouldBeFound("referenceExterne.in=" + DEFAULT_REFERENCE_EXTERNE + "," + UPDATED_REFERENCE_EXTERNE);

        // Get all the attackPatternList where referenceExterne equals to UPDATED_REFERENCE_EXTERNE
        defaultAttackPatternShouldNotBeFound("referenceExterne.in=" + UPDATED_REFERENCE_EXTERNE);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByReferenceExterneIsNullOrNotNull() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where referenceExterne is not null
        defaultAttackPatternShouldBeFound("referenceExterne.specified=true");

        // Get all the attackPatternList where referenceExterne is null
        defaultAttackPatternShouldNotBeFound("referenceExterne.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByTueurProcessusIsEqualToSomething() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where tueurProcessus equals to DEFAULT_TUEUR_PROCESSUS
        defaultAttackPatternShouldBeFound("tueurProcessus.equals=" + DEFAULT_TUEUR_PROCESSUS);

        // Get all the attackPatternList where tueurProcessus equals to UPDATED_TUEUR_PROCESSUS
        defaultAttackPatternShouldNotBeFound("tueurProcessus.equals=" + UPDATED_TUEUR_PROCESSUS);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByTueurProcessusIsInShouldWork() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where tueurProcessus in DEFAULT_TUEUR_PROCESSUS or UPDATED_TUEUR_PROCESSUS
        defaultAttackPatternShouldBeFound("tueurProcessus.in=" + DEFAULT_TUEUR_PROCESSUS + "," + UPDATED_TUEUR_PROCESSUS);

        // Get all the attackPatternList where tueurProcessus equals to UPDATED_TUEUR_PROCESSUS
        defaultAttackPatternShouldNotBeFound("tueurProcessus.in=" + UPDATED_TUEUR_PROCESSUS);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByTueurProcessusIsNullOrNotNull() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where tueurProcessus is not null
        defaultAttackPatternShouldBeFound("tueurProcessus.specified=true");

        // Get all the attackPatternList where tueurProcessus is null
        defaultAttackPatternShouldNotBeFound("tueurProcessus.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where type equals to DEFAULT_TYPE
        defaultAttackPatternShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the attackPatternList where type equals to UPDATED_TYPE
        defaultAttackPatternShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAttackPatternShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the attackPatternList where type equals to UPDATED_TYPE
        defaultAttackPatternShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        // Get all the attackPatternList where type is not null
        defaultAttackPatternShouldBeFound("type.specified=true");

        // Get all the attackPatternList where type is null
        defaultAttackPatternShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttackPatternsByUsesAttackPatternToMalwareIsEqualToSomething() throws Exception {
        // Initialize the database
        Malware usesAttackPatternToMalware = MalwareResourceIntTest.createEntity(em);
        em.persist(usesAttackPatternToMalware);
        em.flush();
        attackPattern.addUsesAttackPatternToMalware(usesAttackPatternToMalware);
        attackPatternRepository.saveAndFlush(attackPattern);
        Long usesAttackPatternToMalwareId = usesAttackPatternToMalware.getId();

        // Get all the attackPatternList where usesAttackPatternToMalware equals to usesAttackPatternToMalwareId
        defaultAttackPatternShouldBeFound("usesAttackPatternToMalwareId.equals=" + usesAttackPatternToMalwareId);

        // Get all the attackPatternList where usesAttackPatternToMalware equals to usesAttackPatternToMalwareId + 1
        defaultAttackPatternShouldNotBeFound("usesAttackPatternToMalwareId.equals=" + (usesAttackPatternToMalwareId + 1));
    }


    @Test
    @Transactional
    public void getAllAttackPatternsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        attackPattern.setLinkOf(linkOf);
        attackPatternRepository.saveAndFlush(attackPattern);
        Long linkOfId = linkOf.getId();

        // Get all the attackPatternList where linkOf equals to linkOfId
        defaultAttackPatternShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the attackPatternList where linkOf equals to linkOfId + 1
        defaultAttackPatternShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAttackPatternShouldBeFound(String filter) throws Exception {
        restAttackPatternMockMvc.perform(get("/api/attack-patterns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attackPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].referenceExterne").value(hasItem(DEFAULT_REFERENCE_EXTERNE.toString())))
            .andExpect(jsonPath("$.[*].tueurProcessus").value(hasItem(DEFAULT_TUEUR_PROCESSUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAttackPatternShouldNotBeFound(String filter) throws Exception {
        restAttackPatternMockMvc.perform(get("/api/attack-patterns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingAttackPattern() throws Exception {
        // Get the attackPattern
        restAttackPatternMockMvc.perform(get("/api/attack-patterns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttackPattern() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        int databaseSizeBeforeUpdate = attackPatternRepository.findAll().size();

        // Update the attackPattern
        AttackPattern updatedAttackPattern = attackPatternRepository.findById(attackPattern.getId()).get();
        // Disconnect from session so that the updates on updatedAttackPattern are not directly saved in db
        em.detach(updatedAttackPattern);
        updatedAttackPattern
            .description(UPDATED_DESCRIPTION)
            .nom(UPDATED_NOM)
            .referenceExterne(UPDATED_REFERENCE_EXTERNE)
            .tueurProcessus(UPDATED_TUEUR_PROCESSUS)
            .type(UPDATED_TYPE);
        AttackPatternDTO attackPatternDTO = attackPatternMapper.toDto(updatedAttackPattern);

        restAttackPatternMockMvc.perform(put("/api/attack-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attackPatternDTO)))
            .andExpect(status().isOk());

        // Validate the AttackPattern in the database
        List<AttackPattern> attackPatternList = attackPatternRepository.findAll();
        assertThat(attackPatternList).hasSize(databaseSizeBeforeUpdate);
        AttackPattern testAttackPattern = attackPatternList.get(attackPatternList.size() - 1);
        assertThat(testAttackPattern.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttackPattern.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAttackPattern.getReferenceExterne()).isEqualTo(UPDATED_REFERENCE_EXTERNE);
        assertThat(testAttackPattern.getTueurProcessus()).isEqualTo(UPDATED_TUEUR_PROCESSUS);
        assertThat(testAttackPattern.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the AttackPattern in Elasticsearch
        verify(mockAttackPatternSearchRepository, times(1)).save(testAttackPattern);
    }

    @Test
    @Transactional
    public void updateNonExistingAttackPattern() throws Exception {
        int databaseSizeBeforeUpdate = attackPatternRepository.findAll().size();

        // Create the AttackPattern
        AttackPatternDTO attackPatternDTO = attackPatternMapper.toDto(attackPattern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restAttackPatternMockMvc.perform(put("/api/attack-patterns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attackPatternDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AttackPattern in the database
        List<AttackPattern> attackPatternList = attackPatternRepository.findAll();
        assertThat(attackPatternList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AttackPattern in Elasticsearch
        verify(mockAttackPatternSearchRepository, times(0)).save(attackPattern);
    }

    @Test
    @Transactional
    public void deleteAttackPattern() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);

        int databaseSizeBeforeDelete = attackPatternRepository.findAll().size();

        // Get the attackPattern
        restAttackPatternMockMvc.perform(delete("/api/attack-patterns/{id}", attackPattern.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AttackPattern> attackPatternList = attackPatternRepository.findAll();
        assertThat(attackPatternList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AttackPattern in Elasticsearch
        verify(mockAttackPatternSearchRepository, times(1)).deleteById(attackPattern.getId());
    }

    @Test
    @Transactional
    public void searchAttackPattern() throws Exception {
        // Initialize the database
        attackPatternRepository.saveAndFlush(attackPattern);
        when(mockAttackPatternSearchRepository.search(queryStringQuery("id:" + attackPattern.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(attackPattern), PageRequest.of(0, 1), 1));
        // Search the attackPattern
        restAttackPatternMockMvc.perform(get("/api/_search/attack-patterns?query=id:" + attackPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attackPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].referenceExterne").value(hasItem(DEFAULT_REFERENCE_EXTERNE.toString())))
            .andExpect(jsonPath("$.[*].tueurProcessus").value(hasItem(DEFAULT_TUEUR_PROCESSUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttackPattern.class);
        AttackPattern attackPattern1 = new AttackPattern();
        attackPattern1.setId(1L);
        AttackPattern attackPattern2 = new AttackPattern();
        attackPattern2.setId(attackPattern1.getId());
        assertThat(attackPattern1).isEqualTo(attackPattern2);
        attackPattern2.setId(2L);
        assertThat(attackPattern1).isNotEqualTo(attackPattern2);
        attackPattern1.setId(null);
        assertThat(attackPattern1).isNotEqualTo(attackPattern2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttackPatternDTO.class);
        AttackPatternDTO attackPatternDTO1 = new AttackPatternDTO();
        attackPatternDTO1.setId(1L);
        AttackPatternDTO attackPatternDTO2 = new AttackPatternDTO();
        assertThat(attackPatternDTO1).isNotEqualTo(attackPatternDTO2);
        attackPatternDTO2.setId(attackPatternDTO1.getId());
        assertThat(attackPatternDTO1).isEqualTo(attackPatternDTO2);
        attackPatternDTO2.setId(2L);
        assertThat(attackPatternDTO1).isNotEqualTo(attackPatternDTO2);
        attackPatternDTO1.setId(null);
        assertThat(attackPatternDTO1).isNotEqualTo(attackPatternDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(attackPatternMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(attackPatternMapper.fromId(null)).isNull();
    }
}
