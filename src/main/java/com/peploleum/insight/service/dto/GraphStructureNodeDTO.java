package com.peploleum.insight.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GFOLGOAS on 05/06/2019.
 */
public class GraphStructureNodeDTO implements Serializable {
    private String nodeId;
    private List<GraphStructureNodeDTO> relations;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public List<GraphStructureNodeDTO> getRelations() {
        return relations;
    }

    public void setRelations(List<GraphStructureNodeDTO> relations) {
        this.relations = relations;
    }

    @Override
    public String toString() {
        return "GraphStructureNodeDTO{" +
            "nodeId=" + getNodeId() +
            "relationIds= " + getRelations().size() + " relations" +
            "}";
    }
}
