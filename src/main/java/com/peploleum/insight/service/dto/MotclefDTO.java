package com.peploleum.insight.service.dto;

import java.io.Serializable;

public class MotclefDTO implements Serializable {

    private String id;
    private String clef;
    private String pond;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
