package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.GeneratorService;
import com.peploleum.insight.service.RawDataService;
import com.peploleum.insight.service.dto.RawDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private RawDataService rawDataService;

    private static final int GEN_THRESHOLD = 20;
    private static final int SINGLE_GEN_THRESHOLD = 20;

    public GeneratorServiceImpl(RawDataService rawDataService) {
        this.rawDataService = rawDataService;
    }

    @Override
    public void feed() {
        this.log.info("FEEDING x " + GEN_THRESHOLD);

        for (int i = 0; i < GEN_THRESHOLD; i++) {
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            for (int j = 0; j < randomThreshold; j++) {
                this.rawDataService.save(generateRawDataDTO());
            }
        }
    }

    private RawDataDTO generateRawDataDTO() {
        final String type = generateRandomType();
        final String name = UUID.randomUUID().toString();
        final ZonedDateTime debut = generateRandomDateTime();

        final RawDataDTO rawDataDTO = new RawDataDTO();
        rawDataDTO.setRawDataContent(UUID.randomUUID().toString() + " " + UUID.randomUUID().toString() + " " + UUID.randomUUID().toString());
        rawDataDTO.setRawDataName(name);
        rawDataDTO.setRawDataCreationDate(Instant.now());
        rawDataDTO.setRawDataExtractedDate(Instant.now());
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

    /**
     * Add duration between 0 and 61 days
     *
     * @param init the initial date
     * @return the new date
     */
    private ZonedDateTime addRandomDuration(ZonedDateTime init) {
        final int day = ThreadLocalRandom.current().nextInt(0, 60);
        final int hour = ThreadLocalRandom.current().nextInt(0, 24);
        final int minute = ThreadLocalRandom.current().nextInt(0, 60);
        final int seconds = ThreadLocalRandom.current().nextInt(0, 60);

        return init.plusDays(day).plusHours(hour).plusMinutes(minute).plusSeconds(seconds);
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

        private Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }
    }

    @Override
    public void clean() {
        this.log.info("deleting all oserved data");
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
