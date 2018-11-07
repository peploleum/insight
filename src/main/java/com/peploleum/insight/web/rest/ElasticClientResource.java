package com.peploleum.insight.web.rest;

import com.peploleum.insight.domain.kibana.EntityMappingInfo;
import com.peploleum.insight.domain.kibana.KibanaDashboardGenerationParameters;
import com.peploleum.insight.service.impl.ElasticClientService;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

/**
 * ElasticClientResource controller
 */
@RestController
@RequestMapping("/api/elastic-client-resource")
public class ElasticClientResource {

    private final Logger log = LoggerFactory.getLogger(ElasticClientResource.class);

    @Autowired
    private ElasticClientService esClientService;

    /**
     * POST  /KibanaDashboardGenerationParameters : Parametres pour la generation d'un dashboard.
     *
     * @param dashboardParameters the KibanaDashboardGenerationParameters to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dashboardId, or with status 400 (Bad Request) if the creation failed
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/post-dashboard")
    public ResponseEntity<List<String>> createDashboard(@RequestBody KibanaDashboardGenerationParameters dashboardParameters) throws URISyntaxException {
        log.debug("REST request to generate KibanaDashboardGenerationParameters : {}", dashboardParameters);
        this.esClientService.generateAndPostKibanaDashboard(dashboardParameters);
        List<String> dashboardId = this.esClientService.getDashboardId();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createDashboardCreationAlert(dashboardParameters.getDashboardTitle()))
            .body(dashboardId);
    }

    /**
     * GET synchronizeElastic
     */
    @GetMapping("/synchronize-elastic")
    public String synchronizeElastic() {
        return "synchronizeElastic";
    }

    /**
     * GET getDashboardId
     */
    @GetMapping("/get-dashboard-ids")
    public ResponseEntity<List<String>> getDashboardId() {
        log.debug("REST getDashboardId request");
        return ResponseEntity.ok()
            .headers(new HttpHeaders())
            .body(this.esClientService.getDashboardId());
    }

    /**
     * GET getEntitiesSchema
     */
    @GetMapping("/get-entities-schema")
    public ResponseEntity<Set<EntityMappingInfo>> getEntitiesSchema() {
        log.debug("REST request to get EntitiesSchema");
        return ResponseEntity.ok()
            .headers(new HttpHeaders())
            .body(this.esClientService.getEntitiesMappingInfo());
    }

    /**
     * DELETE getEntitiesSchema
     */
    @DeleteMapping("/delete-all-dashboard")
    public ResponseEntity<Void> deleteAllDashboard() {
        this.esClientService.deleteAllDashboard();
        return ResponseEntity.ok().headers(new HttpHeaders()).build();
    }

}
