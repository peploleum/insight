package com.peploleum.insight.domain.kibana;

import java.io.Serializable;

/**
 * Information sur le mapping Elasticsearch d'un champ d'une entité
 */
public class EntityFieldMappingInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String fieldName;
    private EntityFieldType fieldType;

    public EntityFieldMappingInfo() {
    }

    public EntityFieldMappingInfo(String fieldName, EntityFieldType fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public EntityFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(EntityFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
