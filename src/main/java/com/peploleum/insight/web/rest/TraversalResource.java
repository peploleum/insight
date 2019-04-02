package com.peploleum.insight.web.rest;

import com.peploleum.insight.service.TraversalService;
import com.peploleum.insight.service.dto.NodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@RestController
@RequestMapping("/api/graph")
public class TraversalResource {
    private final Logger log = LoggerFactory.getLogger(TraversalResource.class);
    private final TraversalService traversalService;

    public TraversalResource(TraversalService traversalService) {
        this.traversalService = traversalService;
    }

    @PostMapping("/traversal")
    public ResponseEntity<List<NodeDTO>> traverse(@Valid @RequestBody NodeDTO sourceNode) throws URISyntaxException {
        log.debug("REST request to get neighbors for : {}", sourceNode);
        final List<NodeDTO> neighbors = this.traversalService.getNeighbors(sourceNode);
        return ResponseEntity.created(new URI("/api/graph/traversal/"))
            .body(neighbors);
    }

    @GetMapping("/traversal/{id}")
    public ResponseEntity<List<NodeDTO>> getNeighbors(@PathVariable String id) throws URISyntaxException {
        final NodeDTO sourceNode = new NodeDTO();
        sourceNode.setId(id);
        sourceNode.setLabel("source");
        sourceNode.setType("source");
        log.debug("REST request to get neighbors for : {}", sourceNode);
        final List<NodeDTO> neighbors = this.traversalService.getNeighbors(sourceNode);
        return ResponseEntity.created(new URI("/api/traversal/"))
            .body(neighbors);
    }

    @GetMapping("/traversal/mock/{id}")
    public ResponseEntity<List<NodeDTO>> getNeighborsMock(@PathVariable String id) throws URISyntaxException {
        log.debug("REST request to get neighbors for : {}", id);
        final List<NodeDTO> neighbors = this.traversalService.getNeighborsMock(id);

        return ResponseEntity.created(new URI("/api/graph/traversal/"))
            .body(neighbors);
    }

    @GetMapping("/traversal/properties/{id}")
    public ResponseEntity<NodeDTO> getByMongoId(@PathVariable String id) throws URISyntaxException {
        log.info("REST request to get properties for : {}", id);
        final NodeDTO properties = this.traversalService.getByMongoId(id);
        return ResponseEntity.created(new URI("/api/graph/traversal/properties"))
            .body(properties);
    }

    @GetMapping("/traversal/janus/{id}")
    public ResponseEntity<NodeDTO> getByJanusId(@PathVariable String id) throws URISyntaxException {
        log.info("REST request to get properties for : {}", id);
        final NodeDTO properties = this.traversalService.getByJanusId(id);
        return ResponseEntity.created(new URI("/api/graph/traversal/janus"))
            .body(properties);
    }

    @GetMapping("/traversal/mock/properties/{id}")
    public ResponseEntity<NodeDTO> getPropertiesMock(@PathVariable String id) throws URISyntaxException {
        log.debug("REST request to get properties for : {}", id);
        final NodeDTO properties = new NodeDTO();
        properties.setId(id);
        properties.setLabel("label");
        properties.setType("RawData");
        return ResponseEntity.created(new URI("/api/graph/traversal/mock/properties"))
            .body(properties);
    }
}
