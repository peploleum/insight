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

import com.peploleum.insight.domain.enumeration.LocationType;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @Column(name = "location_coordinates")
    private String locationCoordinates;

    @ManyToMany(mappedBy = "locations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Biographics> biographics = new HashSet<>();

    @ManyToMany(mappedBy = "locations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @ManyToMany(mappedBy = "locations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Equipment> equipment = new HashSet<>();

    @ManyToMany(mappedBy = "locations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Organisation> organisations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public Location locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public Location locationType(LocationType locationType) {
        this.locationType = locationType;
        return this;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationCoordinates() {
        return locationCoordinates;
    }

    public Location locationCoordinates(String locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
        return this;
    }

    public void setLocationCoordinates(String locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    public Set<Biographics> getBiographics() {
        return biographics;
    }

    public Location biographics(Set<Biographics> biographics) {
        this.biographics = biographics;
        return this;
    }

    public Location addBiographics(Biographics biographics) {
        this.biographics.add(biographics);
        biographics.getLocations().add(this);
        return this;
    }

    public Location removeBiographics(Biographics biographics) {
        this.biographics.remove(biographics);
        biographics.getLocations().remove(this);
        return this;
    }

    public void setBiographics(Set<Biographics> biographics) {
        this.biographics = biographics;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Location events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Location addEvent(Event event) {
        this.events.add(event);
        event.getLocations().add(this);
        return this;
    }

    public Location removeEvent(Event event) {
        this.events.remove(event);
        event.getLocations().remove(this);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public Location equipment(Set<Equipment> equipment) {
        this.equipment = equipment;
        return this;
    }

    public Location addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
        equipment.getLocations().add(this);
        return this;
    }

    public Location removeEquipment(Equipment equipment) {
        this.equipment.remove(equipment);
        equipment.getLocations().remove(this);
        return this;
    }

    public void setEquipment(Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    public Set<Organisation> getOrganisations() {
        return organisations;
    }

    public Location organisations(Set<Organisation> organisations) {
        this.organisations = organisations;
        return this;
    }

    public Location addOrganisation(Organisation organisation) {
        this.organisations.add(organisation);
        organisation.getLocations().add(this);
        return this;
    }

    public Location removeOrganisation(Organisation organisation) {
        this.organisations.remove(organisation);
        organisation.getLocations().remove(this);
        return this;
    }

    public void setOrganisations(Set<Organisation> organisations) {
        this.organisations = organisations;
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
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", locationName='" + getLocationName() + "'" +
            ", locationType='" + getLocationType() + "'" +
            ", locationCoordinates='" + getLocationCoordinates() + "'" +
            "}";
    }
}
