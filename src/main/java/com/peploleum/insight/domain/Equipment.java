package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.peploleum.insight.domain.enumeration.EquipmentType;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "equipment")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @Column(name = "equipment_description")
    private String equipmentDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type")
    private EquipmentType equipmentType;

    @Column(name = "equipment_coordinates")
    private String equipmentCoordinates;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "equipment_location",
               joinColumns = @JoinColumn(name = "equipment_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
    private Set<Location> locations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "equipment_organisation",
               joinColumns = @JoinColumn(name = "equipment_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "organisations_id", referencedColumnName = "id"))
    private Set<Organisation> organisations = new HashSet<>();

    @ManyToMany(mappedBy = "equipment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Biographics> biographics = new HashSet<>();

    @ManyToMany(mappedBy = "equipment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Equipment equipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
        return this;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public Equipment equipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
        return this;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public Equipment equipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
        return this;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentCoordinates() {
        return equipmentCoordinates;
    }

    public Equipment equipmentCoordinates(String equipmentCoordinates) {
        this.equipmentCoordinates = equipmentCoordinates;
        return this;
    }

    public void setEquipmentCoordinates(String equipmentCoordinates) {
        this.equipmentCoordinates = equipmentCoordinates;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Equipment locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Equipment addLocation(Location location) {
        this.locations.add(location);
        location.getEquipment().add(this);
        return this;
    }

    public Equipment removeLocation(Location location) {
        this.locations.remove(location);
        location.getEquipment().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Organisation> getOrganisations() {
        return organisations;
    }

    public Equipment organisations(Set<Organisation> organisations) {
        this.organisations = organisations;
        return this;
    }

    public Equipment addOrganisation(Organisation organisation) {
        this.organisations.add(organisation);
        organisation.getEquipment().add(this);
        return this;
    }

    public Equipment removeOrganisation(Organisation organisation) {
        this.organisations.remove(organisation);
        organisation.getEquipment().remove(this);
        return this;
    }

    public void setOrganisations(Set<Organisation> organisations) {
        this.organisations = organisations;
    }

    public Set<Biographics> getBiographics() {
        return biographics;
    }

    public Equipment biographics(Set<Biographics> biographics) {
        this.biographics = biographics;
        return this;
    }

    public Equipment addBiographics(Biographics biographics) {
        this.biographics.add(biographics);
        biographics.getEquipment().add(this);
        return this;
    }

    public Equipment removeBiographics(Biographics biographics) {
        this.biographics.remove(biographics);
        biographics.getEquipment().remove(this);
        return this;
    }

    public void setBiographics(Set<Biographics> biographics) {
        this.biographics = biographics;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Equipment events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Equipment addEvent(Event event) {
        this.events.add(event);
        event.getEquipment().add(this);
        return this;
    }

    public Equipment removeEvent(Event event) {
        this.events.remove(event);
        event.getEquipment().remove(this);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
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
        Equipment equipment = (Equipment) o;
        if (equipment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), equipment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + getId() +
            ", equipmentName='" + getEquipmentName() + "'" +
            ", equipmentDescription='" + getEquipmentDescription() + "'" +
            ", equipmentType='" + getEquipmentType() + "'" +
            ", equipmentCoordinates='" + getEquipmentCoordinates() + "'" +
            "}";
    }
}
