package com.peploleum.insight.service;

import java.util.LinkedHashMap;

/**
 * Created by GFOLGOAS on 01/04/2019.
 */
public interface InsightGraphRelationService {
    public String save(Long idSource, Long idCible, String name);

    /**
     * Get one entity by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public LinkedHashMap findOne(String id);

    public void linkAll();

    /**
     * Delete the entity by id.
     *
     * @param id the id of the entity
     */

    public void delete(String id);
}
