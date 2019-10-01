package com.peploleum.insight.service.impl.graphyImpl;

import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteralVertex;
import com.microsoft.spring.data.gremlin.mapping.GremlinMappingContext;
import com.microsoft.spring.data.gremlin.query.GremlinTemplate;
import com.peploleum.insight.service.TraversalService;
import com.peploleum.insight.service.dto.GraphStructureNodeDTO;
import com.peploleum.insight.service.dto.NodeDTO;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.Persistent;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
@Service
@Profile("graphy")
public class TraversalServiceImpl implements TraversalService {
    private final Logger log = LoggerFactory.getLogger(TraversalServiceImpl.class);

    private ApplicationContext context;
    private GremlinFactory gremlinFactory;
    private GremlinTemplate template;

    public TraversalServiceImpl(final ApplicationContext context, final GremlinFactory gremlinFactory) {
        this.context = context;
        this.gremlinFactory = gremlinFactory;
        try {
            final GremlinMappingContext mappingContext = new GremlinMappingContext();
            mappingContext.setInitialEntitySet(new EntityScanner(this.context).scan(Persistent.class));
            final MappingGremlinConverter converter = new MappingGremlinConverter(mappingContext);
            this.template = new GremlinTemplate(this.gremlinFactory, converter);
        } catch (ClassNotFoundException e) {
            this.log.error("Erreur lors de l'instanciation du TraversalServiceImpl", e);
        }
    }

    @Override
    public List<NodeDTO> getNeighbors(NodeDTO node) {
        final List<NodeDTO> nodeList = new ArrayList<>();
        final String id = node.getId();
        this.log.info("searching by GraphId: " + id + " and traversing outgoing edges to get neighbors");
        // final String neighborSuffix = ".outE().limit(50).inV().toList()";
        final String neighborSuffix = ".outE().inV().toList()";
        final ResultSet neighborResultSet = this.template.getGremlinClient().submit("g.V(" + id + ")" + neighborSuffix);
        this.log.info("Parsing neighbors");
        neighborResultSet.stream().forEach(result -> {
            this.log.info("------------");
            final LinkedHashMap resultObject = (LinkedHashMap) result.getObject();
            resultObject.keySet().stream().forEach((key -> {
                this.log.info(key + " - " + resultObject.get(key).toString());
            }));
            final NodeDTO neighbor = new NodeDTO();

            final String graphId = resultObject.get("id").toString();
            final String idMongo = smartOpenProperties(resultObject, "idMongo");
            final String type = smartOpenProperties(resultObject, "entityType").replace("\"", "");
            final String symbole = smartOpenProperties(resultObject, "symbole");
            final String name = smartOpenProperties(resultObject, "name");
            final String rawDataSubType = smartOpenProperties(resultObject, "rawDataSubType");
            if(type.equals("RawData")) {
                if(rawDataSubType != null && rawDataSubType.equals("url")) {
                    final ArrayList<String> listMotsClefs = getMapMotClef(resultObject);
                    final String imageHit = smartOpenProperties(resultObject, "imageHit");
                    final String frequence = smartOpenProperties(resultObject, "frequence");
                    final String points = smartOpenProperties(resultObject, "points");
                    final String rawDataUrl = smartOpenProperties(resultObject, "rawDataUrl");
                    HashMap<String, Object> properties = new HashMap<>();
                    properties.put("imageHit", imageHit);
                    properties.put("frequence", frequence);
                    properties.put("points", points);
                    properties.put("listMotsClefs", listMotsClefs);
                    properties.put("rawDataUrl", rawDataUrl);
                    neighbor.setProperties(properties);
                }
            }
            neighbor.setType(type);
            neighbor.setId(graphId);
            neighbor.setIdMongo(idMongo);
            neighbor.setSymbole(symbole);
            neighbor.setLabel(name);


            nodeList.add(neighbor);
            this.log.info("adding node: " + neighbor.toString());
        });
        return nodeList;
    }

    @Override
    public NodeDTO getByMongoId(String mongoId) {
        this.log.info("Searching Node with idMongo property: " + mongoId);
        final String mongoIdQuery = GremlinScriptLiteralVertex.generateHas("idMongo", mongoId);
        final String gremlinQuery = "g.V()." + mongoIdQuery + ".next()";
        final NodeDTO foundNode = internalGetNode(gremlinQuery);
        if (foundNode == null) return null;
        return foundNode;
    }

