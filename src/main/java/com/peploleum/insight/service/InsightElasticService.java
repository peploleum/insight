package com.peploleum.insight.service;

import com.peploleum.insight.domain.InsightEntity;
import org.elasticsearch.common.geo.builders.EnvelopeBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by GFOLGOAS on 04/04/2019.
 */
public interface InsightElasticService {
    /**
     * Search for the InsightEntities matching the query.
     *
     * @param query    the query of the search
     * @param clazz    the class of the searched entities (Es index)
     * @param pageable the pagination information
     * @return the list of entities
     */
    <T extends InsightEntity> Page<InsightEntity> search(String query, Class<T> clazz, Pageable pageable);

    /**
     * Autocomplete based on a search keyword
     * Suggestion only works on fields of Type Completion annotated @CompletionField
     * autoComplete ne fonctionne pas pour le moment, pas certain que le paramètre "suggest"
     * des requêtes ES soient prévu avec la library Jest que l'on utilise => c'est bien dommage
     *
     * @param query the keyword of the search
     * @param clazz the class of the searched entities (Es index)
     * @return the list of suggestions
     */
    <T extends InsightEntity> List<String> autoComplete(String query, Class<T> clazz);

    /**
     * Search for the InsightEntities matching the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @param indices  indices to search from
     * @return the list of entities
     */
    <T extends InsightEntity> Page<InsightEntity> search(String query, List<String> indices, Pageable pageable);

    /**
     * Search for the InsightEntities matching the query.
     *
     * @param query           the query of the search
     * @param pageable        the pagination information
     * @param envelopeBuilder {@link EnvelopeBuilder} extent to geo search in
     * @param indices         indices to search from
     * @return the list of entities
     */
    <T extends InsightEntity> Page<InsightEntity> search(String query, List<String> indices, EnvelopeBuilder envelopeBuilder, Pageable pageable);
}
