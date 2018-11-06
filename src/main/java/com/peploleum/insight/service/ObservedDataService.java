package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.ObservedDataDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ObservedData.
 */
public interface ObservedDataService {

    /**
     * Save a observedData.
     *
     * @param observedDataDTO the entity to save
     * @return the persisted entity
     */
    ObservedDataDTO save(ObservedDataDTO observedDataDTO);

    /**
     * Get all the observedData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ObservedDataDTO> findAll(Pageable pageable);


    /**
     * Get the "id" observedData.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ObservedDataDTO> findOne(Long id);

    /**
     * Delete the "id" observedData.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the observedData corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ObservedDataDTO> search(String query, Pageable pageable);
}
