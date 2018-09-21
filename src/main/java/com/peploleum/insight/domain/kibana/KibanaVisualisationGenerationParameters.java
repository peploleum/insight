package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.peploleum.insight.service.impl.ElasticClientService;

import java.io.Serializable;

/**
 * Visualisation parameters de Kibana
 */
public class KibanaVisualisationGenerationParameters implements Serializable {

    private String visualizationTitle;

    @JsonProperty("visualizationType")
    private KibanaVisualisationType visualizationType;

    private String indexPatternId;
    private String indexPatternName;
    private String indexPatternFieldTarget;

    private String timeFromFilter;
    private String timeToFilter;

    public KibanaVisualisationGenerationParameters() {
    }

    public KibanaVisualisationGenerationParameters(final KibanaObject indexPattern, final String indexPatternFieldTarget, final KibanaVisualisationType visualizationType, String visualizationTitle, String timeFromFilter, String timeToFilter) {
        this.visualizationTitle = visualizationTitle;
        this.visualizationType = visualizationType;
        this.indexPatternId = indexPattern.getId();
        this.indexPatternName = indexPattern.getAttributes().getTitle();
        this.indexPatternFieldTarget = indexPatternFieldTarget;
        this.timeFromFilter = timeFromFilter;
        this.timeToFilter = timeToFilter;
    }

    public String getVisualizationTitle() {
        return visualizationTitle;
    }

    public void setVisualizationTitle(String visualizationTitle) {
        this.visualizationTitle = visualizationTitle;
    }

    public String getIndexPatternId() {
        return indexPatternId;
    }

    public void setIndexPatternId(String indexPatternId) {
        this.indexPatternId = indexPatternId;
    }

    public String getIndexPatternName() {
        return indexPatternName;
    }

    public void setIndexPatternName(String indexPatternName) {
        this.indexPatternName = indexPatternName;
    }

    public String getIndexPatternFieldTarget() {
        return indexPatternFieldTarget;
    }

    public void setIndexPatternFieldTarget(String indexPatternFieldTarget) {
        this.indexPatternFieldTarget = indexPatternFieldTarget;
    }

    public String getTimeFromFilter() {
        return timeFromFilter;
    }

    public void setTimeFromFilter(String timeFromFilter) {
        this.timeFromFilter = timeFromFilter;
    }

    public String getTimeToFilter() {
        return timeToFilter;
    }

    public void setTimeToFilter(String timeToFilter) {
        this.timeToFilter = timeToFilter;
    }

    public KibanaVisualisationType getVisualizationType() {
        return visualizationType;
    }

    public void setVisualizationType(KibanaVisualisationType visualizationType) {
        this.visualizationType = visualizationType;
    }

    @JsonIgnore
    public KibanaObject getVisualisationFromParameters() throws Exception {
        KibanaObject visualisation = null;
        visualisation = KibanaObjectUtils.deserializeJsonFileToKibanaObject(KibanaVisualisationGenerationParameters.class.getResource(this.visualizationType.getJsonModelFileUrl()));
        switch (this.visualizationType) {
            case VISU_TABLE:
            case VISU_PIE:
            case VISU_VERT_BAR:
                visualisation = KibanaObjectUtils.updateDefaultVisualisation(visualisation, this.visualizationType.toString(), this.indexPatternFieldTarget, this.indexPatternId);
                break;
            case VISU_TIMELINE:
                visualisation = KibanaObjectUtils.updateTimelineVisualisation(visualisation, this.visualizationType.toString(), this.indexPatternFieldTarget, this.indexPatternName, this.indexPatternId);
                break;
            case VISU_MAP:
                visualisation = KibanaObjectUtils.updateMapVisualisation(visualisation, this.visualizationType.toString(), this.indexPatternFieldTarget, this.indexPatternId);
                break;
            default:
                break;
        }
        return visualisation;
    }
}
