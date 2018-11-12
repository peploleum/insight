package com.peploleum.insight.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A AttackPattern.
 */
@Entity
@Table(name = "attack_pattern")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "attackpattern")
public class AttackPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "nom")
    private String nom;

    @Column(name = "reference_externe")
    private String referenceExterne;

    @Column(name = "tueur_processus")
    private String tueurProcessus;

    @Column(name = "jhi_type")
    private String type;

    @OneToMany(mappedBy = "isUsesMalwareToAttackPattern")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Malware> usesAttackPatternToMalwares = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfAttackPatterns")
    private NetLink linkOf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public AttackPattern description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public AttackPattern nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public AttackPattern referenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
        return this;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public String getTueurProcessus() {
        return tueurProcessus;
    }

    public AttackPattern tueurProcessus(String tueurProcessus) {
        this.tueurProcessus = tueurProcessus;
        return this;
    }

    public void setTueurProcessus(String tueurProcessus) {
        this.tueurProcessus = tueurProcessus;
    }

    public String getType() {
        return type;
    }

    public AttackPattern type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Malware> getUsesAttackPatternToMalwares() {
        return usesAttackPatternToMalwares;
    }

    public AttackPattern usesAttackPatternToMalwares(Set<Malware> malwares) {
        this.usesAttackPatternToMalwares = malwares;
        return this;
    }

    public AttackPattern addUsesAttackPatternToMalware(Malware malware) {
        this.usesAttackPatternToMalwares.add(malware);
        malware.setIsUsesMalwareToAttackPattern(this);
        return this;
    }

    public AttackPattern removeUsesAttackPatternToMalware(Malware malware) {
        this.usesAttackPatternToMalwares.remove(malware);
        malware.setIsUsesMalwareToAttackPattern(null);
        return this;
    }

    public void setUsesAttackPatternToMalwares(Set<Malware> malwares) {
        this.usesAttackPatternToMalwares = malwares;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public AttackPattern linkOf(NetLink netLink) {
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
        AttackPattern attackPattern = (AttackPattern) o;
        if (attackPattern.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attackPattern.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttackPattern{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", referenceExterne='" + getReferenceExterne() + "'" +
            ", tueurProcessus='" + getTueurProcessus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
