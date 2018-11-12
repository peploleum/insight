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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A NetLink.
 */
@Entity
@Table(name = "net_link")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "netlink")
public class NetLink implements Serializable {

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

    @Column(name = "jhi_level")
    private String level;

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AttackPattern> isLinkOfAttackPatterns = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Campaign> isLinkOfCampaigns = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CourseOfAction> isLinkOfCourseOfActions = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Actor> isLinkOfActors = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActivityPattern> isLinkOfActivityPatterns = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IntrusionSet> isLinkOfIntrusionSets = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Malware> isLinkOfMalwares = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ObservedData> isLinkOfObservedData = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Report> isLinkOfReports = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ThreatActor> isLinkOfThreatActors = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tool> isLinkOfTools = new HashSet<>();

    @OneToMany(mappedBy = "linkOf")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vulnerability> isLinkOfVulnerabilities = new HashSet<>();

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

    public NetLink description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public NetLink nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public NetLink type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public NetLink level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<AttackPattern> getIsLinkOfAttackPatterns() {
        return isLinkOfAttackPatterns;
    }

    public NetLink isLinkOfAttackPatterns(Set<AttackPattern> attackPatterns) {
        this.isLinkOfAttackPatterns = attackPatterns;
        return this;
    }

    public NetLink addIsLinkOfAttackPattern(AttackPattern attackPattern) {
        this.isLinkOfAttackPatterns.add(attackPattern);
        attackPattern.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfAttackPattern(AttackPattern attackPattern) {
        this.isLinkOfAttackPatterns.remove(attackPattern);
        attackPattern.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfAttackPatterns(Set<AttackPattern> attackPatterns) {
        this.isLinkOfAttackPatterns = attackPatterns;
    }

    public Set<Campaign> getIsLinkOfCampaigns() {
        return isLinkOfCampaigns;
    }

    public NetLink isLinkOfCampaigns(Set<Campaign> campaigns) {
        this.isLinkOfCampaigns = campaigns;
        return this;
    }

    public NetLink addIsLinkOfCampaign(Campaign campaign) {
        this.isLinkOfCampaigns.add(campaign);
        campaign.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfCampaign(Campaign campaign) {
        this.isLinkOfCampaigns.remove(campaign);
        campaign.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfCampaigns(Set<Campaign> campaigns) {
        this.isLinkOfCampaigns = campaigns;
    }

    public Set<CourseOfAction> getIsLinkOfCourseOfActions() {
        return isLinkOfCourseOfActions;
    }

    public NetLink isLinkOfCourseOfActions(Set<CourseOfAction> courseOfActions) {
        this.isLinkOfCourseOfActions = courseOfActions;
        return this;
    }

    public NetLink addIsLinkOfCourseOfAction(CourseOfAction courseOfAction) {
        this.isLinkOfCourseOfActions.add(courseOfAction);
        courseOfAction.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfCourseOfAction(CourseOfAction courseOfAction) {
        this.isLinkOfCourseOfActions.remove(courseOfAction);
        courseOfAction.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfCourseOfActions(Set<CourseOfAction> courseOfActions) {
        this.isLinkOfCourseOfActions = courseOfActions;
    }

    public Set<Actor> getIsLinkOfActors() {
        return isLinkOfActors;
    }

    public NetLink isLinkOfActors(Set<Actor> actors) {
        this.isLinkOfActors = actors;
        return this;
    }

    public NetLink addIsLinkOfActor(Actor actor) {
        this.isLinkOfActors.add(actor);
        actor.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfActor(Actor actor) {
        this.isLinkOfActors.remove(actor);
        actor.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfActors(Set<Actor> actors) {
        this.isLinkOfActors = actors;
    }

    public Set<ActivityPattern> getIsLinkOfActivityPatterns() {
        return isLinkOfActivityPatterns;
    }

    public NetLink isLinkOfActivityPatterns(Set<ActivityPattern> activityPatterns) {
        this.isLinkOfActivityPatterns = activityPatterns;
        return this;
    }

    public NetLink addIsLinkOfActivityPattern(ActivityPattern activityPattern) {
        this.isLinkOfActivityPatterns.add(activityPattern);
        activityPattern.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfActivityPattern(ActivityPattern activityPattern) {
        this.isLinkOfActivityPatterns.remove(activityPattern);
        activityPattern.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfActivityPatterns(Set<ActivityPattern> activityPatterns) {
        this.isLinkOfActivityPatterns = activityPatterns;
    }

    public Set<IntrusionSet> getIsLinkOfIntrusionSets() {
        return isLinkOfIntrusionSets;
    }

    public NetLink isLinkOfIntrusionSets(Set<IntrusionSet> intrusionSets) {
        this.isLinkOfIntrusionSets = intrusionSets;
        return this;
    }

    public NetLink addIsLinkOfIntrusionSet(IntrusionSet intrusionSet) {
        this.isLinkOfIntrusionSets.add(intrusionSet);
        intrusionSet.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfIntrusionSet(IntrusionSet intrusionSet) {
        this.isLinkOfIntrusionSets.remove(intrusionSet);
        intrusionSet.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfIntrusionSets(Set<IntrusionSet> intrusionSets) {
        this.isLinkOfIntrusionSets = intrusionSets;
    }

    public Set<Malware> getIsLinkOfMalwares() {
        return isLinkOfMalwares;
    }

    public NetLink isLinkOfMalwares(Set<Malware> malwares) {
        this.isLinkOfMalwares = malwares;
        return this;
    }

    public NetLink addIsLinkOfMalware(Malware malware) {
        this.isLinkOfMalwares.add(malware);
        malware.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfMalware(Malware malware) {
        this.isLinkOfMalwares.remove(malware);
        malware.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfMalwares(Set<Malware> malwares) {
        this.isLinkOfMalwares = malwares;
    }

    public Set<ObservedData> getIsLinkOfObservedData() {
        return isLinkOfObservedData;
    }

    public NetLink isLinkOfObservedData(Set<ObservedData> observedData) {
        this.isLinkOfObservedData = observedData;
        return this;
    }

    public NetLink addIsLinkOfObservedData(ObservedData observedData) {
        this.isLinkOfObservedData.add(observedData);
        observedData.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfObservedData(ObservedData observedData) {
        this.isLinkOfObservedData.remove(observedData);
        observedData.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfObservedData(Set<ObservedData> observedData) {
        this.isLinkOfObservedData = observedData;
    }

    public Set<Report> getIsLinkOfReports() {
        return isLinkOfReports;
    }

    public NetLink isLinkOfReports(Set<Report> reports) {
        this.isLinkOfReports = reports;
        return this;
    }

    public NetLink addIsLinkOfReport(Report report) {
        this.isLinkOfReports.add(report);
        report.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfReport(Report report) {
        this.isLinkOfReports.remove(report);
        report.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfReports(Set<Report> reports) {
        this.isLinkOfReports = reports;
    }

    public Set<ThreatActor> getIsLinkOfThreatActors() {
        return isLinkOfThreatActors;
    }

    public NetLink isLinkOfThreatActors(Set<ThreatActor> threatActors) {
        this.isLinkOfThreatActors = threatActors;
        return this;
    }

    public NetLink addIsLinkOfThreatActor(ThreatActor threatActor) {
        this.isLinkOfThreatActors.add(threatActor);
        threatActor.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfThreatActor(ThreatActor threatActor) {
        this.isLinkOfThreatActors.remove(threatActor);
        threatActor.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfThreatActors(Set<ThreatActor> threatActors) {
        this.isLinkOfThreatActors = threatActors;
    }

    public Set<Tool> getIsLinkOfTools() {
        return isLinkOfTools;
    }

    public NetLink isLinkOfTools(Set<Tool> tools) {
        this.isLinkOfTools = tools;
        return this;
    }

    public NetLink addIsLinkOfTool(Tool tool) {
        this.isLinkOfTools.add(tool);
        tool.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfTool(Tool tool) {
        this.isLinkOfTools.remove(tool);
        tool.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfTools(Set<Tool> tools) {
        this.isLinkOfTools = tools;
    }

    public Set<Vulnerability> getIsLinkOfVulnerabilities() {
        return isLinkOfVulnerabilities;
    }

    public NetLink isLinkOfVulnerabilities(Set<Vulnerability> vulnerabilities) {
        this.isLinkOfVulnerabilities = vulnerabilities;
        return this;
    }

    public NetLink addIsLinkOfVulnerability(Vulnerability vulnerability) {
        this.isLinkOfVulnerabilities.add(vulnerability);
        vulnerability.setLinkOf(this);
        return this;
    }

    public NetLink removeIsLinkOfVulnerability(Vulnerability vulnerability) {
        this.isLinkOfVulnerabilities.remove(vulnerability);
        vulnerability.setLinkOf(null);
        return this;
    }

    public void setIsLinkOfVulnerabilities(Set<Vulnerability> vulnerabilities) {
        this.isLinkOfVulnerabilities = vulnerabilities;
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
        NetLink netLink = (NetLink) o;
        if (netLink.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), netLink.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NetLink{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
