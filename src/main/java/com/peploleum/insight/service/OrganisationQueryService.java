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

import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.OrganisationRepository;
import com.peploleum.insight.repository.search.OrganisationSearchRepository;
import com.peploleum.insight.service.dto.OrganisationCriteria;

import com.peploleum.insight.service.dto.OrganisationDTO;
import com.peploleum.insight.service.mapper.OrganisationMapper;

/**
 * Service for executing complex queries for Organisation entities in the database.
 * The main input is a {@link OrganisationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganisationDTO} or a {@link Page} of {@link OrganisationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganisationQueryService extends QueryService<Organisation> {

    private final Logger log = LoggerFactory.getLogger(OrganisationQueryService.class);

    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    private final OrganisationSearchRepository organisationSearchRepository;

    public OrganisationQueryService(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper, OrganisationSearchRepository organisationSearchRepository) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
        this.organisationSearchRepository = organisationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrganisationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganisationDTO> findByCriteria(OrganisationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Organisation> specification = createSpecification(criteria);
        return organisationMapper.toDto(organisationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrganisationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganisationDTO> findByCriteria(OrganisationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Organisation> specification = createSpecification(criteria);
        return organisationRepository.findAll(specification, page)
            .map(organisationMapper::toDto);
    }

    /**
     * Function to convert OrganisationCriteria to a {@link Specification}
     */
    private Specification<Organisation> createSpecification(OrganisationCriteria criteria) {
        Specification<Organisation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Organisation_.id));
            }
            if (criteria.getOrganisationName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganisationName(), Organisation_.organisationName));
            }
            if (criteria.getOrganisationSize() != null) {
                specification = specification.and(buildSpecification(criteria.getOrganisationSize(), Organisation_.organisationSize));
            }
            if (criteria.getOrganisationCoordinates() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganisationCoordinates(), Organisation_.organisationCoordinates));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLocationId(), Organisation_.locations, Location_.id));
            }
            if (criteria.getBiographicsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBiographicsId(), Organisation_.biographics, Biographics_.id));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEventId(), Organisation_.events, Event_.id));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEquipmentId(), Organisation_.equipment, Equipment_.id));
            }
        }
        return specification;
    }

}
