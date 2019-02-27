package com.peploleum.insight.repository;

import com.peploleum.insight.domain.InsightEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gFolgoas on 27/02/2019.
 */
@SuppressWarnings("unused")
@Repository
public interface GenericRepository extends MongoRepository<InsightEntity, String>, GenericRepositoryCustom {
}
