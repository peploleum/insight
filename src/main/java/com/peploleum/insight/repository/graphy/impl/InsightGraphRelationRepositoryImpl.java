package com.peploleum.insight.repository.graphy.impl;

import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.microsoft.spring.data.gremlin.mapping.GremlinMappingContext;
import com.microsoft.spring.data.gremlin.query.GremlinTemplate;
import com.peploleum.insight.repository.graphy.InsightGraphRelationCustomRepository;
import com.peploleum.insight.repository.graphy.InsightGraphRelationRepository;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Persistent;

import java.util.LinkedHashMap;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public class InsightGraphRelationRepositoryImpl implements InsightGraphRelationCustomRepository {
    private final Logger log = LoggerFactory.getLogger(InsightGraphRelationRepositoryImpl.class);

    @Autowired
    InsightGraphRelationRepository insightGraphRelationRepository;
    private ApplicationContext context;
    private GremlinFactory gremlinFactory;
    private GremlinTemplate template;

    public InsightGraphRelationRepositoryImpl(ApplicationContext context, GremlinFactory gremlinFactory) {
        this.context = context;
        this.gremlinFactory = gremlinFactory;
        try {
            final GremlinMappingContext mappingContext = new GremlinMappingContext();
            mappingContext.setInitialEntitySet(new EntityScanner(this.context).scan(Persistent.class));
            final MappingGremlinConverter converter = new MappingGremlinConverter(mappingContext);
            this.template = new GremlinTemplate(this.gremlinFactory, converter);
            this.template.getGremlinClient();
        } catch (ClassNotFoundException e) {
            this.log.error("Erreur lors de l'instanciation du InsightGraphRelationRepositoryImpl", e);
        }
    }

    @Override
    public void myDeleteById(String id) {
        final ResultSet resultSet = this.template.getGremlinClient().submit("g.E('" + id + "').drop()");
    }

    @Override
    public LinkedHashMap findOne(String id) {
        final ResultSet resultSet = this.template.getGremlinClient().submit("g.E('" + id + "')");
        this.log.info("searching by id:" + id);
        LinkedHashMap resultObjTest = (LinkedHashMap) resultSet.one().getObject();
        return resultObjTest;
    }

    @Override
    public void linkAll() {
        final StringBuilder sb = new StringBuilder();
        sb.append("def allNode = graph.vertices();");
        sb.append("def toDoList = [];");
        sb.append("graph.vertices().each { toDoList.add(it) };");
        sb.append("allNode.each { def currentNode = it; toDoList.remove(currentNode); toDoList.each{ it.addEdge('voit', currentNode)}; toDoList.add(currentNode)}");
        this.template.getGremlinClient().submit(sb.toString());
    }
}
