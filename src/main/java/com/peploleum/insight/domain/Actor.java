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

import io.swagger.annotations.ApiModel;

/**
 * Equivalent a la classe Identity (mot cle interdit dans le jdl) de STIX
 */
@ApiModel(description = "Equivalent a la classe Identity (mot cle interdit dans le jdl) de STIX")
@Entity
@Table(name = "actor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "actor")
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "nom")
    private String nom;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "classe_identite")
    private String classeIdentite;

    @OneToMany(mappedBy = "isTargetsIntrusionSetToActor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IntrusionSet> targetsActorToIntrusionSets = new HashSet<>();

    @OneToMany(mappedBy = "isTargetsMalwareToActor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Malware> targetsActorToMalwares = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfActors")
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

    public Actor description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public Actor nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public Actor type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelle() {
        return libelle;
    }

    public Actor libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getClasseIdentite() {
        return classeIdentite;
    }

    public Actor classeIdentite(String classeIdentite) {
        this.classeIdentite = classeIdentite;
        return this;
    }

    public void setClasseIdentite(String classeIdentite) {
        this.classeIdentite = classeIdentite;
    }

    public Set<IntrusionSet> getTargetsActorToIntrusionSets() {
        return targetsActorToIntrusionSets;
    }

    public Actor targetsActorToIntrusionSets(Set<IntrusionSet> intrusionSets) {
        this.targetsActorToIntrusionSets = intrusionSets;
        return this;
    }

    public Actor addTargetsActorToIntrusionSet(IntrusionSet intrusionSet) {
        this.targetsActorToIntrusionSets.add(intrusionSet);
        intrusionSet.setIsTargetsIntrusionSetToActor(this);
        return this;
    }

    public Actor removeTargetsActorToIntrusionSet(IntrusionSet intrusionSet) {
        this.targetsActorToIntrusionSets.remove(intrusionSet);
        intrusionSet.setIsTargetsIntrusionSetToActor(null);
        return this;
    }

    public void setTargetsActorToIntrusionSets(Set<IntrusionSet> intrusionSets) {
        this.targetsActorToIntrusionSets = intrusionSets;
    }

    public Set<Malware> getTargetsActorToMalwares() {
        return targetsActorToMalwares;
    }

    public Actor targetsActorToMalwares(Set<Malware> malwares) {
        this.targetsActorToMalwares = malwares;
        return this;
    }

    public Actor addTargetsActorToMalware(Malware malware) {
        this.targetsActorToMalwares.add(malware);
        malware.setIsTargetsMalwareToActor(this);
        return this;
    }

    public Actor removeTargetsActorToMalware(Malware malware) {
        this.targetsActorToMalwares.remove(malware);
        malware.setIsTargetsMalwareToActor(null);
        return this;
    }

    public void setTargetsActorToMalwares(Set<Malware> malwares) {
        this.targetsActorToMalwares = malwares;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public Actor linkOf(NetLink netLink) {
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
        Actor actor = (Actor) o;
        if (actor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Actor{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", classeIdentite='" + getClasseIdentite() + "'" +
            "}";
    }
}
