package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.peploleum.insight.domain.enumeration.EventType;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_description")
    private String eventDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "event_coordinates")
    @GeoPointField
    private String eventCoordinates;

    @Column(name = "event_date")
    @Field(type= FieldType.Date)
    private Instant eventDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_equipment",
               joinColumns = @JoinColumn(name = "events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "equipment_id", referencedColumnName = "id"))
    private Set<Equipment> equipment = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_location",
               joinColumns = @JoinColumn(name = "events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
    private Set<Location> locations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_organisation",
               joinColumns = @JoinColumn(name = "events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "organisations_id", referencedColumnName = "id"))
    private Set<Organisation> organisations = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Biographics> biographics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public Event eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Event eventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Event eventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventCoordinates() {
        return eventCoordinates;
    }

    public Event eventCoordinates(String eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
        return this;
    }

    public void setEventCoordinates(String eventCoordinates) {
        this.eventCoordinates = eventCoordinates;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public Event eventDate(Instant eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public Event equipment(Set<Equipment> equipment) {
        this.equipment = equipment;
        return this;
    }

    public Event addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
        equipment.getEvents().add(this);
        return this;
    }

    public Event removeEquipment(Equipment equipment) {
        this.equipment.remove(equipment);
        equipment.getEvents().remove(this);
        return this;
    }

    public void setEquipment(Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Event locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Event addLocation(Location location) {
        this.locations.add(location);
        location.getEvents().add(this);
        return this;
    }

    public Event removeLocation(Location location) {
        this.locations.remove(location);
        location.getEvents().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Organisation> getOrganisations() {
        return organisations;
    }

    public Event organisations(Set<Organisation> organisations) {
        this.organisations = organisations;
        return this;
    }

    public Event addOrganisation(Organisation organisation) {
        this.organisations.add(organisation);
        organisation.getEvents().add(this);
        return this;
    }

    public Event removeOrganisation(Organisation organisation) {
        this.organisations.remove(organisation);
        organisation.getEvents().remove(this);
        return this;
    }

    public void setOrganisations(Set<Organisation> organisations) {
        this.organisations = organisations;
    }

    public Set<Biographics> getBiographics() {
        return biographics;
    }

    public Event biographics(Set<Biographics> biographics) {
        this.biographics = biographics;
        return this;
    }

    public Event addBiographics(Biographics biographics) {
        this.biographics.add(biographics);
        biographics.getEvents().add(this);
        return this;
    }

    public Event removeBiographics(Biographics biographics) {
        this.biographics.remove(biographics);
        biographics.getEvents().remove(this);
        return this;
    }

    public void setBiographics(Set<Biographics> biographics) {
        this.biographics = biographics;
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
        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDescription='" + getEventDescription() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", eventCoordinates='" + getEventCoordinates() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            "}";
    }
}
