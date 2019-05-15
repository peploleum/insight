package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.Biographics;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.repository.BiographicsRepository;
import com.peploleum.insight.repository.search.BiographicsSearchRepository;
import com.peploleum.insight.service.BiographicsService;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.dto.BiographicsDTO;
import com.peploleum.insight.service.mapper.BiographicsMapper;
import com.peploleum.insight.service.util.InsightUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Biographics.
 */
@Service
@Profile("graphy")
public class BiographicsServiceImpl implements BiographicsService {

    private final Logger log = LoggerFactory.getLogger(BiographicsServiceImpl.class);

    private final BiographicsMapper biographicsMapper;

    private final BiographicsRepository biographicsRepository;
    private final BiographicsSearchRepository biographicsSearchRepository;
    private final InsightGraphEntityService insightGraphEntityRepository;

    public BiographicsServiceImpl(BiographicsRepository biographicsRepository, BiographicsMapper biographicsMapper,
                                  BiographicsSearchRepository biographicsSearchRepository, InsightGraphEntityService insightGraphEntityRepository) {
        this.biographicsRepository = biographicsRepository;
        this.biographicsMapper = biographicsMapper;
        this.biographicsSearchRepository = biographicsSearchRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
    }

    /**
     * Save a biographics.
     *
     * @param biographicsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BiographicsDTO save(BiographicsDTO biographicsDTO) {
        log.debug("Request to save Biographics : {}", biographicsDTO);
        if (biographicsDTO.getGeometry() == null && biographicsDTO.getBiographicsCoordinates() != null && !biographicsDTO.getBiographicsCoordinates().isEmpty()) {
            String[] coordinates = biographicsDTO.getBiographicsCoordinates().split(",");
            biographicsDTO.setGeometry(InsightUtil.getGeometryFromCoordinate(coordinates));
        }

        Biographics biographics = biographicsMapper.toEntity(biographicsDTO);
        biographics.setEntityType(InsightEntityType.Biographics);
        biographics = biographicsRepository.save(biographics);
        if (biographics.getExternalId() == null || biographics.getExternalId().isEmpty()) {
            Long externalId = this.insightGraphEntityRepository.save(biographics.getBiographicsName(), biographics.getId(), biographics.getBiographicsSymbol(), InsightEntityType.Biographics);
            biographics.setExternalId(String.valueOf(externalId));
            biographics = biographicsRepository.save(biographics);
        } else {
            this.insightGraphEntityRepository.update(Long.valueOf(biographics.getExternalId()), biographics.getBiographicsName(),
                biographics.getId(), biographics.getBiographicsSymbol(), InsightEntityType.Biographics);
        }
        biographicsSearchRepository.save(biographics);
        return biographicsMapper.toDto(biographics);
    }

    /**
     * Get all the biographics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<BiographicsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Biographics");
        return biographicsRepository.findAll(pageable)
            .map(biographicsMapper::toDto);
    }


    /**
     * Get one biographics by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<BiographicsDTO> findOne(String id) {
        log.debug("Request to get Biographics : {}", id);
        return biographicsRepository.findById(id)
            .map(biographicsMapper::toDto);
    }

    /**
     * Delete the biographics by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Biographics : {}", id);
        biographicsRepository.deleteById(id);
        biographicsSearchRepository.deleteById(id);
    }

    /**
     * Search for the biographics corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<BiographicsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Biographics for query {}", query);
        return biographicsSearchRepository.search(queryStringQuery(query), pageable)
            .map(biographicsMapper::toDto);
    }
}
