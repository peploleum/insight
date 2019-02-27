package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.domain.InsightEntity;
import com.peploleum.insight.service.QuickViewService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by gFolgoas on 27/02/2019.
 */
@RestController
@RequestMapping("/api")
public class QuickViewResource {
    private final Logger log = LoggerFactory.getLogger(QuickViewResource.class);
    private final QuickViewService quickViewService;

    public QuickViewResource(QuickViewService quickViewService) {
        this.quickViewService = quickViewService;
    }

    /**
     * GET  /entity/:id : get the "id" entity.
     *
     * @param id the id of the entity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entity, or with status 404 (Not Found)
     */
    @GetMapping("/entity/{id}")
    @Timed
    public ResponseEntity<InsightEntity> getEntityById(@PathVariable String id) {
        log.debug("REST request to get entity : {}", id);
        Optional<InsightEntity> entity = quickViewService.findEntityById(id);
        return ResponseUtil.wrapOrNotFound(entity);
    }
}
