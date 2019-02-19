package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.RawDataService;
import com.peploleum.insight.service.dto.RawDataDTO;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing RawData.
 */
@RestController
@RequestMapping("/api")
public class RawDataResource {

    private final Logger log = LoggerFactory.getLogger(RawDataResource.class);

    private static final String ENTITY_NAME = "rawData";

    private final RawDataService rawDataService;

    public RawDataResource(RawDataService rawDataService) {
        this.rawDataService = rawDataService;
    }

    /**
     * POST  /raw-data : Create a new rawData.
     *
     * @param rawDataDTO the rawDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rawDataDTO, or with status 400 (Bad Request) if the rawData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/raw-data")
    @Timed
    public ResponseEntity<RawDataDTO> createRawData(@Valid @RequestBody RawDataDTO rawDataDTO) throws URISyntaxException {
        log.debug("REST request to save RawData : {}", rawDataDTO);
        if (rawDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new rawData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RawDataDTO result = rawDataService.save(rawDataDTO);
        return ResponseEntity.created(new URI("/api/raw-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /raw-data : Updates an existing rawData.
     *
     * @param rawDataDTO the rawDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rawDataDTO,
     * or with status 400 (Bad Request) if the rawDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the rawDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/raw-data")
    @Timed
    public ResponseEntity<RawDataDTO> updateRawData(@Valid @RequestBody RawDataDTO rawDataDTO) throws URISyntaxException {
        log.debug("REST request to update RawData : {}", rawDataDTO);
        if (rawDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RawDataDTO result = rawDataService.save(rawDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rawDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /raw-data : get all the rawData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rawData in body
     */
    @GetMapping("/raw-data")
    @Timed
    public ResponseEntity<List<RawDataDTO>> getAllRawData(Pageable pageable) {
        log.debug("REST request to get a page of RawData");
        Page<RawDataDTO> page = rawDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/raw-data");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /raw-data/:id : get the "id" rawData.
     *
     * @param id the id of the rawDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rawDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/raw-data/{id}")
    @Timed
    public ResponseEntity<RawDataDTO> getRawData(@PathVariable String id) {
        log.debug("REST request to get RawData : {}", id);
        Optional<RawDataDTO> rawDataDTO = rawDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rawDataDTO);
    }

    /**
     * DELETE  /raw-data/:id : delete the "id" rawData.
     *
     * @param id the id of the rawDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/raw-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteRawData(@PathVariable String id) {
        log.debug("REST request to delete RawData : {}", id);
        rawDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/raw-data?query=:query : search for the rawData corresponding
     * to the query.
     *
     * @param query    the query of the rawData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/raw-data")
    @Timed
    public ResponseEntity<List<RawDataDTO>> searchRawData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RawData for query {}", query);
        Page<RawDataDTO> page = rawDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/raw-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /raw-data/filter : get all the rawData with specific business filters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rawData in body
     */
    @GetMapping("/raw-data/filter")
    @Timed
    public ResponseEntity<List<RawDataDTO>> getAllRawDataByCustomCriteria(@RequestParam String query, @RequestParam String filter, Pageable pageable) {
        log.debug("REST request to get a page of RawData with filters");

        final QueryBuilder builder = query != null && !query.isEmpty() ? queryStringQuery(query) : QueryBuilders.matchAllQuery();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(builder);
        switch (filter) {
            case "all":
                BoolQueryBuilder filterAll = QueryBuilders.boolQuery()
                    .must(QueryBuilders.existsQuery("rawDataData"))
                    .must(QueryBuilders.existsQuery("rawDataCoordinates"));
                searchQueryBuilder.withFilter(filterAll);
                break;
            case "locations":
                BoolQueryBuilder filterLoc = QueryBuilders.boolQuery()
                    .must(QueryBuilders.existsQuery("rawDataCoordinates"));
                searchQueryBuilder.withFilter(filterLoc);
                break;
            case "images":
                BoolQueryBuilder filterImg = QueryBuilders.boolQuery()
                    .must(QueryBuilders.existsQuery("rawDataData"));
                searchQueryBuilder.withFilter(filterImg);
                break;
            default:
                break;
        }

        searchQueryBuilder.withSort(SortBuilders.fieldSort("rawDataCreationDate").order(SortOrder.DESC));
        NativeSearchQuery esQuery = searchQueryBuilder.withPageable(pageable).build();
        final Page<RawDataDTO> page = rawDataService.search(esQuery);

        // final Query mongoQuery = new Query();
        // switch (filter) {
        //     case "all":
        //         mongoQuery.addCriteria(Criteria.where("rawDataData").exists(true));
        //         mongoQuery.addCriteria(Criteria.where("rawDataCoordinates").exists(true));
        //         break;
        //     case "locations":
        //         mongoQuery.addCriteria(Criteria.where("rawDataCoordinates").exists(true));
        //         break;
        //     case "images":
        //         mongoQuery.addCriteria(Criteria.where("rawDataData").exists(true));
        //         break;
        //     default:
        //         break;
        // }
        // mongoQuery.with(new Sort(Sort.Direction.DESC, "rawDataCreationDate"));
        // final Page<RawDataDTO> page = rawDataService.searchByCriteria(mongoQuery, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/raw-data/filter");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
