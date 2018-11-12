package com.peploleum.insight.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.ObservedDataQueryService;
import com.peploleum.insight.service.ObservedDataService;
import com.peploleum.insight.service.dto.ObservedDataCriteria;
import com.peploleum.insight.service.dto.ObservedDataDTO;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ObservedData.
 */
@RestController
@RequestMapping("/api")
public class ObservedDataResource {

    private final Logger log = LoggerFactory.getLogger(ObservedDataResource.class);

    private static final String ENTITY_NAME = "observedData";

    private final ObservedDataService observedDataService;

    private final ObservedDataQueryService observedDataQueryService;

    public ObservedDataResource(ObservedDataService observedDataService, ObservedDataQueryService observedDataQueryService) {
        this.observedDataService = observedDataService;
        this.observedDataQueryService = observedDataQueryService;
    }

    /**
     * POST  /observed-data : Create a new observedData.
     *
     * @param observedDataDTO the observedDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new observedDataDTO, or with status 400 (Bad Request) if the observedData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/observed-data")
    @Timed
    public ResponseEntity<ObservedDataDTO> createObservedData(@RequestBody ObservedDataDTO observedDataDTO) throws URISyntaxException {
        log.debug("REST request to save ObservedData : {}", observedDataDTO);
        if (observedDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new observedData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ObservedDataDTO result = observedDataService.save(observedDataDTO);
        return ResponseEntity.created(new URI("/api/observed-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /observed-data : Updates an existing observedData.
     *
     * @param observedDataDTO the observedDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated observedDataDTO,
     * or with status 400 (Bad Request) if the observedDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the observedDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/observed-data")
    @Timed
    public ResponseEntity<ObservedDataDTO> updateObservedData(@RequestBody ObservedDataDTO observedDataDTO) throws URISyntaxException {
        log.debug("REST request to update ObservedData : {}", observedDataDTO);
        if (observedDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ObservedDataDTO result = observedDataService.save(observedDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, observedDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /observed-data : get all the observedData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of observedData in body
     */
    @GetMapping("/observed-data")
    @Timed
    public ResponseEntity<List<ObservedDataDTO>> getAllObservedData(ObservedDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ObservedData by criteria: {}", criteria);
        Page<ObservedDataDTO> page = observedDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/observed-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /observed-data/:id : get the "id" observedData.
     *
     * @param id the id of the observedDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the observedDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/observed-data/{id}")
    @Timed
    public ResponseEntity<ObservedDataDTO> getObservedData(@PathVariable Long id) {
        log.debug("REST request to get ObservedData : {}", id);
        Optional<ObservedDataDTO> observedDataDTO = observedDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(observedDataDTO);
    }

    /**
     * DELETE  /observed-data/:id : delete the "id" observedData.
     *
     * @param id the id of the observedDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/observed-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteObservedData(@PathVariable Long id) {
        log.debug("REST request to delete ObservedData : {}", id);
        observedDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/observed-data?query=:query : search for the observedData corresponding
     * to the query.
     *
     * @param query the query of the observedData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/observed-data")
    @Timed
    public ResponseEntity<List<ObservedDataDTO>> searchObservedData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ObservedData for query {}", query);
        Page<ObservedDataDTO> page = observedDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/observed-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
