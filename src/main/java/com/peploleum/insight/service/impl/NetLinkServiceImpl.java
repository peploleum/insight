package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.NetLinkService;
import com.peploleum.insight.domain.NetLink;
import com.peploleum.insight.repository.NetLinkRepository;
import com.peploleum.insight.repository.search.NetLinkSearchRepository;
import com.peploleum.insight.service.dto.NetLinkDTO;
import com.peploleum.insight.service.mapper.NetLinkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing NetLink.
 */
@Service
@Transactional
public class NetLinkServiceImpl implements NetLinkService {

    private final Logger log = LoggerFactory.getLogger(NetLinkServiceImpl.class);

    private final NetLinkRepository netLinkRepository;

    private final NetLinkMapper netLinkMapper;

    private final NetLinkSearchRepository netLinkSearchRepository;

    public NetLinkServiceImpl(NetLinkRepository netLinkRepository, NetLinkMapper netLinkMapper, NetLinkSearchRepository netLinkSearchRepository) {
        this.netLinkRepository = netLinkRepository;
        this.netLinkMapper = netLinkMapper;
        this.netLinkSearchRepository = netLinkSearchRepository;
    }

    /**
     * Save a netLink.
     *
     * @param netLinkDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NetLinkDTO save(NetLinkDTO netLinkDTO) {
        log.debug("Request to save NetLink : {}", netLinkDTO);
        NetLink netLink = netLinkMapper.toEntity(netLinkDTO);
        netLink = netLinkRepository.save(netLink);
        NetLinkDTO result = netLinkMapper.toDto(netLink);
        netLinkSearchRepository.save(netLink);
        return result;
    }

    /**
     * Get all the netLinks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NetLinkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NetLinks");
        return netLinkRepository.findAll(pageable)
            .map(netLinkMapper::toDto);
    }


    /**
     * Get one netLink by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NetLinkDTO> findOne(Long id) {
        log.debug("Request to get NetLink : {}", id);
        return netLinkRepository.findById(id)
            .map(netLinkMapper::toDto);
    }

    /**
     * Delete the netLink by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NetLink : {}", id);
        netLinkRepository.deleteById(id);
        netLinkSearchRepository.deleteById(id);
    }

    /**
     * Search for the netLink corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NetLinkDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NetLinks for query {}", query);
        return netLinkSearchRepository.search(queryStringQuery(query), pageable)
            .map(netLinkMapper::toDto);
    }
}
