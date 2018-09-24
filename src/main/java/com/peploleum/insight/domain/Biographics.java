package com.peploleum.insight.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.peploleum.insight.domain.enumeration.Gender;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * A Biographics.
 */
@Entity
@Table(name = "biographics")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "biographics")
public class Biographics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "biographics_firstname", nullable = false)
    private String biographicsFirstname;

    @NotNull
    @Column(name = "biographics_name", nullable = false)
    private String biographicsName;

    @Column(name = "biographics_age")
    private Integer biographicsAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "biographics_gender")
    private Gender biographicsGender;

    @Lob
    @Column(name = "biographics_photo")
    private byte[] biographicsPhoto;

    @Column(name = "biographics_photo_content_type")
    private String biographicsPhotoContentType;

    @Column(name = "biographics_coordinates")
    @GeoPointField
    private String biographicsCoordinates;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "biographics_event",
               joinColumns = @JoinColumn(name = "biographics_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "events_id", referencedColumnName = "id"))
    private Set<Event> events = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "biographics_equipment",
               joinColumns = @JoinColumn(name = "biographics_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "equipment_id", referencedColumnName = "id"))
    private Set<Equipment> equipment = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "biographics_location",
               joinColumns = @JoinColumn(name = "biographics_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "locations_id", referencedColumnName = "id"))
    private Set<Location> locations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "biographics_organisation",
               joinColumns = @JoinColumn(name = "biographics_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "organisations_id", referencedColumnName = "id"))
    private Set<Organisation> organisations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBiographicsFirstname() {
        return biographicsFirstname;
    }

    public Biographics biographicsFirstname(String biographicsFirstname) {
        this.biographicsFirstname = biographicsFirstname;
        return this;
    }

    public void setBiographicsFirstname(String biographicsFirstname) {
        this.biographicsFirstname = biographicsFirstname;
    }

    public String getBiographicsName() {
        return biographicsName;
    }

    public Biographics biographicsName(String biographicsName) {
        this.biographicsName = biographicsName;
        return this;
    }

    public void setBiographicsName(String biographicsName) {
        this.biographicsName = biographicsName;
    }

    public Integer getBiographicsAge() {
        return biographicsAge;
    }

    public Biographics biographicsAge(Integer biographicsAge) {
        this.biographicsAge = biographicsAge;
        return this;
    }

    public void setBiographicsAge(Integer biographicsAge) {
        this.biographicsAge = biographicsAge;
    }

    public Gender getBiographicsGender() {
        return biographicsGender;
    }

    public Biographics biographicsGender(Gender biographicsGender) {
        this.biographicsGender = biographicsGender;
        return this;
    }

    public void setBiographicsGender(Gender biographicsGender) {
        this.biographicsGender = biographicsGender;
    }

    public byte[] getBiographicsPhoto() {
        return biographicsPhoto;
    }

    public Biographics biographicsPhoto(byte[] biographicsPhoto) {
        this.biographicsPhoto = biographicsPhoto;
        return this;
    }

    public void setBiographicsPhoto(byte[] biographicsPhoto) {
        this.biographicsPhoto = biographicsPhoto;
    }

    public String getBiographicsPhotoContentType() {
        return biographicsPhotoContentType;
    }

    public Biographics biographicsPhotoContentType(String biographicsPhotoContentType) {
        this.biographicsPhotoContentType = biographicsPhotoContentType;
        return this;
    }

    public void setBiographicsPhotoContentType(String biographicsPhotoContentType) {
        this.biographicsPhotoContentType = biographicsPhotoContentType;
    }

    public String getBiographicsCoordinates() {
        return biographicsCoordinates;
    }

    public Biographics biographicsCoordinates(String biographicsCoordinates) {
        this.biographicsCoordinates = biographicsCoordinates;
        return this;
    }

    public void setBiographicsCoordinates(String biographicsCoordinates) {
        this.biographicsCoordinates = biographicsCoordinates;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Biographics events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Biographics addEvent(Event event) {
        this.events.add(event);
        event.getBiographics().add(this);
        return this;
    }

    public Biographics removeEvent(Event event) {
        this.events.remove(event);
        event.getBiographics().remove(this);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public Biographics equipment(Set<Equipment> equipment) {
        this.equipment = equipment;
        return this;
    }

    public Biographics addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
        equipment.getBiographics().add(this);
        return this;
    }

    public Biographics removeEquipment(Equipment equipment) {
        this.equipment.remove(equipment);
        equipment.getBiographics().remove(this);
        return this;
    }

    public void setEquipment(Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Biographics locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Biographics addLocation(Location location) {
        this.locations.add(location);
        location.getBiographics().add(this);
        return this;
    }

    public Biographics removeLocation(Location location) {
        this.locations.remove(location);
        location.getBiographics().remove(this);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Organisation> getOrganisations() {
        return organisations;
    }

    public Biographics organisations(Set<Organisation> organisations) {
        this.organisations = organisations;
        return this;
    }

    public Biographics addOrganisation(Organisation organisation) {
        this.organisations.add(organisation);
        organisation.getBiographics().add(this);
        return this;
    }

    public Biographics removeOrganisation(Organisation organisation) {
        this.organisations.remove(organisation);
        organisation.getBiographics().remove(this);
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
        Biographics biographics = (Biographics) o;
        if (biographics.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), biographics.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Biographics{" +
            "id=" + getId() +
            ", biographicsFirstname='" + getBiographicsFirstname() + "'" +
            ", biographicsName='" + getBiographicsName() + "'" +
            ", biographicsAge=" + getBiographicsAge() +
            ", biographicsGender='" + getBiographicsGender() + "'" +
            ", biographicsPhoto='" + getBiographicsPhoto() + "'" +
            ", biographicsPhotoContentType='" + getBiographicsPhotoContentType() + "'" +
            ", biographicsCoordinates='" + getBiographicsCoordinates() + "'" +
            "}";
    }
}
