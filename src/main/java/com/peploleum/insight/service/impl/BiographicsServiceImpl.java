package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.BiographicsService;
import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.repository.BiographicsRepository;
import com.peploleum.insight.repository.search.BiographicsSearchRepository;
import com.peploleum.insight.service.dto.BiographicsDTO;
import com.peploleum.insight.service.mapper.BiographicsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Biographics.
 */
@Service
public class BiographicsServiceImpl implements BiographicsService {

    private final Logger log = LoggerFactory.getLogger(BiographicsServiceImpl.class);

    private final BiographicsRepository biographicsRepository;

    private final BiographicsMapper biographicsMapper;

    private final BiographicsSearchRepository biographicsSearchRepository;

    public BiographicsServiceImpl(BiographicsRepository biographicsRepository, BiographicsMapper biographicsMapper, BiographicsSearchRepository biographicsSearchRepository) {
        this.biographicsRepository = biographicsRepository;
        this.biographicsMapper = biographicsMapper;
        this.biographicsSearchRepository = biographicsSearchRepository;
    }

    /**
     * Save a biographics.
     *
     * @param biographicsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BiographicsDTO save(BiographicsDTO biographicsDTO) {
        log.debug("Request to save Biographics : {}", biographicsDTO);

        Biographics biographics = biographicsMapper.toEntity(biographicsDTO);
        biographics = biographicsRepository.save(biographics);
        BiographicsDTO result = biographicsMapper.toDto(biographics);
        biographicsSearchRepository.save(biographics);
        return result;
    }

    /**
     * Get all the biographics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<BiographicsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Biographics");
        return biographicsRepository.findAll(pageable)
            .map(biographicsMapper::toDto);
    }


    /**
     * Get one biographics by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<BiographicsDTO> findOne(String id) {
        log.debug("Request to get Biographics : {}", id);
        return biographicsRepository.findById(id)
            .map(biographicsMapper::toDto);
    }

    /**
     * Delete the biographics by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Biographics : {}", id);
        biographicsRepository.deleteById(id);
        biographicsSearchRepository.deleteById(id);
    }

    /**
     * Search for the biographics corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<BiographicsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Biographics for query {}", query);
        return biographicsSearchRepository.search(queryStringQuery(query), pageable)
            .map(biographicsMapper::toDto);
    }
}
