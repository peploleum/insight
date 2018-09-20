package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.EquipmentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Equipment.
 */
public interface EquipmentService {

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    EquipmentDTO save(EquipmentDTO equipmentDTO);

    /**
     * Get all the equipment.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EquipmentDTO> findAll(Pageable pageable);

    /**
     * Get all the Equipment with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<EquipmentDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" equipment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EquipmentDTO> findOne(Long id);

    /**
     * Delete the "id" equipment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the equipment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EquipmentDTO> search(String query, Pageable pageable);
}
