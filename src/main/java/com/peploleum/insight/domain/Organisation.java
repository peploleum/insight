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

import com.peploleum.insight.domain.enumeration.Size;

/**
 * A Organisation.
 */
@Entity
@Table(name = "organisation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "organisation")
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "organisation_name", nullable = false)
    private String organisationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "organisation_size")
    private Size organisationSize;

    @Column(name = "organisation_coordinates")
    private String organisationCoordinates;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "organisation_location",
               joinColumns = @JoinColumn(name = "organisations_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
    private Set<Location> locations = new HashSet<>();

    @ManyToMany(mappedBy = "organisations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Biographics> biographics = new HashSet<>();

    @ManyToMany(mappedBy = "organisations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @ManyToMany(mappedBy = "organisations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Equipment> equipment = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public Organisation organisationName(String organisationName) {
        this.organisationName = organisationName;
        return this;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Size getOrganisationSize() {
        return organisationSize;
    }

    public Organisation organisationSize(Size organisationSize) {
        this.organisationSize = organisationSize;
        return this;
    }

    public void setOrganisationSize(Size organisationSize) {
        this.organisationSize = organisationSize;
    }

    public String getOrganisationCoordinates() {
        return organisationCoordinates;
    }

    public Organisation organisationCoordinates(String organisationCoordinates) {
        this.organisationCoordinates = organisationCoordinates;
        return this;
    }

    public void setOrganisationCoordinates(String organisationCoordinates) {
        this.organisationCoordinates = organisationCoordinates;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Organisation locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Organisation addLocation(Location location) {
        this.locations.add(location);
        location.getOrganisations().add(this);
        return this;
    }

    public Organisation removeLocation(Location location) {
        this.locations.remove(location);
        location.getOrganisations().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Biographics> getBiographics() {
        return biographics;
    }

    public Organisation biographics(Set<Biographics> biographics) {
        this.biographics = biographics;
        return this;
    }

    public Organisation addBiographics(Biographics biographics) {
        this.biographics.add(biographics);
        biographics.getOrganisations().add(this);
        return this;
    }

    public Organisation removeBiographics(Biographics biographics) {
        this.biographics.remove(biographics);
        biographics.getOrganisations().remove(this);
        return this;
    }

    public void setBiographics(Set<Biographics> biographics) {
        this.biographics = biographics;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Organisation events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Organisation addEvent(Event event) {
        this.events.add(event);
        event.getOrganisations().add(this);
        return this;
    }

    public Organisation removeEvent(Event event) {
        this.events.remove(event);
        event.getOrganisations().remove(this);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public Organisation equipment(Set<Equipment> equipment) {
        this.equipment = equipment;
        return this;
    }

    public Organisation addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
        equipment.getOrganisations().add(this);
        return this;
    }

    public Organisation removeEquipment(Equipment equipment) {
        this.equipment.remove(equipment);
        equipment.getOrganisations().remove(this);
        return this;
    }

    public void setEquipment(Set<Equipment> equipment) {
        this.equipment = equipment;
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
        Organisation organisation = (Organisation) o;
        if (organisation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organisation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + getId() +
            ", organisationName='" + getOrganisationName() + "'" +
            ", organisationSize='" + getOrganisationSize() + "'" +
            ", organisationCoordinates='" + getOrganisationCoordinates() + "'" +
            "}";
    }
}
