package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.domain.EntitiesPositionRef;
import com.peploleum.insight.domain.InsightEntity;
import com.peploleum.insight.service.*;
import com.peploleum.insight.service.dto.*;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Created by gFolgoas on 27/02/2019.
 */
@RestController
@RequestMapping("/api")
public class QuickViewResource {
    private final Logger log = LoggerFactory.getLogger(QuickViewResource.class);
    private final QuickViewService quickViewService;

    private final LocationService locationService;
    private final RawDataService rawDataService;
    private final EventService eventService;
    private final EquipmentService equipmentService;
    private final BiographicsService biographicsService;
    private final OrganisationService organisationService;

    public QuickViewResource(QuickViewService quickViewService, LocationService locationService,
                             RawDataService rawDataService, EventService eventService,
                             EquipmentService equipmentService, BiographicsService biographicsService,
                             OrganisationService organisationService) {
        this.quickViewService = quickViewService;
        this.locationService = locationService;
        this.rawDataService = rawDataService;
        this.eventService = eventService;
        this.equipmentService = equipmentService;
        this.biographicsService = biographicsService;
        this.organisationService = organisationService;
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

    /**
     * POST  /entities : get entities by id.
     *
     * @param ids the ids of the entities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entity, or with status 404 (Not Found)
     */
    @PostMapping("/entities")
    @Timed
    public ResponseEntity<List<InsightEntity>> getEntitiesById(@RequestBody List<String> ids) throws URISyntaxException {
        log.debug("REST request to get entities by Id");
        List<InsightEntity> entitiesById = quickViewService.findEntitiesById(ids);
        return new ResponseEntity<>(entitiesById, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * PUT  /updateAnnotation?entityType=:entityType&entityName=:entityName&entityPosition=:entityPosition
     *
     * @param entityType     the type of the entity to create
     * @param entityName     the name of the entity to create
     * @param entityPosition the position in text of the annotation
     * @param rawDataDTO     the rawDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rawDataDTO,
     * or with status 400 (Bad Request) if the rawDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the rawDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/updateAnnotation")
    @Timed
    public ResponseEntity<RawDataDTO> updateAnnotation(@RequestParam String entityType, @RequestParam String entityName,
                                                       @RequestParam Integer entityPosition, @Valid @RequestBody RawDataDTO rawDataDTO) {
        log.debug("REST request to create entity from annotation, rawData id : {}", rawDataDTO.getId());
        if (rawDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "RawData", "idnull");
        }
        String newEntityMongoId = "";
        String newEntityJanusId = ""; // A faire
        switch (entityType) {
            case "Location":
                LocationDTO loc = new LocationDTO();
                loc.setLocationName(entityName);
                newEntityMongoId = this.locationService.save(loc).getId();
                break;
            case "Biographics":
                BiographicsDTO bio = new BiographicsDTO();
                bio.setBiographicsName(entityName);
                bio.setBiographicsFirstname(entityName);
                newEntityMongoId = this.biographicsService.save(bio).getId();
                break;
            case "Equipment":
                EquipmentDTO eq = new EquipmentDTO();
                eq.setEquipmentName(entityName);
                newEntityMongoId = this.equipmentService.save(eq).getId();
                break;
            case "Organisation":
                OrganisationDTO org = new OrganisationDTO();
                org.setOrganisationName(entityName);
                newEntityMongoId = this.organisationService.save(org).getId();
                break;
            case "Event":
                EventDTO ev = new EventDTO();
                ev.setEventName(entityName);
                newEntityMongoId = this.eventService.save(ev).getId();
                break;
            case "RawData":
                RawDataDTO raw = new RawDataDTO();
                raw.setRawDataName(entityName);
                newEntityMongoId = this.rawDataService.save(raw).getId();
                break;
            default:
                break;
        }
        if (newEntityMongoId != null && !newEntityMongoId.isEmpty()) {
            String annotations = rawDataDTO.getRawDataAnnotations();
            ObjectMapper mapper = new ObjectMapper();
            try {
                String updatedPosList;
                EntitiesPositionRef newPos = new EntitiesPositionRef(newEntityMongoId, newEntityJanusId, entityPosition, entityName, entityType);
                if (annotations != null && !annotations.isEmpty()) {
                    List<EntitiesPositionRef> posList = mapper.readValue(annotations, new TypeReference<List<EntitiesPositionRef>>() {
                    });
                    posList.add(newPos);
                    updatedPosList = mapper.writeValueAsString(posList);
                } else {
                    updatedPosList = "[" + mapper.writeValueAsString(newPos) + "]";
                }
                rawDataDTO.setRawDataAnnotations(updatedPosList);
            } catch (Exception e) {
                this.log.error("Error durant le parsing des RawDataAnnotations.", e);
            }
        }
        RawDataDTO result = rawDataService.save(rawDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("RawData", result.getId().toString()))
            .body(result);
    }

}
