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

import com.peploleum.insight.domain.enumeration.Size;

/**
 * A Organisation.
 */
@Document(collection = "organisation")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "organisation")
public class Organisation extends InsightEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    @Mapping(mappingPath = "/mappings/asset_id_mapping.json")
    private String id;

    @NotNull
    @Field("organisation_name")
    private String organisationName;

    @Field("organisation_descrption")
    private String organisationDescrption;

    @Field("organisation_size")
    private Size organisationSize;

    @Field("organisation_coordinates")
    private String organisationCoordinates;

    @Field("organisation_image")
    private byte[] organisationImage;

    @Field("organisation_image_content_type")
    private String organisationImageContentType;

    @Field("organisation_symbol")
    private String organisationSymbol;

    @Field("external_id")
    private String externalId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getOrganisationDescrption() {
        return organisationDescrption;
    }

    public Organisation organisationDescrption(String organisationDescrption) {
        this.organisationDescrption = organisationDescrption;
        return this;
    }

    public void setOrganisationDescrption(String organisationDescrption) {
        this.organisationDescrption = organisationDescrption;
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

    public byte[] getOrganisationImage() {
        return organisationImage;
    }

    public Organisation organisationImage(byte[] organisationImage) {
        this.organisationImage = organisationImage;
        return this;
    }

    public void setOrganisationImage(byte[] organisationImage) {
        this.organisationImage = organisationImage;
    }

    public String getOrganisationImageContentType() {
        return organisationImageContentType;
    }

    public Organisation organisationImageContentType(String organisationImageContentType) {
        this.organisationImageContentType = organisationImageContentType;
        return this;
    }

    public void setOrganisationImageContentType(String organisationImageContentType) {
        this.organisationImageContentType = organisationImageContentType;
    }

    public String getOrganisationSymbol() {
        return organisationSymbol;
    }

    public Organisation organisationSymbol(String organisationSymbol) {
        this.organisationSymbol = organisationSymbol;
        return this;
    }

    public void setOrganisationSymbol(String organisationSymbol) {
        this.organisationSymbol = organisationSymbol;
    }

    public String getExternalId() {
        return externalId;
    }

    public Organisation externalId(String externalId) {
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
            ", organisationDescrption='" + getOrganisationDescrption() + "'" +
            ", organisationSize='" + getOrganisationSize() + "'" +
            ", organisationCoordinates='" + getOrganisationCoordinates() + "'" +
            ", organisationImage='" + getOrganisationImage() + "'" +
            ", organisationImageContentType='" + getOrganisationImageContentType() + "'" +
            ", organisationSymbol='" + getOrganisationSymbol() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
