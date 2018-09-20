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

import com.peploleum.insight.domain.Location;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.LocationRepository;
import com.peploleum.insight.repository.search.LocationSearchRepository;
import com.peploleum.insight.service.dto.LocationCriteria;

import com.peploleum.insight.service.dto.LocationDTO;
import com.peploleum.insight.service.mapper.LocationMapper;

/**
 * Service for executing complex queries for Location entities in the database.
 * The main input is a {@link LocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocationDTO} or a {@link Page} of {@link LocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationQueryService extends QueryService<Location> {

    private final Logger log = LoggerFactory.getLogger(LocationQueryService.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final LocationSearchRepository locationSearchRepository;

    public LocationQueryService(LocationRepository locationRepository, LocationMapper locationMapper, LocationSearchRepository locationSearchRepository) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.locationSearchRepository = locationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LocationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findByCriteria(LocationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Location> specification = createSpecification(criteria);
        return locationMapper.toDto(locationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LocationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> findByCriteria(LocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Location> specification = createSpecification(criteria);
        return locationRepository.findAll(specification, page)
            .map(locationMapper::toDto);
    }

    /**
     * Function to convert LocationCriteria to a {@link Specification}
     */
    private Specification<Location> createSpecification(LocationCriteria criteria) {
        Specification<Location> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Location_.id));
            }
            if (criteria.getLocationName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationName(), Location_.locationName));
            }
            if (criteria.getLocationType() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationType(), Location_.locationType));
            }
            if (criteria.getLocationCoordinates() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationCoordinates(), Location_.locationCoordinates));
            }
            if (criteria.getBiographicsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBiographicsId(), Location_.biographics, Biographics_.id));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEventId(), Location_.events, Event_.id));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEquipmentId(), Location_.equipment, Equipment_.id));
            }
            if (criteria.getOrganisationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrganisationId(), Location_.organisations, Organisation_.id));
            }
        }
        return specification;
    }

}
