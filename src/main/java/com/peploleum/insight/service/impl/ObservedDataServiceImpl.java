package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.ObservedDataService;
import com.peploleum.insight.domain.ObservedData;
import com.peploleum.insight.repository.ObservedDataRepository;
import com.peploleum.insight.repository.search.ObservedDataSearchRepository;
import com.peploleum.insight.service.dto.ObservedDataDTO;
import com.peploleum.insight.service.mapper.ObservedDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ObservedData.
 */
@Service
@Transactional
public class ObservedDataServiceImpl implements ObservedDataService {

    private final Logger log = LoggerFactory.getLogger(ObservedDataServiceImpl.class);

    private final ObservedDataRepository observedDataRepository;

    private final ObservedDataMapper observedDataMapper;

    private final ObservedDataSearchRepository observedDataSearchRepository;

    public ObservedDataServiceImpl(ObservedDataRepository observedDataRepository, ObservedDataMapper observedDataMapper, ObservedDataSearchRepository observedDataSearchRepository) {
        this.observedDataRepository = observedDataRepository;
        this.observedDataMapper = observedDataMapper;
        this.observedDataSearchRepository = observedDataSearchRepository;
    }

    /**
     * Save a observedData.
     *
     * @param observedDataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ObservedDataDTO save(ObservedDataDTO observedDataDTO) {
        log.debug("Request to save ObservedData : {}", observedDataDTO);
        ObservedData observedData = observedDataMapper.toEntity(observedDataDTO);
        observedData = observedDataRepository.save(observedData);
        ObservedDataDTO result = observedDataMapper.toDto(observedData);
        observedDataSearchRepository.save(observedData);
        return result;
    }

    /**
     * Get all the observedData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ObservedDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ObservedData");
        return observedDataRepository.findAll(pageable)
            .map(observedDataMapper::toDto);
    }


    /**
     * Get one observedData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ObservedDataDTO> findOne(Long id) {
        log.debug("Request to get ObservedData : {}", id);
        return observedDataRepository.findById(id)
            .map(observedDataMapper::toDto);
    }

    /**
     * Delete the observedData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ObservedData : {}", id);
        observedDataRepository.deleteById(id);
        observedDataSearchRepository.deleteById(id);
    }

    /**
     * Search for the observedData corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ObservedDataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ObservedData for query {}", query);
        return observedDataSearchRepository.search(queryStringQuery(query), pageable)
            .map(observedDataMapper::toDto);
    }
}