    @Override
    public NodeDTO getByJanusId(String id) {
        this.log.info("Searching Node with janusId property: " + id);
        final String gremlinQuery = "g.V(" + id + ")";
        final NodeDTO foundNode = internalGetNode(gremlinQuery);
        if (foundNode == null) return null;
        return foundNode;
    }

    @Override
    public List<NodeDTO> getNeighborsMock(String id) {
        final List<NodeDTO> neighbors = generateNeighbors();
        return neighbors;
    }

    @Override
    public List<NodeDTO> getNeighborsByProperty(NodeDTO node) {
        return null;
    }



    @Override
    public GraphStructureNodeDTO getGraph(NodeDTO node, int levelOrder) {

       /* final List<NodeDTO> nodeList = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();
        sb.append(".group().by().by(outE().group().by().by(outE().dedup().fold()).dedup().fold())");
        final ResultSet neighborResultSet = this.template.getGremlinClient().submit("g.V(" + node.getId() + ")" + sb.toString());

        neighborResultSet.stream().forEach(result -> {
            final LinkedHashMap resultObject = (LinkedHashMap) result.getObject();
            final NodeDTO neighbor = new NodeDTO();

            final String graphId = resultObject.get("id").toString();
            final String idMongo = smartOpenProperties(resultObject, "idMongo");
            final String type = smartOpenProperties(resultObject, "entityType").replace("\"", "");
            final String symbole = smartOpenProperties(resultObject, "symbole");
            final String name = smartOpenProperties(resultObject, "name");

            neighbor.setType(type);
            neighbor.setId(graphId);
            neighbor.setIdMongo(idMongo);
            neighbor.setSymbole(symbole);
            neighbor.setLabel(name);

            nodeList.add(neighbor);
        });

        return null;*/

        GraphStructureNodeDTO graphRoot = new GraphStructureNodeDTO();
        graphRoot.setNodeId(node.getIdMongo());
        graphRoot.setRelations(this.getGraphElementNeighbors(node, 1, levelOrder));
        return graphRoot;
    }

    private NodeDTO extractNodeDto(final LinkedHashMap resultObject) {
        final NodeDTO node = new NodeDTO();

        final String graphId = resultObject.get("id").toString();
        final String idMongo = smartOpenProperties(resultObject, "idMongo");
        final String type = smartOpenProperties(resultObject, "entityType").replace("\"", "");
        final String symbole = smartOpenProperties(resultObject, "symbole");
        final String name = smartOpenProperties(resultObject, "name");

        node.setType(type);
        node.setId(graphId);
        node.setIdMongo(idMongo);
        node.setSymbole(symbole);
        node.setLabel(name);

        return node;
    }

    private List<GraphStructureNodeDTO> getGraphElementNeighbors(NodeDTO parentNode, int currentDepthLevel, int maxDepthLevel) {
        Map<String, NodeDTO> neighbors = this.getNeighbors(parentNode).stream().collect(Collectors.toMap(NodeDTO::getIdMongo, n -> n, (key1, key2) -> key1));
        List<GraphStructureNodeDTO> childNodes = neighbors.values().stream().map(n -> {
            final GraphStructureNodeDTO el = new GraphStructureNodeDTO();
            el.setNodeId(n.getIdMongo());
            return el;
        }).collect(Collectors.toList());
        currentDepthLevel = currentDepthLevel + 1;
        if (currentDepthLevel <= maxDepthLevel) {
            for (GraphStructureNodeDTO childNode : childNodes) {
                List<GraphStructureNodeDTO> graphElementNeighbors = this.getGraphElementNeighbors(neighbors.get(childNode.getNodeId()), currentDepthLevel, maxDepthLevel);
                childNode.setRelations(graphElementNeighbors);
            }
        }
        return childNodes;
    }

