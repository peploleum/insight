package com.peploleum.insight.service.dto;

import java.util.Objects;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public class NodeDTO {
    private String id;
    private String idMongo;
    private String label;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMongo() {
        return idMongo;
    }

    public void setIdMongo(String idMongo) {
        this.idMongo = idMongo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NodeDTO nodeDTO = (NodeDTO) o;
        if (nodeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nodeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Node{" +
            "id=" + getId() +
            ", idMongo=" + getIdMongo() +
            ", type='" + getType() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
