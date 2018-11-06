package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.AttackPatternService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.AttackPatternDTO;
import com.peploleum.insight.service.dto.AttackPatternCriteria;
import com.peploleum.insight.service.AttackPatternQueryService;
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
 * REST controller for managing AttackPattern.
 */
@RestController
@RequestMapping("/api")
public class AttackPatternResource {

    private final Logger log = LoggerFactory.getLogger(AttackPatternResource.class);

    private static final String ENTITY_NAME = "attackPattern";

    private final AttackPatternService attackPatternService;

    private final AttackPatternQueryService attackPatternQueryService;

    public AttackPatternResource(AttackPatternService attackPatternService, AttackPatternQueryService attackPatternQueryService) {
        this.attackPatternService = attackPatternService;
        this.attackPatternQueryService = attackPatternQueryService;
    }

    /**
     * POST  /attack-patterns : Create a new attackPattern.
     *
     * @param attackPatternDTO the attackPatternDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attackPatternDTO, or with status 400 (Bad Request) if the attackPattern has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attack-patterns")
    @Timed
    public ResponseEntity<AttackPatternDTO> createAttackPattern(@RequestBody AttackPatternDTO attackPatternDTO) throws URISyntaxException {
        log.debug("REST request to save AttackPattern : {}", attackPatternDTO);
        if (attackPatternDTO.getId() != null) {
            throw new BadRequestAlertException("A new attackPattern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttackPatternDTO result = attackPatternService.save(attackPatternDTO);
        return ResponseEntity.created(new URI("/api/attack-patterns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attack-patterns : Updates an existing attackPattern.
     *
     * @param attackPatternDTO the attackPatternDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attackPatternDTO,
     * or with status 400 (Bad Request) if the attackPatternDTO is not valid,
     * or with status 500 (Internal Server Error) if the attackPatternDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attack-patterns")
    @Timed
    public ResponseEntity<AttackPatternDTO> updateAttackPattern(@RequestBody AttackPatternDTO attackPatternDTO) throws URISyntaxException {
        log.debug("REST request to update AttackPattern : {}", attackPatternDTO);
        if (attackPatternDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttackPatternDTO result = attackPatternService.save(attackPatternDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attackPatternDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attack-patterns : get all the attackPatterns.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of attackPatterns in body
     */
    @GetMapping("/attack-patterns")
    @Timed
    public ResponseEntity<List<AttackPatternDTO>> getAllAttackPatterns(AttackPatternCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AttackPatterns by criteria: {}", criteria);
        Page<AttackPatternDTO> page = attackPatternQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attack-patterns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attack-patterns/:id : get the "id" attackPattern.
     *
     * @param id the id of the attackPatternDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attackPatternDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attack-patterns/{id}")
    @Timed
    public ResponseEntity<AttackPatternDTO> getAttackPattern(@PathVariable Long id) {
        log.debug("REST request to get AttackPattern : {}", id);
        Optional<AttackPatternDTO> attackPatternDTO = attackPatternService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attackPatternDTO);
    }

    /**
     * DELETE  /attack-patterns/:id : delete the "id" attackPattern.
     *
     * @param id the id of the attackPatternDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attack-patterns/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttackPattern(@PathVariable Long id) {
        log.debug("REST request to delete AttackPattern : {}", id);
        attackPatternService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attack-patterns?query=:query : search for the attackPattern corresponding
     * to the query.
     *
     * @param query the query of the attackPattern search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/attack-patterns")
    @Timed
    public ResponseEntity<List<AttackPatternDTO>> searchAttackPatterns(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AttackPatterns for query {}", query);
        Page<AttackPatternDTO> page = attackPatternService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/attack-patterns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
