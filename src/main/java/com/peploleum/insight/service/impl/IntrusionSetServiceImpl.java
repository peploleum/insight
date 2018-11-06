package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.IntrusionSetService;
import com.peploleum.insight.domain.IntrusionSet;
import com.peploleum.insight.repository.IntrusionSetRepository;
import com.peploleum.insight.repository.search.IntrusionSetSearchRepository;
import com.peploleum.insight.service.dto.IntrusionSetDTO;
import com.peploleum.insight.service.mapper.IntrusionSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IntrusionSet.
 */
@Service
@Transactional
public class IntrusionSetServiceImpl implements IntrusionSetService {

    private final Logger log = LoggerFactory.getLogger(IntrusionSetServiceImpl.class);

    private final IntrusionSetRepository intrusionSetRepository;

    private final IntrusionSetMapper intrusionSetMapper;

    private final IntrusionSetSearchRepository intrusionSetSearchRepository;

    public IntrusionSetServiceImpl(IntrusionSetRepository intrusionSetRepository, IntrusionSetMapper intrusionSetMapper, IntrusionSetSearchRepository intrusionSetSearchRepository) {
        this.intrusionSetRepository = intrusionSetRepository;
        this.intrusionSetMapper = intrusionSetMapper;
        this.intrusionSetSearchRepository = intrusionSetSearchRepository;
    }

    /**
     * Save a intrusionSet.
     *
     * @param intrusionSetDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IntrusionSetDTO save(IntrusionSetDTO intrusionSetDTO) {
        log.debug("Request to save IntrusionSet : {}", intrusionSetDTO);
        IntrusionSet intrusionSet = intrusionSetMapper.toEntity(intrusionSetDTO);
        intrusionSet = intrusionSetRepository.save(intrusionSet);
        IntrusionSetDTO result = intrusionSetMapper.toDto(intrusionSet);
        intrusionSetSearchRepository.save(intrusionSet);
        return result;
    }

    /**
     * Get all the intrusionSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IntrusionSetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IntrusionSets");
        return intrusionSetRepository.findAll(pageable)
            .map(intrusionSetMapper::toDto);
    }


    /**
     * Get one intrusionSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IntrusionSetDTO> findOne(Long id) {
        log.debug("Request to get IntrusionSet : {}", id);
        return intrusionSetRepository.findById(id)
            .map(intrusionSetMapper::toDto);
    }

    /**
     * Delete the intrusionSet by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IntrusionSet : {}", id);
        intrusionSetRepository.deleteById(id);
        intrusionSetSearchRepository.deleteById(id);
    }

    /**
     * Search for the intrusionSet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IntrusionSetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IntrusionSets for query {}", query);
        return intrusionSetSearchRepository.search(queryStringQuery(query), pageable)
            .map(intrusionSetMapper::toDto);
    }
}
