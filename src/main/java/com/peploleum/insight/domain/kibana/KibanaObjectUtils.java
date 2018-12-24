package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Object a importer dans Kibana
 */
public class KibanaObjectUtils {

    public static KibanaObject deserializeJsonFileToKibanaObject(InputStream input) throws Exception {
        byte[] jsonData = IOUtils.toByteArray(input);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonData, KibanaObject.class);
    }

    /**
     * @param filePath file path depuis /resources/
     */
    /*private static String readFileLineByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        URL resource = ElasticClientService.class.getResource(filePath);

        try (Stream<String> stream = Files.lines(Paths.get(resource.toURI()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
    */
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

    public static KibanaObject updateTimelineVisualisation(KibanaObject visualisation, final String visuTitle, final String targetIndexTemporalFieldName, final String targetIndexName, final String indexPatternId, final String timeInterval) {

        //Modification du title de la visualisation et du l'index_pattern cible
        //Par défaut le champs date
        visualisation.getAttributes().setTitle(visuTitle);

        String interval = timeInterval != null ? timeInterval : "1w";
        final String modifiedVisState = visualisation.getAttributes().getVisState().replace("visualization_title", visuTitle).replace("index_name", targetIndexName).replace("index_temporal_field", targetIndexTemporalFieldName).replace("auto", interval);
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
        String matchingString = dashboard.getAttributes().getPanelsJSON();
        for (int i = 0; i < visualisationIds.size(); i++) {
            matchingString = matchingString.replace("visualization_id_" + (i + 1), visualisationIds.get(i));
        }
        dashboard.getAttributes().setPanelsJSON(matchingString);

        return dashboard;
    }

    public static KibanaObject updateDashboard(KibanaObject dashboard, final String dashboardTitle, final List<String> visualisationIds, final String timeFrom, final String timeTo, final String timeInterval) {

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
            if (field.isAnnotationPresent(org.springframework.data.annotation.Id.class)) {
                // exluding mongodb ids which are Strings
                continue;
            }
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

    public static String getIndexPatternFields(Class<?> cl) {
        //"[{\"name\":\"description\",\"type\":\"string\",\"count\":0,\"scripted\":false,\"searchable\":true,\"aggregatable\":false,\"readFromDocValues\":false}]"
        StringBuilder sb = new StringBuilder("[");

        /*
        //_ID
        sb.append("{");
        sb.append("\"name\":\"").append("\"_id\"").append("\",");
        sb.append("\"type\":\"").append("\"string\"").append("\",");
        sb.append("\"count\":").append("0").append(",");
        sb.append("\"scripted\":").append("false").append(",");
        sb.append("\"searchable\":").append("true").append(",");
        sb.append("\"aggregatable\":").append("true").append(",");
        sb.append("\"readFromDocValues\":").append("false").append("");
        sb.append("},");

        //_INDEX
        sb.append("{");
        sb.append("\"name\":\"").append("\"_index\"").append("\",");
        sb.append("\"type\":\"").append("\"string\"").append("\",");
        sb.append("\"count\":").append("0").append(",");
        sb.append("\"scripted\":").append("false").append(",");
        sb.append("\"searchable\":").append("true").append(",");
        sb.append("\"aggregatable\":").append("true").append(",");
        sb.append("\"readFromDocValues\":").append("false").append("");
        sb.append("},");

        //_SCORE
        sb.append("{");
        sb.append("\"name\":\"").append("\"_score\"").append("\",");
        sb.append("\"type\":\"").append("\"number\"").append("\",");
        sb.append("\"count\":").append("0").append(",");
        sb.append("\"scripted\":").append("false").append(",");
        sb.append("\"searchable\":").append("false").append(",");
        sb.append("\"aggregatable\":").append("false").append(",");
        sb.append("\"readFromDocValues\":").append("false").append("");
        sb.append("},");

        //_SOURCE
        sb.append("{");
        sb.append("\"name\":\"").append("\"_source\"").append("\",");
        sb.append("\"type\":\"").append("\"_source\"").append("\",");
        sb.append("\"count\":").append("0").append(",");
        sb.append("\"scripted\":").append("false").append(",");
        sb.append("\"searchable\":").append("false").append(",");
        sb.append("\"aggregatable\":").append("false").append(",");
        sb.append("\"readFromDocValues\":").append("false").append("");
        sb.append("},");

        //_TYPE
        sb.append("{");
        sb.append("\"name\":\"").append("\"_type\"").append("\",");
        sb.append("\"type\":\"").append("\"string\"").append("\",");
        sb.append("\"count\":").append("0").append(",");
        sb.append("\"scripted\":").append("false").append(",");
        sb.append("\"searchable\":").append("true").append(",");
        sb.append("\"aggregatable\":").append("true").append(",");
        sb.append("\"readFromDocValues\":").append("false").append("");
        sb.append("}");
*/

        boolean isFirst = true;
        for (Field field : cl.getDeclaredFields()) {

            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                String name = field.getName();
                String type = "string";

                if (field.getAnnotation(GeoPointField.class) != null) {
                    type = "geolocation";
                } else if (field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class) != null) {
                    if (field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class).type().equals(FieldType.Date))
                        type = "date";
                } else if (field.getType().equals(Integer.class) || field.getType().equals(Double.class)
                    || field.getType().equals(Float.class) || field.getType().equals(Long.class)) {
                    type = "number";
                }

                if (!isFirst)
                    sb.append(",");
                else
                    isFirst = false;

                sb.append("{");
                sb.append("\"name\":\"").append(name).append("\",");
                sb.append("\"type\":\"").append(type).append("\",");
                sb.append("\"count\":").append("0").append(",");
                sb.append("\"scripted\":").append("false").append(",");
                sb.append("\"searchable\":").append("true").append(",");
                sb.append("\"aggregatable\":");
                if (type.equals("string"))
                    sb.append("false");
                else
                    sb.append("true");
                sb.append(",");
                sb.append("\"readFromDocValues\":").append("false").append("");
                sb.append("}");

                // keyword
                if (type.equals("string")) {
                    sb.append(",{");
                    sb.append("\"name\":\"").append(name).append(".keyword").append("\",");
                    sb.append("\"type\":\"").append(type).append("\",");
                    sb.append("\"count\":").append("0").append(",");
                    sb.append("\"scripted\":").append("false").append(",");
                    sb.append("\"searchable\":").append("true").append(",");
                    sb.append("\"aggregatable\":").append("true").append(",");
                    sb.append("\"readFromDocValues\":").append("true").append("");
                    sb.append("}");
                }

            }
        }

        sb.append("]");
        return sb.toString();
    }
}
