package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.NetLinkDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing NetLink.
 */
public interface NetLinkService {

    /**
     * Save a netLink.
     *
     * @param netLinkDTO the entity to save
     * @return the persisted entity
     */
    NetLinkDTO save(NetLinkDTO netLinkDTO);

    /**
     * Get all the netLinks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NetLinkDTO> findAll(Pageable pageable);


    /**
     * Get the "id" netLink.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NetLinkDTO> findOne(Long id);

    /**
     * Delete the "id" netLink.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the netLink corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NetLinkDTO> search(String query, Pageable pageable);
}
