package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * Attributes des KibanaObjects
 * Attention certain champs doivent obligatoirement être serializés en string
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"title", "hits", "visState", "uiStateJSON", "description", "panelsJSON", "optionsJSON", "version", "timeRestore", "kibanaSavedObjectMeta"})
public class KibanaObjectAttributes implements Serializable {

    //Common attributes
    @JsonProperty("title")
    private String title;
    @JsonProperty("kibanaSavedObjectMeta")
    private KibanaObjectMetadata kibanaSavedObjectMeta;
    private String description;
    private String version;

    //Index Pattern Attributes
    private String timeFieldName;
    private String notExpandable;
    private String fields;
    private String fieldFormatMap;

    //Visualization Attributes
    @JsonRawValue
    @JsonSerialize(using = ToStringSerializer.class)
    private String visState;
    @JsonRawValue
    @JsonSerialize(using = ToStringSerializer.class)
    private String uiStateJSON;

    //Dashboard Attributes
    private String hits;
    @JsonRawValue
    @JsonSerialize(using = ToStringSerializer.class)
    private String panelsJSON;
    @JsonRawValue
    @JsonSerialize(using = ToStringSerializer.class)
    private String optionsJSON;
    private String timeRestore;
    private String timeFrom;
    private String timeTo;

    public KibanaObjectAttributes() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public KibanaObjectMetadata getKibanaSavedObjectMeta() {
        return kibanaSavedObjectMeta;
    }

    public void setKibanaSavedObjectMeta(KibanaObjectMetadata kibanaSavedObjectMeta) {
        this.kibanaSavedObjectMeta = kibanaSavedObjectMeta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimeFieldName() {
        return timeFieldName;
    }

    public void setTimeFieldName(String timeFieldName) {
        this.timeFieldName = timeFieldName;
    }

    public String getNotExpandable() {
        return notExpandable;
    }

    public void setNotExpandable(String notExpandable) {
        this.notExpandable = notExpandable;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFieldFormatMap() {
        return fieldFormatMap;
    }

    public void setFieldFormatMap(String fieldFormatMap) {
        this.fieldFormatMap = fieldFormatMap;
    }

    @JsonRawValue
    public String getVisState() {
        return visState;
    }

    @JsonRawValue
    public void setVisState(String visState) {
        this.visState = visState;
    }

    public String getUiStateJSON() {
        return uiStateJSON;
    }

    public void setUiStateJSON(String uiStateJSON) {
        this.uiStateJSON = uiStateJSON;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getPanelsJSON() {
        return panelsJSON;
    }

    public void setPanelsJSON(String panelsJSON) {
        this.panelsJSON = panelsJSON;
    }

    public String getOptionsJSON() {
        return optionsJSON;
    }

    public void setOptionsJSON(String optionsJSON) {
        this.optionsJSON = optionsJSON;
    }

    public String getTimeRestore() {
        return timeRestore;
    }

    public void setTimeRestore(String timeRestore) {
        this.timeRestore = timeRestore;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
}
