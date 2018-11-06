package com.peploleum.insight.repository;

import com.peploleum.insight.domain.CourseOfAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseOfAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseOfActionRepository extends JpaRepository<CourseOfAction, Long>, JpaSpecificationExecutor<CourseOfAction> {

}
