package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.Tool;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tool entity.
 */
public interface ToolSearchRepository extends ElasticsearchRepository<Tool, Long> {
}
