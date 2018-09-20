package com.peploleum.insight.repository;

import com.peploleum.insight.domain.Biographics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Biographics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BiographicsRepository extends JpaRepository<Biographics, Long>, JpaSpecificationExecutor<Biographics> {

    @Query(value = "select distinct biographics from Biographics biographics left join fetch biographics.events left join fetch biographics.equipment left join fetch biographics.locations left join fetch biographics.organisations",
        countQuery = "select count(distinct biographics) from Biographics biographics")
    Page<Biographics> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct biographics from Biographics biographics left join fetch biographics.events left join fetch biographics.equipment left join fetch biographics.locations left join fetch biographics.organisations")
    List<Biographics> findAllWithEagerRelationships();

    @Query("select biographics from Biographics biographics left join fetch biographics.events left join fetch biographics.equipment left join fetch biographics.locations left join fetch biographics.organisations where biographics.id =:id")
    Optional<Biographics> findOneWithEagerRelationships(@Param("id") Long id);

}
