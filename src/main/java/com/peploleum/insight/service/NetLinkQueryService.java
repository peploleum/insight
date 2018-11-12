package com.peploleum.insight.service;

import java.util.List;

// for static metamodels
import com.peploleum.insight.domain.ActivityPattern_;
import com.peploleum.insight.domain.Actor_;
import com.peploleum.insight.domain.AttackPattern_;
import com.peploleum.insight.domain.Campaign_;
import com.peploleum.insight.domain.CourseOfAction_;
import com.peploleum.insight.domain.IntrusionSet_;
import com.peploleum.insight.domain.Malware_;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.domain.NetLink_;
import com.peploleum.insight.domain.ObservedData_;
import com.peploleum.insight.domain.Report_;
import com.peploleum.insight.domain.ThreatActor_;
import com.peploleum.insight.domain.Tool_;
import com.peploleum.insight.domain.Vulnerability_;// for static metamodels
import com.peploleum.insight.repository.NetLinkRepository;
import com.peploleum.insight.repository.search.NetLinkSearchRepository;
import com.peploleum.insight.service.dto.NetLinkCriteria;
import com.peploleum.insight.service.dto.NetLinkDTO;
import com.peploleum.insight.service.mapper.NetLinkMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for NetLink entities in the database.
 * The main input is a {@link NetLinkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NetLinkDTO} or a {@link Page} of {@link NetLinkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NetLinkQueryService extends QueryService<NetLink> {

    private final Logger log = LoggerFactory.getLogger(NetLinkQueryService.class);

    private final NetLinkRepository netLinkRepository;

    private final NetLinkMapper netLinkMapper;

    private final NetLinkSearchRepository netLinkSearchRepository;

    public NetLinkQueryService(NetLinkRepository netLinkRepository, NetLinkMapper netLinkMapper, NetLinkSearchRepository netLinkSearchRepository) {
        this.netLinkRepository = netLinkRepository;
        this.netLinkMapper = netLinkMapper;
        this.netLinkSearchRepository = netLinkSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NetLinkDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NetLinkDTO> findByCriteria(NetLinkCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NetLink> specification = createSpecification(criteria);
        return netLinkMapper.toDto(netLinkRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NetLinkDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NetLinkDTO> findByCriteria(NetLinkCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NetLink> specification = createSpecification(criteria);
        return netLinkRepository.findAll(specification, page)
            .map(netLinkMapper::toDto);
    }

    /**
     * Function to convert NetLinkCriteria to a {@link Specification}
     */
    private Specification<NetLink> createSpecification(NetLinkCriteria criteria) {
        Specification<NetLink> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), NetLink_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NetLink_.description));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), NetLink_.nom));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), NetLink_.type));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLevel(), NetLink_.level));
            }
            if (criteria.getIsLinkOfAttackPatternId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfAttackPatternId(), NetLink_.isLinkOfAttackPatterns, AttackPattern_.id));
            }
            if (criteria.getIsLinkOfCampaignId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfCampaignId(), NetLink_.isLinkOfCampaigns, Campaign_.id));
            }
            if (criteria.getIsLinkOfCourseOfActionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfCourseOfActionId(), NetLink_.isLinkOfCourseOfActions, CourseOfAction_.id));
            }
            if (criteria.getIsLinkOfActorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfActorId(), NetLink_.isLinkOfActors, Actor_.id));
            }
            if (criteria.getIsLinkOfActivityPatternId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfActivityPatternId(), NetLink_.isLinkOfActivityPatterns, ActivityPattern_.id));
            }
            if (criteria.getIsLinkOfIntrusionSetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfIntrusionSetId(), NetLink_.isLinkOfIntrusionSets, IntrusionSet_.id));
            }
            if (criteria.getIsLinkOfMalwareId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfMalwareId(), NetLink_.isLinkOfMalwares, Malware_.id));
            }
            if (criteria.getIsLinkOfObservedDataId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfObservedDataId(), NetLink_.isLinkOfObservedData, ObservedData_.id));
            }
            if (criteria.getIsLinkOfReportId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfReportId(), NetLink_.isLinkOfReports, Report_.id));
            }
            if (criteria.getIsLinkOfThreatActorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfThreatActorId(), NetLink_.isLinkOfThreatActors, ThreatActor_.id));
            }
            if (criteria.getIsLinkOfToolId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfToolId(), NetLink_.isLinkOfTools, Tool_.id));
            }
            if (criteria.getIsLinkOfVulnerabilityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIsLinkOfVulnerabilityId(), NetLink_.isLinkOfVulnerabilities, Vulnerability_.id));
            }
        }
        return specification;
    }

}
