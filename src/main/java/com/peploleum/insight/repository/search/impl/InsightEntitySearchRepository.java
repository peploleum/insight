package com.peploleum.insight.repository.search.impl;

import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.github.vanroy.springdata.jest.mapper.JestResultsExtractor;
import com.peploleum.insight.domain.InsightEntity;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InsightEntitySearchRepository implements com.peploleum.insight.repository.search.InsightEntitySearchRepository {

    private JestElasticsearchTemplate elasticsearchOperations;

    private final Logger log = LoggerFactory.getLogger(InsightEntitySearchRepository.class);

    private List<Class<?>> domainDocumentClasses = new ArrayList<>();
    private Map<String, Class<?>> domainIndexMatcher = new HashMap<>();
    private List<String> blackList = Arrays.asList(new String[]{"GeoRef", "User"});

    public InsightEntitySearchRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = (JestElasticsearchTemplate) elasticsearchOperations;
    }

    /**
     * scans classpath for Domain documents
     */
    @PostConstruct
    private void postConstruct() {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Document.class));

        for (BeanDefinition bd : scanner.findCandidateComponents("com.peploleum.insight.domain")) {
            try {
                Class<?> cl = Class.forName(bd.getBeanClassName());
                if (!blackList.contains(cl.getSimpleName())) {
                    this.domainDocumentClasses.add(cl);
                    this.domainIndexMatcher.put(cl.getSimpleName().toLowerCase(), cl);
                } else {
                    this.log.debug("avoiding " + cl.getSimpleName());
                }
            } catch (ClassNotFoundException e) {
                this.log.error("Could not scan classpath for domain Documents");
            }
        }
    }

    @Override
    public Page<InsightEntity> search(final NativeSearchQuery query, final List<String> indices) {
        final List<Class<?>> narrowedDomainElements = this.domainDocumentClasses.stream().filter((clazz) -> indices.contains(clazz.getSimpleName().toLowerCase())).collect(Collectors.toList());
        // needs to be a JestResultExtractor
        return this.elasticsearchOperations.query(query, new JestResultsExtractor<Page<InsightEntity>>() {
            @Override
            public Page<InsightEntity> extract(SearchResult response) {
                final List<InsightEntity> result = new ArrayList<>();
                // wrap the results in all required domain elements, and choose to add a hit in the page result depending on the source index
                for (Class<?> domainDocumentClass : narrowedDomainElements) {
                    if (response.getHits(domainDocumentClass).size() <= 0) {
                        return new AggregatedPageImpl<>(Collections.emptyList());
                    }
                    for (SearchResult.Hit searchHit : response.getHits(domainDocumentClass)) {
                        if (!domainDocumentClass.getSimpleName().equalsIgnoreCase(searchHit.index)) {
                            continue;
                        } else {
                            if (searchHit.source == null) {
                                continue;
                            }
                            final InsightEntity source = (InsightEntity) searchHit.source;
                            source.setEntityType(InsightEntityType.valueOf(domainDocumentClass.getSimpleName()));
                            result.add(source);
                        }
                    }
                }
                if (result.size() > 0) {
                    return new AggregatedPageImpl<>(result);
                }
                return new AggregatedPageImpl<>(Collections.emptyList());
            }
        });
    }
}
