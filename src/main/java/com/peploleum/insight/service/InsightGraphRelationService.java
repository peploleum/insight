package com.peploleum.insight.service;

import java.util.LinkedHashMap;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface InsightGraphRelationService {
    String save(Long idSource, Long idCible, String name);

    /**
     * Get one relation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    LinkedHashMap findOne(String id);

    void linkAll();

    /**
     * Delete the entity by id.
     *
     * @param id the id of the entity
     */

    void delete(String id);
}
