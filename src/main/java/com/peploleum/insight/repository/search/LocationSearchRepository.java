package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Location entity.
 */
public interface LocationSearchRepository extends ElasticsearchRepository<Location, String> {
}
