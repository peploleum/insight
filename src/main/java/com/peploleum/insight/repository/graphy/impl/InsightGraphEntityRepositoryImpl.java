package com.peploleum.insight.repository.graphy.impl;

import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteralVertex;
import com.microsoft.spring.data.gremlin.mapping.GremlinMappingContext;
import com.microsoft.spring.data.gremlin.query.GremlinTemplate;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.domain.graphy.InsightGraphEntity;
import com.peploleum.insight.repository.graphy.InsightGraphEntityCustomRepository;
import com.peploleum.insight.repository.graphy.InsightGraphEntityRepository;
import com.peploleum.insight.service.dto.CriteriaDTO;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Persistent;

import java.util.*;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public class InsightGraphEntityRepositoryImpl implements InsightGraphEntityCustomRepository {
    private final Logger log = LoggerFactory.getLogger(InsightGraphEntityRepositoryImpl.class);

    @Autowired
    InsightGraphEntityRepository insightGraphEntityRepository;
    private ApplicationContext context;
    private GremlinFactory gremlinFactory;
    private GremlinTemplate template;

    public InsightGraphEntityRepositoryImpl(ApplicationContext context, GremlinFactory gremlinFactory) {
        this.context = context;
        this.gremlinFactory = gremlinFactory;
        try {
            final GremlinMappingContext mappingContext = new GremlinMappingContext();
            mappingContext.setInitialEntitySet(new EntityScanner(this.context).scan(Persistent.class));
            final MappingGremlinConverter converter = new MappingGremlinConverter(mappingContext);
            this.template = new GremlinTemplate(this.gremlinFactory, converter);
        } catch (ClassNotFoundException e) {
            this.log.error("Erreur lors de l'instanciation du InsightGraphEntityRepositoryImpl", e);
        }
    }

    @Override
    public List<InsightGraphEntity> findByCriteria(CriteriaDTO criteria) {
        String name = "";
        if (!criteria.getValue().isEmpty() && !criteria.getProperty().isEmpty())
            name = "." + GremlinScriptLiteralVertex.generateHas(criteria.getProperty(), criteria.getValue());
        String label = "";
        if (!criteria.getLabel().isEmpty())
            label = ".has(label,'" + criteria.getLabel() + "' )";
        this.log.info("Label : " + label);
        final ResultSet resultSet = this.template.getGremlinClient().submit("g.V()" + label + name);
        this.log.info("searching by criteria:" + name);
        final List<InsightGraphEntity> graphEntityList = new ArrayList<>();
        resultSet.stream().forEach(key0 -> {
                this.log.info("key0 : " + key0);
                graphEntityList.add(extract(key0));
            }
        );

        return graphEntityList;
    }

    @Override
    public InsightGraphEntity findOne(Long id) {
        return null;
    }

    @Override
    public List<InsightGraphEntity> findAllInOutVerticesByCriteria(Long id, CriteriaDTO criteria) {
        String name = "";
        if (!criteria.getValue().isEmpty() && !criteria.getProperty().isEmpty())
            name = "." + GremlinScriptLiteralVertex.generateHas(criteria.getProperty(), criteria.getValue());
        String label = "";
        if (!criteria.getLabel().isEmpty())
            label = ".has(label,'" + criteria.getLabel() + "' )";
        final ResultSet resultSet = this.template.getGremlinClient().submit("g.V(" + id + ").both()" + label + name);
        this.log.info("searching by criteria:" + name);
        final List<InsightGraphEntity> insightGraphEntities = new ArrayList<>();

        resultSet.stream().forEach(key0 -> {
                this.log.info("key0 : " + key0);
                insightGraphEntities.add(extract(key0));
            }
        );
        return insightGraphEntities;
    }

    private InsightGraphEntity extract(Result key0) {
        final InsightGraphEntity graphEntity = new InsightGraphEntity();
        final Map<String, String> emptyMap = new HashMap<String, String>();
        graphEntity.setProperties(emptyMap);
        LinkedHashMap resultObjTest = (LinkedHashMap) key0.getObject();
        resultObjTest.keySet().stream().forEach((key -> {
            this.log.info(key + " - " + resultObjTest.get(key).toString());
            if (key.toString().equals("id"))
                graphEntity.setGraphId(Long.valueOf(resultObjTest.get(key).toString()));
            if (key.toString().equals("label"))
                graphEntity.setEntityType(InsightEntityType.valueOf(resultObjTest.get(key).toString()));
            if (resultObjTest.get(key) instanceof LinkedHashMap) {
                final LinkedHashMap resultObject = (LinkedHashMap) resultObjTest.get(key);
                resultObject.keySet().stream().forEach((key1 -> {
                    ArrayList resultArray = (ArrayList) resultObject.get(key1);
                    LinkedHashMap linkedHashMap = (LinkedHashMap) resultArray.get(0);
                    this.log.info(key1 + " - " + linkedHashMap);
                    linkedHashMap.keySet().stream().forEach((key2 -> {
                        if (key2.toString().equals("value")) {
                            if (key1.toString().equals("idMongo"))
                                graphEntity.setIdMongo(linkedHashMap.get(key2).toString());
                            graphEntity.getProperties().put(key1.toString(), linkedHashMap.get(key2).toString());
                        }
                        if (key2.toString().equals("value"))
                            this.log.info(key2 + " - " + linkedHashMap.get(key2).toString());
                    }));

                }));
            }
        }));
        return graphEntity;
    }

    @Override
    public InsightGraphEntity saveWithProperties(InsightGraphEntity entity) {
        StringBuilder propertiesString = new StringBuilder();
        for (Map.Entry<String, String> props : entity.getProperties().entrySet()) {
            if(props.getKey().equals("motclef")){
                List<String> myList = new ArrayList<String>(Arrays.asList(props.getValue().split(" ")));
                String stringListMotsClefs = "'"+String.join("\', \'",  myList)+"'";
                propertiesString.append(".property('").append(props.getKey()).append("', [").append(stringListMotsClefs).append("] as String[])");
            } else if(props.getKey().equals("symbole")) {
                if (props.getValue() != null) {
                    propertiesString.append(".property('").append(props.getKey()).append("', '").append(props.getValue()).append("')");
                }
            } else {
                propertiesString.append(".property('").append(props.getKey()).append("', '").append(props.getValue()).append("')");
            }
        }

        final ResultSet resultSet = this.template.getGremlinClient().submit(
            "g.addV(\'"+entity.getClass().getSimpleName()+"\')" +
            ".property('entityType', '\""+entity.getEntityType()+"\"')" +
            ".property('name', '"+entity.getName()+"')" +
            ".property('idMongo', '"+entity.getIdMongo()+"')" +
            ".property('_classname', '"+entity.getClass().getName()+"')" +
            propertiesString);
        resultSet.stream().forEach(result -> {
            final LinkedHashMap resultObject = (LinkedHashMap) result.getObject();
            final String graphId = resultObject.get("id").toString();
            entity.setGraphId(Long.valueOf(graphId));
        });
        return entity;
    }
}
