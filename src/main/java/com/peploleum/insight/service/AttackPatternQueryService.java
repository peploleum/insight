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

import com.peploleum.insight.domain.AttackPattern;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.AttackPatternRepository;
import com.peploleum.insight.repository.search.AttackPatternSearchRepository;
import com.peploleum.insight.service.dto.AttackPatternCriteria;

import com.peploleum.insight.service.dto.AttackPatternDTO;
import com.peploleum.insight.service.mapper.AttackPatternMapper;

/**
 * Service for executing complex queries for AttackPattern entities in the database.
 * The main input is a {@link AttackPatternCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AttackPatternDTO} or a {@link Page} of {@link AttackPatternDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttackPatternQueryService extends QueryService<AttackPattern> {

    private final Logger log = LoggerFactory.getLogger(AttackPatternQueryService.class);

    private final AttackPatternRepository attackPatternRepository;

    private final AttackPatternMapper attackPatternMapper;

    private final AttackPatternSearchRepository attackPatternSearchRepository;

    public AttackPatternQueryService(AttackPatternRepository attackPatternRepository, AttackPatternMapper attackPatternMapper, AttackPatternSearchRepository attackPatternSearchRepository) {
        this.attackPatternRepository = attackPatternRepository;
        this.attackPatternMapper = attackPatternMapper;
        this.attackPatternSearchRepository = attackPatternSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AttackPatternDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttackPatternDTO> findByCriteria(AttackPatternCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AttackPattern> specification = createSpecification(criteria);
        return attackPatternMapper.toDto(attackPatternRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AttackPatternDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttackPatternDTO> findByCriteria(AttackPatternCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AttackPattern> specification = createSpecification(criteria);
        return attackPatternRepository.findAll(specification, page)
            .map(attackPatternMapper::toDto);
    }

    /**
     * Function to convert AttackPatternCriteria to a {@link Specification}
     */
    private Specification<AttackPattern> createSpecification(AttackPatternCriteria criteria) {
        Specification<AttackPattern> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AttackPattern_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AttackPattern_.description));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), AttackPattern_.nom));
            }
            if (criteria.getReferenceExterne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferenceExterne(), AttackPattern_.referenceExterne));
            }
            if (criteria.getTueurProcessus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTueurProcessus(), AttackPattern_.tueurProcessus));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), AttackPattern_.type));
            }
            if (criteria.getUsesAttackPatternToMalwareId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsesAttackPatternToMalwareId(), AttackPattern_.usesAttackPatternToMalwares, Malware_.id));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), AttackPattern_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
