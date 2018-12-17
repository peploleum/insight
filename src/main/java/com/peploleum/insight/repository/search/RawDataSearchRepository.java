package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.RawData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RawData entity.
 */
public interface RawDataSearchRepository extends ElasticsearchRepository<RawData, String> {
}
