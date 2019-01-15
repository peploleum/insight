package com.peploleum.insight.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.domain.RawData;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticClientService {

    private final Logger log = LoggerFactory.getLogger(ElasticClientService.class);

    @Value("${application.kibana.uri}")
    private String KIBANA_ENTRY_URI;

    //private Set<KibanaObject> kibanaObjects = new HashSet<>();
    private List<KibanaObject> kibanaIndexPattern = new ArrayList<>();
    private List<KibanaObject> kibanaDashboard = new ArrayList<>();
    private List<KibanaObject> kibanaVisualization = new ArrayList<>();

    private Set<EntityMappingInfo> entitiesMappingInfo = new HashSet<>();

    public ElasticClientService() {
    }

    public List<String> getDashboardId() {
        return this.kibanaDashboard.stream().map(ko -> ko.getId()).collect(Collectors.toList());
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void InitKibanaData() {
        // Load kibana index-pattern, dashboard, visualization
        Set<KibanaObject> kibanaObjects = getAllKibanaObjects();
        this.kibanaIndexPattern = kibanaObjects.stream().filter(ko -> ko.getType().equals(KibanaObject.INDEX_PATTERN)).collect(Collectors.toList());
        this.kibanaDashboard = kibanaObjects.stream().filter(ko -> ko.getType().equals(KibanaObject.DASHBOARD)).collect(Collectors.toList());
        this.kibanaVisualization = kibanaObjects.stream().filter(ko -> ko.getType().equals(KibanaObject.VISUALIZATION)).collect(Collectors.toList());

        // Generate Index-pattern at first launch
        if (this.kibanaIndexPattern.isEmpty()) {
            this.kibanaIndexPattern = generateIndexPatterns();

            createIndexPattern(this.kibanaIndexPattern);
        }

        // Init entityMappingInfo
        this.entitiesMappingInfo = generateEntitiesMappingInfo(this.kibanaIndexPattern);
    }

    private void createIndexPattern(List<KibanaObject> kibanaIndexPattern) {
        String jsonIndexPatterns = objectToJSONString(kibanaIndexPattern);

        if (!jsonIndexPatterns.isEmpty())
            this.postMessageToKibana(jsonIndexPatterns, KibanaMessageUri.POST_BULK_CREATE_OBJECTS);

        this.setDefaultKibanaIndexPattern(RawData.class.getSimpleName().toLowerCase());
    }

    private Set<EntityMappingInfo> generateEntitiesMappingInfo(List<KibanaObject> kibanaIndexPattern) {
        Set<EntityMappingInfo> ret = new HashSet<>();

        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Document.class));

        for (BeanDefinition bd : scanner.findCandidateComponents("com.peploleum.insight.domain")) {
            try {
                Class<?> cl = Class.forName(bd.getBeanClassName());
                Optional<KibanaObject> ip = this.kibanaIndexPattern.stream().filter(ko -> ko.getAttributes().getTitle().equals(cl.getSimpleName().toLowerCase())).findFirst();
                if (ip.isPresent()) {
                    final EntityMappingInfo entityMappingInfo = KibanaObjectUtils.getEntityMappingInfo(cl, ip.get());
                    ret.add(entityMappingInfo);
                }
            } catch (ClassNotFoundException e) {
                this.log.error("Erreur dans l'utilisation du classLoader sur un bean");
            }
        }
        return ret;
    }

    // @Scheduled(fixedDelay = 10000)
    public void updateIndexPatternFiled() {
        log.info("Update indexPattern Fields");

        if (this.kibanaIndexPattern != null && !this.kibanaIndexPattern.isEmpty()) {
            String baseUrl = KIBANA_ENTRY_URI + KibanaMessageUri.GET_UPDATE_INDEX_PATTERN_FIELDS.getUri();

            for (KibanaObject ko : this.kibanaIndexPattern) {
                RestTemplate rt = new RestTemplate();
                String url = baseUrl.replace("$PATTERN$", ko.getAttributes().getTitle());
                final ResponseEntity<String> tResponseEntity = rt.getForEntity(url, String.class);

                log.info("Update of " + ko.getId() + " indexPattern ended with code " + tResponseEntity.getStatusCode());
            }
        }
    }

    public void generateAndPostKibanaDashboard(final KibanaDashboardGenerationParameters dashboardParameters) {
        try {
            KibanaObjectsBundle objectBundle = new KibanaObjectsBundle();
            for (KibanaVisualisationGenerationParameters param : dashboardParameters.getVisualisations()) {
                final KibanaObject visualisation = param.getVisualisationFromParameters();
                KibanaObject index = this.kibanaIndexPattern.stream().filter(ko -> ko.getId().equals(param.getIndexPatternId())).findFirst().get();
                objectBundle.getObjects().add(visualisation);
                objectBundle.getObjects().add(index);
                this.kibanaVisualization.add(visualisation);
            }
            List<String> visualisationIds = objectBundle.getObjects().stream().filter(ko -> ko.getType().equals(KibanaObject.VISUALIZATION)).map(ko -> ko.getId()).collect(Collectors.toList());
            final KibanaObject dash = dashboardParameters.getDashboardFromParameters(visualisationIds);
            objectBundle.getObjects().add(dash);
            this.kibanaDashboard.add(dash);

            final String jsonBundle = objectToJSONString(objectBundle);
            if (!jsonBundle.isEmpty())
                this.postMessageToKibana(jsonBundle, KibanaMessageUri.POST_DASHBOARD);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing de kibana_visualisation_model", e);
        }
    }

    /**
     * Supprime de Kibana, tous les dashboards et visualisations créés
     */
    public void deleteAllDashboard() {
        if (!this.kibanaVisualization.isEmpty()) {
            kibanaVisualization.stream().forEach(ko -> {
                sendDeleteMessageToKibana(ko.getId(), KibanaMessageUri.DELETE_VISUALISATION);
            });
            kibanaVisualization.clear();
        }

        if (!kibanaDashboard.isEmpty()) {
            kibanaDashboard.stream().forEach(ko -> {
                this.sendDeleteMessageToKibana(ko.getId(), KibanaMessageUri.DELETE_DASHBOARD);
            });
            kibanaDashboard.clear();
        }
    }


    /**
     * Generate les index pattern depuis les classes annotées @Document
     */
    private List<KibanaObject> generateIndexPatterns() {
        List<KibanaObject> ret = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Document.class));

        for (BeanDefinition bd : scanner.findCandidateComponents("com.peploleum.insight.domain")) {
            try {
                Class<?> cl = Class.forName(bd.getBeanClassName());
                KibanaObject ip = new KibanaObject(KibanaObject.INDEX_PATTERN, cl.getSimpleName().toLowerCase());
                ip.getAttributes().setFields(KibanaObjectUtils.getIndexPatternFields(cl));
                ret.add(ip);
            } catch (ClassNotFoundException e) {
                this.log.error("Erreur dans l'utilisation du classLoader sur un bean");
            }
        }

        return ret;
    }

    // /**
    //  * Generate une visualisation depuis un model JSON et un indexPattern
    //  */
    // private KibanaObject generateVisualization(final KibanaObject indexPattern, final String targetField, final KibanaVisualisationType visuType, final List<KibanaVisualisationGenerationParameters> visuParamList) {
    //     KibanaObject visualisation = null;
    //     try {
    //         final Instant timeFrom = Instant.parse("2018-09-01T00:00:00.00Z");
    //         final Instant timeTo = Instant.parse("2018-10-01T00:00:00.00Z");
    //         final KibanaVisualisationGenerationParameters visuParam = new KibanaVisualisationGenerationParameters(indexPattern, targetField, visuType, visuType.toString(), timeFrom.toString(), timeTo.toString());
    //         visuParamList.add(visuParam);
    //         visualisation = visuParam.getVisualisationFromParameters();
    //         this.kibanaVisualization.add(visualisation);
    //     } catch (Exception e) {
    //         this.log.error("Erreur durant le parsing de kibana_visualisation_model", e);
    //     }
    //     return visualisation;
    // }

    // /**
    //  * Generate un dashboard depuis un model JSON et une visualisation
    //  */
    // private KibanaObject generateDashboard(final List<KibanaVisualisationGenerationParameters> kibanaVisualisationsParams, List<String> visualisationIds, final String dashboardTitle) {
    //     KibanaObject dashboard = null;
    //     try {
    //         dashboard = new KibanaDashboardGenerationParameters(dashboardTitle, kibanaVisualisationsParams).getDashboardFromParameters(visualisationIds);
    //         this.kibanaDashboard.add(dashboard);
    //     } catch (Exception e) {
    //         this.log.error("Erreur durant le parsing de kibana_visualisation_model", e);
    //     }
    //     return dashboard;
    // }

    private KibanaObject setDefaultKibanaIndexPattern(String indexName) {
        Optional<KibanaObject> defaultIndex = this.kibanaIndexPattern.stream().filter(ko -> ko.getAttributes().getTitle().equals(indexName)).findFirst();
        if (defaultIndex.isPresent()) {
            String jsonValue = "{\"value\":\"" + defaultIndex.get().getId() + "\"}";
            this.postMessageToKibana(jsonValue, KibanaMessageUri.SET_DEFAULT_INDEX_PATTERN);
        }
        return defaultIndex.get();
    }

    private String objectToJSONString(Object obj) {
        String jsonDashboardBundle = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            jsonDashboardBundle = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            this.log.error("Erreur durant le parsing des index patterns", e);
        }
        return jsonDashboardBundle;
    }

    /**
     * Load all dashboard / visualization / index-pattern from Kibana.
     * Request is paginate.
     *
     * @return
     */
    private Set<KibanaObject> getAllKibanaObjects() {
        Set<KibanaObject> ret = new HashSet<>();

        String baseUrl = KIBANA_ENTRY_URI + KibanaMessageUri.GET_FIND_OBJECTS.getUri();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            int page = 0;
            int current;
            int total;

            do {
                page++;

                String url = baseUrl + "&page=" + page + "&per_page=100";
                RestTemplate rt = new RestTemplate();
                final ResponseEntity<String> tResponseEntity = rt.getForEntity(url, String.class);

                log.info(String.valueOf(tResponseEntity.getStatusCode()));
                log.info(tResponseEntity.getBody());

                KibanaFindObjects kfo = objectMapper.readValue(tResponseEntity.getBody(), KibanaFindObjects.class);
                ret.addAll(kfo.getSaved_objects());

                current = kfo.getPage() * kfo.getPer_page();
                total = kfo.getTotal();
            }
            while (current < total);

        } catch (IOException e) {
            log.error("GetAllKibanaObject Failed", e);
        }

        return ret;
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

    private boolean sendDeleteMessageToKibana(final String id, final KibanaMessageUri uriPattern) {
        try {
            final RestTemplate rt = new RestTemplate();
            final StringBuilder sb = new StringBuilder(this.KIBANA_ENTRY_URI);
            sb.append(uriPattern.getUri());
            sb.append(id);
            rt.delete(sb.toString());
            return true;
        } catch (Exception e) {
            this.log.info("Erreur posting kibana json", e);
            return false;
        }
    }

    enum KibanaMessageUri {

        GET_UPDATE_INDEX_PATTERN_FIELDS("api/index_patterns/_fields_for_wildcard?pattern=$PATTERN$&meta_fields=[\"_source\",\"_id\",\"_type\",\"_index\",\"_score\"]"),
        GET_FIND_OBJECTS("api/saved_objects/_find?type=index-pattern&type=visualization&type=dashboard"),
        POST_INDEX_PATTERN("api/saved_objects/index-pattern/"),
        POST_VISUALISATION(""),
        /* POST_DASHBOARD comprend le dashboard et tous les éléments (index pattern + visu) dont il dépend*/
        POST_DASHBOARD("api/kibana/dashboards/import?exclude=index-pattern"),
        POST_BULK_CREATE_OBJECTS("api/saved_objects/_bulk_create"),
        POST_BULK_GET_OBJECTS("api/saved_objects/_bulk_get"),
        SET_DEFAULT_INDEX_PATTERN("api/kibana/settings/defaultIndex"),
        DELETE_VISUALISATION("api/saved_objects/visualization/"),
        DELETE_INDEX_PATTERN("api/saved_objects/index-pattern/"),
        DELETE_DASHBOARD("api/saved_objects/dashboard/");

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
