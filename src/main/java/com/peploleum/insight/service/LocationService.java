package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.LocationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Location.
 */
public interface LocationService {

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save
     * @return the persisted entity
     */
    LocationDTO save(LocationDTO locationDTO);

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LocationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" location.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LocationDTO> findOne(String id);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the location corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LocationDTO> search(String query, Pageable pageable);
}
