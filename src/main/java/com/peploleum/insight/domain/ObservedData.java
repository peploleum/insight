package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ObservedData.
 */
@Entity
@Table(name = "observed_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "observeddata")
public class ObservedData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "objets_observes")
    private String objetsObserves;

    @Column(name = "date_debut")
    @Field(type = FieldType.Date)
    private ZonedDateTime dateDebut;

    @Column(name = "date_fin")
    @Field(type = FieldType.Date)
    private ZonedDateTime dateFin;

    @Column(name = "nombre_jours")
    private Integer nombreJours;

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfObservedData")
    private NetLink linkOf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public ObservedData type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjetsObserves() {
        return objetsObserves;
    }

    public ObservedData objetsObserves(String objetsObserves) {
        this.objetsObserves = objetsObserves;
        return this;
    }

    public void setObjetsObserves(String objetsObserves) {
        this.objetsObserves = objetsObserves;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public ObservedData dateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public ObservedData dateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getNombreJours() {
        return nombreJours;
    }

    public ObservedData nombreJours(Integer nombreJours) {
        this.nombreJours = nombreJours;
        return this;
    }

    public void setNombreJours(Integer nombreJours) {
        this.nombreJours = nombreJours;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public ObservedData linkOf(NetLink netLink) {
        this.linkOf = netLink;
        return this;
    }

    public void setLinkOf(NetLink netLink) {
        this.linkOf = netLink;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObservedData observedData = (ObservedData) o;
        if (observedData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), observedData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ObservedData{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", objetsObserves='" + getObjetsObserves() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", nombreJours=" + getNombreJours() +
            "}";
    }
}
