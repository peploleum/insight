package com.peploleum.insight.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.domain.kibana.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.EventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ElasticClientService {

    private final Logger log = LoggerFactory.getLogger(ElasticClientService.class);

    @Value("${application.kibana.uri}")
    private String KIBANA_ENTRY_URI;

    private Set<KibanaObject> kibanaObjects = new HashSet<>();
    private Set<EntityMappingInfo> entitiesMappingInfo = new HashSet<>();

    public ElasticClientService() {
    }

    public List<String> getDashboardId() {
        return this.kibanaObjects.stream().filter((ko) -> ko.getType().equals(KibanaObject.DASHBOARD)).map(ko -> ko.getId()).collect(Collectors.toList());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void generateAndSaveKibanaIndexPattern() {
        this.generateIndexPatterns();
        final String jsonObjects = this.getJsonObjects();
        if (!jsonObjects.isEmpty())
            this.postMessageToKibana(jsonObjects, KibanaMessageUri.POST_BULK_OBJECT);
    }

    /**
     * Fonction de test, genere un dashboard créer manuellement
     */
    public void generateAndPostKibanaDashboard() {
        if (this.kibanaObjects.stream().anyMatch(ko -> ko.getType().equals(KibanaObject.DASHBOARD)))
            return;

        KibanaObject defaultIndex = this.setDefaultKibanaIndexPattern();
        KibanaObject evenementIndex = this.kibanaObjects.stream().filter(ko -> ko.getType().equals(KibanaObject.INDEX_PATTERN) && ko.getAttributes().getTitle().equals("evenement")).findFirst().get();

        List<KibanaVisualisationGenerationParameters> visuParamList = new ArrayList<>();
        String ipTargetField = "language";
        final KibanaObject visuTable = this.generateVisualization(defaultIndex, ipTargetField, KibanaVisualisationType.VISU_TABLE, visuParamList);
        final KibanaObject visuPie = this.generateVisualization(defaultIndex, ipTargetField, KibanaVisualisationType.VISU_PIE, visuParamList);
        final KibanaObject visuBar = this.generateVisualization(defaultIndex, ipTargetField, KibanaVisualisationType.VISU_VERT_BAR, visuParamList);
        ipTargetField = "date";
        final KibanaObject visuTime = this.generateVisualization(evenementIndex, ipTargetField, KibanaVisualisationType.VISU_TIMELINE, visuParamList);
        ipTargetField = "coordonnee";
        final KibanaObject visuMap = this.generateVisualization(defaultIndex, ipTargetField, KibanaVisualisationType.VISU_MAP, visuParamList);

        final KibanaObject dash = this.generateDashboard(visuParamList, "Dashboard Test");
        if (dash == null) {
            return;
        }

        KibanaObjectsBundle objectBundle = new KibanaObjectsBundle();
        objectBundle.getObjects().add(visuTable);
        objectBundle.getObjects().add(visuPie);
        objectBundle.getObjects().add(visuBar);
        objectBundle.getObjects().add(visuTime);
        objectBundle.getObjects().add(visuMap);
        objectBundle.getObjects().add(defaultIndex);
        objectBundle.getObjects().add(evenementIndex);
        objectBundle.getObjects().add(dash);

        final String jsonBundle = this.getJsonDashboardBundleObjects(objectBundle);
        if (!jsonBundle.isEmpty())
            this.postMessageToKibana(jsonBundle, KibanaMessageUri.POST_DASHBOARD);
    }

    public void generateAndPostKibanaDashboard(final KibanaDashboardGenerationParameters dashboardParameters) {
        if (this.kibanaObjects.stream().anyMatch(ko -> ko.getType().equals(KibanaObject.DASHBOARD)))
            return;

        try {
            KibanaObjectsBundle objectBundle = new KibanaObjectsBundle();
            for (KibanaVisualisationGenerationParameters param : dashboardParameters.getVisualisations()) {
                final KibanaObject visualisation = param.getVisualisationFromParameters();
                KibanaObject index = this.kibanaObjects.stream().filter(ko -> ko.getId().equals(param.getIndexPatternId())).findFirst().get();
                objectBundle.getObjects().add(visualisation);
                objectBundle.getObjects().add(index);
                this.kibanaObjects.add(visualisation);
            }
            final KibanaObject dash = dashboardParameters.getDashboardFromParameters();
            objectBundle.getObjects().add(dash);
            this.kibanaObjects.add(dash);

            final String jsonBundle = this.getJsonDashboardBundleObjects(objectBundle);
            if (!jsonBundle.isEmpty())
                this.postMessageToKibana(jsonBundle, KibanaMessageUri.POST_DASHBOARD);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing de kibana_visualisation_model", e);
        }
    }


    /**
     * Generate les index pattern depuis les classes annotées @Document
     */
    private void generateIndexPatterns() {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Document.class));

        for (BeanDefinition bd : scanner.findCandidateComponents("com.peploleum.insight.domain")) {
            System.out.println(bd.getBeanClassName());
            try {
                Class<?> cl = Class.forName(bd.getBeanClassName());
                KibanaObject ip = new KibanaObject(KibanaObject.INDEX_PATTERN, cl.getSimpleName().toLowerCase());
                this.kibanaObjects.add(ip);
                this.entitiesMappingInfo.add(KibanaObjectUtils.getEntityMappingInfo(cl, ip));
            } catch (ClassNotFoundException e) {
                this.log.error("Erreur dans l'utilisation du classLoader sur un bean");
            }
        }
    }

    /**
     * Generate une visualisation depuis un model JSON et un indexPattern
     */
    private KibanaObject generateVisualization(final KibanaObject indexPattern, final String targetField, final KibanaVisualisationType visuType, final List<KibanaVisualisationGenerationParameters> visuParamList) {
        KibanaObject visualisation = null;
        try {
            final Instant timeFrom = Instant.parse("2018-09-01T00:00:00.00Z");
            final Instant timeTo = Instant.parse("2018-10-01T00:00:00.00Z");
            final KibanaVisualisationGenerationParameters visuParam = new KibanaVisualisationGenerationParameters(indexPattern, targetField, visuType, visuType.toString(), timeFrom.toString(), timeTo.toString());
            visuParamList.add(visuParam);
            visualisation = visuParam.getVisualisationFromParameters();
            this.kibanaObjects.add(visualisation);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing de kibana_visualisation_model", e);
        }
        return visualisation;
    }

    /**
     * Generate un dashboard depuis un model JSON et une visualisation
     */
    private KibanaObject generateDashboard(final List<KibanaVisualisationGenerationParameters> kibanaVisualisationsParams, final String dashboardTitle) {
        KibanaObject dashboard = null;
        try {
            dashboard = new KibanaDashboardGenerationParameters(dashboardTitle, kibanaVisualisationsParams).getDashboardFromParameters();
            this.kibanaObjects.add(dashboard);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing de kibana_visualisation_model", e);
        }
        return dashboard;
    }

    private KibanaObject setDefaultKibanaIndexPattern() {
        KibanaObject defaultIndex = this.kibanaObjects.stream().filter(ko -> ko.getType().equals(KibanaObject.INDEX_PATTERN) && ko.getAttributes().getTitle().equals("personne")).findFirst().get();
        String jsonValue = "{\"value\":\"" + defaultIndex.getId() + "\"}";
        this.postMessageToKibana(jsonValue, KibanaMessageUri.SET_DEFAULT_INDEX_PATTERN);
        return defaultIndex;
    }

    private String getJsonObjects() {
        String jsonIndexPatterns = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            jsonIndexPatterns = objectMapper.writeValueAsString(this.kibanaObjects);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing des index patterns", e);
        }
        return jsonIndexPatterns;
    }

    private String getJsonDashboardBundleObjects(KibanaObjectsBundle bundle) {
        String jsonDashboardBundle = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            jsonDashboardBundle = objectMapper.writeValueAsString(bundle);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing des index patterns", e);
        }
        return jsonDashboardBundle;
    }

    private void postMessageToKibana(final String schema, final KibanaMessageUri uriPattern) {
        try {
            final RestTemplate rt = new RestTemplate();
            final HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
            jsonHeaders.add("kbn-xsrf", "true");
            HttpEntity<String> postVariableRegistryEntry = new HttpEntity<>(schema, jsonHeaders);

            final StringBuilder sb = new StringBuilder(this.KIBANA_ENTRY_URI);
            sb.append(uriPattern.getUri());
            //sb.append("?overwrite");

            final ResponseEntity<String> tResponseEntity = rt.postForEntity(sb.toString(), postVariableRegistryEntry, String.class);

            log.info(String.valueOf(tResponseEntity.getStatusCode()));
            log.info(tResponseEntity.getBody());

        } catch (Exception e) {
            this.log.info("Erreur posting kibana json", e);
        }
    }

    enum KibanaMessageUri {

        POST_INDEX_PATTERN("api/saved_objects/index-pattern/"),
        POST_VISUALISATION(""),
        /* POST_DASHBOARD comprend le dashboard et tous les éléments (index pattern + visu) dont il dépend*/
        POST_DASHBOARD("api/kibana/dashboards/import?exclude=index-pattern"),
        POST_BULK_OBJECT("api/saved_objects/_bulk_create"),
        SET_DEFAULT_INDEX_PATTERN("api/kibana/settings/defaultIndex");

        final String uri;

        KibanaMessageUri(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }
    }

    public Set<EntityMappingInfo> getEntitiesMappingInfo() {
        return entitiesMappingInfo;
    }
}
