package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.NetLinkService;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import com.peploleum.insight.service.dto.NetLinkDTO;
import com.peploleum.insight.service.dto.NetLinkCriteria;
import com.peploleum.insight.service.NetLinkQueryService;
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
 * REST controller for managing NetLink.
 */
@RestController
@RequestMapping("/api")
public class NetLinkResource {

    private final Logger log = LoggerFactory.getLogger(NetLinkResource.class);

    private static final String ENTITY_NAME = "netLink";

    private final NetLinkService netLinkService;

    private final NetLinkQueryService netLinkQueryService;

    public NetLinkResource(NetLinkService netLinkService, NetLinkQueryService netLinkQueryService) {
        this.netLinkService = netLinkService;
        this.netLinkQueryService = netLinkQueryService;
    }

    /**
     * POST  /net-links : Create a new netLink.
     *
     * @param netLinkDTO the netLinkDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new netLinkDTO, or with status 400 (Bad Request) if the netLink has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/net-links")
    @Timed
    public ResponseEntity<NetLinkDTO> createNetLink(@RequestBody NetLinkDTO netLinkDTO) throws URISyntaxException {
        log.debug("REST request to save NetLink : {}", netLinkDTO);
        if (netLinkDTO.getId() != null) {
            throw new BadRequestAlertException("A new netLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NetLinkDTO result = netLinkService.save(netLinkDTO);
        return ResponseEntity.created(new URI("/api/net-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /net-links : Updates an existing netLink.
     *
     * @param netLinkDTO the netLinkDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated netLinkDTO,
     * or with status 400 (Bad Request) if the netLinkDTO is not valid,
     * or with status 500 (Internal Server Error) if the netLinkDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/net-links")
    @Timed
    public ResponseEntity<NetLinkDTO> updateNetLink(@RequestBody NetLinkDTO netLinkDTO) throws URISyntaxException {
        log.debug("REST request to update NetLink : {}", netLinkDTO);
        if (netLinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NetLinkDTO result = netLinkService.save(netLinkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, netLinkDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /net-links : get all the netLinks.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of netLinks in body
     */
    @GetMapping("/net-links")
    @Timed
    public ResponseEntity<List<NetLinkDTO>> getAllNetLinks(NetLinkCriteria criteria, Pageable pageable) {
        log.debug("REST request to get NetLinks by criteria: {}", criteria);
        Page<NetLinkDTO> page = netLinkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/net-links");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /net-links/:id : get the "id" netLink.
     *
     * @param id the id of the netLinkDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the netLinkDTO, or with status 404 (Not Found)
     */
    @GetMapping("/net-links/{id}")
    @Timed
    public ResponseEntity<NetLinkDTO> getNetLink(@PathVariable Long id) {
        log.debug("REST request to get NetLink : {}", id);
        Optional<NetLinkDTO> netLinkDTO = netLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(netLinkDTO);
    }

    /**
     * DELETE  /net-links/:id : delete the "id" netLink.
     *
     * @param id the id of the netLinkDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/net-links/{id}")
    @Timed
    public ResponseEntity<Void> deleteNetLink(@PathVariable Long id) {
        log.debug("REST request to delete NetLink : {}", id);
        netLinkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/net-links?query=:query : search for the netLink corresponding
     * to the query.
     *
     * @param query the query of the netLink search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/net-links")
    @Timed
    public ResponseEntity<List<NetLinkDTO>> searchNetLinks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NetLinks for query {}", query);
        Page<NetLinkDTO> page = netLinkService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/net-links");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
