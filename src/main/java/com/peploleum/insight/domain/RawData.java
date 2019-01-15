package com.peploleum.insight.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A RawData.
 */
@Document(collection = "raw_data")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "rawdata")
public class RawData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    @Mapping(mappingPath = "/mappings/asset_id_mapping.json")
    private String id;

    @NotNull
    @Field("raw_data_name")
    private String rawDataName;

    @Field("raw_data_type")
    private String rawDataType;

    @Field("raw_data_sub_type")
    private String rawDataSubType;

    @Field("raw_data_source_name")
    private String rawDataSourceName;

    @Field("raw_data_source_uri")
    private String rawDataSourceUri;

    @Field("raw_data_source_type")
    private String rawDataSourceType;

    @Field("raw_data_content")
    private String rawDataContent;

    @Field("raw_data_creation_date")
    private Instant rawDataCreationDate;

    @Field("raw_data_extracted_date")
    private Instant rawDataExtractedDate;

    @Field("raw_data_symbol")
    private String rawDataSymbol;

    @Field("raw_data_data")
    private byte[] rawDataData;

    @Field("raw_data_data_content_type")
    private String rawDataDataContentType;

    @Field("raw_data_coordinates")
    private String rawDataCoordinates;

    @Field("raw_data_annotations")
    private String rawDataAnnotations;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRawDataName() {
        return rawDataName;
    }

    public RawData rawDataName(String rawDataName) {
        this.rawDataName = rawDataName;
        return this;
    }

    public void setRawDataName(String rawDataName) {
        this.rawDataName = rawDataName;
    }

    public String getRawDataType() {
        return rawDataType;
    }

    public RawData rawDataType(String rawDataType) {
        this.rawDataType = rawDataType;
        return this;
    }

    public void setRawDataType(String rawDataType) {
        this.rawDataType = rawDataType;
    }

    public String getRawDataSubType() {
        return rawDataSubType;
    }

    public RawData rawDataSubType(String rawDataSubType) {
        this.rawDataSubType = rawDataSubType;
        return this;
    }

    public void setRawDataSubType(String rawDataSubType) {
        this.rawDataSubType = rawDataSubType;
    }

    public String getRawDataSourceName() {
        return rawDataSourceName;
    }

    public RawData rawDataSourceName(String rawDataSourceName) {
        this.rawDataSourceName = rawDataSourceName;
        return this;
    }

    public void setRawDataSourceName(String rawDataSourceName) {
        this.rawDataSourceName = rawDataSourceName;
    }

    public String getRawDataSourceUri() {
        return rawDataSourceUri;
    }

    public RawData rawDataSourceUri(String rawDataSourceUri) {
        this.rawDataSourceUri = rawDataSourceUri;
        return this;
    }

    public void setRawDataSourceUri(String rawDataSourceUri) {
        this.rawDataSourceUri = rawDataSourceUri;
    }

    public String getRawDataSourceType() {
        return rawDataSourceType;
    }

    public RawData rawDataSourceType(String rawDataSourceType) {
        this.rawDataSourceType = rawDataSourceType;
        return this;
    }

    public void setRawDataSourceType(String rawDataSourceType) {
        this.rawDataSourceType = rawDataSourceType;
    }

    public String getRawDataContent() {
        return rawDataContent;
    }

    public RawData rawDataContent(String rawDataContent) {
        this.rawDataContent = rawDataContent;
        return this;
    }

    public void setRawDataContent(String rawDataContent) {
        this.rawDataContent = rawDataContent;
    }

    public Instant getRawDataCreationDate() {
        return rawDataCreationDate;
    }

    public RawData rawDataCreationDate(Instant rawDataCreationDate) {
        this.rawDataCreationDate = rawDataCreationDate;
        return this;
    }

    public void setRawDataCreationDate(Instant rawDataCreationDate) {
        this.rawDataCreationDate = rawDataCreationDate;
    }

    public Instant getRawDataExtractedDate() {
        return rawDataExtractedDate;
    }

    public RawData rawDataExtractedDate(Instant rawDataExtractedDate) {
        this.rawDataExtractedDate = rawDataExtractedDate;
        return this;
    }

    public void setRawDataExtractedDate(Instant rawDataExtractedDate) {
        this.rawDataExtractedDate = rawDataExtractedDate;
    }

    public String getRawDataSymbol() {
        return rawDataSymbol;
    }

    public RawData rawDataSymbol(String rawDataSymbol) {
        this.rawDataSymbol = rawDataSymbol;
        return this;
    }

    public void setRawDataSymbol(String rawDataSymbol) {
        this.rawDataSymbol = rawDataSymbol;
    }

    public byte[] getRawDataData() {
        return rawDataData;
    }

    public RawData rawDataData(byte[] rawDataData) {
        this.rawDataData = rawDataData;
        return this;
    }

    public void setRawDataData(byte[] rawDataData) {
        this.rawDataData = rawDataData;
    }

    public String getRawDataDataContentType() {
        return rawDataDataContentType;
    }

    public RawData rawDataDataContentType(String rawDataDataContentType) {
        this.rawDataDataContentType = rawDataDataContentType;
        return this;
    }

    public void setRawDataDataContentType(String rawDataDataContentType) {
        this.rawDataDataContentType = rawDataDataContentType;
    }

    public String getRawDataCoordinates() {
        return rawDataCoordinates;
    }

    public RawData rawDataCoordinates(String rawDataCoordinates) {
        this.rawDataCoordinates = rawDataCoordinates;
        return this;
    }

    public void setRawDataCoordinates(String rawDataCoordinates) {
        this.rawDataCoordinates = rawDataCoordinates;
    }

    public String getRawDataAnnotations() {
        return rawDataAnnotations;
    }

    public RawData rawDataAnnotations(String rawDataAnnotations) {
        this.rawDataAnnotations = rawDataAnnotations;
        return this;
    }

    public void setRawDataAnnotations(String rawDataAnnotations) {
        this.rawDataAnnotations = rawDataAnnotations;
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
        RawData rawData = (RawData) o;
        if (rawData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rawData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RawData{" +
            "id=" + getId() +
            ", rawDataName='" + getRawDataName() + "'" +
            ", rawDataType='" + getRawDataType() + "'" +
            ", rawDataSubType='" + getRawDataSubType() + "'" +
            ", rawDataSourceName='" + getRawDataSourceName() + "'" +
            ", rawDataSourceUri='" + getRawDataSourceUri() + "'" +
            ", rawDataSourceType='" + getRawDataSourceType() + "'" +
            ", rawDataContent='" + getRawDataContent() + "'" +
            ", rawDataCreationDate='" + getRawDataCreationDate() + "'" +
            ", rawDataExtractedDate='" + getRawDataExtractedDate() + "'" +
            ", rawDataSymbol='" + getRawDataSymbol() + "'" +
            ", rawDataData='" + getRawDataData() + "'" +
            ", rawDataDataContentType='" + getRawDataDataContentType() + "'" +
            ", rawDataCoordinates='" + getRawDataCoordinates() + "'" +
            ", rawDataAnnotations='" + getRawDataAnnotations() + "'" +
            "}";
    }
}
