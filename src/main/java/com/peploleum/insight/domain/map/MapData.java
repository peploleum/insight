package com.peploleum.insight.domain.map;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gFolgoas on 18/01/2019.
 */
public class MapData implements Serializable {

    private String id;
    private String label;
    private String objectType;
    private String description;
    private List<Double> coordinate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Double> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(List<Double> coordinate) {
        this.coordinate = coordinate;
    }
}
