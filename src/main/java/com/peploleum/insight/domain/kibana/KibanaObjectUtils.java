package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.service.impl.ElasticClientService;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Object a importer dans Kibana
 */
public class KibanaObjectUtils {

    public static KibanaObject deserializeJsonFileToKibanaObject(URL resource) throws Exception {
        byte[] jsonData = Files.readAllBytes(Paths.get(resource.toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonData, KibanaObject.class);
    }

    /**
     * @param filePath file path depuis /resources/
     */
    private static String readFileLineByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        URL resource = ElasticClientService.class.getResource(filePath);
        try (Stream<String> stream = Files.lines(Paths.get(resource.toURI()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static KibanaObject updateDefaultVisualisation(KibanaObject visualisation, final String visuTitle, final String targetField, final String indexPatternId) {

        //Modification du title de la visualisation et du field cible de l'index_pattern
        visualisation.getAttributes().setTitle(visuTitle);
        final String modifiedVisState = visualisation.getAttributes().getVisState().replace("visualization_title", visuTitle).replace("index_field", targetField + ".keyword");
        visualisation.getAttributes().setVisState(modifiedVisState);

        //Modification de l'IndexPattern
        KibanaObjectMetadata meta = visualisation.getAttributes().getKibanaSavedObjectMeta();
        final String matchingSource = meta.getSearchSourceJSON().replace("index_pattern_id", indexPatternId);
        meta.setSearchSourceJSON(matchingSource);

        return visualisation;
    }

    public static KibanaObject updateTimelineVisualisation(KibanaObject visualisation, final String visuTitle, final String targetIndexTemporalFieldName, final String targetIndexName, final String indexPatternId) {

        //Modification du title de la visualisation et du l'index_pattern cible
        //Par défaut le champs date
        visualisation.getAttributes().setTitle(visuTitle);
        final String modifiedVisState = visualisation.getAttributes().getVisState().replace("visualization_title", visuTitle).replace("index_name", targetIndexName).replace("index_temporal_field", targetIndexTemporalFieldName);
        visualisation.getAttributes().setVisState(modifiedVisState);

        //Modification de l'IndexPattern
        KibanaObjectMetadata meta = visualisation.getAttributes().getKibanaSavedObjectMeta();
        final String matchingSource = meta.getSearchSourceJSON().replace("index_pattern_id", indexPatternId);
        meta.setSearchSourceJSON(matchingSource);

        return visualisation;
    }

    public static KibanaObject updateMapVisualisation(KibanaObject visualisation, final String visuTitle, final String targetIndexLocationFieldName, final String indexPatternId) {

        //Modification du title de la visualisation et du l'index_pattern cible
        //Par défaut le champs date
        visualisation.getAttributes().setTitle(visuTitle);
        final String modifiedVisState = visualisation.getAttributes().getVisState().replace("visualization_title", visuTitle).replace("index_location_field", targetIndexLocationFieldName);
        visualisation.getAttributes().setVisState(modifiedVisState);

        //Modification de l'IndexPattern
        KibanaObjectMetadata meta = visualisation.getAttributes().getKibanaSavedObjectMeta();
        final String matchingSource = meta.getSearchSourceJSON().replace("index_pattern_id", indexPatternId);
        meta.setSearchSourceJSON(matchingSource);

        return visualisation;
    }

    public static KibanaObject updateDashboard(KibanaObject dashboard, final String dashboardTitle, final List<String> visualisationIds) {

        //Modification du title
        dashboard.getAttributes().setTitle(dashboardTitle);

        //Modification des visualisations ids
        String matchingString = "";
        for (int i = 0; i < visualisationIds.size(); i++) {
            matchingString = dashboard.getAttributes().getPanelsJSON().replace("visualization_id_" + (i + 1), visualisationIds.get(i));
        }
        dashboard.getAttributes().setPanelsJSON(matchingString);

        return dashboard;
    }

    public static KibanaObject updateDashboard(KibanaObject dashboard, final String dashboardTitle, final List<String> visualisationIds, final String timeFrom, final String timeTo) {

        dashboard = updateDashboard(dashboard, dashboardTitle, visualisationIds);
        //Formattage des dates
        dashboard.getAttributes().setTimeFrom(timeFrom);
        dashboard.getAttributes().setTimeTo(timeTo);
        return dashboard;
    }

    /*public static KibanaObject updateDashboardWith5Visualisations(KibanaObject dashboard, final String dashboardTitle, final List<String> visualisationIds) {

        //Modification du title
        dashboard.getAttributes().setTitle(dashboardTitle);

        //Modification des visualisations ids
        String matchingString = dashboard.getAttributes().getPanelsJSON().replace("visualization_id_1", visualisationIds.get(0)).replace("visualization_id_2", visualisationIds.get(1)).replace("visualization_id_3", visualisationIds.get(2)).replace("visualization_id_4", visualisationIds.get(3)).replace("visualization_id_5", visualisationIds.get(4));
        dashboard.getAttributes().setPanelsJSON(matchingString);

        return dashboard;
    }*/

    public static EntityMappingInfo getEntityMappingInfo(Class<?> cl, KibanaObject indexPattern) {
        Set<EntityFieldMappingInfo> fieldMappingInfos = new HashSet<>();
        for (Field field : cl.getDeclaredFields()) {
            EntityFieldMappingInfo fieldInfo = null;
            if (field.getAnnotation(GeoPointField.class) != null) {
                fieldInfo = new EntityFieldMappingInfo(field.getName(), EntityFieldType.GEOLOCATION);
            } else if (field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class) != null) {
                if (field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class).type().equals(FieldType.Date))
                    fieldInfo = new EntityFieldMappingInfo(field.getName(), EntityFieldType.DATE);
            } else if (field.getType().equals(Integer.class) || field.getType().equals(Double.class)
                || field.getType().equals(Float.class) || field.getType().equals(Long.class)) {
                fieldInfo = new EntityFieldMappingInfo(field.getName(), EntityFieldType.NUMBER);
            } else
                fieldInfo = new EntityFieldMappingInfo(field.getName(), EntityFieldType.STRING);
            fieldMappingInfos.add(fieldInfo);
        }
        return new EntityMappingInfo(indexPattern.getId(), indexPattern.getAttributes().getTitle(), fieldMappingInfos);
    }
}
