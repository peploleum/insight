package com.peploleum.insight.domain;

/**
 * Created by gFolgoas on 27/02/2019.
 */
public abstract class InsightEntity {
    private String entityType;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
