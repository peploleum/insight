package com.peploleum.insight.service;

import org.springframework.web.client.RestClientException;

import java.io.IOException;

/**
 * Interface for managing interactions with Graph RESTful API
 */
public interface GraphyService {

    /**
     * Creates an object in Graphy db
     *
     * @param entity a domain mapped DTO
     * @return unique identifier in Graphy db
     * @throws IOException
     */
    String create(Object entity) throws RestClientException;

    /**
     * Creates a relation between source and target objects based on unique ids and types
     *
     * @param idSource   unique id in graph db of the source object
     * @param idTarget   unique id in grahp db of the target object
     * @param sourceType type of the source object
     * @param targetType type of the destination object
     */
    void sendRelationToGraphy(String idSource, String idTarget, String sourceType, String targetType) throws RestClientException;

    /**
     * Creates a relation in graph db between two objects
     *
     * @param sourceDTO e.g. BiographicsDTO
     * @param targetDTO e.g. RawDataDTO
     * @throws RestClientException
     */
    void createRelation(final Object sourceDTO, final Object targetDTO) throws Exception;
}
