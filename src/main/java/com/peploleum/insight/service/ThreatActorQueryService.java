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

import com.peploleum.insight.domain.ThreatActor;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.ThreatActorRepository;
import com.peploleum.insight.repository.search.ThreatActorSearchRepository;
import com.peploleum.insight.service.dto.ThreatActorCriteria;

import com.peploleum.insight.service.dto.ThreatActorDTO;
import com.peploleum.insight.service.mapper.ThreatActorMapper;

/**
 * Service for executing complex queries for ThreatActor entities in the database.
 * The main input is a {@link ThreatActorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ThreatActorDTO} or a {@link Page} of {@link ThreatActorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ThreatActorQueryService extends QueryService<ThreatActor> {

    private final Logger log = LoggerFactory.getLogger(ThreatActorQueryService.class);

    private final ThreatActorRepository threatActorRepository;

    private final ThreatActorMapper threatActorMapper;

    private final ThreatActorSearchRepository threatActorSearchRepository;

    public ThreatActorQueryService(ThreatActorRepository threatActorRepository, ThreatActorMapper threatActorMapper, ThreatActorSearchRepository threatActorSearchRepository) {
        this.threatActorRepository = threatActorRepository;
        this.threatActorMapper = threatActorMapper;
        this.threatActorSearchRepository = threatActorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ThreatActorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ThreatActorDTO> findByCriteria(ThreatActorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ThreatActor> specification = createSpecification(criteria);
        return threatActorMapper.toDto(threatActorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ThreatActorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ThreatActorDTO> findByCriteria(ThreatActorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ThreatActor> specification = createSpecification(criteria);
        return threatActorRepository.findAll(specification, page)
            .map(threatActorMapper::toDto);
    }

    /**
     * Function to convert ThreatActorCriteria to a {@link Specification}
     */
    private Specification<ThreatActor> createSpecification(ThreatActorCriteria criteria) {
        Specification<ThreatActor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ThreatActor_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), ThreatActor_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ThreatActor_.type));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), ThreatActor_.libelle));
            }
            if (criteria.getSpecification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecification(), ThreatActor_.specification));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), ThreatActor_.role));
            }
            if (criteria.getIsTargetsThreatActorToVulnerabilityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsTargetsThreatActorToVulnerabilityId(), ThreatActor_.isTargetsThreatActorToVulnerabilities, Vulnerability_.id));
            }
            if (criteria.getIsUsesThreatActorToToolId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsUsesThreatActorToToolId(), ThreatActor_.isUsesThreatActorToTools, Tool_.id));
            }
            if (criteria.getIsUsesThreatActorToMalwareId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsUsesThreatActorToMalwareId(), ThreatActor_.isUsesThreatActorToMalware, Malware_.id));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), ThreatActor_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
