package com.peploleum.insight.domain.kibana;

/**
 * Dashboard de Kibana
 */
public class KibanaDashboard extends KibanaObject {

    private static final long serialVersionUID = 1L;

    public KibanaDashboard(String title) {
        super(KibanaObject.DASHBOARD, title);
    }
}
