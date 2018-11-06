package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.ActivityPatternDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ActivityPattern.
 */
public interface ActivityPatternService {

    /**
     * Save a activityPattern.
     *
     * @param activityPatternDTO the entity to save
     * @return the persisted entity
     */
    ActivityPatternDTO save(ActivityPatternDTO activityPatternDTO);

    /**
     * Get all the activityPatterns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ActivityPatternDTO> findAll(Pageable pageable);


    /**
     * Get the "id" activityPattern.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ActivityPatternDTO> findOne(Long id);

    /**
     * Delete the "id" activityPattern.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the activityPattern corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ActivityPatternDTO> search(String query, Pageable pageable);
}
