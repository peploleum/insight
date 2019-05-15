package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.repository.OrganisationRepository;
import com.peploleum.insight.repository.search.OrganisationSearchRepository;
import com.peploleum.insight.service.OrganisationService;
import com.peploleum.insight.service.dto.OrganisationDTO;
import com.peploleum.insight.service.mapper.OrganisationMapper;
import com.peploleum.insight.service.util.InsightUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Organisation.
 */
@Service
@Profile("!graphy")
public class OrganisationServiceImpl implements OrganisationService {

    private final Logger log = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    private final OrganisationMapper organisationMapper;

    private final OrganisationRepository organisationRepository;
    private final OrganisationSearchRepository organisationSearchRepository;

    public OrganisationServiceImpl(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper,
                                   OrganisationSearchRepository organisationSearchRepository) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
        this.organisationSearchRepository = organisationSearchRepository;
    }

    /**
     * Save a organisation.
     *
     * @param organisationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganisationDTO save(OrganisationDTO organisationDTO) {
        log.debug("Request to save Organisation : {}", organisationDTO);
        if (organisationDTO.getGeometry() == null && organisationDTO.getOrganisationCoordinates() != null && !organisationDTO.getOrganisationCoordinates().isEmpty()) {
            String[] coordinates = organisationDTO.getOrganisationCoordinates().split(",");
            organisationDTO.setGeometry(InsightUtil.getGeometryFromCoordinate(coordinates));
        }

        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation.setEntityType(InsightEntityType.Organisation);
        organisation = organisationRepository.save(organisation);
        organisationSearchRepository.save(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Get all the organisations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<OrganisationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Organisations");
        return organisationRepository.findAll(pageable)
            .map(organisationMapper::toDto);
    }


    /**
     * Get one organisation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<OrganisationDTO> findOne(String id) {
        log.debug("Request to get Organisation : {}", id);
        return organisationRepository.findById(id)
            .map(organisationMapper::toDto);
    }

    /**
     * Delete the organisation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Organisation : {}", id);
        organisationRepository.deleteById(id);
        organisationSearchRepository.deleteById(id);
    }

    /**
     * Search for the organisation corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<OrganisationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Organisations for query {}", query);
        return organisationSearchRepository.search(queryStringQuery(query), pageable)
            .map(organisationMapper::toDto);
    }
}
