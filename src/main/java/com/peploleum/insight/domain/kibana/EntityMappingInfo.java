package com.peploleum.insight.domain.kibana;

import java.io.Serializable;
import java.util.Set;

/**
 * Information sur le mapping Elasticsearch de l'entité et son index pattern correspondant dans Kibana
 */
public class EntityMappingInfo implements Serializable {

    private String indexPatternId;
    private String indexPatternName;
    private Set<EntityFieldMappingInfo> fields;

    public EntityMappingInfo() {
    }

    public EntityMappingInfo(final String indexPatternId, final String indexPatternName, final Set<EntityFieldMappingInfo> fields) {
        this.indexPatternId = indexPatternId;
        this.indexPatternName = indexPatternName;
        this.fields = fields;
    }

    public String getIndexPatternId() {
        return indexPatternId;
    }

    public void setIndexPatternId(String indexPatternId) {
        this.indexPatternId = indexPatternId;
    }

    public String getIndexPatternName() {
        return indexPatternName;
    }

    public void setIndexPatternName(String indexPatternName) {
        this.indexPatternName = indexPatternName;
    }

    public Set<EntityFieldMappingInfo> getFields() {
        return fields;
    }

    public void setFields(Set<EntityFieldMappingInfo> fields) {
        this.fields = fields;
    }
}
