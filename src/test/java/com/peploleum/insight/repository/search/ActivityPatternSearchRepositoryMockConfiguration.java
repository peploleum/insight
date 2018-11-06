package com.peploleum.insight.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ActivityPatternSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ActivityPatternSearchRepositoryMockConfiguration {

    @MockBean
    private ActivityPatternSearchRepository mockActivityPatternSearchRepository;

}
