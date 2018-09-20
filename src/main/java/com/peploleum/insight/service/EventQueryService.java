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

import com.peploleum.insight.domain.Event;
import com.peploleum.insight.domain.*; // for static metamodels
import com.peploleum.insight.repository.EventRepository;
import com.peploleum.insight.repository.search.EventSearchRepository;
import com.peploleum.insight.service.dto.EventCriteria;

import com.peploleum.insight.service.dto.EventDTO;
import com.peploleum.insight.service.mapper.EventMapper;

/**
 * Service for executing complex queries for Event entities in the database.
 * The main input is a {@link EventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventDTO} or a {@link Page} of {@link EventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventQueryService extends QueryService<Event> {

    private final Logger log = LoggerFactory.getLogger(EventQueryService.class);

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventSearchRepository eventSearchRepository;

    public EventQueryService(EventRepository eventRepository, EventMapper eventMapper, EventSearchRepository eventSearchRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventSearchRepository = eventSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EventDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventDTO> findByCriteria(EventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Event> specification = createSpecification(criteria);
        return eventMapper.toDto(eventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findByCriteria(EventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Event> specification = createSpecification(criteria);
        return eventRepository.findAll(specification, page)
            .map(eventMapper::toDto);
    }

    /**
     * Function to convert EventCriteria to a {@link Specification}
     */
    private Specification<Event> createSpecification(EventCriteria criteria) {
        Specification<Event> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Event_.id));
            }
            if (criteria.getEventName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventName(), Event_.eventName));
            }
            if (criteria.getEventDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventDescription(), Event_.eventDescription));
            }
            if (criteria.getEventType() != null) {
                specification = specification.and(buildSpecification(criteria.getEventType(), Event_.eventType));
            }
            if (criteria.getEventCoordinates() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventCoordinates(), Event_.eventCoordinates));
            }
            if (criteria.getEquipmentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEquipmentId(), Event_.equipment, Equipment_.id));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLocationId(), Event_.locations, Location_.id));
            }
            if (criteria.getOrganisationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrganisationId(), Event_.organisations, Organisation_.id));
            }
            if (criteria.getBiographicsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBiographicsId(), Event_.biographics, Biographics_.id));
            }
        }
        return specification;
    }

}
