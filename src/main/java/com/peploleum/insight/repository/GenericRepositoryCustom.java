package com.peploleum.insight.repository;

import com.peploleum.insight.domain.InsightEntity;

import java.util.Optional;

/**
 * Created by gFolgoas on 27/02/2019.
 */
public interface GenericRepositoryCustom {
    Optional<InsightEntity> findEntityById(String id);
}
