package com.peploleum.insight.repository;

import com.peploleum.insight.domain.Event;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends MongoRepository<Event, String> {

}
