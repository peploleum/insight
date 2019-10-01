package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.RawData;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.domain.map.GeometryCollection;
import com.peploleum.insight.domain.map.InsightShape;
import com.peploleum.insight.repository.RawDataRepository;
import com.peploleum.insight.repository.search.RawDataSearchRepository;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.RawDataService;
import com.peploleum.insight.service.dto.RawDataDTO;
import com.peploleum.insight.service.dto.ScoreDTO;
import com.peploleum.insight.service.mapper.RawDataMapper;
import com.peploleum.insight.service.util.InsightUtil;
import com.vividsolutions.jts.geom.Coordinate;
import org.elasticsearch.common.geo.builders.PointBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing RawData.
 */
@Service
@Profile("graphy")
public class RawDataServiceImpl implements RawDataService {

    private final Logger log = LoggerFactory.getLogger(RawDataServiceImpl.class);

    private final RawDataMapper rawDataMapper;

    private final RawDataRepository rawDataRepository;
    private final RawDataSearchRepository rawDataSearchRepository;
    private final InsightGraphEntityService insightGraphEntityRepository;

    private final MongoTemplate mongoTemplate;

    public RawDataServiceImpl(RawDataRepository rawDataRepository, RawDataMapper rawDataMapper,
                              RawDataSearchRepository rawDataSearchRepository, MongoTemplate mongoTemplate,
                              InsightGraphEntityService insightGraphEntityRepository) {
        this.rawDataRepository = rawDataRepository;
        this.rawDataMapper = rawDataMapper;
        this.rawDataSearchRepository = rawDataSearchRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Save a rawData.
     *
     * @param rawDataDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RawDataDTO save(RawDataDTO rawDataDTO) {
        log.debug("Request to save RawData : {}", rawDataDTO);
        if (rawDataDTO.getGeometry() == null && rawDataDTO.getRawDataCoordinates() != null && !rawDataDTO.getRawDataCoordinates().isEmpty()) {
            String[] coordinates = rawDataDTO.getRawDataCoordinates().split(",");
            rawDataDTO.setGeometry(InsightUtil.getGeometryFromCoordinate(coordinates));
        }

        RawData rawData = rawDataMapper.toEntity(rawDataDTO);
        rawData.setEntityType(InsightEntityType.RawData);

        rawData = rawDataRepository.save(rawData);

        if (rawData.getExternalId() == null || rawData.getExternalId().isEmpty()) {
            Long externalId;
            if(rawDataDTO.getScoreDTO() == null) {
                externalId = this.insightGraphEntityRepository.save(rawData.getRawDataName(), rawData.getId(), rawData.getRawDataSymbol(), InsightEntityType.RawData);
            } else {
                externalId = this.insightGraphEntityRepository.saveWithProperties(rawData.getRawDataName(), rawData.getId(), rawData.getRawDataSubType(), rawData.getRawDataSourceUri(), rawData.getRawDataSymbol(), InsightEntityType.RawData, rawDataDTO.getScoreDTO());
            }
            rawData.setExternalId(String.valueOf(externalId));
            rawData = rawDataRepository.save(rawData);
        } else {
            this.insightGraphEntityRepository.update(Long.valueOf(rawData.getExternalId()), rawData.getRawDataName(),
                rawData.getId(), rawData.getRawDataSymbol(), InsightEntityType.RawData);
        }
        rawDataSearchRepository.save(rawData);
        return rawDataMapper.toDto(rawData);
    }

    /**
     * Get all the rawData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<RawDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RawData");
        return rawDataRepository.findAll(pageable)
            .map(rawDataMapper::toDto);
    }


    /**
     * Get one rawData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<RawDataDTO> findOne(String id) {
        log.debug("Request to get RawData : {}", id);
        return rawDataRepository.findById(id)
            .map(rawDataMapper::toDto);
    }

    /**
     * Delete the rawData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete RawData : {}", id);
        rawDataRepository.deleteById(id);
        rawDataSearchRepository.deleteById(id);
    }

    /**
     * Search for the rawData corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<RawDataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RawData for query {}", query);
        return rawDataSearchRepository.search(queryStringQuery(query), pageable)
            .map(rawDataMapper::toDto);
    }

    /**
     * Queries via a specific {@link Query}
     *
     * @param query the query of the search
     * @return list of matching entities
     */
    @Override
    public List<RawDataDTO> searchByCriteria(Query query) {
        return this.mongoTemplate.find(query, RawData.class).stream().map(rawDataMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Queries via specific {@link Query}
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return a {@link Page} with the result
     */
    @Override
    public Page<RawDataDTO> searchByCriteria(Query query, Pageable pageable) {
        query.with(pageable);
        final List<RawDataDTO> resultList = this.mongoTemplate.find(query, RawData.class).stream().map(rawDataMapper::toDto).collect(Collectors.toList());
        return PageableExecutionUtils.getPage(resultList, pageable, () -> resultList.size());
    }

    @Override
    public Page<RawDataDTO> search(NativeSearchQuery query) {
        log.debug("Request to search for a page of RawData for query");
        return this.rawDataSearchRepository.search(query).map(rawDataMapper::toDto);
    }
}
