package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.RawDataService;
import com.peploleum.insight.domain.RawData;
import com.peploleum.insight.repository.RawDataRepository;
import com.peploleum.insight.repository.search.RawDataSearchRepository;
import com.peploleum.insight.service.dto.RawDataDTO;
import com.peploleum.insight.service.mapper.RawDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RawData.
 */
@Service
public class RawDataServiceImpl implements RawDataService {

    private final Logger log = LoggerFactory.getLogger(RawDataServiceImpl.class);

    private final RawDataRepository rawDataRepository;

    private final RawDataMapper rawDataMapper;

    private final RawDataSearchRepository rawDataSearchRepository;

    public RawDataServiceImpl(RawDataRepository rawDataRepository, RawDataMapper rawDataMapper, RawDataSearchRepository rawDataSearchRepository) {
        this.rawDataRepository = rawDataRepository;
        this.rawDataMapper = rawDataMapper;
        this.rawDataSearchRepository = rawDataSearchRepository;
    }

    /**
     * Save a rawData.
     *
     * @param rawDataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RawDataDTO save(RawDataDTO rawDataDTO) {
        log.debug("Request to save RawData : {}", rawDataDTO);

        RawData rawData = rawDataMapper.toEntity(rawDataDTO);
        rawData = rawDataRepository.save(rawData);
        RawDataDTO result = rawDataMapper.toDto(rawData);
        rawDataSearchRepository.save(rawData);
        return result;
    }

    /**
     * Get all the rawData.
     *
     * @return the list of entities
     */
    @Override
    public List<RawDataDTO> findAll() {
        log.debug("Request to get all RawData");
        return rawDataRepository.findAll().stream()
            .map(rawDataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one rawData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<RawDataDTO> findOne(String id) {
        log.debug("Request to get RawData : {}", id);
        return rawDataRepository.findById(id)
            .map(rawDataMapper::toDto);
    }

    /**
     * Delete the rawData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete RawData : {}", id);
        rawDataRepository.deleteById(id);
        rawDataSearchRepository.deleteById(id);
    }

    /**
     * Search for the rawData corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    public List<RawDataDTO> search(String query) {
        log.debug("Request to search RawData for query {}", query);
        return StreamSupport
            .stream(rawDataSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rawDataMapper::toDto)
            .collect(Collectors.toList());
    }
}
