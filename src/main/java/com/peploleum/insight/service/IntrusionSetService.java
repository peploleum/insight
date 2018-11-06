package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.IntrusionSetDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing IntrusionSet.
 */
public interface IntrusionSetService {

    /**
     * Save a intrusionSet.
     *
     * @param intrusionSetDTO the entity to save
     * @return the persisted entity
     */
    IntrusionSetDTO save(IntrusionSetDTO intrusionSetDTO);

    /**
     * Get all the intrusionSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IntrusionSetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" intrusionSet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IntrusionSetDTO> findOne(Long id);

    /**
     * Delete the "id" intrusionSet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the intrusionSet corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IntrusionSetDTO> search(String query, Pageable pageable);
}
