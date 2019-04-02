package com.peploleum.insight.domain;

import com.peploleum.insight.domain.enumeration.InsightEntityType;

/**
 * Created by gFolgoas on 27/02/2019.
 */
public abstract class InsightEntity {
    private InsightEntityType entityType;

    public InsightEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(InsightEntityType entityType) {
        this.entityType = entityType;
    }
}
