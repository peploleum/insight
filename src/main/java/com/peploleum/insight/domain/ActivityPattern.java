package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
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
 * Equivalent a la classe Indicator (mot cle interdit dans le jdl) de STIX
 */
@ApiModel(description = "Equivalent a la classe Indicator (mot cle interdit dans le jdl) de STIX")
@Entity
@Table(name = "activity_pattern")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "activitypattern")
public class ActivityPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "modele")
    private String modele;

    @Column(name = "nom")
    private String nom;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "valide_a_partir_de")
    @Field(type = FieldType.Date)
    private ZonedDateTime valideAPartirDe;

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfActivityPatterns")
    private NetLink linkOf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModele() {
        return modele;
    }

    public ActivityPattern modele(String modele) {
        this.modele = modele;
        return this;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getNom() {
        return nom;
    }

    public ActivityPattern nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public ActivityPattern type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getValideAPartirDe() {
        return valideAPartirDe;
    }

    public ActivityPattern valideAPartirDe(ZonedDateTime valideAPartirDe) {
        this.valideAPartirDe = valideAPartirDe;
        return this;
    }

    public void setValideAPartirDe(ZonedDateTime valideAPartirDe) {
        this.valideAPartirDe = valideAPartirDe;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public ActivityPattern linkOf(NetLink netLink) {
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
        ActivityPattern activityPattern = (ActivityPattern) o;
        if (activityPattern.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityPattern.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityPattern{" +
            "id=" + getId() +
            ", modele='" + getModele() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", valideAPartirDe='" + getValideAPartirDe() + "'" +
            "}";
    }
}
