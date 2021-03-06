package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.GraphStructureNodeDTO;
import com.peploleum.insight.service.dto.NodeDTO;

import java.util.List;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface TraversalService {
    /**
     * Fetches the neighbors for a given node. Source vertex is chosen based on 'id' property and all outgoing edges are walked (limit 50)
     *
     * @param node
     * @return the neighbors
     */
    public List<NodeDTO> getNeighbors(final NodeDTO node);

    public NodeDTO getByMongoId(final String mongoId);

    public NodeDTO getByJanusId(final String id);


    /**
     * Fetches the neighbors for a given node. Source vertex is chosen based on 'id' property and all outgoing edges are walked (limit 50)
     *
     * @param id
     * @return the neighbors
     */
    public List<NodeDTO> getNeighborsMock(final String id);

    public GraphStructureNodeDTO getGraph(final NodeDTO node, final int levelOrder);
}
