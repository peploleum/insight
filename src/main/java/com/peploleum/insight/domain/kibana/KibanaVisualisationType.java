package com.peploleum.insight.domain.kibana;

/**
 * Type de visualisations de Kibana
 */
public enum KibanaVisualisationType {
    VISU_TABLE("/kibana_visualization_model_table.json"),
    VISU_PIE("/kibana_visualization_model_pie.json"),
    VISU_VERT_BAR("/kibana_visualization_model_vertBar.json"),
    VISU_TIMELINE("/kibana_visualization_model_timeline.json"),
    VISU_MAP("/kibana_visualization_model_map.json");

    final String jsonModelFileUrl;

    KibanaVisualisationType(String uri) {
        this.jsonModelFileUrl = uri;
    }

    public String getJsonModelFileUrl() {
        return jsonModelFileUrl;
    }
}
