package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.enumeration.EquipmentType;
import com.peploleum.insight.domain.enumeration.EventType;
import com.peploleum.insight.domain.enumeration.LocationType;
import com.peploleum.insight.domain.enumeration.Size;
import com.peploleum.insight.domain.map.GeometryCollection;
import com.peploleum.insight.domain.map.InsightShape;
import com.peploleum.insight.service.*;
import com.peploleum.insight.service.dto.*;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.lang.StringEscapeUtils;
import org.elasticsearch.common.geo.builders.PointBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EventService eventService;
    private final EquipmentService equipmentService;
    private final OrganisationService organisationService;

    private static final int GEN_THRESHOLD = 10;
    private static final int SINGLE_GEN_THRESHOLD = 10;

    public GeneratorServiceImpl(final RawDataService rawDataService, final BiographicsService biographicsService,
                                final LocationService locationService, final EventService eventService,
                                final EquipmentService equipmentService, final OrganisationService organisationService) {
        this.rawDataService = rawDataService;
        this.biographicsService = biographicsService;
        this.locationService = locationService;
        this.eventService = eventService;
        this.equipmentService = equipmentService;
        this.organisationService = organisationService;
    }

    @Override
    public void feed(int threshold) {
        this.log.info("FEEDING x " + threshold);

        for (int i = 0; i < threshold; i++) {
            final int randomThreshold = ThreadLocalRandom.current().nextInt(0, SINGLE_GEN_THRESHOLD);
            for (int j = 0; j < randomThreshold; j++) {
                final RawDataDTO rawDataDTO = generateRawDataDTO();
                final BiographicsDTO biographicsDTO = generateBiographicsDTO();
                final EventDTO eventDTO = generateEventDTO();
                final EquipmentDTO equipmentDTO = generateEquipmentDTO();
                final LocationDTO locationDTO = generateLocationDTO();
                final OrganisationDTO organisationDTO = generateOrganisationDTO();

                final RawDataDTO savedRawDataDTO = this.rawDataService.save(rawDataDTO);
                final BiographicsDTO savedBiographicsDTO = this.biographicsService.save(biographicsDTO);
                final EquipmentDTO savedEquipmentDTO = this.equipmentService.save(equipmentDTO);
                final EventDTO savedEventDTO = this.eventService.save(eventDTO);
                final OrganisationDTO savedOrganisationDTO = this.organisationService.save(organisationDTO);
                final LocationDTO savedLocationDTO = this.locationService.save(locationDTO);

                this.rawDataService.save(savedRawDataDTO);
                this.biographicsService.save(savedBiographicsDTO);
                this.equipmentService.save(savedEquipmentDTO);
                this.eventService.save(savedEventDTO);
                this.organisationService.save(savedOrganisationDTO);
                this.locationService.save(savedLocationDTO);
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
        final double latitude = this.generateLatitude();
        final double longitude = this.generateLongitude();
        rawDataDTO.setRawDataCoordinates(generateLocationAttribute(latitude, longitude));
        final PointBuilder pointBuilder = new PointBuilder().coordinate(new Coordinate(longitude, latitude));
        final String geoPointAsString = pointBuilder.toString();
        final String s = StringEscapeUtils.escapeJava(geoPointAsString);
        final InsightShape geometry = new InsightShape();
        geometry.setType(pointBuilder.type().toString().toLowerCase());
        geometry.setCoordinates(new Double[]{pointBuilder.longitude(), pointBuilder.latitude()});
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.setGeometries(new InsightShape[]{geometry});
        rawDataDTO.setGeometry(geometryCollection);
        rawDataDTO.setRawDataType(UUID.randomUUID().toString());
        rawDataDTO.setRawDataSubType(type);

        return rawDataDTO;
    }

    private BiographicsDTO generateBiographicsDTO() {
        final BiographicsDTO biographicsDTO = new BiographicsDTO();
        biographicsDTO.setBiographicsAge(ThreadLocalRandom.current().nextInt(0, 120));
        biographicsDTO.setBiographicsFirstname(UUID.randomUUID().toString());
        biographicsDTO.setBiographicsName(UUID.randomUUID().toString());
        final double latitude = this.generateLatitude();
        final double longitude = this.generateLongitude();
        biographicsDTO.setBiographicsCoordinates(generateLocationAttribute(latitude, longitude));
        final PointBuilder pointBuilder = new PointBuilder().coordinate(new Coordinate(longitude, latitude));
        final InsightShape geometry = new InsightShape();
        geometry.setType(pointBuilder.type().toString().toLowerCase());
        geometry.setCoordinates(new Double[]{pointBuilder.longitude(), pointBuilder.latitude()});
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.setGeometries(new InsightShape[]{geometry});
        biographicsDTO.setGeometry(geometryCollection);
        return biographicsDTO;
    }

    private LocationDTO generateLocationDTO() {
        final LocationDTO locationDTO = new LocationDTO();
        final double latitude = this.generateLatitude();
        final double longitude = this.generateLongitude();
        locationDTO.setLocationCoordinates(generateLocationAttribute(latitude, longitude));
        final PointBuilder pointBuilder = new PointBuilder().coordinate(new Coordinate(longitude, latitude));
        final InsightShape geometry = new InsightShape();
        geometry.setType(pointBuilder.type().toString().toLowerCase());
        geometry.setCoordinates(new Double[]{pointBuilder.longitude(), pointBuilder.latitude()});
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.setGeometries(new InsightShape[]{geometry});
        locationDTO.setGeometry(geometryCollection);
        locationDTO.setLocationName(UUID.randomUUID().toString());
        locationDTO.setLocationType(LocationType.CITY);
        return locationDTO;
    }

    private EventDTO generateEventDTO() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName(UUID.randomUUID().toString());
        eventDTO.setEventDate(generateRandomDateTime().toInstant());
        eventDTO.setEventType(EventType.TERRORIST);
        final double latitude = this.generateLatitude();
        final double longitude = this.generateLongitude();
        eventDTO.setEventCoordinates(generateLocationAttribute(latitude, longitude));
        final PointBuilder pointBuilder = new PointBuilder().coordinate(new Coordinate(longitude, latitude));
        final InsightShape geometry = new InsightShape();
        geometry.setType(pointBuilder.type().toString().toLowerCase());
        geometry.setCoordinates(new Double[]{pointBuilder.longitude(), pointBuilder.latitude()});
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.setGeometries(new InsightShape[]{geometry});
        eventDTO.setGeometry(geometryCollection);
        return eventDTO;
    }

    private EquipmentDTO generateEquipmentDTO() {
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setEquipmentName(UUID.randomUUID().toString());
        equipmentDTO.setEquipmentType(EquipmentType.WEAPON);
        final double latitude = this.generateLatitude();
        final double longitude = this.generateLongitude();
        equipmentDTO.setEquipmentCoordinates(generateLocationAttribute(latitude, longitude));
        final PointBuilder pointBuilder = new PointBuilder().coordinate(new Coordinate(longitude, latitude));
        final InsightShape geometry = new InsightShape();
        geometry.setType(pointBuilder.type().toString().toLowerCase());
        geometry.setCoordinates(new Double[]{pointBuilder.longitude(), pointBuilder.latitude()});
        GeometryCollection geometryCollection = new GeometryCollection();
        geometryCollection.setGeometries(new InsightShape[]{geometry});
        equipmentDTO.setGeometry(geometryCollection);
        return equipmentDTO;
    }

    private OrganisationDTO generateOrganisationDTO() {
        final OrganisationDTO organisationDTO = new OrganisationDTO();
        organisationDTO.setOrganisationCoordinates(generateLocationAttribute(this.generateLatitude(), this.generateLongitude()));
        organisationDTO.setOrganisationName(UUID.randomUUID().toString());
        organisationDTO.setOrganisationSize(Size.MEDIUM);
        return organisationDTO;
    }

    private String generateLocationAttribute(double latitude, double longitude) {
        return latitude + "," + longitude;
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
