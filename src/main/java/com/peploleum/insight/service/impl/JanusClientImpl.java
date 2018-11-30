package com.peploleum.insight.service.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JanusClientImpl {

    public final static Logger log = LoggerFactory.getLogger(JanusClientImpl.class);

    public static void main(String[] args) {


        GremlinObject gremlinObject = new GremlinObject("map = new HashMap();map.put('storage.backend', 'cql');map.put('storage.hostname', 'cassandra');map.put('graph.graphname', 'test');ConfiguredGraphFactory.createConfiguration(new MapConfiguration(map));");
        JSONObject gremlinJSON = new JSONObject(gremlinObject);
        ObjectMapper mapperObj = new ObjectMapper();
        mapperObj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            String jsonStr = mapperObj.writeValueAsString(gremlinObject);
            System.out.println(jsonStr);
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Object> entity = new HttpEntity<Object>(jsonStr, headers);

            String url = "http://localhost:8182";

            JanusClientImpl.log.info("Calling " + url);

            final ResponseEntity<String> tResponseEntity = rt.exchange(url, HttpMethod.POST, entity, String.class);
            JanusClientImpl.log.info("Received " + tResponseEntity);
        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    public static class GremlinObject {
        String gremlin;
        public GremlinObject(String content){
            this.gremlin = content;
        }
    }


}
