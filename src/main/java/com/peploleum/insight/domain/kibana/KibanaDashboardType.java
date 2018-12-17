package com.peploleum.insight.domain.kibana;

/**
 * Type de dashboard (selon le nombre de visualisations voulu) de Kibana
 */
public enum KibanaDashboardType {
    DASH_SINGLE("/kibana_dashboard_model.json"),
    DASH_DOUBLE("/kibana_dashboard_model_2Visu.json"),
    DASH_TRIPLE("/kibana_dashboard_model_3Visu.json"),
    DASH_QUADRUPLE("/kibana_dashboard_model_4Visu.json"),
    DASH_QUINTUPLE("/kibana_dashboard_model_5Visu.json");

    final String jsonModelFileUrl;

    KibanaDashboardType(String uri) {
        this.jsonModelFileUrl = uri;
    }

    public String getJsonModelFileUrl() {
        return jsonModelFileUrl;
    }

    public static KibanaDashboardType getTypeFromNumber(final int nbre) {
        switch (nbre) {
            case 1:
                return KibanaDashboardType.DASH_SINGLE;
            case 2:
                return KibanaDashboardType.DASH_DOUBLE;
            case 3:
                return KibanaDashboardType.DASH_TRIPLE;
            case 4:
                return KibanaDashboardType.DASH_QUADRUPLE;
            case 5:
                return KibanaDashboardType.DASH_QUINTUPLE;
            default:
                return KibanaDashboardType.DASH_SINGLE;
        }
    }
}
