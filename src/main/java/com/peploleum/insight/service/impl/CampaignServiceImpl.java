package com.peploleum.insight.service.impl;

import com.peploleum.insight.service.CampaignService;
import com.peploleum.insight.domain.Campaign;
import com.peploleum.insight.repository.CampaignRepository;
import com.peploleum.insight.repository.search.CampaignSearchRepository;
import com.peploleum.insight.service.dto.CampaignDTO;
import com.peploleum.insight.service.mapper.CampaignMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Campaign.
 */
@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);

    private final CampaignRepository campaignRepository;

    private final CampaignMapper campaignMapper;

    private final CampaignSearchRepository campaignSearchRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository, CampaignMapper campaignMapper, CampaignSearchRepository campaignSearchRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.campaignSearchRepository = campaignSearchRepository;
    }

    /**
     * Save a campaign.
     *
     * @param campaignDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CampaignDTO save(CampaignDTO campaignDTO) {
        log.debug("Request to save Campaign : {}", campaignDTO);
        Campaign campaign = campaignMapper.toEntity(campaignDTO);
        campaign = campaignRepository.save(campaign);
        CampaignDTO result = campaignMapper.toDto(campaign);
        campaignSearchRepository.save(campaign);
        return result;
    }

    /**
     * Get all the campaigns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CampaignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campaigns");
        return campaignRepository.findAll(pageable)
            .map(campaignMapper::toDto);
    }


    /**
     * Get one campaign by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CampaignDTO> findOne(Long id) {
        log.debug("Request to get Campaign : {}", id);
        return campaignRepository.findById(id)
            .map(campaignMapper::toDto);
    }

    /**
     * Delete the campaign by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Campaign : {}", id);
        campaignRepository.deleteById(id);
        campaignSearchRepository.deleteById(id);
    }

    /**
     * Search for the campaign corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CampaignDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Campaigns for query {}", query);
        return campaignSearchRepository.search(queryStringQuery(query), pageable)
            .map(campaignMapper::toDto);
    }
}
