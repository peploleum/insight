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

import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.EquipmentRepository;
import com.peploleum.insight.repository.search.EquipmentSearchRepository;
import com.peploleum.insight.service.dto.EquipmentCriteria;

import com.peploleum.insight.service.dto.EquipmentDTO;
import com.peploleum.insight.service.mapper.EquipmentMapper;

/**
 * Service for executing complex queries for Equipment entities in the database.
 * The main input is a {@link EquipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipmentDTO} or a {@link Page} of {@link EquipmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipmentQueryService extends QueryService<Equipment> {

    private final Logger log = LoggerFactory.getLogger(EquipmentQueryService.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    private final EquipmentSearchRepository equipmentSearchRepository;

    public EquipmentQueryService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper, EquipmentSearchRepository equipmentSearchRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.equipmentSearchRepository = equipmentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EquipmentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipmentDTO> findByCriteria(EquipmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentMapper.toDto(equipmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipmentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipmentDTO> findByCriteria(EquipmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Equipment> specification = createSpecification(criteria);
        return equipmentRepository.findAll(specification, page)
            .map(equipmentMapper::toDto);
    }

    /**
     * Function to convert EquipmentCriteria to a {@link Specification}
     */
    private Specification<Equipment> createSpecification(EquipmentCriteria criteria) {
        Specification<Equipment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Equipment_.id));
            }
            if (criteria.getEquipmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEquipmentName(), Equipment_.equipmentName));
            }
            if (criteria.getEquipmentDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEquipmentDescription(), Equipment_.equipmentDescription));
            }
            if (criteria.getEquipmentType() != null) {
                specification = specification.and(buildSpecification(criteria.getEquipmentType(), Equipment_.equipmentType));
            }
            if (criteria.getEquipmentCoordinates() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEquipmentCoordinates(), Equipment_.equipmentCoordinates));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLocationId(), Equipment_.locations, Location_.id));
            }
            if (criteria.getOrganisationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrganisationId(), Equipment_.organisations, Organisation_.id));
            }
            if (criteria.getBiographicsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBiographicsId(), Equipment_.biographics, Biographics_.id));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEventId(), Equipment_.events, Event_.id));
            }
        }
        return specification;
    }

}
