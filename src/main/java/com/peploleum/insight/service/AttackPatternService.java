package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.AttackPatternDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing AttackPattern.
 */
public interface AttackPatternService {

    /**
     * Save a attackPattern.
     *
     * @param attackPatternDTO the entity to save
     * @return the persisted entity
     */
    AttackPatternDTO save(AttackPatternDTO attackPatternDTO);

    /**
     * Get all the attackPatterns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AttackPatternDTO> findAll(Pageable pageable);


    /**
     * Get the "id" attackPattern.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AttackPatternDTO> findOne(Long id);

    /**
     * Delete the "id" attackPattern.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the attackPattern corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AttackPatternDTO> search(String query, Pageable pageable);
}
