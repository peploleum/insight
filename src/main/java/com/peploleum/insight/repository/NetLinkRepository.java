package com.peploleum.insight.repository;

import com.peploleum.insight.domain.NetLink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NetLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetLinkRepository extends JpaRepository<NetLink, Long>, JpaSpecificationExecutor<NetLink> {

}
