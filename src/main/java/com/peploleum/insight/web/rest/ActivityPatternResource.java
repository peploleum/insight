package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.ActivityPatternService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.ActivityPatternDTO;
import com.peploleum.insight.service.dto.ActivityPatternCriteria;
import com.peploleum.insight.service.ActivityPatternQueryService;
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
 * REST controller for managing ActivityPattern.
 */
@RestController
@RequestMapping("/api")
public class ActivityPatternResource {

    private final Logger log = LoggerFactory.getLogger(ActivityPatternResource.class);

    private static final String ENTITY_NAME = "activityPattern";

    private final ActivityPatternService activityPatternService;

    private final ActivityPatternQueryService activityPatternQueryService;

    public ActivityPatternResource(ActivityPatternService activityPatternService, ActivityPatternQueryService activityPatternQueryService) {
        this.activityPatternService = activityPatternService;
        this.activityPatternQueryService = activityPatternQueryService;
    }

    /**
     * POST  /activity-patterns : Create a new activityPattern.
     *
     * @param activityPatternDTO the activityPatternDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityPatternDTO, or with status 400 (Bad Request) if the activityPattern has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-patterns")
    @Timed
    public ResponseEntity<ActivityPatternDTO> createActivityPattern(@RequestBody ActivityPatternDTO activityPatternDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityPattern : {}", activityPatternDTO);
        if (activityPatternDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityPattern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityPatternDTO result = activityPatternService.save(activityPatternDTO);
        return ResponseEntity.created(new URI("/api/activity-patterns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-patterns : Updates an existing activityPattern.
     *
     * @param activityPatternDTO the activityPatternDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityPatternDTO,
     * or with status 400 (Bad Request) if the activityPatternDTO is not valid,
     * or with status 500 (Internal Server Error) if the activityPatternDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-patterns")
    @Timed
    public ResponseEntity<ActivityPatternDTO> updateActivityPattern(@RequestBody ActivityPatternDTO activityPatternDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityPattern : {}", activityPatternDTO);
        if (activityPatternDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityPatternDTO result = activityPatternService.save(activityPatternDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityPatternDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-patterns : get all the activityPatterns.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of activityPatterns in body
     */
    @GetMapping("/activity-patterns")
    @Timed
    public ResponseEntity<List<ActivityPatternDTO>> getAllActivityPatterns(ActivityPatternCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ActivityPatterns by criteria: {}", criteria);
        Page<ActivityPatternDTO> page = activityPatternQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-patterns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activity-patterns/:id : get the "id" activityPattern.
     *
     * @param id the id of the activityPatternDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityPatternDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activity-patterns/{id}")
    @Timed
    public ResponseEntity<ActivityPatternDTO> getActivityPattern(@PathVariable Long id) {
        log.debug("REST request to get ActivityPattern : {}", id);
        Optional<ActivityPatternDTO> activityPatternDTO = activityPatternService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityPatternDTO);
    }

    /**
     * DELETE  /activity-patterns/:id : delete the "id" activityPattern.
     *
     * @param id the id of the activityPatternDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-patterns/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityPattern(@PathVariable Long id) {
        log.debug("REST request to delete ActivityPattern : {}", id);
        activityPatternService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/activity-patterns?query=:query : search for the activityPattern corresponding
     * to the query.
     *
     * @param query the query of the activityPattern search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/activity-patterns")
    @Timed
    public ResponseEntity<List<ActivityPatternDTO>> searchActivityPatterns(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ActivityPatterns for query {}", query);
        Page<ActivityPatternDTO> page = activityPatternService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/activity-patterns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
