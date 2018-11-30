package com.peploleum.insight.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class JanusClientImpl {

    public final static Logger log = LoggerFactory.getLogger(JanusClientImpl.class);

    public static void main(String[] args) {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8182";
        JanusClientImpl.log.info("Calling " + url);
        final ResponseEntity<String> tResponseEntity = rt.getForEntity(url, String.class);
        JanusClientImpl.log.info("Received " + tResponseEntity);
    }
}
