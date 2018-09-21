package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.enumeration.Gender;
import com.peploleum.insight.service.BiographicsService;
import com.peploleum.insight.service.GeneratorService;
import com.peploleum.insight.service.dto.BiographicsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private BiographicsService biographicsService;

    private static final int GEN_THRESHOLD = 20;
    private static final int SINGLE_GEN_THRESHOLD = 200;

    public GeneratorServiceImpl(BiographicsService biographicsService) {
        this.biographicsService = biographicsService;
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
                biographicsDTO.setBiographicsCoordinates("[" + this.generateLongitude() + "," + this.generateLatitude() + "]");
                this.biographicsService.save(biographicsDTO);
                j++;
            }
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                this.log.error(e.getMessage(), e);
            }
            i++;
        }
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
