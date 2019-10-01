package com.peploleum.insight.domain.dictionary;

import java.io.Serializable;

public class Motclef implements Serializable {

    private String clef;
    private String pond;

    public String getClef() {
        return clef;
    }

    public void setClef(String clef) {
        this.clef = clef;
    }

    public String getPond() {
        return pond;
    }

    public void setPond(String pond) {
        this.pond = pond;
    }

}
