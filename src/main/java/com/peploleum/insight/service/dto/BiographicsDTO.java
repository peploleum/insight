package com.peploleum.insight.service.dto;

import com.peploleum.insight.domain.enumeration.Gender;
import com.peploleum.insight.domain.map.GeometryCollection;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Biographics entity.
 */
public class BiographicsDTO implements Serializable {

    private String id;

    @NotNull
    private String biographicsFirstname;

    @NotNull
    private String biographicsName;

    private Integer biographicsAge;

    private Gender biographicsGender;

    private byte[] biographicsImage;
    private String biographicsImageContentType;

    private String biographicsCoordinates;
    private GeometryCollection geometry;
    private String biographicsSymbol;

    private String externalId;

    private Instant biographicsCreationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public byte[] getBiographicsImage() {
        return biographicsImage;
    }

    public void setBiographicsImage(byte[] biographicsImage) {
        this.biographicsImage = biographicsImage;
    }

    public String getBiographicsImageContentType() {
        return biographicsImageContentType;
    }

    public void setBiographicsImageContentType(String biographicsImageContentType) {
        this.biographicsImageContentType = biographicsImageContentType;
    }

    public String getBiographicsCoordinates() {
        return biographicsCoordinates;
    }

    public void setBiographicsCoordinates(String biographicsCoordinates) {
        this.biographicsCoordinates = biographicsCoordinates;
    }

    public String getBiographicsSymbol() {
        return biographicsSymbol;
    }

    public void setBiographicsSymbol(String biographicsSymbol) {
        this.biographicsSymbol = biographicsSymbol;
    }

    public String getExternalId() {
        return externalId;
    }

    public GeometryCollection getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryCollection geometry) {
        this.geometry = geometry;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Instant getBiographicsCreationDate() {
        return biographicsCreationDate;
    }

    public void setBiographicsCreationDate(Instant biographicsCreationDate) {
        this.biographicsCreationDate = biographicsCreationDate;
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
            ", biographicsImage='" + getBiographicsImage() + "'" +
            ", biographicsCoordinates='" + getBiographicsCoordinates() + "'" +
            ", biographicsSymbol='" + getBiographicsSymbol() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
