package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.domain.map.MapData;
import com.peploleum.insight.service.RawDataService;
import com.peploleum.insight.service.dto.RawDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gFolgoas on 18/01/2019.
 */
@RestController
@RequestMapping("/api")
public class MapResource {
    private final Logger log = LoggerFactory.getLogger(MapResource.class);
    private final RawDataService rawDataService;

    public MapResource(RawDataService rawDataService) {
        this.rawDataService = rawDataService;
    }

    /**
     * GET  map/get-all-data : get all data.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of data in body
     */
    @GetMapping("/map/get-all-data")
    @Timed
    public ResponseEntity<List<MapData>> getAllData() {
        log.debug("REST request to get a page of Events");
        Page<RawDataDTO> allRawData = this.rawDataService.findAll(Pageable.unpaged());
        List<MapData> rawData = allRawData.getContent().stream().map((RawDataDTO dto) -> {
            MapData data = new MapData();
            data.setId(dto.getId());
            data.setLabel(dto.getRawDataName());
            data.setDescription(dto.getRawDataContent());
            data.setObjectType("RawData");

            String[] doubleAsText = dto.getRawDataCoordinates().split(",");
            List<Double> results = new ArrayList<>();
            for (String textValue : doubleAsText) {
                results.add(Double.parseDouble(textValue));
            }
            data.setCoordinate(results);
            return data;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(rawData, new HttpHeaders(), HttpStatus.OK);
    }
}
