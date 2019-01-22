package com.peploleum.insight.repository;

import com.peploleum.insight.domain.RawData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the RawData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RawDataRepository extends MongoRepository<RawData, String> {
}
