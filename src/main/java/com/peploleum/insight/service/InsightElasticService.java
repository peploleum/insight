package com.peploleum.insight.service;

import com.peploleum.insight.domain.InsightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by GFOLGOAS on 04/04/2019.
 */
public interface InsightElasticService {
    /**
     * Search for the InsightEntities corresponding to the query.
     *
     * @param query    the query of the search
     * @param clazz    the class of the searched entities (Es index)
     * @param pageable the pagination information
     * @return the list of entities
     */
    <T extends InsightEntity> Page<T> search(String query, Class<T> clazz, Pageable pageable);

    /**
     * Autocomplete based on a search keyword
     * Suggestion only works on fields annotated @CompletionField
     *
     * @param query the keyword of the search
     * @param clazz the class of the searched entities (Es index)
     * @return the list of suggestions
     */
    <T extends InsightEntity> List<String> autoComplete(String query, Class<T> clazz);
}
