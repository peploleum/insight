package com.peploleum.insight.repository.graphy;

import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import com.peploleum.insight.domain.graphy.InsightGraphRelation;
import org.springframework.stereotype.Repository;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@Repository
public interface InsightGraphRelationRepository extends GremlinRepository<InsightGraphRelation, String>, InsightGraphRelationCustomRepository {
}
