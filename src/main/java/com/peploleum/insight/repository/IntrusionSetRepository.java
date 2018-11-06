package com.peploleum.insight.repository;

import com.peploleum.insight.domain.IntrusionSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IntrusionSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntrusionSetRepository extends JpaRepository<IntrusionSet, Long>, JpaSpecificationExecutor<IntrusionSet> {

}
