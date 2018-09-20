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

import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.BiographicsRepository;
import com.peploleum.insight.repository.search.BiographicsSearchRepository;
import com.peploleum.insight.service.dto.BiographicsCriteria;

import com.peploleum.insight.service.dto.BiographicsDTO;
import com.peploleum.insight.service.mapper.BiographicsMapper;

/**
 * Service for executing complex queries for Biographics entities in the database.
 * The main input is a {@link BiographicsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BiographicsDTO} or a {@link Page} of {@link BiographicsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BiographicsQueryService extends QueryService<Biographics> {

    private final Logger log = LoggerFactory.getLogger(BiographicsQueryService.class);

    private final BiographicsRepository biographicsRepository;

    private final BiographicsMapper biographicsMapper;

    private final BiographicsSearchRepository biographicsSearchRepository;

    public BiographicsQueryService(BiographicsRepository biographicsRepository, BiographicsMapper biographicsMapper, BiographicsSearchRepository biographicsSearchRepository) {
        this.biographicsRepository = biographicsRepository;
        this.biographicsMapper = biographicsMapper;
        this.biographicsSearchRepository = biographicsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BiographicsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BiographicsDTO> findByCriteria(BiographicsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Biographics> specification = createSpecification(criteria);
        return biographicsMapper.toDto(biographicsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BiographicsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BiographicsDTO> findByCriteria(BiographicsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Biographics> specification = createSpecification(criteria);
        return biographicsRepository.findAll(specification, page)
            .map(biographicsMapper::toDto);
    }

    /**
     * Function to convert BiographicsCriteria to a {@link Specification}
     */
    private Specification<Biographics> createSpecification(BiographicsCriteria criteria) {
        Specification<Biographics> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Biographics_.id));
            }
            if (criteria.getBiographicsFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBiographicsFirstname(), Biographics_.biographicsFirstname));
            }
            if (criteria.getBiographicsName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBiographicsName(), Biographics_.biographicsName));
            }
            if (criteria.getBiographicsAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBiographicsAge(), Biographics_.biographicsAge));
            }
            if (criteria.getBiographicsGender() != null) {
                specification = specification.and(buildSpecification(criteria.getBiographicsGender(), Biographics_.biographicsGender));
            }
            if (criteria.getBiographicsCoordinates() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBiographicsCoordinates(), Biographics_.biographicsCoordinates));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEventId(), Biographics_.events, Event_.id));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEquipmentId(), Biographics_.equipment, Equipment_.id));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLocationId(), Biographics_.locations, Location_.id));
            }
            if (criteria.getOrganisationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrganisationId(), Biographics_.organisations, Organisation_.id));
            }
        }
        return specification;
    }

}
