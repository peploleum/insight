package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.IntrusionSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IntrusionSet entity.
 */
public interface IntrusionSetSearchRepository extends ElasticsearchRepository<IntrusionSet, Long> {
}
