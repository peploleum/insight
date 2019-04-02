package com.peploleum.insight.domain.graphy;

import com.microsoft.spring.data.gremlin.annotation.Edge;
import com.microsoft.spring.data.gremlin.annotation.EdgeFrom;
import com.microsoft.spring.data.gremlin.annotation.EdgeTo;
import com.microsoft.spring.data.gremlin.annotation.GeneratedValue;
import org.springframework.data.annotation.Id;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@Edge
public class InsightGraphRelation {
    @Id
    @GeneratedValue
    private String id;

    private String name;

    @EdgeFrom
    private InsightGraphEntity objectFrom;

    @EdgeTo
    private InsightGraphEntity objectTo;

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

    public InsightGraphEntity getObjectFrom() {
        return objectFrom;
    }

    public void setObjectFrom(InsightGraphEntity objectFrom) {
        this.objectFrom = objectFrom;
    }

    public InsightGraphEntity getObjectTo() {
        return objectTo;
    }

    public void setObjectTo(InsightGraphEntity objectTo) {
        this.objectTo = objectTo;
    }
}
