package com.peploleum.insight.domain.kibana;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Object Result from Kibana Find REST API
 */
public class KibanaFindObjects implements Serializable {
    private static final long serialVersionUID = 1L;

    private int total;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    private int page;
    private int per_page;
    private List<KibanaObject> saved_objects = new ArrayList<>();

    public List<KibanaObject> getSaved_objects() {
        return saved_objects;
    }

    public void setSaved_objects(List<KibanaObject> saved_objects) {
        this.saved_objects = saved_objects;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
