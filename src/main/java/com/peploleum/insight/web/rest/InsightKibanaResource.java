package com.peploleum.insight.web.rest;

import com.peploleum.insight.domain.kibana.EntityMappingInfo;
import com.peploleum.insight.domain.kibana.KibanaDashboardGenerationParameters;
import com.peploleum.insight.service.InsightKibanaService;
import com.peploleum.insight.service.dto.KibanaObjectReferenceDTO;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@RequestMapping("/api/insight-kibana")
@Profile("kibana")
public class InsightKibanaResource {

    private final Logger log = LoggerFactory.getLogger(InsightKibanaResource.class);

    @Autowired
    private InsightKibanaService insKibanaService;

    /**
     * POST  /KibanaDashboardGenerationParameters : Parametres pour la generation d'un dashboard.
     *
     * @param dashboardParameters the KibanaDashboardGenerationParameters to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dashboardId, or with status 400 (Bad Request) if the creation failed
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/post-dashboard")
    public ResponseEntity<List<KibanaObjectReferenceDTO>> createDashboard(@RequestBody KibanaDashboardGenerationParameters dashboardParameters) throws URISyntaxException {
        log.debug("REST request to generate KibanaDashboardGenerationParameters : {}", dashboardParameters);
        this.insKibanaService.generateAndPostKibanaDashboard(dashboardParameters);
        List<KibanaObjectReferenceDTO> dashboardRefs = this.insKibanaService.getDashboardRef();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createDashboardCreationAlert(dashboardParameters.getDashboardTitle()))
            .body(dashboardRefs);
    }

    /**
     * GET synchronizeElastic
     */
    @GetMapping("/synchronize-elastic")
    public String synchronizeElastic() {
        return "synchronizeElastic";
    }

    /**
     * GET getDashboardRef
     */
    @GetMapping("/get-dashboard-ids")
    public ResponseEntity<List<KibanaObjectReferenceDTO>> getDashboardRef() {
        log.debug("REST getDashboardRef request");
        return ResponseEntity.ok()
            .headers(new HttpHeaders())
            .body(this.insKibanaService.getDashboardRef());
    }

    /**
     * GET getEntitiesSchema
     */
    @GetMapping("/get-entities-schema")
    public ResponseEntity<Set<EntityMappingInfo>> getEntitiesSchema() {
        log.debug("REST request to get EntitiesSchema");
        return ResponseEntity.ok()
            .headers(new HttpHeaders())
            .body(this.insKibanaService.getEntitiesMappingInfo());
    }

    /**
     * DELETE deleteAllDashboard
     */
    @DeleteMapping("/delete-all-dashboard")
    public ResponseEntity<Void> deleteAllDashboard() {
        this.insKibanaService.deleteAllDashboard();
        return ResponseEntity.ok().headers(new HttpHeaders()).build();
    }

    /**
     * DELETE  /delete-single-kibana-object/:id : delete object by "id".
     *
     * @param id the id of the object (dashboard or visualisation) to delete
     */
    @DeleteMapping("/delete-single-kibana-object/{id}")
    public ResponseEntity<Void> deleteSingleKibanaObject(@PathVariable String id) {
        this.insKibanaService.deleteSingleKibanaObject(id);
        return ResponseEntity.ok().headers(new HttpHeaders()).build();
    }

}
