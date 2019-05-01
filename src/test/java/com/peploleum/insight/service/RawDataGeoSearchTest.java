package com.peploleum.insight.service;

import com.peploleum.insight.InsightApp;
import com.peploleum.insight.domain.RawData;
import com.peploleum.insight.domain.map.InsightShape;
import com.peploleum.insight.repository.RawDataRepository;
import com.peploleum.insight.repository.search.RawDataSearchRepository;
import com.vividsolutions.jts.geom.Coordinate;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class RawDataGeoSearchTest {
    @Autowired
    private RawDataService rawDataService;

    @Autowired
    private RawDataRepository rawDataRepository;
    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.RawDataSearchRepositoryMockConfiguration
     */
    @Autowired
    private RawDataSearchRepository mockRawDataSearchRepository;

    private final Logger log = LoggerFactory.getLogger(RawDataGeoSearchTest.class);

    @Before
    public void setup() {

    }

    @Test
    public void createGeoShapeTest() throws IOException {
        final GeoShapeQueryBuilder region = QueryBuilders
            .geoShapeQuery("geometry", ShapeBuilders.newEnvelope(
                new Coordinate(-6, 41),
                new Coordinate(8, 51)))
            .relation(ShapeRelation.WITHIN);
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        final NativeSearchQuery nativeSearchQuery = builder.withFilter(region).withPageable(PageRequest.of(0, 10)).build();
        RawData rawData = new RawData();
        final String bidou = "bidou";
        rawData.setRawDataName(bidou);
        final InputStream pointStream = RawDataGeoSearchTest.class.getResourceAsStream("/geo/point.json");
        final String pointAsString = new String(StreamUtils.copyToByteArray(pointStream));
        rawData.setGeometry(new InsightShape());
        this.rawDataRepository.save(rawData);
        final Page<RawData> search = mockRawDataSearchRepository.search(nativeSearchQuery);
    }

    @After
    public void cleanTest() {
    }

}
