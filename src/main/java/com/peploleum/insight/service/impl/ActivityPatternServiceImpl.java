package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.ActivityPatternService;
import com.peploleum.insight.domain.ActivityPattern;
import com.peploleum.insight.repository.ActivityPatternRepository;
import com.peploleum.insight.repository.search.ActivityPatternSearchRepository;
import com.peploleum.insight.service.dto.ActivityPatternDTO;
import com.peploleum.insight.service.mapper.ActivityPatternMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ActivityPattern.
 */
@Service
@Transactional
public class ActivityPatternServiceImpl implements ActivityPatternService {

    private final Logger log = LoggerFactory.getLogger(ActivityPatternServiceImpl.class);

    private final ActivityPatternRepository activityPatternRepository;

    private final ActivityPatternMapper activityPatternMapper;

    private final ActivityPatternSearchRepository activityPatternSearchRepository;

    public ActivityPatternServiceImpl(ActivityPatternRepository activityPatternRepository, ActivityPatternMapper activityPatternMapper, ActivityPatternSearchRepository activityPatternSearchRepository) {
        this.activityPatternRepository = activityPatternRepository;
        this.activityPatternMapper = activityPatternMapper;
        this.activityPatternSearchRepository = activityPatternSearchRepository;
    }

    /**
     * Save a activityPattern.
     *
     * @param activityPatternDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ActivityPatternDTO save(ActivityPatternDTO activityPatternDTO) {
        log.debug("Request to save ActivityPattern : {}", activityPatternDTO);
        ActivityPattern activityPattern = activityPatternMapper.toEntity(activityPatternDTO);
        activityPattern = activityPatternRepository.save(activityPattern);
        ActivityPatternDTO result = activityPatternMapper.toDto(activityPattern);
        activityPatternSearchRepository.save(activityPattern);
        return result;
    }

    /**
     * Get all the activityPatterns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityPatternDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityPatterns");
        return activityPatternRepository.findAll(pageable)
            .map(activityPatternMapper::toDto);
    }


    /**
     * Get one activityPattern by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityPatternDTO> findOne(Long id) {
        log.debug("Request to get ActivityPattern : {}", id);
        return activityPatternRepository.findById(id)
            .map(activityPatternMapper::toDto);
    }

    /**
     * Delete the activityPattern by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ActivityPattern : {}", id);
        activityPatternRepository.deleteById(id);
        activityPatternSearchRepository.deleteById(id);
    }

    /**
     * Search for the activityPattern corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityPatternDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ActivityPatterns for query {}", query);
        return activityPatternSearchRepository.search(queryStringQuery(query), pageable)
            .map(activityPatternMapper::toDto);
    }
}
