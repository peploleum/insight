package com.peploleum.insight.domain.kibana;

import com.peploleum.insight.service.impl.ElasticClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard parameters de Kibana
 */
public class KibanaDashboardGenerationParameters {

    private String dashboardTitle;
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

    public KibanaObject getDashboardFromParameters() throws Exception {
        KibanaDashboardType dashboardType = KibanaDashboardType.getTypeFromNumber(this.visualisations.size());
        KibanaObject dashboard = KibanaObjectUtils.deserializeJsonFileToKibanaObject(ElasticClientService.class.getResource(dashboardType.getJsonModelFileUrl()));

        KibanaVisualisationGenerationParameters timelineVisualisationParams = this.visualisations.stream().filter(visu -> visu.getVisualizationType().equals(KibanaVisualisationType.VISU_TIMELINE)).findAny().get();
        final String timeFrom = timelineVisualisationParams != null ? timelineVisualisationParams.getTimeFromFilter() : "";
        final String timeTo = timelineVisualisationParams != null ? timelineVisualisationParams.getTimeToFilter() : "";

        dashboard = KibanaObjectUtils.updateDashboardWith5VisualisationsAndTime(dashboard, this.dashboardTitle, this.visualisations.stream().map(visu -> visu.getIndexPatternId()).collect(Collectors.toList()), timeFrom, timeTo);
        return dashboard;
    }
}
