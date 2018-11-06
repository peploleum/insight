package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "objets_references")
    private String objetsReferences;

    @Column(name = "date_publication")
    private ZonedDateTime datePublication;

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfReports")
    private NetLink linkOf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Report nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public Report type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelle() {
        return libelle;
    }

    public Report libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getObjetsReferences() {
        return objetsReferences;
    }

    public Report objetsReferences(String objetsReferences) {
        this.objetsReferences = objetsReferences;
        return this;
    }

    public void setObjetsReferences(String objetsReferences) {
        this.objetsReferences = objetsReferences;
    }

    public ZonedDateTime getDatePublication() {
        return datePublication;
    }

    public Report datePublication(ZonedDateTime datePublication) {
        this.datePublication = datePublication;
        return this;
    }

    public void setDatePublication(ZonedDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public Report linkOf(NetLink netLink) {
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
        Report report = (Report) o;
        if (report.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), report.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", objetsReferences='" + getObjetsReferences() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            "}";
    }
}
