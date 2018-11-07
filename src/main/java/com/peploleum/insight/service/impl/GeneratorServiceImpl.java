package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.*;
import com.peploleum.insight.service.dto.ActorDTO;
import com.peploleum.insight.service.dto.NetLinkDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private ActorService actorService;
    private IntrusionSetService intrusionSetService;
    private ThreatActorService threatActorService;
    private ObservedDataService observedDataService;
    private AttackPatternService attackPatternService;
    private NetLinkService netLinkService;

    private static final int GEN_THRESHOLD = 20;
    private static final int SINGLE_GEN_THRESHOLD = 20;

    public GeneratorServiceImpl(ActorService actorService, IntrusionSetService intrusionSetService, ThreatActorService threatActorService, ObservedDataService observedDataService, AttackPatternService attackPatternService, NetLinkService netLinkService) {
        this.actorService = actorService;
        this.intrusionSetService = intrusionSetService;
        this.threatActorService = threatActorService;
        this.observedDataService = observedDataService;
        this.attackPatternService = attackPatternService;
        this.netLinkService = netLinkService;
    }

    @Override
    public void feed() {
        this.log.info("FEEDING x " + GEN_THRESHOLD);

        List<NetLinkDTO> netlinkList = new ArrayList<>();
        for (int i = 0; i < GEN_THRESHOLD; i++) {
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            for(int j = 0; j<randomThreshold; j++)
            {
                netlinkList.add(this.netLinkService.save(generateNetLink()));
            }
        }

        for (int i = 0; i < GEN_THRESHOLD; i++) {
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            for(int j = 0; j<randomThreshold; j++)
            {
                ActorDTO actorDTO = generateActor();

                int idx = ThreadLocalRandom.current().nextInt(0, netlinkList.size());
                actorDTO.setLinkOfId(netlinkList.get(idx).getId());

                this.actorService.save(actorDTO);
            }
        }
    }

    private NetLinkDTO generateNetLink()
    {
        final String netlinkName = UUID.randomUUID().toString();
        final String netlinkDescrition = UUID.randomUUID().toString();
        final String netlinkType = UUID.randomUUID().toString();
        final String netlinkLevel = UUID.randomUUID().toString();

        this.log.info("Generating netlink object with values");
        this.log.info("netlinkName " + netlinkName);
        this.log.info("netlinkDescrition " + netlinkDescrition);
        this.log.info("netlinkType " + netlinkType);
        this.log.info("netlinkLevel " + netlinkLevel);

        NetLinkDTO netLinkDTO = new NetLinkDTO();
        netLinkDTO.setNom(netlinkName);
        netLinkDTO.setDescription(netlinkDescrition);
        netLinkDTO.setType(netlinkType);
        netLinkDTO.setLevel(netlinkLevel);

        return netLinkDTO;
    }

    private ActorDTO generateActor()
    {
        final String actorName = UUID.randomUUID().toString();
        final String actorLibelle = UUID.randomUUID().toString();
        final String actorDescription = UUID.randomUUID().toString();
        final String actorType = UUID.randomUUID().toString();
        final String actorClasseIdentite = UUID.randomUUID().toString();

        this.log.info("Generating actor object with values");
        this.log.info("actorName " + actorName);
        this.log.info("actorLibelle " + actorLibelle);
        this.log.info("actorDescription " + actorDescription);
        this.log.info("actorType " + actorType);
        this.log.info("actorClasseIdentite " + actorClasseIdentite);

        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setNom(actorName);
        actorDTO.setLibelle(actorLibelle);
        actorDTO.setDescription(actorDescription);
        actorDTO.setType(actorType);
        actorDTO.setClasseIdentite(actorClasseIdentite);

        return actorDTO;
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
        return ThreadLocalRandom.current().nextDouble(41, 51);
    }

    private double generateLongitude() {
        return ThreadLocalRandom.current().nextDouble(-6, 8.3);
    }
}
