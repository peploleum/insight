package com.peploleum.insight.service.impl;

import com.peploleum.insight.repository.search.GeoRefSearchRepository;
import com.peploleum.insight.service.GeoRefService;
import com.peploleum.insight.service.dto.GeoRefDTO;
import com.peploleum.insight.service.mapper.GeoRefMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by GFOLGOAS on 15/02/2019.
 */
@Service
public class GeoRefServiceImpl implements GeoRefService {
    private final Logger log = LoggerFactory.getLogger(GeoRefServiceImpl.class);
    private final GeoRefSearchRepository geoRefSearchRepository;
    private final GeoRefMapper geoRefMapper;

    private final int DEFAULT_PAGE_SIZE = 50;

    public GeoRefServiceImpl(GeoRefSearchRepository geoRefSearchRepository, GeoRefMapper geoRefMapper) {
        this.geoRefSearchRepository = geoRefSearchRepository;
        this.geoRefMapper = geoRefMapper;
    }

    @Override
    public Page<GeoRefDTO> search(String query) {
        log.debug("Request to search for a page of Events for query {}", query);
        return geoRefSearchRepository.search(queryStringQuery(query), PageRequest.of(0, this.DEFAULT_PAGE_SIZE)).map(geoRefMapper::toDto);
    }
}
