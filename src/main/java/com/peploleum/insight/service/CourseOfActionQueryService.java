package com.peploleum.insight.service;

import java.util.List;

// for static metamodels
import com.peploleum.insight.domain.CourseOfAction;
import com.peploleum.insight.domain.CourseOfAction_;
import com.peploleum.insight.domain.NetLink_;// for static metamodels
import com.peploleum.insight.repository.CourseOfActionRepository;
import com.peploleum.insight.repository.search.CourseOfActionSearchRepository;
import com.peploleum.insight.service.dto.CourseOfActionCriteria;
import com.peploleum.insight.service.dto.CourseOfActionDTO;
import com.peploleum.insight.service.mapper.CourseOfActionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for CourseOfAction entities in the database.
 * The main input is a {@link CourseOfActionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CourseOfActionDTO} or a {@link Page} of {@link CourseOfActionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseOfActionQueryService extends QueryService<CourseOfAction> {

    private final Logger log = LoggerFactory.getLogger(CourseOfActionQueryService.class);

    private final CourseOfActionRepository courseOfActionRepository;

    private final CourseOfActionMapper courseOfActionMapper;

    private final CourseOfActionSearchRepository courseOfActionSearchRepository;

    public CourseOfActionQueryService(CourseOfActionRepository courseOfActionRepository, CourseOfActionMapper courseOfActionMapper, CourseOfActionSearchRepository courseOfActionSearchRepository) {
        this.courseOfActionRepository = courseOfActionRepository;
        this.courseOfActionMapper = courseOfActionMapper;
        this.courseOfActionSearchRepository = courseOfActionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CourseOfActionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CourseOfActionDTO> findByCriteria(CourseOfActionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CourseOfAction> specification = createSpecification(criteria);
        return courseOfActionMapper.toDto(courseOfActionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CourseOfActionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CourseOfActionDTO> findByCriteria(CourseOfActionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CourseOfAction> specification = createSpecification(criteria);
        return courseOfActionRepository.findAll(specification, page)
            .map(courseOfActionMapper::toDto);
    }

    /**
     * Function to convert CourseOfActionCriteria to a {@link Specification}
     */
    private Specification<CourseOfAction> createSpecification(CourseOfActionCriteria criteria) {
        Specification<CourseOfAction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CourseOfAction_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CourseOfAction_.description));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), CourseOfAction_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), CourseOfAction_.type));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), CourseOfAction_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
