package com.peploleum.insight.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CourseOfActionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CourseOfActionSearchRepositoryMockConfiguration {

    @MockBean
    private CourseOfActionSearchRepository mockCourseOfActionSearchRepository;

}
