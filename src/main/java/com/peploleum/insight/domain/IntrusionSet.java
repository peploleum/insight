package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A IntrusionSet.
 */
@Entity
@Table(name = "intrusion_set")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "intrusionset")
public class IntrusionSet implements Serializable {

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

    @Column(name = "objectif")
    private String objectif;

    @Column(name = "niveau_ressource")
    private String niveauRessource;

    @OneToMany(mappedBy = "usesToolToIntrusionSet")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tool> isUsesIntrusionSetToTools = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("targetsActorToIntrusionSets")
    private Actor isTargetsIntrusionSetToActor;

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfIntrusionSets")
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

    public IntrusionSet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public IntrusionSet nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public IntrusionSet type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjectif() {
        return objectif;
    }

    public IntrusionSet objectif(String objectif) {
        this.objectif = objectif;
        return this;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getNiveauRessource() {
        return niveauRessource;
    }

    public IntrusionSet niveauRessource(String niveauRessource) {
        this.niveauRessource = niveauRessource;
        return this;
    }

    public void setNiveauRessource(String niveauRessource) {
        this.niveauRessource = niveauRessource;
    }

    public Set<Tool> getIsUsesIntrusionSetToTools() {
        return isUsesIntrusionSetToTools;
    }

    public IntrusionSet isUsesIntrusionSetToTools(Set<Tool> tools) {
        this.isUsesIntrusionSetToTools = tools;
        return this;
    }

    public IntrusionSet addIsUsesIntrusionSetToTool(Tool tool) {
        this.isUsesIntrusionSetToTools.add(tool);
        tool.setUsesToolToIntrusionSet(this);
        return this;
    }

    public IntrusionSet removeIsUsesIntrusionSetToTool(Tool tool) {
        this.isUsesIntrusionSetToTools.remove(tool);
        tool.setUsesToolToIntrusionSet(null);
        return this;
    }

    public void setIsUsesIntrusionSetToTools(Set<Tool> tools) {
        this.isUsesIntrusionSetToTools = tools;
    }

    public Actor getIsTargetsIntrusionSetToActor() {
        return isTargetsIntrusionSetToActor;
    }

    public IntrusionSet isTargetsIntrusionSetToActor(Actor actor) {
        this.isTargetsIntrusionSetToActor = actor;
        return this;
    }

    public void setIsTargetsIntrusionSetToActor(Actor actor) {
        this.isTargetsIntrusionSetToActor = actor;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public IntrusionSet linkOf(NetLink netLink) {
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
        IntrusionSet intrusionSet = (IntrusionSet) o;
        if (intrusionSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), intrusionSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IntrusionSet{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", objectif='" + getObjectif() + "'" +
            ", niveauRessource='" + getNiveauRessource() + "'" +
            "}";
    }
}
