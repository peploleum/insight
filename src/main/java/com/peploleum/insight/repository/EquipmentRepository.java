package com.peploleum.insight.repository;

import com.peploleum.insight.domain.Equipment;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Equipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends MongoRepository<Equipment, String> {

}
