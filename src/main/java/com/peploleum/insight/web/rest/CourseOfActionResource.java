package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.CourseOfActionService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.CourseOfActionDTO;
import com.peploleum.insight.service.dto.CourseOfActionCriteria;
import com.peploleum.insight.service.CourseOfActionQueryService;
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
 * REST controller for managing CourseOfAction.
 */
@RestController
@RequestMapping("/api")
public class CourseOfActionResource {

    private final Logger log = LoggerFactory.getLogger(CourseOfActionResource.class);

    private static final String ENTITY_NAME = "courseOfAction";

    private final CourseOfActionService courseOfActionService;

    private final CourseOfActionQueryService courseOfActionQueryService;

    public CourseOfActionResource(CourseOfActionService courseOfActionService, CourseOfActionQueryService courseOfActionQueryService) {
        this.courseOfActionService = courseOfActionService;
        this.courseOfActionQueryService = courseOfActionQueryService;
    }

    /**
     * POST  /course-of-actions : Create a new courseOfAction.
     *
     * @param courseOfActionDTO the courseOfActionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseOfActionDTO, or with status 400 (Bad Request) if the courseOfAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-of-actions")
    @Timed
    public ResponseEntity<CourseOfActionDTO> createCourseOfAction(@RequestBody CourseOfActionDTO courseOfActionDTO) throws URISyntaxException {
        log.debug("REST request to save CourseOfAction : {}", courseOfActionDTO);
        if (courseOfActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseOfAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseOfActionDTO result = courseOfActionService.save(courseOfActionDTO);
        return ResponseEntity.created(new URI("/api/course-of-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-of-actions : Updates an existing courseOfAction.
     *
     * @param courseOfActionDTO the courseOfActionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseOfActionDTO,
     * or with status 400 (Bad Request) if the courseOfActionDTO is not valid,
     * or with status 500 (Internal Server Error) if the courseOfActionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-of-actions")
    @Timed
    public ResponseEntity<CourseOfActionDTO> updateCourseOfAction(@RequestBody CourseOfActionDTO courseOfActionDTO) throws URISyntaxException {
        log.debug("REST request to update CourseOfAction : {}", courseOfActionDTO);
        if (courseOfActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseOfActionDTO result = courseOfActionService.save(courseOfActionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseOfActionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-of-actions : get all the courseOfActions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of courseOfActions in body
     */
    @GetMapping("/course-of-actions")
    @Timed
    public ResponseEntity<List<CourseOfActionDTO>> getAllCourseOfActions(CourseOfActionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CourseOfActions by criteria: {}", criteria);
        Page<CourseOfActionDTO> page = courseOfActionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-of-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-of-actions/:id : get the "id" courseOfAction.
     *
     * @param id the id of the courseOfActionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseOfActionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/course-of-actions/{id}")
    @Timed
    public ResponseEntity<CourseOfActionDTO> getCourseOfAction(@PathVariable Long id) {
        log.debug("REST request to get CourseOfAction : {}", id);
        Optional<CourseOfActionDTO> courseOfActionDTO = courseOfActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseOfActionDTO);
    }

    /**
     * DELETE  /course-of-actions/:id : delete the "id" courseOfAction.
     *
     * @param id the id of the courseOfActionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-of-actions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseOfAction(@PathVariable Long id) {
        log.debug("REST request to delete CourseOfAction : {}", id);
        courseOfActionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/course-of-actions?query=:query : search for the courseOfAction corresponding
     * to the query.
     *
     * @param query the query of the courseOfAction search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/course-of-actions")
    @Timed
    public ResponseEntity<List<CourseOfActionDTO>> searchCourseOfActions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CourseOfActions for query {}", query);
        Page<CourseOfActionDTO> page = courseOfActionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/course-of-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
