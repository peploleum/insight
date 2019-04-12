package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.Location;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.repository.LocationRepository;
import com.peploleum.insight.repository.search.LocationSearchRepository;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.LocationService;
import com.peploleum.insight.service.dto.LocationDTO;
import com.peploleum.insight.service.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Location.
 */
@Service
@Profile("graphy")
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationMapper locationMapper;

    private final LocationRepository locationRepository;
    private final LocationSearchRepository locationSearchRepository;
    private final InsightGraphEntityService insightGraphEntityRepository;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper,
                               LocationSearchRepository locationSearchRepository, InsightGraphEntityService insightGraphEntityRepository) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.locationSearchRepository = locationSearchRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
    }

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);

        Location location = locationMapper.toEntity(locationDTO);
        location.setEntityType(InsightEntityType.Location);
        location = locationRepository.save(location);
        locationSearchRepository.save(location);
        if (location.getExternalId() == null || location.getExternalId().isEmpty()) {
            Long externalId = this.insightGraphEntityRepository.save(location.getLocationName(), location.getId(), InsightEntityType.Location);
            location.setExternalId(String.valueOf(externalId));
            location = locationRepository.save(location);
        } else {
            this.insightGraphEntityRepository.update(Long.valueOf(location.getExternalId()), location.getLocationName(),
                location.getId(), InsightEntityType.Location);
        }
        return locationMapper.toDto(location);
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }


    /**
     * Get one location by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<LocationDTO> findOne(String id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id)
            .map(locationMapper::toDto);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
        locationSearchRepository.deleteById(id);
    }

    /**
     * Search for the location corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<LocationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Locations for query {}", query);
        return locationSearchRepository.search(queryStringQuery(query), pageable)
            .map(locationMapper::toDto);
    }
}
