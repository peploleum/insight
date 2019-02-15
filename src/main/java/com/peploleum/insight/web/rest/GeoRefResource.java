package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.domain.map.MapData;
import com.peploleum.insight.service.GeoRefService;
import com.peploleum.insight.service.dto.GeoRefDTO;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by GFOLGOAS on 15/02/2019.
 */
@RestController
@RequestMapping("/api")
public class GeoRefResource {
    private final Logger log = LoggerFactory.getLogger(GeoRefResource.class);
    private final GeoRefService geoRefService;

    public GeoRefResource(GeoRefService geoRefService) {
        this.geoRefService = geoRefService;
    }

    /**
     * SEARCH  /map/_search/georef?query=:query : search for the georefs corresponding
     * to the query.
     *
     * @param query the query of the georef search
     * @return the result of the search
     */
    @GetMapping("/map/_search/georef")
    @Timed
    public ResponseEntity<List<MapData>> searchGeoRef(@RequestParam String query) {
        log.debug("REST request to search for a page of Locations for query {}", query);
        Page<GeoRefDTO> page = geoRefService.search(query);
        List<MapData> mapDto = page.getContent().stream().map(geoRef -> {
            MapData dto = new MapData();
            dto.setId(geoRef.getId());
            dto.setLabel(geoRef.getGeonameid());
            dto.setCoordinate(geoRef.getLocation());
            return dto;
        }).collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/georef");
        return new ResponseEntity<>(mapDto, headers, HttpStatus.OK);
    }
}
