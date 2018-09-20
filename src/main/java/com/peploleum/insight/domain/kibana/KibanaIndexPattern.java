package com.peploleum.insight.domain.kibana;

/**
 * Index Pattern de Kibana
 */
public class KibanaIndexPattern extends KibanaObject {

    public KibanaIndexPattern(String title) {
        super(KibanaObject.INDEX_PATTERN, title);
    }
}
