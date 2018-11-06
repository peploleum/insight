package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.CourseOfAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourseOfAction entity.
 */
public interface CourseOfActionSearchRepository extends ElasticsearchRepository<CourseOfAction, Long> {
}
