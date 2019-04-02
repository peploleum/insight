package com.peploleum.insight.service.dto;

import java.io.Serializable;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public class EdgeDTO implements Serializable {
    private String from;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
