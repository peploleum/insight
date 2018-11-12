package com.peploleum.insight.service;

import java.util.List;

// for static metamodels
import com.peploleum.insight.domain.IntrusionSet_;
import com.peploleum.insight.domain.Malware_;
import com.peploleum.insight.domain.NetLink_;
import com.peploleum.insight.domain.ThreatActor_;
import com.peploleum.insight.domain.Tool;
import com.peploleum.insight.domain.Tool_;
import com.peploleum.insight.domain.Vulnerability_;// for static metamodels
import com.peploleum.insight.repository.ToolRepository;
import com.peploleum.insight.repository.search.ToolSearchRepository;
import com.peploleum.insight.service.dto.ToolCriteria;
import com.peploleum.insight.service.dto.ToolDTO;
import com.peploleum.insight.service.mapper.ToolMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for Tool entities in the database.
 * The main input is a {@link ToolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ToolDTO} or a {@link Page} of {@link ToolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ToolQueryService extends QueryService<Tool> {

    private final Logger log = LoggerFactory.getLogger(ToolQueryService.class);

    private final ToolRepository toolRepository;

    private final ToolMapper toolMapper;

    private final ToolSearchRepository toolSearchRepository;

    public ToolQueryService(ToolRepository toolRepository, ToolMapper toolMapper, ToolSearchRepository toolSearchRepository) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
        this.toolSearchRepository = toolSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ToolDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ToolDTO> findByCriteria(ToolCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tool> specification = createSpecification(criteria);
        return toolMapper.toDto(toolRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ToolDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ToolDTO> findByCriteria(ToolCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tool> specification = createSpecification(criteria);
        return toolRepository.findAll(specification, page)
            .map(toolMapper::toDto);
    }

    /**
     * Function to convert ToolCriteria to a {@link Specification}
     */
    private Specification<Tool> createSpecification(ToolCriteria criteria) {
        Specification<Tool> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Tool_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Tool_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Tool_.type));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Tool_.libelle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Tool_.description));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), Tool_.version));
            }
            if (criteria.getIsTargetsToolToVulnerabilityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsTargetsToolToVulnerabilityId(), Tool_.isTargetsToolToVulnerabilities, Vulnerability_.id));
            }
            if (criteria.getUsesToolToIntrusionSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsesToolToIntrusionSetId(), Tool_.usesToolToIntrusionSet, IntrusionSet_.id));
            }
            if (criteria.getIsUsesToolToMalwareId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsUsesToolToMalwareId(), Tool_.isUsesToolToMalware, Malware_.id));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), Tool_.linkOf, NetLink_.id));
            }
            if (criteria.getUsesToolToThreatActorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsesToolToThreatActorId(), Tool_.usesToolToThreatActor, ThreatActor_.id));
            }
        }
        return specification;
    }

}
