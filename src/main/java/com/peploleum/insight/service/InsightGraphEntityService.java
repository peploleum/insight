package com.peploleum.insight.service;

import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.domain.graphy.InsightGraphEntity;
import com.peploleum.insight.service.dto.CriteriaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface InsightGraphEntityService {

    /**
     * Create and save a InsightGraphEntity Vertex
     *
     * @param name    name of the Entity element
     * @param idMongo unique Identifier in mongo database
     * @param type    Type of Entity
     * @return unique identifier in graph db
     */
    Long save(String name, String idMongo, InsightEntityType type);

    /**
     * Get one entity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InsightGraphEntity> findOne(Long id);

    List<InsightGraphEntity> findByCriteria(CriteriaDTO criteria);

    List<InsightGraphEntity> findAllInOutVerticesByCriteria(Long id, CriteriaDTO criteria);

    /**
     * Delete the entity by id.
     *
     * @param id the id of the entity
     */

    void delete(Long id);
}
