package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.ToolService;
import com.peploleum.insight.domain.Tool;
import com.peploleum.insight.repository.ToolRepository;
import com.peploleum.insight.repository.search.ToolSearchRepository;
import com.peploleum.insight.service.dto.ToolDTO;
import com.peploleum.insight.service.mapper.ToolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Tool.
 */
@Service
@Transactional
public class ToolServiceImpl implements ToolService {

    private final Logger log = LoggerFactory.getLogger(ToolServiceImpl.class);

    private final ToolRepository toolRepository;

    private final ToolMapper toolMapper;

    private final ToolSearchRepository toolSearchRepository;

    public ToolServiceImpl(ToolRepository toolRepository, ToolMapper toolMapper, ToolSearchRepository toolSearchRepository) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
        this.toolSearchRepository = toolSearchRepository;
    }

    /**
     * Save a tool.
     *
     * @param toolDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ToolDTO save(ToolDTO toolDTO) {
        log.debug("Request to save Tool : {}", toolDTO);
        Tool tool = toolMapper.toEntity(toolDTO);
        tool = toolRepository.save(tool);
        ToolDTO result = toolMapper.toDto(tool);
        toolSearchRepository.save(tool);
        return result;
    }

    /**
     * Get all the tools.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ToolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tools");
        return toolRepository.findAll(pageable)
            .map(toolMapper::toDto);
    }


    /**
     * Get one tool by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ToolDTO> findOne(Long id) {
        log.debug("Request to get Tool : {}", id);
        return toolRepository.findById(id)
            .map(toolMapper::toDto);
    }

    /**
     * Delete the tool by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tool : {}", id);
        toolRepository.deleteById(id);
        toolSearchRepository.deleteById(id);
    }

    /**
     * Search for the tool corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ToolDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Tools for query {}", query);
        return toolSearchRepository.search(queryStringQuery(query), pageable)
            .map(toolMapper::toDto);
    }
}
