package com.peploleum.insight.domain.dictionary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "motclef"
})
public class Theme implements Serializable {

    private String name;
    @JsonProperty("motclef")
    private List<Motclef> motclef = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

       public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("motclef")
    public List<Motclef> getMotclef() {
        return motclef;
    }

    @JsonProperty("motclef")
    public void setMotclef(List<Motclef> motclef) {
        this.motclef = motclef;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
