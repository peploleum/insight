package com.peploleum.insight.service;

import java.util.List;

// for static metamodels
import com.peploleum.insight.domain.Actor_;
import com.peploleum.insight.domain.IntrusionSet;
import com.peploleum.insight.domain.IntrusionSet_;
import com.peploleum.insight.domain.NetLink_;
import com.peploleum.insight.domain.Tool_;// for static metamodels
import com.peploleum.insight.repository.IntrusionSetRepository;
import com.peploleum.insight.repository.search.IntrusionSetSearchRepository;
import com.peploleum.insight.service.dto.IntrusionSetCriteria;
import com.peploleum.insight.service.dto.IntrusionSetDTO;
import com.peploleum.insight.service.mapper.IntrusionSetMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for IntrusionSet entities in the database.
 * The main input is a {@link IntrusionSetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IntrusionSetDTO} or a {@link Page} of {@link IntrusionSetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IntrusionSetQueryService extends QueryService<IntrusionSet> {

    private final Logger log = LoggerFactory.getLogger(IntrusionSetQueryService.class);

    private final IntrusionSetRepository intrusionSetRepository;

    private final IntrusionSetMapper intrusionSetMapper;

    private final IntrusionSetSearchRepository intrusionSetSearchRepository;

    public IntrusionSetQueryService(IntrusionSetRepository intrusionSetRepository, IntrusionSetMapper intrusionSetMapper, IntrusionSetSearchRepository intrusionSetSearchRepository) {
        this.intrusionSetRepository = intrusionSetRepository;
        this.intrusionSetMapper = intrusionSetMapper;
        this.intrusionSetSearchRepository = intrusionSetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IntrusionSetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IntrusionSetDTO> findByCriteria(IntrusionSetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IntrusionSet> specification = createSpecification(criteria);
        return intrusionSetMapper.toDto(intrusionSetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IntrusionSetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IntrusionSetDTO> findByCriteria(IntrusionSetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IntrusionSet> specification = createSpecification(criteria);
        return intrusionSetRepository.findAll(specification, page)
            .map(intrusionSetMapper::toDto);
    }

    /**
     * Function to convert IntrusionSetCriteria to a {@link Specification}
     */
    private Specification<IntrusionSet> createSpecification(IntrusionSetCriteria criteria) {
        Specification<IntrusionSet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), IntrusionSet_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), IntrusionSet_.description));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), IntrusionSet_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), IntrusionSet_.type));
            }
            if (criteria.getObjectif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjectif(), IntrusionSet_.objectif));
            }
            if (criteria.getNiveauRessource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNiveauRessource(), IntrusionSet_.niveauRessource));
            }
            if (criteria.getIsUsesIntrusionSetToToolId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsUsesIntrusionSetToToolId(), IntrusionSet_.isUsesIntrusionSetToTools, Tool_.id));
            }
            if (criteria.getIsTargetsIntrusionSetToActorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsTargetsIntrusionSetToActorId(), IntrusionSet_.isTargetsIntrusionSetToActor, Actor_.id));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), IntrusionSet_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
