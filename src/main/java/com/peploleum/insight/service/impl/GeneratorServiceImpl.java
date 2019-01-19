package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.*;
import com.peploleum.insight.service.dto.RawDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private RawDataService rawDataService;
    private final BiographicsService biographicsService;
    private final LocationService locationService;
    private final GraphyClientService graphyClientService;

    @Value("${application.graph.enabled}")
    private boolean graphEnabled;

    @Value("${application.graph.host}")
    private String graphHost;

    @Value("${application.graph.port}")
    private int graphPort;
    private static final int GEN_THRESHOLD = 20;
    private static final int SINGLE_GEN_THRESHOLD = 20;

    public GeneratorServiceImpl(final RawDataService rawDataService, final BiographicsService biographicsService, final LocationService locationService, final GraphyClientService graphyClientService) {
        this.rawDataService = rawDataService;
        this.biographicsService = biographicsService;
        this.locationService = locationService;
        this.graphyClientService = graphyClientService;
    }

    @Override
    public void feed(int threshold) {
        this.log.info("FEEDING x " + threshold);

        for (int i = 0; i < threshold; i++) {
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            for (int j = 0; j < randomThreshold; j++) {
                final RawDataDTO rawDataDTO = generateRawDataDTO();
                if (this.graphEnabled) {
                    try {
                        this.graphyClientService.sendToGraphy(rawDataDTO);
                    } catch (Exception e) {
                        this.log.error("Failed to write entity to graphy: " + rawDataDTO.getId(), e);
                    }
                }
                this.rawDataService.save(rawDataDTO);
            }
        }
    }

    @Override
    public void feed() {
        this.feed(GEN_THRESHOLD);
    }

    private RawDataDTO generateRawDataDTO() {
        final String type = generateRandomType();
        final String name = UUID.randomUUID().toString();

        final RawDataDTO rawDataDTO = new RawDataDTO();
        rawDataDTO.setRawDataContent(UUID.randomUUID().toString() + " " + UUID.randomUUID().toString() + " " + UUID.randomUUID().toString());
        rawDataDTO.setRawDataName(name);
        rawDataDTO.setRawDataCreationDate(generateRandomDateTime().toInstant());
        rawDataDTO.setRawDataExtractedDate(generateRandomDateTime().toInstant());
        rawDataDTO.setRawDataCoordinates(this.generateLatitude() + "," + this.generateLongitude());
        rawDataDTO.setRawDataType(UUID.randomUUID().toString());
        rawDataDTO.setRawDataSubType(type);

        return rawDataDTO;
    }

    private String generateRandomType() {
        final int index = ThreadLocalRandom.current().nextInt(0, Type.values().length);

        return Type.values()[index].getLabel();
    }

    private ZonedDateTime generateRandomDateTime() {
        final int month = ThreadLocalRandom.current().nextInt(1, 13);
        final int year = ThreadLocalRandom.current().nextInt(2017, 2019);
        final int hour = ThreadLocalRandom.current().nextInt(0, 24);
        final int minute = ThreadLocalRandom.current().nextInt(0, 60);
        final int seconds = ThreadLocalRandom.current().nextInt(0, 60);
        final int day = ThreadLocalRandom.current().nextInt(1, 29);

        return ZonedDateTime.of(year, month, day, hour, minute, seconds, 0, ZoneId.systemDefault());
    }

    private double generateLatitude() {
        return ThreadLocalRandom.current().nextDouble(41, 51);
    }

    private double generateLongitude() {
        return ThreadLocalRandom.current().nextDouble(-6, 8.3);
    }

    enum Type {
        INFO("INFO"),
        ERROR("ERROR"),
        WARNING("WARNING"),
        ALERT("ALERT");

        private String label;

        Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }
    }

    @Override
    public void clean() {
        this.log.info("deleting all raw data");
        Pageable page = PageRequest.of(0, 100);
        boolean last = false;
        while (!last) {
            final Page<RawDataDTO> allRawDataDTOs = this.rawDataService.findAll(page);
            last = (allRawDataDTOs.getNumberOfElements() < 100);
            try {
                this.log.info("deleting " + allRawDataDTOs.getNumberOfElements() + " elements");
                allRawDataDTOs.map(rdto -> rdto.getId()).forEach(id -> this.rawDataService.delete(id));
            } catch (Exception e) {
                this.log.warn("failed to delete elements ", e.getMessage(), e);
            }
        }
    }
}
