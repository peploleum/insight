package com.peploleum.insight.repository;

import com.peploleum.insight.domain.Biographics;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Biographics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BiographicsRepository extends MongoRepository<Biographics, String> {

}
