package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.ToolService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.ToolDTO;
import com.peploleum.insight.service.dto.ToolCriteria;
import com.peploleum.insight.service.ToolQueryService;
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
 * REST controller for managing Tool.
 */
@RestController
@RequestMapping("/api")
public class ToolResource {

    private final Logger log = LoggerFactory.getLogger(ToolResource.class);

    private static final String ENTITY_NAME = "tool";

    private final ToolService toolService;

    private final ToolQueryService toolQueryService;

    public ToolResource(ToolService toolService, ToolQueryService toolQueryService) {
        this.toolService = toolService;
        this.toolQueryService = toolQueryService;
    }

    /**
     * POST  /tools : Create a new tool.
     *
     * @param toolDTO the toolDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new toolDTO, or with status 400 (Bad Request) if the tool has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tools")
    @Timed
    public ResponseEntity<ToolDTO> createTool(@RequestBody ToolDTO toolDTO) throws URISyntaxException {
        log.debug("REST request to save Tool : {}", toolDTO);
        if (toolDTO.getId() != null) {
            throw new BadRequestAlertException("A new tool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolDTO result = toolService.save(toolDTO);
        return ResponseEntity.created(new URI("/api/tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tools : Updates an existing tool.
     *
     * @param toolDTO the toolDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated toolDTO,
     * or with status 400 (Bad Request) if the toolDTO is not valid,
     * or with status 500 (Internal Server Error) if the toolDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tools")
    @Timed
    public ResponseEntity<ToolDTO> updateTool(@RequestBody ToolDTO toolDTO) throws URISyntaxException {
        log.debug("REST request to update Tool : {}", toolDTO);
        if (toolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToolDTO result = toolService.save(toolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, toolDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tools : get all the tools.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tools in body
     */
    @GetMapping("/tools")
    @Timed
    public ResponseEntity<List<ToolDTO>> getAllTools(ToolCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tools by criteria: {}", criteria);
        Page<ToolDTO> page = toolQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tools/:id : get the "id" tool.
     *
     * @param id the id of the toolDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the toolDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tools/{id}")
    @Timed
    public ResponseEntity<ToolDTO> getTool(@PathVariable Long id) {
        log.debug("REST request to get Tool : {}", id);
        Optional<ToolDTO> toolDTO = toolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toolDTO);
    }

    /**
     * DELETE  /tools/:id : delete the "id" tool.
     *
     * @param id the id of the toolDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tools/{id}")
    @Timed
    public ResponseEntity<Void> deleteTool(@PathVariable Long id) {
        log.debug("REST request to delete Tool : {}", id);
        toolService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tools?query=:query : search for the tool corresponding
     * to the query.
     *
     * @param query the query of the tool search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tools")
    @Timed
    public ResponseEntity<List<ToolDTO>> searchTools(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tools for query {}", query);
        Page<ToolDTO> page = toolService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tools");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
