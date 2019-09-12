package com.peploleum.insight.service.dto;

import java.io.Serializable;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */

public class CriteriaDTO implements Serializable {
    private String label;
    private String property;
    private String value;


    public CriteriaDTO(String label, String property, String value) {
        this.label = label;
        this.property = property;
        this.value = value;
    }

    public CriteriaDTO() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
