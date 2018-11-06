package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Tool;
import com.peploleum.insight.domain.Vulnerability;
import com.peploleum.insight.domain.IntrusionSet;
import com.peploleum.insight.domain.Malware;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.domain.ThreatActor;
import com.peploleum.insight.repository.ToolRepository;
import com.peploleum.insight.repository.search.ToolSearchRepository;
import com.peploleum.insight.service.ToolService;
import com.peploleum.insight.service.dto.ToolDTO;
import com.peploleum.insight.service.mapper.ToolMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.ToolCriteria;
import com.peploleum.insight.service.ToolQueryService;

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
 * Test class for the ToolResource REST controller.
 *
 * @see ToolResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class ToolResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    @Autowired
    private ToolRepository toolRepository;


    @Autowired
    private ToolMapper toolMapper;
    

    @Autowired
    private ToolService toolService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.ToolSearchRepositoryMockConfiguration
     */
    @Autowired
    private ToolSearchRepository mockToolSearchRepository;

    @Autowired
    private ToolQueryService toolQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restToolMockMvc;

    private Tool tool;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ToolResource toolResource = new ToolResource(toolService, toolQueryService);
        this.restToolMockMvc = MockMvcBuilders.standaloneSetup(toolResource)
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
    public static Tool createEntity(EntityManager em) {
        Tool tool = new Tool()
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .version(DEFAULT_VERSION);
        return tool;
    }

    @Before
    public void initTest() {
        tool = createEntity(em);
    }

    @Test
    @Transactional
    public void createTool() throws Exception {
        int databaseSizeBeforeCreate = toolRepository.findAll().size();

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);
        restToolMockMvc.perform(post("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toolDTO)))
            .andExpect(status().isCreated());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeCreate + 1);
        Tool testTool = toolList.get(toolList.size() - 1);
        assertThat(testTool.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testTool.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTool.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTool.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTool.getVersion()).isEqualTo(DEFAULT_VERSION);

        // Validate the Tool in Elasticsearch
        verify(mockToolSearchRepository, times(1)).save(testTool);
    }

    @Test
    @Transactional
    public void createToolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toolRepository.findAll().size();

        // Create the Tool with an existing ID
        tool.setId(1L);
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToolMockMvc.perform(post("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeCreate);

        // Validate the Tool in Elasticsearch
        verify(mockToolSearchRepository, times(0)).save(tool);
    }

    @Test
    @Transactional
    public void getAllTools() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList
        restToolMockMvc.perform(get("/api/tools?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tool.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }
    

    @Test
    @Transactional
    public void getTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get the tool
        restToolMockMvc.perform(get("/api/tools/{id}", tool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tool.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getAllToolsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where nom equals to DEFAULT_NOM
        defaultToolShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the toolList where nom equals to UPDATED_NOM
        defaultToolShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllToolsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultToolShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the toolList where nom equals to UPDATED_NOM
        defaultToolShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllToolsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where nom is not null
        defaultToolShouldBeFound("nom.specified=true");

        // Get all the toolList where nom is null
        defaultToolShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllToolsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where type equals to DEFAULT_TYPE
        defaultToolShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the toolList where type equals to UPDATED_TYPE
        defaultToolShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllToolsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultToolShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the toolList where type equals to UPDATED_TYPE
        defaultToolShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllToolsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where type is not null
        defaultToolShouldBeFound("type.specified=true");

        // Get all the toolList where type is null
        defaultToolShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllToolsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where libelle equals to DEFAULT_LIBELLE
        defaultToolShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the toolList where libelle equals to UPDATED_LIBELLE
        defaultToolShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllToolsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultToolShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the toolList where libelle equals to UPDATED_LIBELLE
        defaultToolShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllToolsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where libelle is not null
        defaultToolShouldBeFound("libelle.specified=true");

        // Get all the toolList where libelle is null
        defaultToolShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllToolsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description equals to DEFAULT_DESCRIPTION
        defaultToolShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the toolList where description equals to UPDATED_DESCRIPTION
        defaultToolShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllToolsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultToolShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the toolList where description equals to UPDATED_DESCRIPTION
        defaultToolShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllToolsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where description is not null
        defaultToolShouldBeFound("description.specified=true");

        // Get all the toolList where description is null
        defaultToolShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllToolsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where version equals to DEFAULT_VERSION
        defaultToolShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the toolList where version equals to UPDATED_VERSION
        defaultToolShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllToolsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultToolShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the toolList where version equals to UPDATED_VERSION
        defaultToolShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void getAllToolsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        // Get all the toolList where version is not null
        defaultToolShouldBeFound("version.specified=true");

        // Get all the toolList where version is null
        defaultToolShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    public void getAllToolsByIsTargetsToolToVulnerabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        Vulnerability isTargetsToolToVulnerability = VulnerabilityResourceIntTest.createEntity(em);
        em.persist(isTargetsToolToVulnerability);
        em.flush();
        tool.addIsTargetsToolToVulnerability(isTargetsToolToVulnerability);
        toolRepository.saveAndFlush(tool);
        Long isTargetsToolToVulnerabilityId = isTargetsToolToVulnerability.getId();

        // Get all the toolList where isTargetsToolToVulnerability equals to isTargetsToolToVulnerabilityId
        defaultToolShouldBeFound("isTargetsToolToVulnerabilityId.equals=" + isTargetsToolToVulnerabilityId);

        // Get all the toolList where isTargetsToolToVulnerability equals to isTargetsToolToVulnerabilityId + 1
        defaultToolShouldNotBeFound("isTargetsToolToVulnerabilityId.equals=" + (isTargetsToolToVulnerabilityId + 1));
    }


    @Test
    @Transactional
    public void getAllToolsByUsesToolToIntrusionSetIsEqualToSomething() throws Exception {
        // Initialize the database
        IntrusionSet usesToolToIntrusionSet = IntrusionSetResourceIntTest.createEntity(em);
        em.persist(usesToolToIntrusionSet);
        em.flush();
        tool.setUsesToolToIntrusionSet(usesToolToIntrusionSet);
        toolRepository.saveAndFlush(tool);
        Long usesToolToIntrusionSetId = usesToolToIntrusionSet.getId();

        // Get all the toolList where usesToolToIntrusionSet equals to usesToolToIntrusionSetId
        defaultToolShouldBeFound("usesToolToIntrusionSetId.equals=" + usesToolToIntrusionSetId);

        // Get all the toolList where usesToolToIntrusionSet equals to usesToolToIntrusionSetId + 1
        defaultToolShouldNotBeFound("usesToolToIntrusionSetId.equals=" + (usesToolToIntrusionSetId + 1));
    }


    @Test
    @Transactional
    public void getAllToolsByIsUsesToolToMalwareIsEqualToSomething() throws Exception {
        // Initialize the database
        Malware isUsesToolToMalware = MalwareResourceIntTest.createEntity(em);
        em.persist(isUsesToolToMalware);
        em.flush();
        tool.setIsUsesToolToMalware(isUsesToolToMalware);
        toolRepository.saveAndFlush(tool);
        Long isUsesToolToMalwareId = isUsesToolToMalware.getId();

        // Get all the toolList where isUsesToolToMalware equals to isUsesToolToMalwareId
        defaultToolShouldBeFound("isUsesToolToMalwareId.equals=" + isUsesToolToMalwareId);

        // Get all the toolList where isUsesToolToMalware equals to isUsesToolToMalwareId + 1
        defaultToolShouldNotBeFound("isUsesToolToMalwareId.equals=" + (isUsesToolToMalwareId + 1));
    }


    @Test
    @Transactional
    public void getAllToolsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        tool.setLinkOf(linkOf);
        toolRepository.saveAndFlush(tool);
        Long linkOfId = linkOf.getId();

        // Get all the toolList where linkOf equals to linkOfId
        defaultToolShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the toolList where linkOf equals to linkOfId + 1
        defaultToolShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }


    @Test
    @Transactional
    public void getAllToolsByUsesToolToThreatActorIsEqualToSomething() throws Exception {
        // Initialize the database
        ThreatActor usesToolToThreatActor = ThreatActorResourceIntTest.createEntity(em);
        em.persist(usesToolToThreatActor);
        em.flush();
        tool.setUsesToolToThreatActor(usesToolToThreatActor);
        toolRepository.saveAndFlush(tool);
        Long usesToolToThreatActorId = usesToolToThreatActor.getId();

        // Get all the toolList where usesToolToThreatActor equals to usesToolToThreatActorId
        defaultToolShouldBeFound("usesToolToThreatActorId.equals=" + usesToolToThreatActorId);

        // Get all the toolList where usesToolToThreatActor equals to usesToolToThreatActorId + 1
        defaultToolShouldNotBeFound("usesToolToThreatActorId.equals=" + (usesToolToThreatActorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultToolShouldBeFound(String filter) throws Exception {
        restToolMockMvc.perform(get("/api/tools?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tool.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultToolShouldNotBeFound(String filter) throws Exception {
        restToolMockMvc.perform(get("/api/tools?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingTool() throws Exception {
        // Get the tool
        restToolMockMvc.perform(get("/api/tools/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        int databaseSizeBeforeUpdate = toolRepository.findAll().size();

        // Update the tool
        Tool updatedTool = toolRepository.findById(tool.getId()).get();
        // Disconnect from session so that the updates on updatedTool are not directly saved in db
        em.detach(updatedTool);
        updatedTool
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION);
        ToolDTO toolDTO = toolMapper.toDto(updatedTool);

        restToolMockMvc.perform(put("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toolDTO)))
            .andExpect(status().isOk());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);
        Tool testTool = toolList.get(toolList.size() - 1);
        assertThat(testTool.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testTool.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTool.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTool.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTool.getVersion()).isEqualTo(UPDATED_VERSION);

        // Validate the Tool in Elasticsearch
        verify(mockToolSearchRepository, times(1)).save(testTool);
    }

    @Test
    @Transactional
    public void updateNonExistingTool() throws Exception {
        int databaseSizeBeforeUpdate = toolRepository.findAll().size();

        // Create the Tool
        ToolDTO toolDTO = toolMapper.toDto(tool);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restToolMockMvc.perform(put("/api/tools")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tool in the database
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tool in Elasticsearch
        verify(mockToolSearchRepository, times(0)).save(tool);
    }

    @Test
    @Transactional
    public void deleteTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);

        int databaseSizeBeforeDelete = toolRepository.findAll().size();

        // Get the tool
        restToolMockMvc.perform(delete("/api/tools/{id}", tool.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tool> toolList = toolRepository.findAll();
        assertThat(toolList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Tool in Elasticsearch
        verify(mockToolSearchRepository, times(1)).deleteById(tool.getId());
    }

    @Test
    @Transactional
    public void searchTool() throws Exception {
        // Initialize the database
        toolRepository.saveAndFlush(tool);
        when(mockToolSearchRepository.search(queryStringQuery("id:" + tool.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tool), PageRequest.of(0, 1), 1));
        // Search the tool
        restToolMockMvc.perform(get("/api/_search/tools?query=id:" + tool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tool.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tool.class);
        Tool tool1 = new Tool();
        tool1.setId(1L);
        Tool tool2 = new Tool();
        tool2.setId(tool1.getId());
        assertThat(tool1).isEqualTo(tool2);
        tool2.setId(2L);
        assertThat(tool1).isNotEqualTo(tool2);
        tool1.setId(null);
        assertThat(tool1).isNotEqualTo(tool2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToolDTO.class);
        ToolDTO toolDTO1 = new ToolDTO();
        toolDTO1.setId(1L);
        ToolDTO toolDTO2 = new ToolDTO();
        assertThat(toolDTO1).isNotEqualTo(toolDTO2);
        toolDTO2.setId(toolDTO1.getId());
        assertThat(toolDTO1).isEqualTo(toolDTO2);
        toolDTO2.setId(2L);
        assertThat(toolDTO1).isNotEqualTo(toolDTO2);
        toolDTO1.setId(null);
        assertThat(toolDTO1).isNotEqualTo(toolDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(toolMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(toolMapper.fromId(null)).isNull();
    }
}
