package com.peploleum.insight.domain.graphy;

import com.microsoft.spring.data.gremlin.annotation.EdgeSet;
import com.microsoft.spring.data.gremlin.annotation.GeneratedValue;
import com.microsoft.spring.data.gremlin.annotation.VertexSet;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public class Network {
    @Id
    @GeneratedValue
    private String id;
    @EdgeSet
    private List<Object> edges;
    @VertexSet
    private List<Object> vertexes;

    public Network() {
        this.edges = new ArrayList<>();
        this.vertexes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getEdges() {
        return edges;
    }

    public void setEdges(List<Object> edges) {
        this.edges = edges;
    }

    public List<Object> getVertexes() {
        return vertexes;
    }

    public void setVertexes(List<Object> vertexes) {
        this.vertexes = vertexes;
    }
}
