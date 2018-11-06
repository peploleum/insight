package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.Campaign;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Campaign entity.
 */
public interface CampaignSearchRepository extends ElasticsearchRepository<Campaign, Long> {
}
