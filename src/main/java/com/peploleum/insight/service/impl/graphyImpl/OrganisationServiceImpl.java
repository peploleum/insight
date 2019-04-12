package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.Organisation;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.repository.OrganisationRepository;
import com.peploleum.insight.repository.search.OrganisationSearchRepository;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.OrganisationService;
import com.peploleum.insight.service.dto.OrganisationDTO;
import com.peploleum.insight.service.mapper.OrganisationMapper;
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
@Profile("graphy")
public class OrganisationServiceImpl implements OrganisationService {

    private final Logger log = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    private final OrganisationMapper organisationMapper;

    private final OrganisationRepository organisationRepository;
    private final OrganisationSearchRepository organisationSearchRepository;
    private final InsightGraphEntityService insightGraphEntityRepository;

    public OrganisationServiceImpl(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper,
                                   OrganisationSearchRepository organisationSearchRepository, InsightGraphEntityService insightGraphEntityRepository) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
        this.organisationSearchRepository = organisationSearchRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
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

        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation.setEntityType(InsightEntityType.Organisation);
        organisation = organisationRepository.save(organisation);
        organisationSearchRepository.save(organisation);
        if (organisation.getExternalId() == null || organisation.getExternalId().isEmpty()) {
            Long externalId = this.insightGraphEntityRepository.save(organisation.getOrganisationName(), organisation.getId(), InsightEntityType.Organisation);
            organisation.setExternalId(String.valueOf(externalId));
            organisation = organisationRepository.save(organisation);
        } else {
            this.insightGraphEntityRepository.update(Long.valueOf(organisation.getExternalId()), organisation.getOrganisationName(),
                organisation.getId(), InsightEntityType.Organisation);
        }
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
