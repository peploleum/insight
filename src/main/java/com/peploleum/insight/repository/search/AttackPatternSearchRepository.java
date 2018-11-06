package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.AttackPattern;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AttackPattern entity.
 */
public interface AttackPatternSearchRepository extends ElasticsearchRepository<AttackPattern, Long> {
}
