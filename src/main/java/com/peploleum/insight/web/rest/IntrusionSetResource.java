package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.IntrusionSetService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.IntrusionSetDTO;
import com.peploleum.insight.service.dto.IntrusionSetCriteria;
import com.peploleum.insight.service.IntrusionSetQueryService;
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
 * REST controller for managing IntrusionSet.
 */
@RestController
@RequestMapping("/api")
public class IntrusionSetResource {

    private final Logger log = LoggerFactory.getLogger(IntrusionSetResource.class);

    private static final String ENTITY_NAME = "intrusionSet";

    private final IntrusionSetService intrusionSetService;

    private final IntrusionSetQueryService intrusionSetQueryService;

    public IntrusionSetResource(IntrusionSetService intrusionSetService, IntrusionSetQueryService intrusionSetQueryService) {
        this.intrusionSetService = intrusionSetService;
        this.intrusionSetQueryService = intrusionSetQueryService;
    }

    /**
     * POST  /intrusion-sets : Create a new intrusionSet.
     *
     * @param intrusionSetDTO the intrusionSetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new intrusionSetDTO, or with status 400 (Bad Request) if the intrusionSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/intrusion-sets")
    @Timed
    public ResponseEntity<IntrusionSetDTO> createIntrusionSet(@RequestBody IntrusionSetDTO intrusionSetDTO) throws URISyntaxException {
        log.debug("REST request to save IntrusionSet : {}", intrusionSetDTO);
        if (intrusionSetDTO.getId() != null) {
            throw new BadRequestAlertException("A new intrusionSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IntrusionSetDTO result = intrusionSetService.save(intrusionSetDTO);
        return ResponseEntity.created(new URI("/api/intrusion-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /intrusion-sets : Updates an existing intrusionSet.
     *
     * @param intrusionSetDTO the intrusionSetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated intrusionSetDTO,
     * or with status 400 (Bad Request) if the intrusionSetDTO is not valid,
     * or with status 500 (Internal Server Error) if the intrusionSetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/intrusion-sets")
    @Timed
    public ResponseEntity<IntrusionSetDTO> updateIntrusionSet(@RequestBody IntrusionSetDTO intrusionSetDTO) throws URISyntaxException {
        log.debug("REST request to update IntrusionSet : {}", intrusionSetDTO);
        if (intrusionSetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IntrusionSetDTO result = intrusionSetService.save(intrusionSetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, intrusionSetDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /intrusion-sets : get all the intrusionSets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of intrusionSets in body
     */
    @GetMapping("/intrusion-sets")
    @Timed
    public ResponseEntity<List<IntrusionSetDTO>> getAllIntrusionSets(IntrusionSetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IntrusionSets by criteria: {}", criteria);
        Page<IntrusionSetDTO> page = intrusionSetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/intrusion-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /intrusion-sets/:id : get the "id" intrusionSet.
     *
     * @param id the id of the intrusionSetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the intrusionSetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/intrusion-sets/{id}")
    @Timed
    public ResponseEntity<IntrusionSetDTO> getIntrusionSet(@PathVariable Long id) {
        log.debug("REST request to get IntrusionSet : {}", id);
        Optional<IntrusionSetDTO> intrusionSetDTO = intrusionSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(intrusionSetDTO);
    }

    /**
     * DELETE  /intrusion-sets/:id : delete the "id" intrusionSet.
     *
     * @param id the id of the intrusionSetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/intrusion-sets/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntrusionSet(@PathVariable Long id) {
        log.debug("REST request to delete IntrusionSet : {}", id);
        intrusionSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/intrusion-sets?query=:query : search for the intrusionSet corresponding
     * to the query.
     *
     * @param query the query of the intrusionSet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/intrusion-sets")
    @Timed
    public ResponseEntity<List<IntrusionSetDTO>> searchIntrusionSets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IntrusionSets for query {}", query);
        Page<IntrusionSetDTO> page = intrusionSetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/intrusion-sets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
