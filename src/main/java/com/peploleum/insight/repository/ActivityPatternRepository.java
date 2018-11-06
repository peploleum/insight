package com.peploleum.insight.repository;

import com.peploleum.insight.domain.ActivityPattern;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActivityPattern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityPatternRepository extends JpaRepository<ActivityPattern, Long>, JpaSpecificationExecutor<ActivityPattern> {

}
