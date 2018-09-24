package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

/**
 * Object a importer dans Kibana
 */
@JsonIgnoreProperties({""})
public class KibanaObject implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DASHBOARD = "dashboard";
    public static final String VISUALIZATION = "visualization";
    public static final String INDEX_PATTERN = "index-pattern";

    protected final String id = UUID.randomUUID().toString();
    @JsonProperty("type")
    protected String type;
    protected final String version = "1";
    protected KibanaObjectAttributes attributes = new KibanaObjectAttributes();

    @JsonCreator
    public KibanaObject(@JsonProperty("type") String type, @JsonProperty("title") String title) {
        this.type = type;
        this.attributes.setTitle(title);
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public KibanaObjectAttributes getAttributes() {
        return attributes;
    }
}
