package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.ThreatActorService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.ThreatActorDTO;
import com.peploleum.insight.service.dto.ThreatActorCriteria;
import com.peploleum.insight.service.ThreatActorQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ThreatActor.
 */
@RestController
@RequestMapping("/api")
public class ThreatActorResource {

    private final Logger log = LoggerFactory.getLogger(ThreatActorResource.class);

    private static final String ENTITY_NAME = "threatActor";

    private final ThreatActorService threatActorService;

    private final ThreatActorQueryService threatActorQueryService;

    public ThreatActorResource(ThreatActorService threatActorService, ThreatActorQueryService threatActorQueryService) {
        this.threatActorService = threatActorService;
        this.threatActorQueryService = threatActorQueryService;
    }

    /**
     * POST  /threat-actors : Create a new threatActor.
     *
     * @param threatActorDTO the threatActorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new threatActorDTO, or with status 400 (Bad Request) if the threatActor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/threat-actors")
    @Timed
    public ResponseEntity<ThreatActorDTO> createThreatActor(@RequestBody ThreatActorDTO threatActorDTO) throws URISyntaxException {
        log.debug("REST request to save ThreatActor : {}", threatActorDTO);
        if (threatActorDTO.getId() != null) {
            throw new BadRequestAlertException("A new threatActor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThreatActorDTO result = threatActorService.save(threatActorDTO);
        return ResponseEntity.created(new URI("/api/threat-actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /threat-actors : Updates an existing threatActor.
     *
     * @param threatActorDTO the threatActorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated threatActorDTO,
     * or with status 400 (Bad Request) if the threatActorDTO is not valid,
     * or with status 500 (Internal Server Error) if the threatActorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/threat-actors")
    @Timed
    public ResponseEntity<ThreatActorDTO> updateThreatActor(@RequestBody ThreatActorDTO threatActorDTO) throws URISyntaxException {
        log.debug("REST request to update ThreatActor : {}", threatActorDTO);
        if (threatActorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ThreatActorDTO result = threatActorService.save(threatActorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, threatActorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /threat-actors : get all the threatActors.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of threatActors in body
     */
    @GetMapping("/threat-actors")
    @Timed
    public ResponseEntity<List<ThreatActorDTO>> getAllThreatActors(ThreatActorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ThreatActors by criteria: {}", criteria);
        Page<ThreatActorDTO> page = threatActorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/threat-actors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /threat-actors/:id : get the "id" threatActor.
     *
     * @param id the id of the threatActorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the threatActorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/threat-actors/{id}")
    @Timed
    public ResponseEntity<ThreatActorDTO> getThreatActor(@PathVariable Long id) {
        log.debug("REST request to get ThreatActor : {}", id);
        Optional<ThreatActorDTO> threatActorDTO = threatActorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(threatActorDTO);
    }

    /**
     * DELETE  /threat-actors/:id : delete the "id" threatActor.
     *
     * @param id the id of the threatActorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/threat-actors/{id}")
    @Timed
    public ResponseEntity<Void> deleteThreatActor(@PathVariable Long id) {
        log.debug("REST request to delete ThreatActor : {}", id);
        threatActorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/threat-actors?query=:query : search for the threatActor corresponding
     * to the query.
     *
     * @param query the query of the threatActor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/threat-actors")
    @Timed
    public ResponseEntity<List<ThreatActorDTO>> searchThreatActors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ThreatActors for query {}", query);
        Page<ThreatActorDTO> page = threatActorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/threat-actors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
