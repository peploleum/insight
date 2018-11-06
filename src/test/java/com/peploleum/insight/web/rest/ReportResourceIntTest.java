package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Report;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.ReportRepository;
import com.peploleum.insight.repository.search.ReportSearchRepository;
import com.peploleum.insight.service.ReportService;
import com.peploleum.insight.service.dto.ReportDTO;
import com.peploleum.insight.service.mapper.ReportMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;
import com.peploleum.insight.service.dto.ReportCriteria;
import com.peploleum.insight.service.ReportQueryService;

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
 * Test class for the ReportResource REST controller.
 *
 * @see ReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class ReportResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJETS_REFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_OBJETS_REFERENCES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_PUBLICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_PUBLICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ReportRepository reportRepository;


    @Autowired
    private ReportMapper reportMapper;
    

    @Autowired
    private ReportService reportService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.ReportSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportSearchRepository mockReportSearchRepository;

    @Autowired
    private ReportQueryService reportQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportMockMvc;

    private Report report;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportResource reportResource = new ReportResource(reportService, reportQueryService);
        this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource)
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
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .libelle(DEFAULT_LIBELLE)
            .objetsReferences(DEFAULT_OBJETS_REFERENCES)
            .datePublication(DEFAULT_DATE_PUBLICATION);
        return report;
    }

    @Before
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testReport.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testReport.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testReport.getObjetsReferences()).isEqualTo(DEFAULT_OBJETS_REFERENCES);
        assertThat(testReport.getDatePublication()).isEqualTo(DEFAULT_DATE_PUBLICATION);

        // Validate the Report in Elasticsearch
        verify(mockReportSearchRepository, times(1)).save(testReport);
    }

    @Test
    @Transactional
    public void createReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);

        // Validate the Report in Elasticsearch
        verify(mockReportSearchRepository, times(0)).save(report);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc.perform(get("/api/reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].objetsReferences").value(hasItem(DEFAULT_OBJETS_REFERENCES.toString())))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(sameInstant(DEFAULT_DATE_PUBLICATION))));
    }
    

    @Test
    @Transactional
    public void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.objetsReferences").value(DEFAULT_OBJETS_REFERENCES.toString()))
            .andExpect(jsonPath("$.datePublication").value(sameInstant(DEFAULT_DATE_PUBLICATION)));
    }

    @Test
    @Transactional
    public void getAllReportsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where nom equals to DEFAULT_NOM
        defaultReportShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the reportList where nom equals to UPDATED_NOM
        defaultReportShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllReportsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultReportShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the reportList where nom equals to UPDATED_NOM
        defaultReportShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllReportsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where nom is not null
        defaultReportShouldBeFound("nom.specified=true");

        // Get all the reportList where nom is null
        defaultReportShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where type equals to DEFAULT_TYPE
        defaultReportShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the reportList where type equals to UPDATED_TYPE
        defaultReportShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllReportsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultReportShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the reportList where type equals to UPDATED_TYPE
        defaultReportShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllReportsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where type is not null
        defaultReportShouldBeFound("type.specified=true");

        // Get all the reportList where type is null
        defaultReportShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where libelle equals to DEFAULT_LIBELLE
        defaultReportShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the reportList where libelle equals to UPDATED_LIBELLE
        defaultReportShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllReportsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultReportShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the reportList where libelle equals to UPDATED_LIBELLE
        defaultReportShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllReportsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where libelle is not null
        defaultReportShouldBeFound("libelle.specified=true");

        // Get all the reportList where libelle is null
        defaultReportShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByObjetsReferencesIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where objetsReferences equals to DEFAULT_OBJETS_REFERENCES
        defaultReportShouldBeFound("objetsReferences.equals=" + DEFAULT_OBJETS_REFERENCES);

        // Get all the reportList where objetsReferences equals to UPDATED_OBJETS_REFERENCES
        defaultReportShouldNotBeFound("objetsReferences.equals=" + UPDATED_OBJETS_REFERENCES);
    }

    @Test
    @Transactional
    public void getAllReportsByObjetsReferencesIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where objetsReferences in DEFAULT_OBJETS_REFERENCES or UPDATED_OBJETS_REFERENCES
        defaultReportShouldBeFound("objetsReferences.in=" + DEFAULT_OBJETS_REFERENCES + "," + UPDATED_OBJETS_REFERENCES);

        // Get all the reportList where objetsReferences equals to UPDATED_OBJETS_REFERENCES
        defaultReportShouldNotBeFound("objetsReferences.in=" + UPDATED_OBJETS_REFERENCES);
    }

    @Test
    @Transactional
    public void getAllReportsByObjetsReferencesIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where objetsReferences is not null
        defaultReportShouldBeFound("objetsReferences.specified=true");

        // Get all the reportList where objetsReferences is null
        defaultReportShouldNotBeFound("objetsReferences.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByDatePublicationIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where datePublication equals to DEFAULT_DATE_PUBLICATION
        defaultReportShouldBeFound("datePublication.equals=" + DEFAULT_DATE_PUBLICATION);

        // Get all the reportList where datePublication equals to UPDATED_DATE_PUBLICATION
        defaultReportShouldNotBeFound("datePublication.equals=" + UPDATED_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void getAllReportsByDatePublicationIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where datePublication in DEFAULT_DATE_PUBLICATION or UPDATED_DATE_PUBLICATION
        defaultReportShouldBeFound("datePublication.in=" + DEFAULT_DATE_PUBLICATION + "," + UPDATED_DATE_PUBLICATION);

        // Get all the reportList where datePublication equals to UPDATED_DATE_PUBLICATION
        defaultReportShouldNotBeFound("datePublication.in=" + UPDATED_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void getAllReportsByDatePublicationIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where datePublication is not null
        defaultReportShouldBeFound("datePublication.specified=true");

        // Get all the reportList where datePublication is null
        defaultReportShouldNotBeFound("datePublication.specified=false");
    }

    @Test
    @Transactional
    public void getAllReportsByDatePublicationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where datePublication greater than or equals to DEFAULT_DATE_PUBLICATION
        defaultReportShouldBeFound("datePublication.greaterOrEqualThan=" + DEFAULT_DATE_PUBLICATION);

        // Get all the reportList where datePublication greater than or equals to UPDATED_DATE_PUBLICATION
        defaultReportShouldNotBeFound("datePublication.greaterOrEqualThan=" + UPDATED_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void getAllReportsByDatePublicationIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where datePublication less than or equals to DEFAULT_DATE_PUBLICATION
        defaultReportShouldNotBeFound("datePublication.lessThan=" + DEFAULT_DATE_PUBLICATION);

        // Get all the reportList where datePublication less than or equals to UPDATED_DATE_PUBLICATION
        defaultReportShouldBeFound("datePublication.lessThan=" + UPDATED_DATE_PUBLICATION);
    }


    @Test
    @Transactional
    public void getAllReportsByLinkOfIsEqualToSomething() throws Exception {
        // Initialize the database
        NetLink linkOf = NetLinkResourceIntTest.createEntity(em);
        em.persist(linkOf);
        em.flush();
        report.setLinkOf(linkOf);
        reportRepository.saveAndFlush(report);
        Long linkOfId = linkOf.getId();

        // Get all the reportList where linkOf equals to linkOfId
        defaultReportShouldBeFound("linkOfId.equals=" + linkOfId);

        // Get all the reportList where linkOf equals to linkOfId + 1
        defaultReportShouldNotBeFound("linkOfId.equals=" + (linkOfId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc.perform(get("/api/reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].objetsReferences").value(hasItem(DEFAULT_OBJETS_REFERENCES.toString())))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(sameInstant(DEFAULT_DATE_PUBLICATION))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc.perform(get("/api/reports?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .libelle(UPDATED_LIBELLE)
            .objetsReferences(UPDATED_OBJETS_REFERENCES)
            .datePublication(UPDATED_DATE_PUBLICATION);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testReport.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testReport.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testReport.getObjetsReferences()).isEqualTo(UPDATED_OBJETS_REFERENCES);
        assertThat(testReport.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);

        // Validate the Report in Elasticsearch
        verify(mockReportSearchRepository, times(1)).save(testReport);
    }

    @Test
    @Transactional
    public void updateNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restReportMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Report in Elasticsearch
        verify(mockReportSearchRepository, times(0)).save(report);
    }

    @Test
    @Transactional
    public void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Get the report
        restReportMockMvc.perform(delete("/api/reports/{id}", report.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Report in Elasticsearch
        verify(mockReportSearchRepository, times(1)).deleteById(report.getId());
    }

    @Test
    @Transactional
    public void searchReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        when(mockReportSearchRepository.search(queryStringQuery("id:" + report.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(report), PageRequest.of(0, 1), 1));
        // Search the report
        restReportMockMvc.perform(get("/api/_search/reports?query=id:" + report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].objetsReferences").value(hasItem(DEFAULT_OBJETS_REFERENCES.toString())))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(sameInstant(DEFAULT_DATE_PUBLICATION))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = new Report();
        report1.setId(1L);
        Report report2 = new Report();
        report2.setId(report1.getId());
        assertThat(report1).isEqualTo(report2);
        report2.setId(2L);
        assertThat(report1).isNotEqualTo(report2);
        report1.setId(null);
        assertThat(report1).isNotEqualTo(report2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDTO.class);
        ReportDTO reportDTO1 = new ReportDTO();
        reportDTO1.setId(1L);
        ReportDTO reportDTO2 = new ReportDTO();
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO2.setId(reportDTO1.getId());
        assertThat(reportDTO1).isEqualTo(reportDTO2);
        reportDTO2.setId(2L);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
        reportDTO1.setId(null);
        assertThat(reportDTO1).isNotEqualTo(reportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reportMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reportMapper.fromId(null)).isNull();
    }
}
