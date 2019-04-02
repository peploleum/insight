package com.peploleum.insight.repository.search;

import com.peploleum.insight.domain.RawData;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

/**
 * Created by gFolgoas on 19/02/2019.
 */
public interface RawDataSearchRepositoryCustom {
    /**
     * Search for the rawData matching the query.
     *
     * @param query the SearchQuery of the search
     * @return the list of entities
     */
    Page<RawData> search(NativeSearchQuery query);
}
