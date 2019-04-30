package com.peploleum.insight.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peploleum.insight.InsightApp;
import com.peploleum.insight.service.dto.RawDataDTO;
import com.vividsolutions.jts.geom.Coordinate;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class RawDataGeoSearchTest {
    @Autowired
    private RawDataService rawDataService;
    private final Logger log = LoggerFactory.getLogger(RawDataGeoSearchTest.class);

    @Before
    public void setup() {

    }

    @Test
    public void createGeoShapeTest() throws IOException {
        final String rawQuery = "{\n" +
            "    \"query\":{\n" +
            "        \"bool\": {\n" +
            "            \"must\": {\n" +
            "                \"match_all\": {}\n" +
            "            },\n" +
            "            \"filter\": {\n" +
            "                \"geo_shape\": {\n" +
            "                    \"region\": {\n" +
            "                        \"shape\": {\n" +
            "                            \"type\": \"envelope\",\n" +
            "                            \"coordinates\" : [[75.00, 25.0], [80.1, 30.2]]\n" +
            "                        },\n" +
            "                        \"relation\": \"within\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
        final GeoShapeQueryBuilder region = QueryBuilders
            .geoShapeQuery("region", ShapeBuilders.newEnvelope(
                new Coordinate(-6, 41),
                new Coordinate(8, 51)))
            .relation(ShapeRelation.WITHIN);
        final RawDataDTO rawDataDTO = new RawDataDTO();
        final String bidou = "bidou";
        rawDataDTO.setRawDataName(bidou);
        final InputStream pointStream = RawDataGeoSearchTest.class.getResourceAsStream("/point.json");
        final String pointAsString = new String(StreamUtils.copyToByteArray(pointStream));
        rawDataDTO.setGeometry(pointAsString);
        this.rawDataService.save(rawDataDTO);
        final Page<RawDataDTO> search = this.rawDataService.search(region.toString(), PageRequest.of(0, 10));
        Assert.assertNotNull(search);
    }

    @After
    public void cleanTest() {
    }

}
