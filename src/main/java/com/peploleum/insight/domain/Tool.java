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
 * A Tool.
 */
@Entity
@Table(name = "tool")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tool")
public class Tool implements Serializable {

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

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @OneToMany(mappedBy = "targetsVulnerabilityToTool")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vulnerability> isTargetsToolToVulnerabilities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("isUsesIntrusionSetToTools")
    private IntrusionSet usesToolToIntrusionSet;

    @ManyToOne
    @JsonIgnoreProperties("usesMalwareToTools")
    private Malware isUsesToolToMalware;

    @ManyToOne
    @JsonIgnoreProperties("isLinkOfTools")
    private NetLink linkOf;

    @ManyToOne
    @JsonIgnoreProperties("isUsesThreatActorToTools")
    private ThreatActor usesToolToThreatActor;

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

    public Tool nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public Tool type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelle() {
        return libelle;
    }

    public Tool libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public Tool description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public Tool version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Vulnerability> getIsTargetsToolToVulnerabilities() {
        return isTargetsToolToVulnerabilities;
    }

    public Tool isTargetsToolToVulnerabilities(Set<Vulnerability> vulnerabilities) {
        this.isTargetsToolToVulnerabilities = vulnerabilities;
        return this;
    }

    public Tool addIsTargetsToolToVulnerability(Vulnerability vulnerability) {
        this.isTargetsToolToVulnerabilities.add(vulnerability);
        vulnerability.setTargetsVulnerabilityToTool(this);
        return this;
    }

    public Tool removeIsTargetsToolToVulnerability(Vulnerability vulnerability) {
        this.isTargetsToolToVulnerabilities.remove(vulnerability);
        vulnerability.setTargetsVulnerabilityToTool(null);
        return this;
    }

    public void setIsTargetsToolToVulnerabilities(Set<Vulnerability> vulnerabilities) {
        this.isTargetsToolToVulnerabilities = vulnerabilities;
    }

    public IntrusionSet getUsesToolToIntrusionSet() {
        return usesToolToIntrusionSet;
    }

    public Tool usesToolToIntrusionSet(IntrusionSet intrusionSet) {
        this.usesToolToIntrusionSet = intrusionSet;
        return this;
    }

    public void setUsesToolToIntrusionSet(IntrusionSet intrusionSet) {
        this.usesToolToIntrusionSet = intrusionSet;
    }

    public Malware getIsUsesToolToMalware() {
        return isUsesToolToMalware;
    }

    public Tool isUsesToolToMalware(Malware malware) {
        this.isUsesToolToMalware = malware;
        return this;
    }

    public void setIsUsesToolToMalware(Malware malware) {
        this.isUsesToolToMalware = malware;
    }

    public NetLink getLinkOf() {
        return linkOf;
    }

    public Tool linkOf(NetLink netLink) {
        this.linkOf = netLink;
        return this;
    }

    public void setLinkOf(NetLink netLink) {
        this.linkOf = netLink;
    }

    public ThreatActor getUsesToolToThreatActor() {
        return usesToolToThreatActor;
    }

    public Tool usesToolToThreatActor(ThreatActor threatActor) {
        this.usesToolToThreatActor = threatActor;
        return this;
    }

    public void setUsesToolToThreatActor(ThreatActor threatActor) {
        this.usesToolToThreatActor = threatActor;
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
        Tool tool = (Tool) o;
        if (tool.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tool.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tool{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
