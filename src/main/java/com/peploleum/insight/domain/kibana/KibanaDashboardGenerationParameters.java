package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

        KibanaVisualisationGenerationParameters timelineVisualisationParams = this.visualisations.stream().filter(visu -> visu.getVisualizationType().equals(KibanaVisualisationType.VISU_TIMELINE)).findAny().orElse(null);
        if (timelineVisualisationParams != null) {
            final String timeFrom = timelineVisualisationParams.getTimeFromFilter() != null ? timelineVisualisationParams.getTimeFromFilter() : "2000-01-01T00:00:00.00Z";
            final String timeTo = timelineVisualisationParams.getTimeToFilter() != null ? timelineVisualisationParams.getTimeToFilter() : "2020-01-01T00:00:00.00Z";
            dashboard = KibanaObjectUtils.updateDashboard(dashboard, this.dashboardTitle, visualisationIds, timeFrom, timeTo);
        } else {
            dashboard = KibanaObjectUtils.updateDashboard(dashboard, this.dashboardTitle, visualisationIds);
        }
        return dashboard;
    }
}
