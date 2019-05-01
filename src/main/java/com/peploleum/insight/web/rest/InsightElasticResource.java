package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.domain.InsightEntity;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.service.InsightElasticService;
import com.peploleum.insight.service.util.InsightUtil;
import com.peploleum.insight.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GFOLGOAS on 04/04/2019.
 */
@RestController
@RequestMapping("/api/insight-elastic")
public class InsightElasticResource {
    private final Logger log = LoggerFactory.getLogger(InsightElasticResource.class);

    private InsightElasticService elasticService;

    public InsightElasticResource(InsightElasticService elasticService) {
        this.elasticService = elasticService;
    }

    /**
     * SEARCH  /_autocomplete/{searchEntity}?query=:query : get autoComplete suggestion matching the query
     *
     * @param searchEntity the target entity
     * @param query        the search keyword
     * @return the list of suggestion
     */
    @GetMapping("/_autocomplete/{searchEntity}")
    @Timed
    public ResponseEntity<List<String>> autoComplete(@PathVariable InsightEntityType searchEntity, @RequestParam String query) {
        log.debug("REST request to get suggestions base on search query {}", query);
        List<String> suggestions = this.elasticService.autoComplete(query, InsightUtil.getClassFromType(searchEntity));
        return new ResponseEntity<>(suggestions, HttpStatus.OK);
    }

    /**
     * SEARCH  /_search/{searchEntity}?query=:query : search entities matching the query
     *
     * @param searchEntity the target entity
     * @param query        the search keyword
     * @return the list of results
     */
    @GetMapping("/_search/{searchEntity}")
    @Timed
    public ResponseEntity<List<InsightEntity>> search(@PathVariable InsightEntityType searchEntity, @RequestParam String query, Pageable page) {
        log.debug("REST request to get InsightEntities base on search query {}", query);
        Page<InsightEntity> search = this.elasticService.search(query, InsightUtil.getClassFromType(searchEntity), page);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, search, "/api/insight-elastic/_search/" + searchEntity);
        return new ResponseEntity<>(search.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/_search/indices/")
    @Timed
    public ResponseEntity<List<InsightEntity>> search(@RequestParam String query, @RequestParam("indices") List<String> indices, Pageable page) {
        log.debug("REST request to get InsightEntities base on search query {}", query);
        log.debug("REST request to get InsightEntities base on search indices {}", indices.size());
        Page<InsightEntity> search = this.elasticService.search(query, indices, page);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, search, "/api/insight-elastic/_search/indices");
        return new ResponseEntity<>(search.getContent(), headers, HttpStatus.OK);
    }
}
