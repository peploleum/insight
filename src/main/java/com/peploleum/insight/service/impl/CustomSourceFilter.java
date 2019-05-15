package com.peploleum.insight.service.impl;

import org.springframework.data.elasticsearch.core.query.SourceFilter;

/**
 * Elastic SourceFilter : excludes specific attributes from "source" objects on page result parsing. The resulting excluded attributes are null.
 */
public class CustomSourceFilter implements SourceFilter {
    @Override
    public String[] getIncludes() {
        return new String[0];
    }

    @Override
    public String[] getExcludes() {
        return new String[]{"*Date", "*Image", "*Data"};
    }
}
