package com.peploleum.insight.domain.graphy;

import com.microsoft.spring.data.gremlin.annotation.GeneratedValue;
import com.microsoft.spring.data.gremlin.annotation.Vertex;
import com.peploleum.insight.domain.InsightEntity;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@Vertex
public class InsightGraphEntity extends InsightEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long graphId;
    private String idMongo;
    private String name;
    private Map<String, String> properties;

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getIdMongo() {
        return idMongo;
    }

    public void setIdMongo(String idMongo) {
        this.idMongo = idMongo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
