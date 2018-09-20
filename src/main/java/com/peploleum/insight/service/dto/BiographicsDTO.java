package com.peploleum.insight.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.peploleum.insight.domain.enumeration.Gender;

/**
 * A DTO for the Biographics entity.
 */
public class BiographicsDTO implements Serializable {

    private Long id;

    @NotNull
    private String biographicsFirstname;

    @NotNull
    private String biographicsName;

    private Integer biographicsAge;

    private Gender biographicsGender;

    @Lob
    private byte[] biographicsPhoto;
    private String biographicsPhotoContentType;

    private String biographicsCoordinates;

    private Set<EventDTO> events = new HashSet<>();

    private Set<EquipmentDTO> equipment = new HashSet<>();

    private Set<LocationDTO> locations = new HashSet<>();

    private Set<OrganisationDTO> organisations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBiographicsFirstname() {
        return biographicsFirstname;
    }

    public void setBiographicsFirstname(String biographicsFirstname) {
        this.biographicsFirstname = biographicsFirstname;
    }

    public String getBiographicsName() {
        return biographicsName;
    }

    public void setBiographicsName(String biographicsName) {
        this.biographicsName = biographicsName;
    }

    public Integer getBiographicsAge() {
        return biographicsAge;
    }

    public void setBiographicsAge(Integer biographicsAge) {
        this.biographicsAge = biographicsAge;
    }

    public Gender getBiographicsGender() {
        return biographicsGender;
    }

    public void setBiographicsGender(Gender biographicsGender) {
        this.biographicsGender = biographicsGender;
    }

    public byte[] getBiographicsPhoto() {
        return biographicsPhoto;
    }

    public void setBiographicsPhoto(byte[] biographicsPhoto) {
        this.biographicsPhoto = biographicsPhoto;
    }

    public String getBiographicsPhotoContentType() {
        return biographicsPhotoContentType;
    }

    public void setBiographicsPhotoContentType(String biographicsPhotoContentType) {
        this.biographicsPhotoContentType = biographicsPhotoContentType;
    }

    public String getBiographicsCoordinates() {
        return biographicsCoordinates;
    }

    public void setBiographicsCoordinates(String biographicsCoordinates) {
        this.biographicsCoordinates = biographicsCoordinates;
    }

    public Set<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(Set<EventDTO> events) {
        this.events = events;
    }

    public Set<EquipmentDTO> getEquipment() {
        return equipment;
    }

    public void setEquipment(Set<EquipmentDTO> equipment) {
        this.equipment = equipment;
    }

    public Set<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<LocationDTO> locations) {
        this.locations = locations;
    }

    public Set<OrganisationDTO> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(Set<OrganisationDTO> organisations) {
        this.organisations = organisations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BiographicsDTO biographicsDTO = (BiographicsDTO) o;
        if (biographicsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), biographicsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BiographicsDTO{" +
            "id=" + getId() +
            ", biographicsFirstname='" + getBiographicsFirstname() + "'" +
            ", biographicsName='" + getBiographicsName() + "'" +
            ", biographicsAge=" + getBiographicsAge() +
            ", biographicsGender='" + getBiographicsGender() + "'" +
            ", biographicsPhoto='" + getBiographicsPhoto() + "'" +
            ", biographicsCoordinates='" + getBiographicsCoordinates() + "'" +
            "}";
    }
}
