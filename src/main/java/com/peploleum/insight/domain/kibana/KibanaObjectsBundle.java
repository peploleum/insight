package com.peploleum.insight.domain.kibana;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard de Kibana
 */
public class KibanaObjectsBundle {

    private List<KibanaObject> objects = new ArrayList<>();

    public KibanaObjectsBundle() {
    }

    public List<KibanaObject> getObjects() {
        return objects;
    }

    public void setObjects(List<KibanaObject> objects) {
        this.objects = objects;
    }
}
