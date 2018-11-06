package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ThreatActor entity.
 */
public class ThreatActorDTO implements Serializable {

    private Long id;

    private String nom;

    private String type;

    private String libelle;

    private String specification;

    private String role;

    private Long isUsesThreatActorToMalwareId;

    private String isUsesThreatActorToMalwareNom;

    private Long linkOfId;

    private String linkOfNom;

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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getIsUsesThreatActorToMalwareId() {
        return isUsesThreatActorToMalwareId;
    }

    public void setIsUsesThreatActorToMalwareId(Long malwareId) {
        this.isUsesThreatActorToMalwareId = malwareId;
    }

    public String getIsUsesThreatActorToMalwareNom() {
        return isUsesThreatActorToMalwareNom;
    }

    public void setIsUsesThreatActorToMalwareNom(String malwareNom) {
        this.isUsesThreatActorToMalwareNom = malwareNom;
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

        ThreatActorDTO threatActorDTO = (ThreatActorDTO) o;
        if (threatActorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), threatActorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ThreatActorDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", specification='" + getSpecification() + "'" +
            ", role='" + getRole() + "'" +
            ", isUsesThreatActorToMalware=" + getIsUsesThreatActorToMalwareId() +
            ", isUsesThreatActorToMalware='" + getIsUsesThreatActorToMalwareNom() + "'" +
            ", linkOf=" + getLinkOfId() +
            ", linkOf='" + getLinkOfNom() + "'" +
            "}";
    }
}
