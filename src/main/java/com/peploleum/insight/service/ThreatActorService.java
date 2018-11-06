package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.ThreatActorDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ThreatActor.
 */
public interface ThreatActorService {

    /**
     * Save a threatActor.
     *
     * @param threatActorDTO the entity to save
     * @return the persisted entity
     */
    ThreatActorDTO save(ThreatActorDTO threatActorDTO);

    /**
     * Get all the threatActors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ThreatActorDTO> findAll(Pageable pageable);


    /**
     * Get the "id" threatActor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ThreatActorDTO> findOne(Long id);

    /**
     * Delete the "id" threatActor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the threatActor corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ThreatActorDTO> search(String query, Pageable pageable);
}
