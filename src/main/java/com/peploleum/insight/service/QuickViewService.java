package com.peploleum.insight.service;

import com.peploleum.insight.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by gFolgoas on 27/02/2019.
 */
@Service
public class QuickViewService {
    private final Logger log = LoggerFactory.getLogger(QuickViewService.class);

    private final MongoOperations mongoOperations;

    public QuickViewService(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Get entity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<InsightEntity> findEntityById(String id) {
        log.debug("Request to get Entity : {}", id);

        Class[] classArray = {RawData.class, Event.class, Biographics.class, Equipment.class, Location.class, Organisation.class};
        List<Class> classCollection = Arrays.asList(classArray);
        InsightEntity result = null;
        for (Class coll : classCollection) {
            AggregationResults<InsightEntity> aggr = this.mongoOperations.aggregate(
                Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").is(id))), coll, InsightEntity.class);
            if (!CollectionUtils.isEmpty(aggr.getMappedResults())) {
                result = aggr.getMappedResults().get(0);
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    /**
     * Get entities by list of ids.
     *
     * @param ids the ids of the entities
     * @return the list of entities
     */
    public List<InsightEntity> findEntitiesById(List<String> ids) {
        log.debug("Request to get Entities");
        Class[] classArray = {RawData.class, Event.class, Biographics.class, Equipment.class, Location.class, Organisation.class};
        List<Class> classCollection = Arrays.asList(classArray);
        List<InsightEntity> result = new ArrayList<>();

        for (Class coll : classCollection) {
            AggregationResults<InsightEntity> aggr = this.mongoOperations.aggregate(
                Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").in(ids))), coll, InsightEntity.class);
            if (!CollectionUtils.isEmpty(aggr.getMappedResults())) {
                result.addAll(aggr.getMappedResults());
                if (result.size() == ids.size()) {
                    break;
                }
            }
        }

        return result;
    }


}
