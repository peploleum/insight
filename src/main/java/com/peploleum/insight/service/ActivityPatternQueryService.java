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

import com.peploleum.insight.domain.ActivityPattern;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.ActivityPatternRepository;
import com.peploleum.insight.repository.search.ActivityPatternSearchRepository;
import com.peploleum.insight.service.dto.ActivityPatternCriteria;

import com.peploleum.insight.service.dto.ActivityPatternDTO;
import com.peploleum.insight.service.mapper.ActivityPatternMapper;

/**
 * Service for executing complex queries for ActivityPattern entities in the database.
 * The main input is a {@link ActivityPatternCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActivityPatternDTO} or a {@link Page} of {@link ActivityPatternDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActivityPatternQueryService extends QueryService<ActivityPattern> {

    private final Logger log = LoggerFactory.getLogger(ActivityPatternQueryService.class);

    private final ActivityPatternRepository activityPatternRepository;

    private final ActivityPatternMapper activityPatternMapper;

    private final ActivityPatternSearchRepository activityPatternSearchRepository;

    public ActivityPatternQueryService(ActivityPatternRepository activityPatternRepository, ActivityPatternMapper activityPatternMapper, ActivityPatternSearchRepository activityPatternSearchRepository) {
        this.activityPatternRepository = activityPatternRepository;
        this.activityPatternMapper = activityPatternMapper;
        this.activityPatternSearchRepository = activityPatternSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ActivityPatternDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActivityPatternDTO> findByCriteria(ActivityPatternCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ActivityPattern> specification = createSpecification(criteria);
        return activityPatternMapper.toDto(activityPatternRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActivityPatternDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActivityPatternDTO> findByCriteria(ActivityPatternCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ActivityPattern> specification = createSpecification(criteria);
        return activityPatternRepository.findAll(specification, page)
            .map(activityPatternMapper::toDto);
    }

    /**
     * Function to convert ActivityPatternCriteria to a {@link Specification}
     */
    private Specification<ActivityPattern> createSpecification(ActivityPatternCriteria criteria) {
        Specification<ActivityPattern> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ActivityPattern_.id));
            }
            if (criteria.getModele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModele(), ActivityPattern_.modele));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), ActivityPattern_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ActivityPattern_.type));
            }
            if (criteria.getValideAPartirDe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValideAPartirDe(), ActivityPattern_.valideAPartirDe));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), ActivityPattern_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
