package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.service.InsightElasticService;
import com.peploleum.insight.service.util.InsightUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
