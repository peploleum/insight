package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.OrganisationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Organisation.
 */
public interface OrganisationService {

    /**
     * Save a organisation.
     *
     * @param organisationDTO the entity to save
     * @return the persisted entity
     */
    OrganisationDTO save(OrganisationDTO organisationDTO);

    /**
     * Get all the organisations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrganisationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" organisation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrganisationDTO> findOne(String id);

    /**
     * Delete the "id" organisation.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the organisation corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrganisationDTO> search(String query, Pageable pageable);
}
