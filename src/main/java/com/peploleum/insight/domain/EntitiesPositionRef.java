package com.peploleum.insight.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by GFOLGOAS on 18/03/2019.
 */
public class EntitiesPositionRef implements Serializable {
    private String idMongo;
    private String idJanus;
    private Integer position;
    private String entityWord;
    private String entityType;

    @JsonCreator
    public EntitiesPositionRef(@JsonProperty("idMongo") String idMongo, @JsonProperty("idJanus") String idJanus,
                               @JsonProperty("position") Integer position, @JsonProperty("entityWord") String entityWord,
                               @JsonProperty("entityType") String entityType) {
        this.idMongo = idMongo;
        this.idJanus = idJanus;
        this.position = position;
        this.entityWord = entityWord;
        this.entityType = entityType;
    }

    public String getIdMongo() {
        return idMongo;
    }

    public void setIdMongo(String idMongo) {
        this.idMongo = idMongo;
    }

    public String getIdJanus() {
        return idJanus;
    }

    public void setIdJanus(String idJanus) {
        this.idJanus = idJanus;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getEntityWord() {
        return entityWord;
    }

    public void setEntityWord(String entityWord) {
        this.entityWord = entityWord;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
