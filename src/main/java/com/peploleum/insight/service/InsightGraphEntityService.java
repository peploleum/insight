package com.peploleum.insight.service;

import com.peploleum.insight.domain.graphy.InsightGraphEntity;
import com.peploleum.insight.service.dto.CriteriaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface InsightGraphEntityService {

    /**
     * Get one entity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<InsightGraphEntity> findOne(Long id);

    public List<InsightGraphEntity> findByCriteria(CriteriaDTO criteria);

    public List<InsightGraphEntity> findAllInOutVerticesByCriteria(Long id, CriteriaDTO criteria);

    /**
     * Delete the entity by id.
     *
     * @param id the id of the entity
     */

    public void delete(Long id);
}
