package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Tool entity.
 */
public class ToolDTO implements Serializable {

    private Long id;

    private String nom;

    private String type;

    private String libelle;

    private String description;

    private String version;

    private Long usesToolToIntrusionSetId;

    private String usesToolToIntrusionSetNom;

    private Long isUsesToolToMalwareId;

    private String isUsesToolToMalwareNom;

    private Long linkOfId;

    private String linkOfNom;

    private Long usesToolToThreatActorId;

    private String usesToolToThreatActorNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getUsesToolToIntrusionSetId() {
        return usesToolToIntrusionSetId;
    }

    public void setUsesToolToIntrusionSetId(Long intrusionSetId) {
        this.usesToolToIntrusionSetId = intrusionSetId;
    }

    public String getUsesToolToIntrusionSetNom() {
        return usesToolToIntrusionSetNom;
    }

    public void setUsesToolToIntrusionSetNom(String intrusionSetNom) {
        this.usesToolToIntrusionSetNom = intrusionSetNom;
    }

    public Long getIsUsesToolToMalwareId() {
        return isUsesToolToMalwareId;
    }

    public void setIsUsesToolToMalwareId(Long malwareId) {
        this.isUsesToolToMalwareId = malwareId;
    }

    public String getIsUsesToolToMalwareNom() {
        return isUsesToolToMalwareNom;
    }

    public void setIsUsesToolToMalwareNom(String malwareNom) {
        this.isUsesToolToMalwareNom = malwareNom;
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

    public Long getUsesToolToThreatActorId() {
        return usesToolToThreatActorId;
    }

    public void setUsesToolToThreatActorId(Long threatActorId) {
        this.usesToolToThreatActorId = threatActorId;
    }

    public String getUsesToolToThreatActorNom() {
        return usesToolToThreatActorNom;
    }

    public void setUsesToolToThreatActorNom(String threatActorNom) {
        this.usesToolToThreatActorNom = threatActorNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ToolDTO toolDTO = (ToolDTO) o;
        if (toolDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), toolDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ToolDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", version='" + getVersion() + "'" +
            ", usesToolToIntrusionSet=" + getUsesToolToIntrusionSetId() +
            ", usesToolToIntrusionSet='" + getUsesToolToIntrusionSetNom() + "'" +
            ", isUsesToolToMalware=" + getIsUsesToolToMalwareId() +
            ", isUsesToolToMalware='" + getIsUsesToolToMalwareNom() + "'" +
            ", linkOf=" + getLinkOfId() +
            ", linkOf='" + getLinkOfNom() + "'" +
            ", usesToolToThreatActor=" + getUsesToolToThreatActorId() +
            ", usesToolToThreatActor='" + getUsesToolToThreatActorNom() + "'" +
            "}";
    }
}
