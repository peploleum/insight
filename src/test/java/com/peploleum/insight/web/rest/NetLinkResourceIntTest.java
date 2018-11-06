package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.domain.AttackPattern;
import com.peploleum.insight.domain.Campaign;
import com.peploleum.insight.domain.CourseOfAction;
import com.peploleum.insight.domain.Actor;
import com.peploleum.insight.domain.ActivityPattern;
import com.peploleum.insight.domain.IntrusionSet;
import com.peploleum.insight.domain.Malware;
import com.peploleum.insight.domain.ObservedData;
import com.peploleum.insight.domain.Report;
import com.peploleum.insight.domain.ThreatActor;
import com.peploleum.insight.domain.Tool;
import com.peploleum.insight.domain.Vulnerability;
import com.peploleum.insight.repository.NetLinkRepository;
import com.peploleum.insight.repository.search.NetLinkSearchRepository;
import com.peploleum.insight.service.NetLinkService;
import com.peploleum.insight.service.dto.NetLinkDTO;
import com.peploleum.insight.service.mapper.NetLinkMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.NetLinkCriteria;
import com.peploleum.insight.service.NetLinkQueryService;

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
 * Test class for the NetLinkResource REST controller.
 *
 * @see NetLinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class NetLinkResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    @Autowired
    private NetLinkRepository netLinkRepository;


    @Autowired
    private NetLinkMapper netLinkMapper;
    

    @Autowired
    private NetLinkService netLinkService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.NetLinkSearchRepositoryMockConfiguration
     */
    @Autowired
    private NetLinkSearchRepository mockNetLinkSearchRepository;

    @Autowired
    private NetLinkQueryService netLinkQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNetLinkMockMvc;

    private NetLink netLink;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NetLinkResource netLinkResource = new NetLinkResource(netLinkService, netLinkQueryService);
        this.restNetLinkMockMvc = MockMvcBuilders.standaloneSetup(netLinkResource)
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
    public static NetLink createEntity(EntityManager em) {
        NetLink netLink = new NetLink()
            .description(DEFAULT_DESCRIPTION)
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .level(DEFAULT_LEVEL);
        return netLink;
    }

    @Before
    public void initTest() {
        netLink = createEntity(em);
    }

    @Test
    @Transactional
    public void createNetLink() throws Exception {
        int databaseSizeBeforeCreate = netLinkRepository.findAll().size();

        // Create the NetLink
        NetLinkDTO netLinkDTO = netLinkMapper.toDto(netLink);
        restNetLinkMockMvc.perform(post("/api/net-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netLinkDTO)))
            .andExpect(status().isCreated());

        // Validate the NetLink in the database
        List<NetLink> netLinkList = netLinkRepository.findAll();
        assertThat(netLinkList).hasSize(databaseSizeBeforeCreate + 1);
        NetLink testNetLink = netLinkList.get(netLinkList.size() - 1);
        assertThat(testNetLink.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNetLink.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testNetLink.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNetLink.getLevel()).isEqualTo(DEFAULT_LEVEL);

        // Validate the NetLink in Elasticsearch
        verify(mockNetLinkSearchRepository, times(1)).save(testNetLink);
    }

    @Test
    @Transactional
    public void createNetLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = netLinkRepository.findAll().size();

        // Create the NetLink with an existing ID
        netLink.setId(1L);
        NetLinkDTO netLinkDTO = netLinkMapper.toDto(netLink);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetLinkMockMvc.perform(post("/api/net-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netLinkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NetLink in the database
        List<NetLink> netLinkList = netLinkRepository.findAll();
        assertThat(netLinkList).hasSize(databaseSizeBeforeCreate);

        // Validate the NetLink in Elasticsearch
        verify(mockNetLinkSearchRepository, times(0)).save(netLink);
    }

    @Test
    @Transactional
    public void getAllNetLinks() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList
        restNetLinkMockMvc.perform(get("/api/net-links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }
    

    @Test
    @Transactional
    public void getNetLink() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get the netLink
        restNetLinkMockMvc.perform(get("/api/net-links/{id}", netLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(netLink.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getAllNetLinksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where description equals to DEFAULT_DESCRIPTION
        defaultNetLinkShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the netLinkList where description equals to UPDATED_DESCRIPTION
        defaultNetLinkShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNetLinksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNetLinkShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the netLinkList where description equals to UPDATED_DESCRIPTION
        defaultNetLinkShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNetLinksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where description is not null
        defaultNetLinkShouldBeFound("description.specified=true");

        // Get all the netLinkList where description is null
        defaultNetLinkShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllNetLinksByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where nom equals to DEFAULT_NOM
        defaultNetLinkShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the netLinkList where nom equals to UPDATED_NOM
        defaultNetLinkShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllNetLinksByNomIsInShouldWork() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultNetLinkShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the netLinkList where nom equals to UPDATED_NOM
        defaultNetLinkShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllNetLinksByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where nom is not null
        defaultNetLinkShouldBeFound("nom.specified=true");

        // Get all the netLinkList where nom is null
        defaultNetLinkShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllNetLinksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where type equals to DEFAULT_TYPE
        defaultNetLinkShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the netLinkList where type equals to UPDATED_TYPE
        defaultNetLinkShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNetLinksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultNetLinkShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the netLinkList where type equals to UPDATED_TYPE
        defaultNetLinkShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNetLinksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where type is not null
        defaultNetLinkShouldBeFound("type.specified=true");

        // Get all the netLinkList where type is null
        defaultNetLinkShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllNetLinksByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where level equals to DEFAULT_LEVEL
        defaultNetLinkShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the netLinkList where level equals to UPDATED_LEVEL
        defaultNetLinkShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllNetLinksByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultNetLinkShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the netLinkList where level equals to UPDATED_LEVEL
        defaultNetLinkShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllNetLinksByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        // Get all the netLinkList where level is not null
        defaultNetLinkShouldBeFound("level.specified=true");

        // Get all the netLinkList where level is null
        defaultNetLinkShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfAttackPatternIsEqualToSomething() throws Exception {
        // Initialize the database
        AttackPattern isLinkOfAttackPattern = AttackPatternResourceIntTest.createEntity(em);
        em.persist(isLinkOfAttackPattern);
        em.flush();
        netLink.addIsLinkOfAttackPattern(isLinkOfAttackPattern);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfAttackPatternId = isLinkOfAttackPattern.getId();

        // Get all the netLinkList where isLinkOfAttackPattern equals to isLinkOfAttackPatternId
        defaultNetLinkShouldBeFound("isLinkOfAttackPatternId.equals=" + isLinkOfAttackPatternId);

        // Get all the netLinkList where isLinkOfAttackPattern equals to isLinkOfAttackPatternId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfAttackPatternId.equals=" + (isLinkOfAttackPatternId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfCampaignIsEqualToSomething() throws Exception {
        // Initialize the database
        Campaign isLinkOfCampaign = CampaignResourceIntTest.createEntity(em);
        em.persist(isLinkOfCampaign);
        em.flush();
        netLink.addIsLinkOfCampaign(isLinkOfCampaign);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfCampaignId = isLinkOfCampaign.getId();

        // Get all the netLinkList where isLinkOfCampaign equals to isLinkOfCampaignId
        defaultNetLinkShouldBeFound("isLinkOfCampaignId.equals=" + isLinkOfCampaignId);

        // Get all the netLinkList where isLinkOfCampaign equals to isLinkOfCampaignId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfCampaignId.equals=" + (isLinkOfCampaignId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfCourseOfActionIsEqualToSomething() throws Exception {
        // Initialize the database
        CourseOfAction isLinkOfCourseOfAction = CourseOfActionResourceIntTest.createEntity(em);
        em.persist(isLinkOfCourseOfAction);
        em.flush();
        netLink.addIsLinkOfCourseOfAction(isLinkOfCourseOfAction);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfCourseOfActionId = isLinkOfCourseOfAction.getId();

        // Get all the netLinkList where isLinkOfCourseOfAction equals to isLinkOfCourseOfActionId
        defaultNetLinkShouldBeFound("isLinkOfCourseOfActionId.equals=" + isLinkOfCourseOfActionId);

        // Get all the netLinkList where isLinkOfCourseOfAction equals to isLinkOfCourseOfActionId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfCourseOfActionId.equals=" + (isLinkOfCourseOfActionId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfActorIsEqualToSomething() throws Exception {
        // Initialize the database
        Actor isLinkOfActor = ActorResourceIntTest.createEntity(em);
        em.persist(isLinkOfActor);
        em.flush();
        netLink.addIsLinkOfActor(isLinkOfActor);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfActorId = isLinkOfActor.getId();

        // Get all the netLinkList where isLinkOfActor equals to isLinkOfActorId
        defaultNetLinkShouldBeFound("isLinkOfActorId.equals=" + isLinkOfActorId);

        // Get all the netLinkList where isLinkOfActor equals to isLinkOfActorId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfActorId.equals=" + (isLinkOfActorId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfActivityPatternIsEqualToSomething() throws Exception {
        // Initialize the database
        ActivityPattern isLinkOfActivityPattern = ActivityPatternResourceIntTest.createEntity(em);
        em.persist(isLinkOfActivityPattern);
        em.flush();
        netLink.addIsLinkOfActivityPattern(isLinkOfActivityPattern);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfActivityPatternId = isLinkOfActivityPattern.getId();

        // Get all the netLinkList where isLinkOfActivityPattern equals to isLinkOfActivityPatternId
        defaultNetLinkShouldBeFound("isLinkOfActivityPatternId.equals=" + isLinkOfActivityPatternId);

        // Get all the netLinkList where isLinkOfActivityPattern equals to isLinkOfActivityPatternId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfActivityPatternId.equals=" + (isLinkOfActivityPatternId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfIntrusionSetIsEqualToSomething() throws Exception {
        // Initialize the database
        IntrusionSet isLinkOfIntrusionSet = IntrusionSetResourceIntTest.createEntity(em);
        em.persist(isLinkOfIntrusionSet);
        em.flush();
        netLink.addIsLinkOfIntrusionSet(isLinkOfIntrusionSet);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfIntrusionSetId = isLinkOfIntrusionSet.getId();

        // Get all the netLinkList where isLinkOfIntrusionSet equals to isLinkOfIntrusionSetId
        defaultNetLinkShouldBeFound("isLinkOfIntrusionSetId.equals=" + isLinkOfIntrusionSetId);

        // Get all the netLinkList where isLinkOfIntrusionSet equals to isLinkOfIntrusionSetId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfIntrusionSetId.equals=" + (isLinkOfIntrusionSetId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfMalwareIsEqualToSomething() throws Exception {
        // Initialize the database
        Malware isLinkOfMalware = MalwareResourceIntTest.createEntity(em);
        em.persist(isLinkOfMalware);
        em.flush();
        netLink.addIsLinkOfMalware(isLinkOfMalware);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfMalwareId = isLinkOfMalware.getId();

        // Get all the netLinkList where isLinkOfMalware equals to isLinkOfMalwareId
        defaultNetLinkShouldBeFound("isLinkOfMalwareId.equals=" + isLinkOfMalwareId);

        // Get all the netLinkList where isLinkOfMalware equals to isLinkOfMalwareId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfMalwareId.equals=" + (isLinkOfMalwareId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfObservedDataIsEqualToSomething() throws Exception {
        // Initialize the database
        ObservedData isLinkOfObservedData = ObservedDataResourceIntTest.createEntity(em);
        em.persist(isLinkOfObservedData);
        em.flush();
        netLink.addIsLinkOfObservedData(isLinkOfObservedData);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfObservedDataId = isLinkOfObservedData.getId();

        // Get all the netLinkList where isLinkOfObservedData equals to isLinkOfObservedDataId
        defaultNetLinkShouldBeFound("isLinkOfObservedDataId.equals=" + isLinkOfObservedDataId);

        // Get all the netLinkList where isLinkOfObservedData equals to isLinkOfObservedDataId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfObservedDataId.equals=" + (isLinkOfObservedDataId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfReportIsEqualToSomething() throws Exception {
        // Initialize the database
        Report isLinkOfReport = ReportResourceIntTest.createEntity(em);
        em.persist(isLinkOfReport);
        em.flush();
        netLink.addIsLinkOfReport(isLinkOfReport);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfReportId = isLinkOfReport.getId();

        // Get all the netLinkList where isLinkOfReport equals to isLinkOfReportId
        defaultNetLinkShouldBeFound("isLinkOfReportId.equals=" + isLinkOfReportId);

        // Get all the netLinkList where isLinkOfReport equals to isLinkOfReportId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfReportId.equals=" + (isLinkOfReportId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfThreatActorIsEqualToSomething() throws Exception {
        // Initialize the database
        ThreatActor isLinkOfThreatActor = ThreatActorResourceIntTest.createEntity(em);
        em.persist(isLinkOfThreatActor);
        em.flush();
        netLink.addIsLinkOfThreatActor(isLinkOfThreatActor);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfThreatActorId = isLinkOfThreatActor.getId();

        // Get all the netLinkList where isLinkOfThreatActor equals to isLinkOfThreatActorId
        defaultNetLinkShouldBeFound("isLinkOfThreatActorId.equals=" + isLinkOfThreatActorId);

        // Get all the netLinkList where isLinkOfThreatActor equals to isLinkOfThreatActorId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfThreatActorId.equals=" + (isLinkOfThreatActorId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfToolIsEqualToSomething() throws Exception {
        // Initialize the database
        Tool isLinkOfTool = ToolResourceIntTest.createEntity(em);
        em.persist(isLinkOfTool);
        em.flush();
        netLink.addIsLinkOfTool(isLinkOfTool);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfToolId = isLinkOfTool.getId();

        // Get all the netLinkList where isLinkOfTool equals to isLinkOfToolId
        defaultNetLinkShouldBeFound("isLinkOfToolId.equals=" + isLinkOfToolId);

        // Get all the netLinkList where isLinkOfTool equals to isLinkOfToolId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfToolId.equals=" + (isLinkOfToolId + 1));
    }


    @Test
    @Transactional
    public void getAllNetLinksByIsLinkOfVulnerabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        Vulnerability isLinkOfVulnerability = VulnerabilityResourceIntTest.createEntity(em);
        em.persist(isLinkOfVulnerability);
        em.flush();
        netLink.addIsLinkOfVulnerability(isLinkOfVulnerability);
        netLinkRepository.saveAndFlush(netLink);
        Long isLinkOfVulnerabilityId = isLinkOfVulnerability.getId();

        // Get all the netLinkList where isLinkOfVulnerability equals to isLinkOfVulnerabilityId
        defaultNetLinkShouldBeFound("isLinkOfVulnerabilityId.equals=" + isLinkOfVulnerabilityId);

        // Get all the netLinkList where isLinkOfVulnerability equals to isLinkOfVulnerabilityId + 1
        defaultNetLinkShouldNotBeFound("isLinkOfVulnerabilityId.equals=" + (isLinkOfVulnerabilityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultNetLinkShouldBeFound(String filter) throws Exception {
        restNetLinkMockMvc.perform(get("/api/net-links?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultNetLinkShouldNotBeFound(String filter) throws Exception {
        restNetLinkMockMvc.perform(get("/api/net-links?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingNetLink() throws Exception {
        // Get the netLink
        restNetLinkMockMvc.perform(get("/api/net-links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNetLink() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        int databaseSizeBeforeUpdate = netLinkRepository.findAll().size();

        // Update the netLink
        NetLink updatedNetLink = netLinkRepository.findById(netLink.getId()).get();
        // Disconnect from session so that the updates on updatedNetLink are not directly saved in db
        em.detach(updatedNetLink);
        updatedNetLink
            .description(UPDATED_DESCRIPTION)
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .level(UPDATED_LEVEL);
        NetLinkDTO netLinkDTO = netLinkMapper.toDto(updatedNetLink);

        restNetLinkMockMvc.perform(put("/api/net-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netLinkDTO)))
            .andExpect(status().isOk());

        // Validate the NetLink in the database
        List<NetLink> netLinkList = netLinkRepository.findAll();
        assertThat(netLinkList).hasSize(databaseSizeBeforeUpdate);
        NetLink testNetLink = netLinkList.get(netLinkList.size() - 1);
        assertThat(testNetLink.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNetLink.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNetLink.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNetLink.getLevel()).isEqualTo(UPDATED_LEVEL);

        // Validate the NetLink in Elasticsearch
        verify(mockNetLinkSearchRepository, times(1)).save(testNetLink);
    }

    @Test
    @Transactional
    public void updateNonExistingNetLink() throws Exception {
        int databaseSizeBeforeUpdate = netLinkRepository.findAll().size();

        // Create the NetLink
        NetLinkDTO netLinkDTO = netLinkMapper.toDto(netLink);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restNetLinkMockMvc.perform(put("/api/net-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(netLinkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NetLink in the database
        List<NetLink> netLinkList = netLinkRepository.findAll();
        assertThat(netLinkList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NetLink in Elasticsearch
        verify(mockNetLinkSearchRepository, times(0)).save(netLink);
    }

    @Test
    @Transactional
    public void deleteNetLink() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);

        int databaseSizeBeforeDelete = netLinkRepository.findAll().size();

        // Get the netLink
        restNetLinkMockMvc.perform(delete("/api/net-links/{id}", netLink.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NetLink> netLinkList = netLinkRepository.findAll();
        assertThat(netLinkList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NetLink in Elasticsearch
        verify(mockNetLinkSearchRepository, times(1)).deleteById(netLink.getId());
    }

    @Test
    @Transactional
    public void searchNetLink() throws Exception {
        // Initialize the database
        netLinkRepository.saveAndFlush(netLink);
        when(mockNetLinkSearchRepository.search(queryStringQuery("id:" + netLink.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(netLink), PageRequest.of(0, 1), 1));
        // Search the netLink
        restNetLinkMockMvc.perform(get("/api/_search/net-links?query=id:" + netLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(netLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetLink.class);
        NetLink netLink1 = new NetLink();
        netLink1.setId(1L);
        NetLink netLink2 = new NetLink();
        netLink2.setId(netLink1.getId());
        assertThat(netLink1).isEqualTo(netLink2);
        netLink2.setId(2L);
        assertThat(netLink1).isNotEqualTo(netLink2);
        netLink1.setId(null);
        assertThat(netLink1).isNotEqualTo(netLink2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetLinkDTO.class);
        NetLinkDTO netLinkDTO1 = new NetLinkDTO();
        netLinkDTO1.setId(1L);
        NetLinkDTO netLinkDTO2 = new NetLinkDTO();
        assertThat(netLinkDTO1).isNotEqualTo(netLinkDTO2);
        netLinkDTO2.setId(netLinkDTO1.getId());
        assertThat(netLinkDTO1).isEqualTo(netLinkDTO2);
        netLinkDTO2.setId(2L);
        assertThat(netLinkDTO1).isNotEqualTo(netLinkDTO2);
        netLinkDTO1.setId(null);
        assertThat(netLinkDTO1).isNotEqualTo(netLinkDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(netLinkMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(netLinkMapper.fromId(null)).isNull();
    }
}
