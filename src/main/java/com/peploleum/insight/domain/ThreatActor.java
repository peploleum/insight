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
 * A ThreatActor.
 */
@Entity
@Table(name = "threat_actor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "threatactor")
public class ThreatActor implements Serializable {

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

    @Column(name = "jhi_specification")
    private String specification;

    @Column(name = "jhi_role")
    private String role;

    @OneToMany(mappedBy = "targetsVulnerabilityToThreatActor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vulnerability> isTargetsThreatActorToVulnerabilities = new HashSet<>();

    @OneToMany(mappedBy = "usesToolToThreatActor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tool> isUsesThreatActorToTools = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("usesMalwareToThreatActors")
    private Malware isUsesThreatActorToMalware;

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfThreatActors")
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

    public ThreatActor nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public ThreatActor type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelle() {
        return libelle;
    }

    public ThreatActor libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getSpecification() {
        return specification;
    }

    public ThreatActor specification(String specification) {
        this.specification = specification;
        return this;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getRole() {
        return role;
    }

    public ThreatActor role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Vulnerability> getIsTargetsThreatActorToVulnerabilities() {
        return isTargetsThreatActorToVulnerabilities;
    }

    public ThreatActor isTargetsThreatActorToVulnerabilities(Set<Vulnerability> vulnerabilities) {
        this.isTargetsThreatActorToVulnerabilities = vulnerabilities;
        return this;
    }

    public ThreatActor addIsTargetsThreatActorToVulnerability(Vulnerability vulnerability) {
        this.isTargetsThreatActorToVulnerabilities.add(vulnerability);
        vulnerability.setTargetsVulnerabilityToThreatActor(this);
        return this;
    }

    public ThreatActor removeIsTargetsThreatActorToVulnerability(Vulnerability vulnerability) {
        this.isTargetsThreatActorToVulnerabilities.remove(vulnerability);
        vulnerability.setTargetsVulnerabilityToThreatActor(null);
        return this;
    }

    public void setIsTargetsThreatActorToVulnerabilities(Set<Vulnerability> vulnerabilities) {
        this.isTargetsThreatActorToVulnerabilities = vulnerabilities;
    }

    public Set<Tool> getIsUsesThreatActorToTools() {
        return isUsesThreatActorToTools;
    }

    public ThreatActor isUsesThreatActorToTools(Set<Tool> tools) {
        this.isUsesThreatActorToTools = tools;
        return this;
    }

    public ThreatActor addIsUsesThreatActorToTool(Tool tool) {
        this.isUsesThreatActorToTools.add(tool);
        tool.setUsesToolToThreatActor(this);
        return this;
    }

    public ThreatActor removeIsUsesThreatActorToTool(Tool tool) {
        this.isUsesThreatActorToTools.remove(tool);
        tool.setUsesToolToThreatActor(null);
        return this;
    }

    public void setIsUsesThreatActorToTools(Set<Tool> tools) {
        this.isUsesThreatActorToTools = tools;
    }

    public Malware getIsUsesThreatActorToMalware() {
        return isUsesThreatActorToMalware;
    }

    public ThreatActor isUsesThreatActorToMalware(Malware malware) {
        this.isUsesThreatActorToMalware = malware;
        return this;
    }

    public void setIsUsesThreatActorToMalware(Malware malware) {
        this.isUsesThreatActorToMalware = malware;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public ThreatActor linkOf(NetLink netLink) {
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
        ThreatActor threatActor = (ThreatActor) o;
        if (threatActor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), threatActor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ThreatActor{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", specification='" + getSpecification() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
