package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the IntrusionSet entity.
 */
public class IntrusionSetDTO implements Serializable {

    private Long id;

    private String description;

    private String nom;

    private String type;

    private String objectif;

    private String niveauRessource;

    private Long isTargetsIntrusionSetToActorId;

    private String isTargetsIntrusionSetToActorNom;

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

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getNiveauRessource() {
        return niveauRessource;
    }

    public void setNiveauRessource(String niveauRessource) {
        this.niveauRessource = niveauRessource;
    }

    public Long getIsTargetsIntrusionSetToActorId() {
        return isTargetsIntrusionSetToActorId;
    }

    public void setIsTargetsIntrusionSetToActorId(Long actorId) {
        this.isTargetsIntrusionSetToActorId = actorId;
    }

    public String getIsTargetsIntrusionSetToActorNom() {
        return isTargetsIntrusionSetToActorNom;
    }

    public void setIsTargetsIntrusionSetToActorNom(String actorNom) {
        this.isTargetsIntrusionSetToActorNom = actorNom;
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

        IntrusionSetDTO intrusionSetDTO = (IntrusionSetDTO) o;
        if (intrusionSetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), intrusionSetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IntrusionSetDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", objectif='" + getObjectif() + "'" +
            ", niveauRessource='" + getNiveauRessource() + "'" +
            ", isTargetsIntrusionSetToActor=" + getIsTargetsIntrusionSetToActorId() +
            ", isTargetsIntrusionSetToActor='" + getIsTargetsIntrusionSetToActorNom() + "'" +
            ", linkOf=" + getLinkOfId() +
            ", linkOf='" + getLinkOfNom() + "'" +
            "}";
    }
}
