package com.peploleum.insight.domain.kibana;

/**
 * Visualisation de Kibana
 */
public class KibanaVisualisation extends KibanaObject {

    private static final long serialVersionUID = 1L;

    public KibanaVisualisation(String title) {
        super(KibanaObject.VISUALIZATION, title);
    }

}
