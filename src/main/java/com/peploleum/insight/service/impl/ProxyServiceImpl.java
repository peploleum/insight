package com.peploleum.insight.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service Implementation for proxying request to external API.
 */
@Service
public class ProxyServiceImpl {

    private final Logger log = LoggerFactory.getLogger(ProxyServiceImpl.class);

    @Value("${application.houston.uri}")
    private String HOUSTON_API_URI;

    public ProxyServiceImpl() {
    }

    public String postFormToHouston(final String form) throws RestClientException {
        try {
            final RestTemplate rt = new RestTemplate();
            final HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> postVariableRegistryEntry = new HttpEntity<>(form, jsonHeaders);

            final StringBuilder sb = new StringBuilder(this.HOUSTON_API_URI);
            sb.append("/start_process94A");

            final ResponseEntity<String> tResponseEntity = rt.postForEntity(sb.toString(), postVariableRegistryEntry, String.class);

            return tResponseEntity.getBody();

        } catch (RestClientException e) {
            throw e;
        }
    }
}
