package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.CourseOfAction;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.CourseOfActionRepository;
import com.peploleum.insight.repository.search.CourseOfActionSearchRepository;
import com.peploleum.insight.service.CourseOfActionService;
import com.peploleum.insight.service.dto.CourseOfActionDTO;
import com.peploleum.insight.service.mapper.CourseOfActionMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.CourseOfActionCriteria;
import com.peploleum.insight.service.CourseOfActionQueryService;

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
 * Test class for the CourseOfActionResource REST controller.
 *
 * @see CourseOfActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class CourseOfActionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private CourseOfActionRepository courseOfActionRepository;


    @Autowired
    private CourseOfActionMapper courseOfActionMapper;
    

    @Autowired
    private CourseOfActionService courseOfActionService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.CourseOfActionSearchRepositoryMockConfiguration
     */
    @Autowired
    private CourseOfActionSearchRepository mockCourseOfActionSearchRepository;

    @Autowired
    private CourseOfActionQueryService courseOfActionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseOfActionMockMvc;

    private CourseOfAction courseOfAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseOfActionResource courseOfActionResource = new CourseOfActionResource(courseOfActionService, courseOfActionQueryService);
        this.restCourseOfActionMockMvc = MockMvcBuilders.standaloneSetup(courseOfActionResource)
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
    public static CourseOfAction createEntity(EntityManager em) {
        CourseOfAction courseOfAction = new CourseOfAction()
            .description(DEFAULT_DESCRIPTION)
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE);
        return courseOfAction;
    }

    @Before
    public void initTest() {
        courseOfAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseOfAction() throws Exception {
        int databaseSizeBeforeCreate = courseOfActionRepository.findAll().size();

        // Create the CourseOfAction
        CourseOfActionDTO courseOfActionDTO = courseOfActionMapper.toDto(courseOfAction);
        restCourseOfActionMockMvc.perform(post("/api/course-of-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOfActionDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseOfAction in the database
        List<CourseOfAction> courseOfActionList = courseOfActionRepository.findAll();
        assertThat(courseOfActionList).hasSize(databaseSizeBeforeCreate + 1);
        CourseOfAction testCourseOfAction = courseOfActionList.get(courseOfActionList.size() - 1);
        assertThat(testCourseOfAction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourseOfAction.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCourseOfAction.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the CourseOfAction in Elasticsearch
        verify(mockCourseOfActionSearchRepository, times(1)).save(testCourseOfAction);
    }

    @Test
    @Transactional
    public void createCourseOfActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseOfActionRepository.findAll().size();

        // Create the CourseOfAction with an existing ID
        courseOfAction.setId(1L);
        CourseOfActionDTO courseOfActionDTO = courseOfActionMapper.toDto(courseOfAction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseOfActionMockMvc.perform(post("/api/course-of-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOfActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseOfAction in the database
        List<CourseOfAction> courseOfActionList = courseOfActionRepository.findAll();
        assertThat(courseOfActionList).hasSize(databaseSizeBeforeCreate);

        // Validate the CourseOfAction in Elasticsearch
        verify(mockCourseOfActionSearchRepository, times(0)).save(courseOfAction);
    }

    @Test
    @Transactional
    public void getAllCourseOfActions() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList
        restCourseOfActionMockMvc.perform(get("/api/course-of-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseOfAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getCourseOfAction() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get the courseOfAction
        restCourseOfActionMockMvc.perform(get("/api/course-of-actions/{id}", courseOfAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseOfAction.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where description equals to DEFAULT_DESCRIPTION
        defaultCourseOfActionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the courseOfActionList where description equals to UPDATED_DESCRIPTION
        defaultCourseOfActionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCourseOfActionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the courseOfActionList where description equals to UPDATED_DESCRIPTION
        defaultCourseOfActionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where description is not null
        defaultCourseOfActionShouldBeFound("description.specified=true");

        // Get all the courseOfActionList where description is null
        defaultCourseOfActionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where nom equals to DEFAULT_NOM
        defaultCourseOfActionShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the courseOfActionList where nom equals to UPDATED_NOM
        defaultCourseOfActionShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultCourseOfActionShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the courseOfActionList where nom equals to UPDATED_NOM
        defaultCourseOfActionShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where nom is not null
        defaultCourseOfActionShouldBeFound("nom.specified=true");

        // Get all the courseOfActionList where nom is null
        defaultCourseOfActionShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where type equals to DEFAULT_TYPE
        defaultCourseOfActionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the courseOfActionList where type equals to UPDATED_TYPE
        defaultCourseOfActionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCourseOfActionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the courseOfActionList where type equals to UPDATED_TYPE
        defaultCourseOfActionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        // Get all the courseOfActionList where type is not null
        defaultCourseOfActionShouldBeFound("type.specified=true");

        // Get all the courseOfActionList where type is null
        defaultCourseOfActionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllCourseOfActionsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        courseOfAction.setLinkOf(linkOf);
        courseOfActionRepository.saveAndFlush(courseOfAction);
        Long linkOfId = linkOf.getId();

        // Get all the courseOfActionList where linkOf equals to linkOfId
        defaultCourseOfActionShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the courseOfActionList where linkOf equals to linkOfId + 1
        defaultCourseOfActionShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCourseOfActionShouldBeFound(String filter) throws Exception {
        restCourseOfActionMockMvc.perform(get("/api/course-of-actions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseOfAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCourseOfActionShouldNotBeFound(String filter) throws Exception {
        restCourseOfActionMockMvc.perform(get("/api/course-of-actions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCourseOfAction() throws Exception {
        // Get the courseOfAction
        restCourseOfActionMockMvc.perform(get("/api/course-of-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseOfAction() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        int databaseSizeBeforeUpdate = courseOfActionRepository.findAll().size();

        // Update the courseOfAction
        CourseOfAction updatedCourseOfAction = courseOfActionRepository.findById(courseOfAction.getId()).get();
        // Disconnect from session so that the updates on updatedCourseOfAction are not directly saved in db
        em.detach(updatedCourseOfAction);
        updatedCourseOfAction
            .description(UPDATED_DESCRIPTION)
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE);
        CourseOfActionDTO courseOfActionDTO = courseOfActionMapper.toDto(updatedCourseOfAction);

        restCourseOfActionMockMvc.perform(put("/api/course-of-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOfActionDTO)))
            .andExpect(status().isOk());

        // Validate the CourseOfAction in the database
        List<CourseOfAction> courseOfActionList = courseOfActionRepository.findAll();
        assertThat(courseOfActionList).hasSize(databaseSizeBeforeUpdate);
        CourseOfAction testCourseOfAction = courseOfActionList.get(courseOfActionList.size() - 1);
        assertThat(testCourseOfAction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourseOfAction.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCourseOfAction.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the CourseOfAction in Elasticsearch
        verify(mockCourseOfActionSearchRepository, times(1)).save(testCourseOfAction);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseOfAction() throws Exception {
        int databaseSizeBeforeUpdate = courseOfActionRepository.findAll().size();

        // Create the CourseOfAction
        CourseOfActionDTO courseOfActionDTO = courseOfActionMapper.toDto(courseOfAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCourseOfActionMockMvc.perform(put("/api/course-of-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseOfActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseOfAction in the database
        List<CourseOfAction> courseOfActionList = courseOfActionRepository.findAll();
        assertThat(courseOfActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CourseOfAction in Elasticsearch
        verify(mockCourseOfActionSearchRepository, times(0)).save(courseOfAction);
    }

    @Test
    @Transactional
    public void deleteCourseOfAction() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);

        int databaseSizeBeforeDelete = courseOfActionRepository.findAll().size();

        // Get the courseOfAction
        restCourseOfActionMockMvc.perform(delete("/api/course-of-actions/{id}", courseOfAction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseOfAction> courseOfActionList = courseOfActionRepository.findAll();
        assertThat(courseOfActionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CourseOfAction in Elasticsearch
        verify(mockCourseOfActionSearchRepository, times(1)).deleteById(courseOfAction.getId());
    }

    @Test
    @Transactional
    public void searchCourseOfAction() throws Exception {
        // Initialize the database
        courseOfActionRepository.saveAndFlush(courseOfAction);
        when(mockCourseOfActionSearchRepository.search(queryStringQuery("id:" + courseOfAction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(courseOfAction), PageRequest.of(0, 1), 1));
        // Search the courseOfAction
        restCourseOfActionMockMvc.perform(get("/api/_search/course-of-actions?query=id:" + courseOfAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseOfAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseOfAction.class);
        CourseOfAction courseOfAction1 = new CourseOfAction();
        courseOfAction1.setId(1L);
        CourseOfAction courseOfAction2 = new CourseOfAction();
        courseOfAction2.setId(courseOfAction1.getId());
        assertThat(courseOfAction1).isEqualTo(courseOfAction2);
        courseOfAction2.setId(2L);
        assertThat(courseOfAction1).isNotEqualTo(courseOfAction2);
        courseOfAction1.setId(null);
        assertThat(courseOfAction1).isNotEqualTo(courseOfAction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseOfActionDTO.class);
        CourseOfActionDTO courseOfActionDTO1 = new CourseOfActionDTO();
        courseOfActionDTO1.setId(1L);
        CourseOfActionDTO courseOfActionDTO2 = new CourseOfActionDTO();
        assertThat(courseOfActionDTO1).isNotEqualTo(courseOfActionDTO2);
        courseOfActionDTO2.setId(courseOfActionDTO1.getId());
        assertThat(courseOfActionDTO1).isEqualTo(courseOfActionDTO2);
        courseOfActionDTO2.setId(2L);
        assertThat(courseOfActionDTO1).isNotEqualTo(courseOfActionDTO2);
        courseOfActionDTO1.setId(null);
        assertThat(courseOfActionDTO1).isNotEqualTo(courseOfActionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(courseOfActionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(courseOfActionMapper.fromId(null)).isNull();
    }
}
