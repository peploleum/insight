package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.RawDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.mongodb.core.query.Query;

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
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RawDataDTO> findAll(Pageable pageable);


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
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RawDataDTO> search(String query, Pageable pageable);

    /**
     * Search for the rawData mathing the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    List<RawDataDTO> searchByCriteria(Query query);

    /**
     * Search for the rawData matching the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the page of entities
     */
    Page<RawDataDTO> searchByCriteria(Query query, Pageable pageable);

    /**
     * Search for the rawData corresponding to the query.
     *
     * @param query the SearchQuery of the search
     * @return the list of entities
     */
    Page<RawDataDTO> search(NativeSearchQuery query);
}
