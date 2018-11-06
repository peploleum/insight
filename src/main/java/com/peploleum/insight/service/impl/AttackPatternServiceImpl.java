package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.AttackPatternService;
import com.peploleum.insight.domain.AttackPattern;
import com.peploleum.insight.repository.AttackPatternRepository;
import com.peploleum.insight.repository.search.AttackPatternSearchRepository;
import com.peploleum.insight.service.dto.AttackPatternDTO;
import com.peploleum.insight.service.mapper.AttackPatternMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AttackPattern.
 */
@Service
@Transactional
public class AttackPatternServiceImpl implements AttackPatternService {

    private final Logger log = LoggerFactory.getLogger(AttackPatternServiceImpl.class);

    private final AttackPatternRepository attackPatternRepository;

    private final AttackPatternMapper attackPatternMapper;

    private final AttackPatternSearchRepository attackPatternSearchRepository;

    public AttackPatternServiceImpl(AttackPatternRepository attackPatternRepository, AttackPatternMapper attackPatternMapper, AttackPatternSearchRepository attackPatternSearchRepository) {
        this.attackPatternRepository = attackPatternRepository;
        this.attackPatternMapper = attackPatternMapper;
        this.attackPatternSearchRepository = attackPatternSearchRepository;
    }

    /**
     * Save a attackPattern.
     *
     * @param attackPatternDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AttackPatternDTO save(AttackPatternDTO attackPatternDTO) {
        log.debug("Request to save AttackPattern : {}", attackPatternDTO);
        AttackPattern attackPattern = attackPatternMapper.toEntity(attackPatternDTO);
        attackPattern = attackPatternRepository.save(attackPattern);
        AttackPatternDTO result = attackPatternMapper.toDto(attackPattern);
        attackPatternSearchRepository.save(attackPattern);
        return result;
    }

    /**
     * Get all the attackPatterns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttackPatternDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AttackPatterns");
        return attackPatternRepository.findAll(pageable)
            .map(attackPatternMapper::toDto);
    }


    /**
     * Get one attackPattern by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AttackPatternDTO> findOne(Long id) {
        log.debug("Request to get AttackPattern : {}", id);
        return attackPatternRepository.findById(id)
            .map(attackPatternMapper::toDto);
    }

    /**
     * Delete the attackPattern by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttackPattern : {}", id);
        attackPatternRepository.deleteById(id);
        attackPatternSearchRepository.deleteById(id);
    }

    /**
     * Search for the attackPattern corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttackPatternDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AttackPatterns for query {}", query);
        return attackPatternSearchRepository.search(queryStringQuery(query), pageable)
            .map(attackPatternMapper::toDto);
    }
}
