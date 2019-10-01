package com.peploleum.insight.domain.dictionary;

import java.io.Serializable;
import java.util.List;

public class Theme implements Serializable {

    private String name;
    private List<Motclef> motclef;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Motclef> getMotclef() {
        return motclef;
    }

    public void setMotclef(List<Motclef> motclef) {
        this.motclef = motclef;
    }
}
