package com.peploleum.insight.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.peploleum.insight.domain.enumeration.Gender;

/**
 * A Biographics.
 */
@Document(collection = "biographics")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "biographics")
public class Biographics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    @Mapping(mappingPath = "/mappings/asset_id_mapping.json")
    private String id;

    @NotNull
    @Field("biographics_firstname")
    private String biographicsFirstname;

    @NotNull
    @Field("biographics_name")
    private String biographicsName;

    @Field("biographics_age")
    private Integer biographicsAge;

    @Field("biographics_gender")
    private Gender biographicsGender;

    @Field("biographics_image")
    private byte[] biographicsImage;

    @Field("biographics_image_content_type")
    private String biographicsImageContentType;

    @Field("biographics_coordinates")
    private String biographicsCoordinates;

    @Field("biographics_symbol")
    private String biographicsSymbol;

    @Field("external_id")
    private String externalId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public byte[] getBiographicsImage() {
        return biographicsImage;
    }

    public Biographics biographicsImage(byte[] biographicsImage) {
        this.biographicsImage = biographicsImage;
        return this;
    }

    public void setBiographicsImage(byte[] biographicsImage) {
        this.biographicsImage = biographicsImage;
    }

    public String getBiographicsImageContentType() {
        return biographicsImageContentType;
    }

    public Biographics biographicsImageContentType(String biographicsImageContentType) {
        this.biographicsImageContentType = biographicsImageContentType;
        return this;
    }

    public void setBiographicsImageContentType(String biographicsImageContentType) {
        this.biographicsImageContentType = biographicsImageContentType;
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

    public String getBiographicsSymbol() {
        return biographicsSymbol;
    }

    public Biographics biographicsSymbol(String biographicsSymbol) {
        this.biographicsSymbol = biographicsSymbol;
        return this;
    }

    public void setBiographicsSymbol(String biographicsSymbol) {
        this.biographicsSymbol = biographicsSymbol;
    }

    public String getExternalId() {
        return externalId;
    }

    public Biographics externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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
            ", biographicsImage='" + getBiographicsImage() + "'" +
            ", biographicsImageContentType='" + getBiographicsImageContentType() + "'" +
            ", biographicsCoordinates='" + getBiographicsCoordinates() + "'" +
            ", biographicsSymbol='" + getBiographicsSymbol() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
