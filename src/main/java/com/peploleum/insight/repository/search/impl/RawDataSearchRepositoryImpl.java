package com.peploleum.insight.repository.search.impl;

import com.peploleum.insight.domain.RawData;
import com.peploleum.insight.repository.search.RawDataSearchRepository;
import com.peploleum.insight.repository.search.RawDataSearchRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

/**
 * Created by gFolgoas on 19/02/2019.
 */
public class RawDataSearchRepositoryImpl implements RawDataSearchRepositoryCustom {

    private final ElasticsearchOperations esOps;

    @Autowired
    RawDataSearchRepository rawDataSearchRepository;

    public RawDataSearchRepositoryImpl(ElasticsearchOperations esOps) {
        this.esOps = esOps;
    }

    @Override
    public Page<RawData> search(NativeSearchQuery query) {
        return this.esOps.queryForPage(query, RawData.class);
    }
}
