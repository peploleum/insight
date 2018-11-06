package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.NetLink;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the NetLink entity.
 */
public interface NetLinkSearchRepository extends ElasticsearchRepository<NetLink, Long> {
}
