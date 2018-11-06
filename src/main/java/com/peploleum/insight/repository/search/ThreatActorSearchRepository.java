package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.ThreatActor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ThreatActor entity.
 */
public interface ThreatActorSearchRepository extends ElasticsearchRepository<ThreatActor, Long> {
}
