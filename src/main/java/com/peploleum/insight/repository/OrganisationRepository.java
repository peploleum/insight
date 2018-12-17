package com.peploleum.insight.repository;

import com.peploleum.insight.domain.Organisation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Organisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganisationRepository extends MongoRepository<Organisation, String> {

}
