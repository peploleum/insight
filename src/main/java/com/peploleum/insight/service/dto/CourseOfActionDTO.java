package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CourseOfAction entity.
 */
public class CourseOfActionDTO implements Serializable {

    private Long id;

    private String description;

    private String nom;

    private String type;

    private Long linkOfId;

    private String linkOfNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLinkOfId() {
        return linkOfId;
    }

    public void setLinkOfId(Long netLinkId) {
        this.linkOfId = netLinkId;
    }

    public String getLinkOfNom() {
        return linkOfNom;
    }

    public void setLinkOfNom(String netLinkNom) {
        this.linkOfNom = netLinkNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseOfActionDTO courseOfActionDTO = (CourseOfActionDTO) o;
        if (courseOfActionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseOfActionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseOfActionDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", linkOf=" + getLinkOfId() +
            ", linkOf='" + getLinkOfNom() + "'" +
            "}";
    }
}
