package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.domain.graphy.InsightGraphEntity;
import com.peploleum.insight.repository.graphy.InsightGraphEntityRepository;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.dto.CriteriaDTO;
import com.peploleum.insight.service.util.InsightUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@Service
@Profile("graphy")
public class InsightGraphEntityServiceImpl implements InsightGraphEntityService {

    private final Logger log = LoggerFactory.getLogger(InsightGraphEntityServiceImpl.class);
    private final InsightGraphEntityRepository insightGraphEntityRepository;

    public InsightGraphEntityServiceImpl(InsightGraphEntityRepository insightGraphEntityRepository) {
        this.insightGraphEntityRepository = insightGraphEntityRepository;
    }

    @Override
    public Long save(String name, String idMongo, InsightEntityType type) {
        log.debug("Request to save InsightGraphEntity");

        InsightGraphEntity entity = new InsightGraphEntity();
        if (entity.getProperties() == null)
            entity.setProperties(new HashMap<>());
        entity.getProperties().put(InsightUtil.getEntityFieldNameFromType(type), name);
        entity.setIdMongo(idMongo);
        entity.setName(name);
        entity.setEntityType(type);
        entity = this.insightGraphEntityRepository.save(entity);

        this.log.info("Vertex " + type.toString() + " saved: " + entity.getGraphId());
        return entity.getGraphId();
    }

    @Override
    public Long update(Long graphId, String name, String idMongo, InsightEntityType type) {
        log.debug("Request to update InsightGraphEntity : {}", graphId);

        Optional<InsightGraphEntity> optEntity = this.findOne(graphId);
        if (!optEntity.isPresent()) {
            return this.save(name, idMongo, type);
        }
        InsightGraphEntity entity = optEntity.get();
        if (entity.getProperties() == null)
            entity.setProperties(new HashMap<>());
        entity.getProperties().put(InsightUtil.getEntityFieldNameFromType(type), name);
        entity.setIdMongo(idMongo);
        entity.setName(name);
        entity.setEntityType(type);
        entity = this.insightGraphEntityRepository.save(entity);

        this.log.info("Vertex " + type.toString() + " saved: " + entity.getGraphId());
        return entity.getGraphId();
    }

    @Override
    public Optional<InsightGraphEntity> findOne(Long id) {
        log.debug("Request to get InsightGraphEntity : {}", id);
        return insightGraphEntityRepository.findById(id);
    }

    @Override
    public List<InsightGraphEntity> findByCriteria(CriteriaDTO criteria) {
        log.debug("Request to get a list of InsightGraphEntity by Criteria", criteria.getProperty());
        List<InsightGraphEntity> insightGraphEntities = insightGraphEntityRepository.findByCriteria(criteria);
        log.info(insightGraphEntities.toString());
        return insightGraphEntities;
    }

    @Override
    public List<InsightGraphEntity> findAllInOutVerticesByCriteria(Long id, CriteriaDTO criteria) {
        log.debug("Request to get of all the InsightGraphEntity In/Out Vertices by Criteria", criteria.getProperty());
        List<InsightGraphEntity> insightGraphEntities = insightGraphEntityRepository.findAllInOutVerticesByCriteria(id, criteria);
        log.info(insightGraphEntities.toString());
        return insightGraphEntities;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsightGraphEntity : {}", id);
        insightGraphEntityRepository.deleteById(id);
    }
}
