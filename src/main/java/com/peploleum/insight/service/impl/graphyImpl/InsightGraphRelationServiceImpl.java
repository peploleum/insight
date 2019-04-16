package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.graphy.InsightGraphEntity;
import com.peploleum.insight.domain.graphy.InsightGraphRelation;
import com.peploleum.insight.repository.graphy.InsightGraphEntityRepository;
import com.peploleum.insight.repository.graphy.InsightGraphRelationRepository;
import com.peploleum.insight.service.InsightGraphRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@Service
@Profile("graphy")
public class InsightGraphRelationServiceImpl implements InsightGraphRelationService {
    private final Logger log = LoggerFactory.getLogger(InsightGraphRelationServiceImpl.class);

    private final InsightGraphRelationRepository insightGraphRelationRepository;
    private final InsightGraphEntityRepository insightGraphEntityRepository;

    public InsightGraphRelationServiceImpl(InsightGraphRelationRepository insightGraphRelationRepository, InsightGraphEntityRepository insightGraphEntityRepository) {
        this.insightGraphRelationRepository = insightGraphRelationRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
    }

    @Override
    public String save(Long idSource, Long idCible, String name) {
        log.debug("Request to save InsightGraphRelation : {}");
        InsightGraphRelation relation = new InsightGraphRelation();

        InsightGraphEntity entitySource = this.insightGraphEntityRepository.findById(idSource).get();
        relation.setObjectFrom(entitySource);
        InsightGraphEntity entityCible = this.insightGraphEntityRepository.findById(idCible).get();
        relation.setObjectTo(entityCible);
        relation.setName(name);
        relation = this.insightGraphRelationRepository.save(relation);

        this.log.info("Relation between : " + relation.getObjectFrom().toString() + " and " + relation.getObjectTo().toString() + " saved: " + relation.getId());
        return relation.getId();
    }

    @Override
    public LinkedHashMap findOne(String id) {
        log.debug("Request to get InsightGraphRelation : {}", id);
        return this.insightGraphRelationRepository.findOne(id);
    }

    @Override
    public void linkAll() {
        log.debug("All vertices will be linked to each other");
        this.insightGraphRelationRepository.linkAll();
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InsightGraphRelation : {}", id);
        this.insightGraphRelationRepository.myDeleteById(id);
    }
}
