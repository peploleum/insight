package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.CourseOfActionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CourseOfAction.
 */
public interface CourseOfActionService {

    /**
     * Save a courseOfAction.
     *
     * @param courseOfActionDTO the entity to save
     * @return the persisted entity
     */
    CourseOfActionDTO save(CourseOfActionDTO courseOfActionDTO);

    /**
     * Get all the courseOfActions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CourseOfActionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" courseOfAction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CourseOfActionDTO> findOne(Long id);

    /**
     * Delete the "id" courseOfAction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the courseOfAction corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CourseOfActionDTO> search(String query, Pageable pageable);
}
