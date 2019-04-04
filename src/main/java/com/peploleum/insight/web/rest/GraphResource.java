package com.peploleum.insight.web.rest;

import com.peploleum.insight.service.InsightGraphRelationService;
import com.peploleum.insight.service.dto.InsightGraphRelationDTO;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

/**
 * Created by GFOLGOAS on 02/04/2019.
 */
@RestController
@RequestMapping("/api/graph")
@Profile("graphy")
public class GraphResource {
    private final Logger log = LoggerFactory.getLogger(GraphResource.class);
    private final InsightGraphRelationService insightGraphRelationService;

    public GraphResource(InsightGraphRelationService insightGraphRelationService) {
        this.insightGraphRelationService = insightGraphRelationService;
    }

    @PostMapping("/relation")
    public ResponseEntity<String> createRelation(@Valid @RequestBody InsightGraphRelationDTO relationDTO) throws URISyntaxException {
        log.debug("REST request to save InsightGraphRelation : {}", relationDTO);
        String result = this.insightGraphRelationService.save(Long.valueOf(relationDTO.getIdJanusSource()), Long.valueOf(relationDTO.getIdJanusCible()),
            relationDTO.getName());
        return ResponseEntity.created(new URI("/api/graph/relation/" + result))
            .headers(HeaderUtil.createEntityCreationAlert("InsightGraphRelation", result))
            .body(result);
    }

    @GetMapping("/relation/{id}")
    public @ResponseBody
    String getRelation(@PathVariable String id) throws URISyntaxException {
        log.debug("REST request to get Biographics : {}", id);
        LinkedHashMap relation = insightGraphRelationService.findOne(id);
        return relation.get(1).toString();
    }

    @GetMapping("/linkallvertices")
    public ResponseEntity<String> linkAllVertices() throws URISyntaxException {
        insightGraphRelationService.linkAll();
        return new ResponseEntity<>("All the vertices has been linked !", HttpStatus.OK);
    }

    @DeleteMapping("/relation/{id}")
    public ResponseEntity<Void> deleteRelation(@PathVariable String id) {
        log.debug("REST request to delete Biographics : {}", id);
        insightGraphRelationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("InsightGraphRelation", id)).build();
    }
}
