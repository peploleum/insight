package com.peploleum.insight.domain.dictionary;

import java.io.Serializable;

public class Motclef implements Serializable {

    private String clef;
    private Integer pond;

    public String getClef() {
        return clef;
    }

    public void setClef(String clef) {
        this.clef = clef;
    }

    public Integer getPond() {
        return pond;
    }

    public void setPond(Integer pond) {
        this.pond = pond;
    }
}
