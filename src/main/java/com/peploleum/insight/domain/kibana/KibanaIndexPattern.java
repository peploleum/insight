package com.peploleum.insight.domain.kibana;

/**
 * Index Pattern de Kibana
 */
public class KibanaIndexPattern extends KibanaObject {

    private static final long serialVersionUID = 1L;

    public KibanaIndexPattern(String title) {
        super(KibanaObject.INDEX_PATTERN, title);
    }
}
