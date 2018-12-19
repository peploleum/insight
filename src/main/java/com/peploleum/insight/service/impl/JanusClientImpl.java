package com.peploleum.insight.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ehcache.xml.model.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class JanusClientImpl {

    public final static Logger log = LoggerFactory.getLogger(JanusClientImpl.class);

    public static void main(String[] args) {

        // ./bin/gremlin-server.sh ./conf/gremlin-server/gremlin-server-configuration.yaml

        Runnable target = new Runnable() {
            @Override
            public void run() {
                final String command1 = "map = new HashMap();map.put('storage.backend', 'cql');map.put('storage.hostname', 'cassandra');map.put('graph.graphname', 'testa');ConfiguredGraphFactory.createConfiguration(new MapConfiguration(map));";
                GremlinObject gremlinObject = new GremlinObject(command1);
                final RestTemplate rt = new RestTemplate();
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                ObjectMapper mapperObj = new ObjectMapper();
                mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                try {
                    String jsonStr = mapperObj.writeValueAsString(gremlinObject);
                    HttpEntity<Object> entity = new HttpEntity<Object>(jsonStr, headers);
                    String url = "http://localhost:8182";
                    JanusClientImpl.log.info("Calling " + url);

                    final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
                    JanusClientImpl.log.info("Received " + tResponseEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int cpt = 0;
                while (cpt < 10000) {
                    GremlinObject newObject = new GremlinObject("def g=ConfiguredGraphFactory.open('testa'); for(i = 0; i < 1000; i++) { g.addVertex(label, 'book');  }");
                    mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    try {
                        String jsonStr = mapperObj.writeValueAsString(newObject);
                        System.out.println(jsonStr);
                        HttpEntity<Object> entity = new HttpEntity<Object>(jsonStr, headers);

                        String url = "http://localhost:8182";

                        JanusClientImpl.log.info("Calling " + url);

                        final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
                        JanusClientImpl.log.info("Received " + tResponseEntity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cpt++;
                }
            }
        };
        //for (int i = 0; i < 10; i++) {
        new Thread(target).start();
        System.out.println("new Thread started");
        //}





        //TODO parcourir tous les Vertex, pour chaque vertex créer un edge vers tous les autres

        // g.addV().property('name','A').as('a').addV().property('name','B').as('b').addE('next').from('a').to('b').iterate()
        // g.V().has('name', 'A').outE().inV().path()
        // g.vertices('4224').next().addEdge('deteste', g.vertices('4128').next())
        // def frenchChefCookbook = g.addVertex(label, 'book', 'name', 'The French Chef Cookbook', 'year' , 1968, 'ISBN', '0-394-40135-2'); juliaChild = g.vertices('8272').next(); juliaChild.addEdge('authored', frenchChefCookbook)
        // l=g.vertices(); v=g.vertices(); l.each{ v.each { it.addEdge('knows well', g.vertices(it).next()) } }
        // def l=[]; l.add( g.vertices('4224').next()); l.add( g.vertices('4128').next()); l.remove(g.vertices('4128').next()); l.each{ def ad = it; println(ad)
        // def allNode = g.vertices(); def toDoList = g.vertices(); allNode.each { def currentNode = it; toDoList.remove(currentNode); toDoList.each{ it.addEdge('voit', currentNode);}}
        // g.addVertex(label, 'person', 'name', 'Bob'); g.addVertex(label, 'person', 'name', 'Roger'); g.addVertex(label, 'person', 'name', 'Kevin'); g.addVertex(label, 'person', 'name', 'Dylan');
        // def g=ConfiguredGraphFactory.drop('testo')
        // toDoList = []; toDoList.add(g.V('8432').next()); toDoList.add(g.V('4264').next()); toDoList.add(g.V('4168').next()); toDoList.add(g.V('4336').next());
        // g.vertices().each { toDoList.add(it) }
        // ajout d'arêtes entre tous les noeuds sans boucles
        // def allNode = g.vertices(); def toDoList = []; g.vertices().each { toDoList.add(it) }; allNode.each { def currentNode = it; toDoList.remove(currentNode); toDoList.each{ it.addEdge('voit', currentNode)}; toDoList.add(currentNode)}
        // for(i = 0; i < 5; i++) { g.addVertex(label, 'person', 'name', i);  }
        // def v1 = g.addV().property('name', '" + UUID.randomUUID().toString() + "')")
    }

    public static class GremlinObject {
        String gremlin;

        public GremlinObject(String content) {
            this.gremlin = content;
        }
    }


}
