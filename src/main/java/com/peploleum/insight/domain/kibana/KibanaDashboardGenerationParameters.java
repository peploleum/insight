package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.peploleum.insight.service.impl.ElasticClientService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dashboard parameters de Kibana
 */
public class KibanaDashboardGenerationParameters implements Serializable {

    private String dashboardTitle;
    @JsonProperty("visualisations")
    private List<KibanaVisualisationGenerationParameters> visualisations = new ArrayList<>();

    public KibanaDashboardGenerationParameters() {
    }

    public KibanaDashboardGenerationParameters(final String dashboardTitle, final List<KibanaVisualisationGenerationParameters> visualisations) {
        this.dashboardTitle = dashboardTitle;
        this.visualisations = visualisations;
    }

    public String getDashboardTitle() {
        return dashboardTitle;
    }

    public void setDashboardTitle(String dashboardTitle) {
        this.dashboardTitle = dashboardTitle;
    }

    public List<KibanaVisualisationGenerationParameters> getVisualisations() {
        return visualisations;
    }

    public void setVisualisations(List<KibanaVisualisationGenerationParameters> visualisations) {
        this.visualisations = visualisations;
    }

    @JsonIgnore
    public KibanaObject getDashboardFromParameters(List<String> visualisationIds) throws Exception {
        KibanaDashboardType dashboardType = KibanaDashboardType.getTypeFromNumber(this.visualisations.size());
        KibanaObject dashboard = KibanaObjectUtils.deserializeJsonFileToKibanaObject(KibanaDashboardGenerationParameters.class.getResource(dashboardType.getJsonModelFileUrl()));

        KibanaVisualisationGenerationParameters timelineVisualisationParams = this.visualisations.stream().filter(visu -> visu.getVisualizationType().equals(KibanaVisualisationType.VISU_TIMELINE)).findAny().get();
        final String timeFrom = timelineVisualisationParams.getTimeFromFilter() != null ? timelineVisualisationParams.getTimeFromFilter() : "2018-09-01T00:00:00.00Z";
        final String timeTo = timelineVisualisationParams.getTimeToFilter() != null ? timelineVisualisationParams.getTimeToFilter() : "2018-10-01T00:00:00.00Z";

        dashboard = KibanaObjectUtils.updateDashboardWith5VisualisationsAndTime(dashboard, this.dashboardTitle, visualisationIds, timeFrom, timeTo);
        return dashboard;
    }
}
