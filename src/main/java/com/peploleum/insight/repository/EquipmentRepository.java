package com.peploleum.insight.repository;

import com.peploleum.insight.domain.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Equipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {

    @Query(value = "select distinct equipment from Equipment equipment left join fetch equipment.locations left join fetch equipment.organisations",
        countQuery = "select count(distinct equipment) from Equipment equipment")
    Page<Equipment> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct equipment from Equipment equipment left join fetch equipment.locations left join fetch equipment.organisations")
    List<Equipment> findAllWithEagerRelationships();

    @Query("select equipment from Equipment equipment left join fetch equipment.locations left join fetch equipment.organisations where equipment.id =:id")
    Optional<Equipment> findOneWithEagerRelationships(@Param("id") Long id);

}
