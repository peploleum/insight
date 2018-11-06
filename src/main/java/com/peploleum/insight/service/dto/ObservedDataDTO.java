package com.peploleum.insight.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ObservedData entity.
 */
public class ObservedDataDTO implements Serializable {

    private Long id;

    private String type;

    private String objetsObserves;

    private ZonedDateTime dateDebut;

    private ZonedDateTime dateFin;

    private Integer nombreJours;

    private Long linkOfId;

    private String linkOfNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjetsObserves() {
        return objetsObserves;
    }

    public void setObjetsObserves(String objetsObserves) {
        this.objetsObserves = objetsObserves;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getNombreJours() {
        return nombreJours;
    }

    public void setNombreJours(Integer nombreJours) {
        this.nombreJours = nombreJours;
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

        ObservedDataDTO observedDataDTO = (ObservedDataDTO) o;
        if (observedDataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), observedDataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ObservedDataDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", objetsObserves='" + getObjetsObserves() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", nombreJours=" + getNombreJours() +
            ", linkOf=" + getLinkOfId() +
            ", linkOf='" + getLinkOfNom() + "'" +
            "}";
    }
}
