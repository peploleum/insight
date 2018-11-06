package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.ThreatActorService;
import com.peploleum.insight.domain.ThreatActor;
import com.peploleum.insight.repository.ThreatActorRepository;
import com.peploleum.insight.repository.search.ThreatActorSearchRepository;
import com.peploleum.insight.service.dto.ThreatActorDTO;
import com.peploleum.insight.service.mapper.ThreatActorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ThreatActor.
 */
@Service
@Transactional
public class ThreatActorServiceImpl implements ThreatActorService {

    private final Logger log = LoggerFactory.getLogger(ThreatActorServiceImpl.class);

    private final ThreatActorRepository threatActorRepository;

    private final ThreatActorMapper threatActorMapper;

    private final ThreatActorSearchRepository threatActorSearchRepository;

    public ThreatActorServiceImpl(ThreatActorRepository threatActorRepository, ThreatActorMapper threatActorMapper, ThreatActorSearchRepository threatActorSearchRepository) {
        this.threatActorRepository = threatActorRepository;
        this.threatActorMapper = threatActorMapper;
        this.threatActorSearchRepository = threatActorSearchRepository;
    }

    /**
     * Save a threatActor.
     *
     * @param threatActorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ThreatActorDTO save(ThreatActorDTO threatActorDTO) {
        log.debug("Request to save ThreatActor : {}", threatActorDTO);
        ThreatActor threatActor = threatActorMapper.toEntity(threatActorDTO);
        threatActor = threatActorRepository.save(threatActor);
        ThreatActorDTO result = threatActorMapper.toDto(threatActor);
        threatActorSearchRepository.save(threatActor);
        return result;
    }

    /**
     * Get all the threatActors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ThreatActorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ThreatActors");
        return threatActorRepository.findAll(pageable)
            .map(threatActorMapper::toDto);
    }


    /**
     * Get one threatActor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ThreatActorDTO> findOne(Long id) {
        log.debug("Request to get ThreatActor : {}", id);
        return threatActorRepository.findById(id)
            .map(threatActorMapper::toDto);
    }

    /**
     * Delete the threatActor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ThreatActor : {}", id);
        threatActorRepository.deleteById(id);
        threatActorSearchRepository.deleteById(id);
    }

    /**
     * Search for the threatActor corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ThreatActorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ThreatActors for query {}", query);
        return threatActorSearchRepository.search(queryStringQuery(query), pageable)
            .map(threatActorMapper::toDto);
    }
}
