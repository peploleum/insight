package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.GraphyService;
import com.peploleum.insight.service.dto.InsightGraphRelationDTO;
import com.peploleum.insight.service.util.GraphyHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

@Service
public class GraphyServiceImpl implements GraphyService {

    private String apiRootUrl;
    @Value("${application.graph.enabled}")
    private boolean graphEnabled;

    @Value("${application.graph.host}")
    private String graphHost;

    @Value("${application.graph.port}")
    private int graphPort;

    private final Logger log = LoggerFactory.getLogger(GraphyServiceImpl.class);

    public GraphyServiceImpl() {
    }

    @PostConstruct
    public void setup() {
        this.apiRootUrl = "http://" + this.graphHost + ":" + this.graphPort + "/api/";
    }

    @Override
    public String create(Object entity) throws RestClientException {
        this.log.debug("Sending Entity " + entity);
        return this.doSend(entity);
    }

    @Override
    public void sendRelationToGraphy(String idSource, String idTarget, String sourceType, String targetType) throws RestClientException {
        this.log.debug("Sending relation " + idSource + " to " + idTarget + " typeSource: " + sourceType + " typeTarget: " + targetType);
        this.doSendRelation(idSource, idTarget, sourceType, targetType);
    }


    @Override
    public void createRelation(Object sourceDTO, Object targetDTO) throws Exception {
        try {
            final Object sourceExternalIdValue = extractFieldValue(sourceDTO);
            final Object targetExternalIdValue = extractFieldValue(targetDTO);
            this.doSendRelation(sourceExternalIdValue.toString(), targetExternalIdValue.toString(), TypeResolver.resolve(sourceDTO), TypeResolver.resolve(targetDTO));
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
            throw e;
        }
    }

    private Object extractFieldValue(Object dto) throws IllegalAccessException {
        Field sourceExternalIdField = org.springframework.util.ReflectionUtils.findField(dto.getClass(), "externalId");
        org.springframework.util.ReflectionUtils.makeAccessible(sourceExternalIdField);
        return sourceExternalIdField.get(dto);
    }

    private String doSendRelation(String idSource, String idTarget, String sourceType, String targetType) throws RestClientException {
        this.log.info("Creating relation between " + idSource + "/" + sourceType + " and " + idTarget + "/" + targetType);
        final InsightGraphRelationDTO insightGraphRelationDTO = new InsightGraphRelationDTO();
        insightGraphRelationDTO.setIdJanusSource(idSource);
        insightGraphRelationDTO.setIdJanusCible(idTarget);
        insightGraphRelationDTO.setName("linked to");
        insightGraphRelationDTO.setTypeSource(sourceType);
        insightGraphRelationDTO.setTypeCible(targetType);

        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = GraphyHttpUtils.getBasicHeaders();
        final ResponseEntity<String> tResponseEntity;
        try {
            tResponseEntity = rt.exchange(this.apiRootUrl + "relation", HttpMethod.POST,
                new HttpEntity<>(insightGraphRelationDTO, headers), String.class);
            log.debug("Received " + tResponseEntity.getBody());
            return tResponseEntity.getBody();
        } catch (RestClientException e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }

    private String doSend(final Object dto) throws RestClientException {
        final RestTemplate rt = new RestTemplate();
        final HttpHeaders headers = GraphyHttpUtils.getBasicHeaders();
        final ResponseEntity<String> tResponseEntity;
        try {
            final String graphyEnpointSuffix = GraphyHttpUtils.getGraphyEndpointUrl(dto);
            if (graphyEnpointSuffix.isEmpty()) {
                this.log.warn("Failed to find endpoint for entity");
                return null;
            }
            final String url = this.apiRootUrl + graphyEnpointSuffix;
            this.log.debug("Sending " + dto.toString() + " to " + url);
            tResponseEntity = rt.exchange(url, HttpMethod.POST,
                new HttpEntity<>(dto, headers), String.class);
            log.debug("Received " + tResponseEntity.getBody());
            return tResponseEntity.getBody();
        } catch (RestClientException e) {
            this.log.warn("Failed to send entity");
            this.log.debug(e.getMessage(), e);
            throw e;
        }
    }
}

class TypeResolver {
    public static String resolve(Object object) {
        return object.getClass().getSimpleName().substring(0, object.getClass().getSimpleName().length() - 3);
    }
}