    private NodeDTO internalGetNode(String gremlinQuery) {
        this.log.info("GremlinQuery: " + gremlinQuery);
        final ResultSet resultSet = this.template.getGremlinClient().submit(gremlinQuery);
        this.log.info("Parsing first result");
        final NodeDTO foundNode = new NodeDTO();
        final Result result = resultSet.one();
        if (result == null) {
            this.log.info("No result");
            return null;
        }
        this.log.info("Found result");
        final LinkedHashMap resultObject = (LinkedHashMap) result.getObject();
        final String id = resultObject.get("id").toString();
        foundNode.setId(id);
        foundNode.setIdMongo(smartOpenProperties(resultObject, "idMongo"));
        foundNode.setType(smartOpenProperties(resultObject, "entityType").replace("\"", ""));
        foundNode.setSymbole(smartOpenProperties(resultObject, "symbole"));
        try {
            final String name = smartOpenProperties(resultObject, "name");
            foundNode.setLabel(name);
        } catch (Exception e) {
            this.log.warn("failed to extract name of janus entity");
            final String searchKey = "properties";
            this.log.info("Searching key: " + searchKey);
            if (searchKey != null) {
                final String label = smartOpenProperties(resultObject, searchKey);
                if (label != null) {
                    foundNode.setLabel(label);
                    this.log.info("found node: " + foundNode.toString());
                }
            }
        }

        return foundNode;
    }

    private String smartOpenProperties(LinkedHashMap object, String key) {
        final LinkedHashMap properties = (LinkedHashMap) object.get("properties");
        if (properties != null) {
            final Object prop = properties.get(key);
            if (prop != null) {
                final LinkedHashMap propertyList = (LinkedHashMap) ((ArrayList) prop).get(0);
                if (propertyList != null) {
                    return propertyList.get("value").toString();
                }
            }
        }
        return null;
    }

    private ArrayList<String> getMapMotClef(LinkedHashMap object){

        final LinkedHashMap properties = (LinkedHashMap) object.get("properties");
        if (properties != null) {
            final Object linkedHashMapMotsclefs = properties.get("motclef");
            if (linkedHashMapMotsclefs != null) {
                final LinkedHashMap propertyList = (LinkedHashMap) ((ArrayList) linkedHashMapMotsclefs).get(0);
                if (propertyList != null) {
                    return (ArrayList<String>) propertyList.get("value");
                }
            }
        }
        return null;
    }

    public List<NodeDTO> getAllUnlinkedVertices(String relationName) {
        this.log.info("Searching unlinked: " + relationName + " vertices");
        final List<NodeDTO> nodeList = new ArrayList<>();
        final ResultSet unlinkedVerticesSet = this.template.getGremlinClient().submit("g.V().not(inE())");
        this.log.info("Parsing vertices");
        unlinkedVerticesSet.stream().forEach(result -> {
            this.log.info("-------------");
            final LinkedHashMap resultObject = (LinkedHashMap) result.getObject();
            resultObject.keySet().stream().forEach((key -> {
                this.log.info(key + " - " + resultObject.get(key).toString());
            }));
            final NodeDTO neighbor = new NodeDTO();

            final String graphId = resultObject.get("id").toString();
            final String idMongo = smartOpenProperties(resultObject, "idMongo");
            final String type = smartOpenProperties(resultObject, "entityType").replace("\"", "");
            final String symbole = smartOpenProperties(resultObject, "symbole");
            final String name = smartOpenProperties(resultObject, "name");

            neighbor.setType(type);
            neighbor.setId(graphId);
            neighbor.setIdMongo(idMongo);
            neighbor.setSymbole(symbole);
            neighbor.setLabel(name);
            if (type.equals("rawdata")){
                nodeList.add(neighbor);
            }

            this.log.info("adding node: " + neighbor.toString());
        });
        return nodeList;
    }

    public static List<NodeDTO> generateNeighbors() {
        final List<NodeDTO> neighbors = new ArrayList<>();
        final NodeDTO bioNode = new NodeDTO();
        final String bioMongoId = UUID.randomUUID().toString();
        bioNode.setId(bioMongoId);
        bioNode.setLabel("Paul");
        bioNode.setType("Biographics");
        neighbors.add(bioNode);
        NodeDTO eventNode = new NodeDTO();
        final String eventMongoId = UUID.randomUUID().toString();
        eventNode.setId(eventMongoId);
        eventNode.setLabel("Bombing");
        eventNode.setType("Event");
        neighbors.add(eventNode);
        final String eqMongoId = UUID.randomUUID().toString();
        final NodeDTO eqNode = new NodeDTO();
        eqNode.setId(eqMongoId);
        eqNode.setLabel("Gun");
        eqNode.setType("Equipment");
        neighbors.add(eqNode);
        final NodeDTO rawDataNode = new NodeDTO();
        final String rawDataMongoId = UUID.randomUUID().toString();
        rawDataNode.setId(rawDataMongoId);
        rawDataNode.setLabel("Tweet");
        rawDataNode.setType("RawData");
        neighbors.add(rawDataNode);
        return neighbors;
    }
}
