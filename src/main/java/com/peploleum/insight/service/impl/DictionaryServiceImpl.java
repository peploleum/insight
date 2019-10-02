package com.peploleum.insight.service.impl;

import com.peploleum.insight.domain.dictionary.Dictionary;
import com.peploleum.insight.domain.dictionary.Theme;
import com.peploleum.insight.repository.DictionaryRepository;
import com.peploleum.insight.service.DictionaryService;
import com.peploleum.insight.service.dto.DictionaryDTO;
import com.peploleum.insight.service.mapper.DictionaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing Dictionary.
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final Logger log = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    private DictionaryMapper dictionaryMapper;
    private DictionaryRepository dictionaryRepository;
    private final MongoTemplate mongoTemplate;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, MongoTemplate mongoTemplate,
                                 DictionaryMapper dictionaryMapper) {
        this.dictionaryRepository = dictionaryRepository;
        this.mongoTemplate = mongoTemplate;
        this.dictionaryMapper = dictionaryMapper;
    }

    @Override
    public DictionaryDTO save(DictionaryDTO dictionaryDTO) {
        log.debug("Request to save Dictionary : {}", dictionaryDTO);
        Dictionary dictionary = dictionaryMapper.toEntity(dictionaryDTO);
        dictionary = dictionaryRepository.save(dictionary);
        return dictionaryMapper.toDto(dictionary);
    }

    @Override
    public Optional<DictionaryDTO> findOne(String id) {
        log.debug("Request to get RawData : {}", id);
        return dictionaryRepository.findById(id)
            .map(dictionaryMapper::toDto);
    }


    @Override
    public void delete(String id) {
        log.debug("Request to delete Dictionary : {}", id);
        dictionaryRepository.deleteById(id);
    }

    @Override
    public Page<DictionaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RawData");
        return dictionaryRepository.findAll(pageable)
            .map(dictionaryMapper::toDto);
    }

    @Override
    public Optional<String> findThemeNamePerMotclef(String motclef) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(motclef));
        return Optional.ofNullable(mongoTemplate.find(query, Theme.class)
            .get(0).getName());
    }

    /*@Override
    public Optional<String> findPonderation(String motclef) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clef").is(motclef));
        return Optional.ofNullable(mongoTemplate.find(query, Motclef.class)
            .get(0).getPond());
    }*/

    @Override
    public Optional<String> findPonderation(String motclef) {
        return Optional.empty();
    }
}

