package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to generate data.
 */
@RestController
@RequestMapping("/api")
public class GeneratorResource {

    private final Logger log = LoggerFactory.getLogger(GeneratorResource.class);

    private final GeneratorService generatorService;

    public GeneratorResource(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    /**
     * GET  /bulk
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/generator/bulk")
    @Timed
    public ResponseEntity<Void> bulk() {
        log.debug("REST request to bulk : {}");
        this.generatorService.feed();
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /clean
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/generator/clean")
    @Timed
    public ResponseEntity<Void> clean() {
        log.debug("REST request to clean : {}");
        this.generatorService.clean();
        return ResponseEntity.ok().build();
    }
}
