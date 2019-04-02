package com.peploleum.insight.service;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface InsightGraphManagementService {
    /**
     * Creates a composite graph index for a given property name
     *
     * @param propertyKey the property name to index
     */
    void buildIndex(final String propertyKey);
}
