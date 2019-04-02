package com.peploleum.insight.service.impl.graphyImpl;

import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.repository.EquipmentRepository;
import com.peploleum.insight.repository.search.EquipmentSearchRepository;
import com.peploleum.insight.service.EquipmentService;
import com.peploleum.insight.service.InsightGraphEntityService;
import com.peploleum.insight.service.dto.EquipmentDTO;
import com.peploleum.insight.service.mapper.EquipmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Equipment.
 */
@Service
@Profile("graphy")
public class EquipmentServiceImpl implements EquipmentService {

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    private final EquipmentMapper equipmentMapper;

    private final EquipmentRepository equipmentRepository;
    private final EquipmentSearchRepository equipmentSearchRepository;
    private final InsightGraphEntityService insightGraphEntityRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper,
                                EquipmentSearchRepository equipmentSearchRepository, InsightGraphEntityService insightGraphEntityRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.equipmentSearchRepository = equipmentSearchRepository;
        this.insightGraphEntityRepository = insightGraphEntityRepository;
    }

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        log.debug("Request to save Equipment : {}", equipmentDTO);

        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        equipmentSearchRepository.save(equipment);
        Long externalId = this.insightGraphEntityRepository.save(equipment.getEquipmentName(), equipment.getId(), InsightEntityType.Equipment);
        equipment.setExternalId(String.valueOf(externalId));
        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(equipment);
    }

    /**
     * Get all the equipment.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<EquipmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Equipment");
        return equipmentRepository.findAll(pageable)
            .map(equipmentMapper::toDto);
    }


    /**
     * Get one equipment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<EquipmentDTO> findOne(String id) {
        log.debug("Request to get Equipment : {}", id);
        return equipmentRepository.findById(id)
            .map(equipmentMapper::toDto);
    }

    /**
     * Delete the equipment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
        equipmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the equipment corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<EquipmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Equipment for query {}", query);
        return equipmentSearchRepository.search(queryStringQuery(query), pageable)
            .map(equipmentMapper::toDto);
    }
}
