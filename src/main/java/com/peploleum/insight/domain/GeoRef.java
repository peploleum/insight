package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GFOLGOAS on 15/02/2019.
 */
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gazetter", type = "doc")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoRef implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String asciiname;

    @GeoPointField
    private List<Double> location;

    /**
     * Toutes propriétés inconnues seront ajoutées à properties
     **/
    private Map<String, String> properties = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsciiname() {
        return asciiname;
    }

    public void setAsciiname(String asciiname) {
        this.asciiname = asciiname;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setProperties(String name, String value) {
        this.properties.put(name, value);
    }
}
