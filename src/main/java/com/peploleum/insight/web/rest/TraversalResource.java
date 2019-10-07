package com.peploleum.insight.web.rest;

import com.peploleum.insight.service.TraversalService;
import com.peploleum.insight.service.dto.GraphStructureNodeDTO;
import com.peploleum.insight.service.dto.NodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@RestController
@RequestMapping("/api/graph")
@Profile("graphy")
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

    @GetMapping("/traversal/getGraph/{id}")
    public ResponseEntity<GraphStructureNodeDTO> getGraph(@PathVariable String id, @RequestParam int levelOrder) throws URISyntaxException {
        final NodeDTO sourceNode = this.traversalService.getByJanusId(id);
        log.debug("REST request to get graph for : {}", sourceNode);
        final GraphStructureNodeDTO graph = this.traversalService.getGraph(sourceNode, levelOrder);
        return ResponseEntity.created(new URI("/api/traversal/getGraph/"))
            .body(graph);
    }

    @PostMapping("/traversal/multiple")
    public ResponseEntity<List<NodeDTO>> getNeighborsForIds(@RequestBody List<String> ids) throws URISyntaxException {
        log.debug("REST request to get neighbors for " + ids.size() + " ids");
        Set<NodeDTO> setNeighbors = new HashSet<>();
        for (String id : ids) {
            final NodeDTO sourceNode = new NodeDTO();
            sourceNode.setId(id);
            sourceNode.setLabel("source");
            sourceNode.setType("source");
            setNeighbors.addAll(this.traversalService.getNeighbors(sourceNode));
        }
        return ResponseEntity.created(new URI("/api/traversal/"))
            .body(setNeighbors.stream().collect(Collectors.toList()));
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

    @GetMapping("/traversal/status/{id}")
    public ResponseEntity<Map<String, Integer>> getProcessStatusForId(@PathVariable String id) throws URISyntaxException {
        log.info("REST request to get properties for : {}", id);
        final Map<String, String> propRequests = new HashMap<>();
        propRequests.put("rawDataSubType", ".has('rawDataSubType', 'url')");
        propRequests.put("imageHit", ".has('imageHit', neq('0'))");
        Map<String, Integer> status = this.traversalService.countProperties(id, propRequests);
        return ResponseEntity.created(new URI("/api/graph/traversal/janus"))
            .body(status);
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

    @GetMapping("/traversal/vertices/{id}")
    public ResponseEntity<List<NodeDTO>> getAllUnlinkedVertices(@PathVariable String id) throws URISyntaxException {
        log.info("REST request to get properties for : {}", id);
        Set<NodeDTO> setUnlinkedVertices = new HashSet<>();
        setUnlinkedVertices.addAll(this.traversalService.getAllUnlinkedVertices(id));
        return ResponseEntity.created(new URI("/api/traversal/vertices/{id}"))
            .body(setUnlinkedVertices.stream().collect(Collectors.toList()));
    }

    @GetMapping("/traversal/biographics/{id}/{prop}")
    public ResponseEntity<List<NodeDTO>> getNeighborsByProperty(@PathVariable String id, @PathVariable String prop) throws URISyntaxException {
        final NodeDTO sourceNode = new NodeDTO();
        sourceNode.setId(id);
        sourceNode.setLabel("source");
        sourceNode.setType("source");
        log.debug("REST request to get the rawdataURLs link to this biographics : {}", sourceNode);
        final List<NodeDTO> neighbors = this.traversalService.getNeighborsByProperty(sourceNode);
        return ResponseEntity.created(new URI("/api/traversal/biographics/{id}"))
            .body(neighbors);
    }
}
