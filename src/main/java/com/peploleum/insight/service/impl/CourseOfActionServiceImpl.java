package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.CourseOfActionService;
import com.peploleum.insight.domain.CourseOfAction;
import com.peploleum.insight.repository.CourseOfActionRepository;
import com.peploleum.insight.repository.search.CourseOfActionSearchRepository;
import com.peploleum.insight.service.dto.CourseOfActionDTO;
import com.peploleum.insight.service.mapper.CourseOfActionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CourseOfAction.
 */
@Service
@Transactional
public class CourseOfActionServiceImpl implements CourseOfActionService {

    private final Logger log = LoggerFactory.getLogger(CourseOfActionServiceImpl.class);

    private final CourseOfActionRepository courseOfActionRepository;

    private final CourseOfActionMapper courseOfActionMapper;

    private final CourseOfActionSearchRepository courseOfActionSearchRepository;

    public CourseOfActionServiceImpl(CourseOfActionRepository courseOfActionRepository, CourseOfActionMapper courseOfActionMapper, CourseOfActionSearchRepository courseOfActionSearchRepository) {
        this.courseOfActionRepository = courseOfActionRepository;
        this.courseOfActionMapper = courseOfActionMapper;
        this.courseOfActionSearchRepository = courseOfActionSearchRepository;
    }

    /**
     * Save a courseOfAction.
     *
     * @param courseOfActionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CourseOfActionDTO save(CourseOfActionDTO courseOfActionDTO) {
        log.debug("Request to save CourseOfAction : {}", courseOfActionDTO);
        CourseOfAction courseOfAction = courseOfActionMapper.toEntity(courseOfActionDTO);
        courseOfAction = courseOfActionRepository.save(courseOfAction);
        CourseOfActionDTO result = courseOfActionMapper.toDto(courseOfAction);
        courseOfActionSearchRepository.save(courseOfAction);
        return result;
    }

    /**
     * Get all the courseOfActions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseOfActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseOfActions");
        return courseOfActionRepository.findAll(pageable)
            .map(courseOfActionMapper::toDto);
    }


    /**
     * Get one courseOfAction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseOfActionDTO> findOne(Long id) {
        log.debug("Request to get CourseOfAction : {}", id);
        return courseOfActionRepository.findById(id)
            .map(courseOfActionMapper::toDto);
    }

    /**
     * Delete the courseOfAction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseOfAction : {}", id);
        courseOfActionRepository.deleteById(id);
        courseOfActionSearchRepository.deleteById(id);
    }

    /**
     * Search for the courseOfAction corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseOfActionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CourseOfActions for query {}", query);
        return courseOfActionSearchRepository.search(queryStringQuery(query), pageable)
            .map(courseOfActionMapper::toDto);
    }
}
