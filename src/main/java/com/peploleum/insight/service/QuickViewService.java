package com.peploleum.insight.service;

import com.peploleum.insight.domain.InsightEntity;
import com.peploleum.insight.repository.GenericRepository;
import com.peploleum.insight.repository.GenericRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by gFolgoas on 27/02/2019.
 */
@Service
public class QuickViewService implements GenericRepositoryCustom {
    private final Logger log = LoggerFactory.getLogger(QuickViewService.class);

    @Autowired
    GenericRepository genericRepository;
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
    @Override
    public Optional<InsightEntity> findEntityById(String id) {
        log.debug("Request to get RawData : {}", id);


        /*LookupOperation lookupOperation = LookupOperation.newLookup()
            .from("Department")
            .localField("dept_id")
            .foreignField("_id")
            .as("departments");*/

        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").is(id)));
        List<InsightEntity> results = this.mongoOperations.aggregate(aggregation, "raw_data", InsightEntity.class).getMappedResults();


        return Optional.of(results.get(0));
    }


}
