package com.peploleum.insight.domain.kibana;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

/**
 * Metadata des KibanaObject
 */
public class KibanaObjectMetadata implements Serializable {

    @JsonRawValue
    @JsonProperty("searchSourceJSON")
    @JsonSerialize(using = ToStringSerializer.class)
    private String searchSourceJSON;

    public KibanaObjectMetadata() {}

    public String getSearchSourceJSON() {
        return searchSourceJSON;
    }

    public void setSearchSourceJSON(String searchSourceJSON) {
        this.searchSourceJSON = searchSourceJSON;
    }
}
