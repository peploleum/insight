package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.InsightEntity;
import com.peploleum.insight.repository.search.InsightEntitySearchRepository;
import com.peploleum.insight.service.InsightElasticService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest.Suggestion;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry.Option;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by GFOLGOAS on 04/04/2019.
 * <p>
 * Elasticsearch Custom Queries
 */
@Service
public class InsightElasticServiceImpl implements InsightElasticService {
    private final Logger log = LoggerFactory.getLogger(InsightElasticServiceImpl.class);
    private final Integer NUMBER_SUGGESTION_TO_RETURN = 5;
    private final ElasticsearchOperations esOps;
    private final InsightEntitySearchRepository insightEntitySearchRepository;
    private final CustomSourceFilter customSourceFilter = new CustomSourceFilter();

    public InsightElasticServiceImpl(ElasticsearchOperations esOps, InsightEntitySearchRepository insightEntitySearchRepository) {
        this.esOps = esOps;
        this.insightEntitySearchRepository = insightEntitySearchRepository;
    }

    @Override
    public <T extends InsightEntity> Page<InsightEntity> search(String query, Class<T> clazz, Pageable pageable) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryStringQuery(query));
        NativeSearchQuery esQuery = searchQueryBuilder.withPageable(pageable).build();
        return (Page<InsightEntity>) this.esOps.queryForPage(esQuery, clazz);
    }

    @Override
    public <T extends InsightEntity> Page<InsightEntity> search(String query, List<String> indices, Pageable pageable) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryStringQuery(query));
        searchQueryBuilder.withIndices(indices.toArray(new String[indices.size()]));
        // exclude date from source results
        searchQueryBuilder.withSourceFilter(customSourceFilter);
        NativeSearchQuery esQuery = searchQueryBuilder.withPageable(pageable).build();
        return this.insightEntitySearchRepository.search(esQuery, indices);
    }

    @Override
    public <T extends InsightEntity> List<String> autoComplete(String query, Class<T> clazz) {
        SearchRequest searchRequest = new SearchRequest(clazz.getName().toLowerCase());
        CompletionSuggestionBuilder suggestBuilder = new CompletionSuggestionBuilder(getAutoCompleteField(clazz.getSimpleName()));
        suggestBuilder.size(NUMBER_SUGGESTION_TO_RETURN)
            .prefix(query, Fuzziness.AUTO)
            .analyzer("standard");

        final String suggestionName = "suggestion-name";
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(new SuggestBuilder().addSuggestion(suggestionName, suggestBuilder));
        searchRequest.source(sourceBuilder);

        List<String> results = new ArrayList<>();
        try {
            // getClient => fonction non implémentée avec JestElasticsearchTemplate
            Client client = this.esOps.getClient();
            SearchResponse response = client.search(searchRequest).get();
            Suggestion<Entry<Option>> suggestion = response.getSuggest().getSuggestion(suggestionName);
            for (Entry<Option> entry : suggestion.getEntries()) {
                for (Option option : entry.getOptions()) {
                    results.add(option.getText().toString());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            this.log.error("Error in autoComplete ES search", e);
        }
        return results;
    }

    static String getAutoCompleteField(String entityType) {
        switch (entityType) {
            case "Biographics":
                return "suggest";
            default:
                return null;
        }
    }
}
