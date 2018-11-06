package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the NetLink entity.
 */
public class NetLinkDTO implements Serializable {

    private Long id;

    private String description;

    private String nom;

    private String type;

    private String level;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NetLinkDTO netLinkDTO = (NetLinkDTO) o;
        if (netLinkDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), netLinkDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NetLinkDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
