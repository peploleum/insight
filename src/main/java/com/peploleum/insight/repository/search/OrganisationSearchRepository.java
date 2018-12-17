package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.Organisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Organisation entity.
 */
public interface OrganisationSearchRepository extends ElasticsearchRepository<Organisation, String> {
}
