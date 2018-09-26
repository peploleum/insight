package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.enumeration.EventType;
import com.peploleum.insight.domain.enumeration.Gender;
import com.peploleum.insight.service.BiographicsService;
import com.peploleum.insight.service.EventService;
import com.peploleum.insight.service.GeneratorService;
import com.peploleum.insight.service.dto.BiographicsDTO;
import com.peploleum.insight.service.dto.EventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private BiographicsService biographicsService;
    private EventService eventService;

    private static final int GEN_THRESHOLD = 20;
    private static final int SINGLE_GEN_THRESHOLD = 20;

    public GeneratorServiceImpl(BiographicsService biographicsService, EventService eventService) {
        this.biographicsService = biographicsService;
        this.eventService = eventService;
    }

    @Override
    public void feed() {
        this.log.info("FEEDING x " + GEN_THRESHOLD);
        int i = 0;
        while (i < GEN_THRESHOLD) {
            final int index = ThreadLocalRandom.current().nextInt(0, 3);

            final Gender randomGender = Gender.values()[index];
            final String biographicsFirstname = UUID.randomUUID().toString();
            final String biographicsLastname = UUID.randomUUID().toString();
            int j = 0;
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            this.log.info("Generating " + randomThreshold + " objects with values");
            this.log.info("biographicsFirstname " + biographicsFirstname);
            this.log.info("biographicsLastname " + biographicsLastname);
            this.log.info("randomGender " + randomGender);
            while (j < randomThreshold) {
                BiographicsDTO biographicsDTO = new BiographicsDTO();
                biographicsDTO.setBiographicsFirstname(biographicsFirstname);
                biographicsDTO.setBiographicsName(biographicsLastname);
                biographicsDTO.setBiographicsAge(12);
                biographicsDTO.setBiographicsGender(randomGender);
                biographicsDTO.setBiographicsCoordinates(this.generateLatitude() + "," + this.generateLongitude());
                this.biographicsService.save(biographicsDTO);
                j++;
            }
            i++;
        }

        i = 0;
        while (i < GEN_THRESHOLD) {
            final int index = ThreadLocalRandom.current().nextInt(0, 4);
            final EventType randomEventType = EventType.values()[index];
            final String eventName = UUID.randomUUID().toString();
            int j = 0;
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            this.log.info("Generating " + randomThreshold + " objects with values");
            this.log.info("eventName " + eventName);
            this.log.info("randomEventType " + randomEventType);
            while (j < randomThreshold) {
                final EventDTO eventDTO = new EventDTO();
                eventDTO.setEventName(eventName);
                eventDTO.setEventType(randomEventType);
                eventDTO.setEventDescription(UUID.randomUUID().toString());
                eventDTO.setEventCoordinates(this.generateLatitude() + "," + this.generateLongitude());
                final Instant instant = generateRandomInstant();
                eventDTO.setEventDate(instant);
                this.eventService.save(eventDTO);
                j++;
            }
            i++;
        }
    }

    private Instant generateRandomInstant() {
        final int monthIndex = ThreadLocalRandom.current().nextInt(0, 11);
        final Month month = Month.values()[monthIndex];
        final int year = ThreadLocalRandom.current().nextInt(2014, 2018);
        final int hour = ThreadLocalRandom.current().nextInt(0, 23);
        final int minute = ThreadLocalRandom.current().nextInt(0, 59);
        final int seconds = ThreadLocalRandom.current().nextInt(0, 59);
        final int day = ThreadLocalRandom.current().nextInt(1, 29);
        LocalDateTime specificDate = LocalDateTime.of(year, month, day, hour, minute, seconds);
        return specificDate.toInstant(ZoneOffset.UTC);
    }

    private double generateLatitude() {
        final double v = ThreadLocalRandom.current().nextDouble(41, 51);
        return v;
    }

    private double generateLongitude() {
        final double v = ThreadLocalRandom.current().nextDouble(-6, 8.3);
        return v;
    }
}
