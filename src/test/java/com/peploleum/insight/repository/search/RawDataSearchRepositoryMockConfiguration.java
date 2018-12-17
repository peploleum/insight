package com.peploleum.insight.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RawDataSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RawDataSearchRepositoryMockConfiguration {

    @MockBean
    private RawDataSearchRepository mockRawDataSearchRepository;

}
