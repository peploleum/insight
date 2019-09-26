package com.peploleum.insight.repository.graphy;

import com.peploleum.insight.domain.graphy.InsightGraphEntity;
import com.peploleum.insight.service.dto.CriteriaDTO;

import java.util.List;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface InsightGraphEntityCustomRepository {
    List<InsightGraphEntity> findByCriteria(CriteriaDTO criteria);

    InsightGraphEntity findOne(Long id);

    List<InsightGraphEntity> findAllInOutVerticesByCriteria(Long id, CriteriaDTO criteria);

    InsightGraphEntity saveWithProperties(InsightGraphEntity entity);
}
