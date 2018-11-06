package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.ObservedData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ObservedData entity.
 */
public interface ObservedDataSearchRepository extends ElasticsearchRepository<ObservedData, Long> {
}
