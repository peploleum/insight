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

import com.peploleum.insight.domain.enumeration.LocationType;

/**
 * A Location.
 */
@Document(collection = "location")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    @Mapping(mappingPath = "/mappings/asset_id_mapping.json")
    private String id;

    @NotNull
    @Field("location_name")
    private String locationName;

    @Field("location_type")
    private LocationType locationType;

    @Field("location_coordinates")
    private String locationCoordinates;

    @Field("location_image")
    private byte[] locationImage;

    @Field("location_image_content_type")
    private String locationImageContentType;

    @Field("location_symbol")
    private String locationSymbol;

    @Field("external_id")
    private String externalId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public byte[] getLocationImage() {
        return locationImage;
    }

    public Location locationImage(byte[] locationImage) {
        this.locationImage = locationImage;
        return this;
    }

    public void setLocationImage(byte[] locationImage) {
        this.locationImage = locationImage;
    }

    public String getLocationImageContentType() {
        return locationImageContentType;
    }

    public Location locationImageContentType(String locationImageContentType) {
        this.locationImageContentType = locationImageContentType;
        return this;
    }

    public void setLocationImageContentType(String locationImageContentType) {
        this.locationImageContentType = locationImageContentType;
    }

    public String getLocationSymbol() {
        return locationSymbol;
    }

    public Location locationSymbol(String locationSymbol) {
        this.locationSymbol = locationSymbol;
        return this;
    }

    public void setLocationSymbol(String locationSymbol) {
        this.locationSymbol = locationSymbol;
    }

    public String getExternalId() {
        return externalId;
    }

    public Location externalId(String externalId) {
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
            ", locationImage='" + getLocationImage() + "'" +
            ", locationImageContentType='" + getLocationImageContentType() + "'" +
            ", locationSymbol='" + getLocationSymbol() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
