package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.RawDataDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RawData.
 */
public interface RawDataService {

    /**
     * Save a rawData.
     *
     * @param rawDataDTO the entity to save
     * @return the persisted entity
     */
    RawDataDTO save(RawDataDTO rawDataDTO);

    /**
     * Get all the rawData.
     *
     * @return the list of entities
     */
    List<RawDataDTO> findAll();


    /**
     * Get the "id" rawData.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RawDataDTO> findOne(String id);

    /**
     * Delete the "id" rawData.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the rawData corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<RawDataDTO> search(String query);
}
