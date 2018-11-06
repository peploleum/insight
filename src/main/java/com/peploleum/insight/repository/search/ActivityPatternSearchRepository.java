package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.ActivityPattern;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ActivityPattern entity.
 */
public interface ActivityPatternSearchRepository extends ElasticsearchRepository<ActivityPattern, Long> {
}
