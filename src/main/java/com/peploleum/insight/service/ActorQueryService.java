package com.peploleum.insight.service;

import java.util.List;

// for static metamodels
import com.peploleum.insight.domain.Actor;
import com.peploleum.insight.domain.Actor_;
import com.peploleum.insight.domain.IntrusionSet_;
import com.peploleum.insight.domain.Malware_;
import com.peploleum.insight.domain.NetLink_;// for static metamodels
import com.peploleum.insight.repository.ActorRepository;
import com.peploleum.insight.repository.search.ActorSearchRepository;
import com.peploleum.insight.service.dto.ActorCriteria;
import com.peploleum.insight.service.dto.ActorDTO;
import com.peploleum.insight.service.mapper.ActorMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for Actor entities in the database.
 * The main input is a {@link ActorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActorDTO} or a {@link Page} of {@link ActorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActorQueryService extends QueryService<Actor> {

    private final Logger log = LoggerFactory.getLogger(ActorQueryService.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    private final ActorSearchRepository actorSearchRepository;

    public ActorQueryService(ActorRepository actorRepository, ActorMapper actorMapper, ActorSearchRepository actorSearchRepository) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.actorSearchRepository = actorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ActorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActorDTO> findByCriteria(ActorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorMapper.toDto(actorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActorDTO> findByCriteria(ActorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.findAll(specification, page)
            .map(actorMapper::toDto);
    }

    /**
     * Function to convert ActorCriteria to a {@link Specification}
     */
    private Specification<Actor> createSpecification(ActorCriteria criteria) {
        Specification<Actor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Actor_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Actor_.description));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Actor_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Actor_.type));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Actor_.libelle));
            }
            if (criteria.getClasseIdentite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasseIdentite(), Actor_.classeIdentite));
            }
            if (criteria.getTargetsActorToIntrusionSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTargetsActorToIntrusionSetId(), Actor_.targetsActorToIntrusionSets, IntrusionSet_.id));
            }
            if (criteria.getTargetsActorToMalwareId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTargetsActorToMalwareId(), Actor_.targetsActorToMalwares, Malware_.id));
            }
            if (criteria.getLinkOfId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkOfId(), Actor_.linkOf, NetLink_.id));
            }
        }
        return specification;
    }

}
