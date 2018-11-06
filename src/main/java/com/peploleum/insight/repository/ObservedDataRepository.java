package com.peploleum.insight.repository;

import com.peploleum.insight.domain.ObservedData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ObservedData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObservedDataRepository extends JpaRepository<ObservedData, Long>, JpaSpecificationExecutor<ObservedData> {

}
