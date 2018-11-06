package com.peploleum.insight.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ActivityPattern entity.
 */
public class ActivityPatternDTO implements Serializable {

    private Long id;

    private String modele;

    private String nom;

    private String type;

    private ZonedDateTime valideAPartirDe;

    private Long linkOfId;

    private String linkOfNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
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

    public ZonedDateTime getValideAPartirDe() {
        return valideAPartirDe;
    }

    public void setValideAPartirDe(ZonedDateTime valideAPartirDe) {
        this.valideAPartirDe = valideAPartirDe;
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

        ActivityPatternDTO activityPatternDTO = (ActivityPatternDTO) o;
        if (activityPatternDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityPatternDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityPatternDTO{" +
            "id=" + getId() +
            ", modele='" + getModele() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", valideAPartirDe='" + getValideAPartirDe() + "'" +
            ", linkOf=" + getLinkOfId() +
            ", linkOf='" + getLinkOfNom() + "'" +
            "}";
    }
}
