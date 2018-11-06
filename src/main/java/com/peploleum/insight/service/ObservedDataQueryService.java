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

import com.peploleum.insight.domain.ObservedData;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.ObservedDataRepository;
import com.peploleum.insight.repository.search.ObservedDataSearchRepository;
import com.peploleum.insight.service.dto.ObservedDataCriteria;

import com.peploleum.insight.service.dto.ObservedDataDTO;
import com.peploleum.insight.service.mapper.ObservedDataMapper;

/**
 * Service for executing complex queries for ObservedData entities in the database.
 * The main input is a {@link ObservedDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ObservedDataDTO} or a {@link Page} of {@link ObservedDataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ObservedDataQueryService extends QueryService<ObservedData> {

    private final Logger log = LoggerFactory.getLogger(ObservedDataQueryService.class);

    private final ObservedDataRepository observedDataRepository;

    private final ObservedDataMapper observedDataMapper;

    private final ObservedDataSearchRepository observedDataSearchRepository;

    public ObservedDataQueryService(ObservedDataRepository observedDataRepository, ObservedDataMapper observedDataMapper, ObservedDataSearchRepository observedDataSearchRepository) {
        this.observedDataRepository = observedDataRepository;
        this.observedDataMapper = observedDataMapper;
        this.observedDataSearchRepository = observedDataSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ObservedDataDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ObservedDataDTO> findByCriteria(ObservedDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ObservedData> specification = createSpecification(criteria);
        return observedDataMapper.toDto(observedDataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ObservedDataDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ObservedDataDTO> findByCriteria(ObservedDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ObservedData> specification = createSpecification(criteria);
        return observedDataRepository.findAll(specification, page)
            .map(observedDataMapper::toDto);
    }

    /**
     * Function to convert ObservedDataCriteria to a {@link Specification}
     */
    private Specification<ObservedData> createSpecification(ObservedDataCriteria criteria) {
        Specification<ObservedData> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ObservedData_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ObservedData_.type));
            }
            if (criteria.getObjetsObserves() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjetsObserves(), ObservedData_.objetsObserves));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), ObservedData_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), ObservedData_.dateFin));
            }
            if (criteria.getNombreJours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNombreJours(), ObservedData_.nombreJours));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), ObservedData_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
