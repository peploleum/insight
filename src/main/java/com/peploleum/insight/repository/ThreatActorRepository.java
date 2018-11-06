package com.peploleum.insight.repository;

import com.peploleum.insight.domain.ThreatActor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ThreatActor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThreatActorRepository extends JpaRepository<ThreatActor, Long>, JpaSpecificationExecutor<ThreatActor> {

}
