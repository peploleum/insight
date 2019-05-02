package com.peploleum.insight.domain.map;

public class GeometryCollection {
    private String type = "geometrycollection";
    private InsightShape[] geometries;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InsightShape[] getGeometries() {
        return geometries;
    }

    public void setGeometries(InsightShape[] geometries) {
        this.geometries = geometries;
    }

    @Override
    public String toString() {
        return this.type + super.toString();
    }
}
