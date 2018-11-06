package com.peploleum.insight.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.peploleum.insight.domain.Report;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.ReportRepository;
import com.peploleum.insight.repository.search.ReportSearchRepository;
import com.peploleum.insight.service.dto.ReportCriteria;

import com.peploleum.insight.service.dto.ReportDTO;
import com.peploleum.insight.service.mapper.ReportMapper;

/**
 * Service for executing complex queries for Report entities in the database.
 * The main input is a {@link ReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportDTO} or a {@link Page} of {@link ReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportQueryService extends QueryService<Report> {

    private final Logger log = LoggerFactory.getLogger(ReportQueryService.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    private final ReportSearchRepository reportSearchRepository;

    public ReportQueryService(ReportRepository reportRepository, ReportMapper reportMapper, ReportSearchRepository reportSearchRepository) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
        this.reportSearchRepository = reportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReportDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> findByCriteria(ReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportMapper.toDto(reportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportDTO> findByCriteria(ReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification, page)
            .map(reportMapper::toDto);
    }

    /**
     * Function to convert ReportCriteria to a {@link Specification}
     */
    private Specification<Report> createSpecification(ReportCriteria criteria) {
        Specification<Report> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Report_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Report_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Report_.type));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Report_.libelle));
            }
            if (criteria.getObjetsReferences() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjetsReferences(), Report_.objetsReferences));
            }
            if (criteria.getDatePublication() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePublication(), Report_.datePublication));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), Report_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
