package com.peploleum.insight.repository;

import com.peploleum.insight.domain.AttackPattern;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AttackPattern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttackPatternRepository extends JpaRepository<AttackPattern, Long>, JpaSpecificationExecutor<AttackPattern> {

}
