package com.peploleum.insight.domain.kibana;

/**
 * Dashboard de Kibana
 */
public class KibanaDashboard extends KibanaObject {

    public KibanaDashboard(String title) {
        super(KibanaObject.DASHBOARD, title);
    }
}
