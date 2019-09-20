package com.peploleum.insight.service;

import com.peploleum.insight.domain.dictionary.Dictionary;
import com.peploleum.insight.service.dto.DictionaryDTO;
import com.peploleum.insight.service.dto.ThemeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing dictionary.
 */
public interface DictionaryService {

    void delete(String id);

    List<Dictionary> findAll();

    /**
     * Save a dictionary.
     *
     * @param dictionaryDTO the dictionary to save
     * @return the persisted dictionary
     */
    DictionaryDTO save(DictionaryDTO dictionaryDTO);

    /**
     * Get the dictionary.
     *
     * @return the dictionary
     */
    Optional<DictionaryDTO> findOne(String id);

    Optional<String> findThemeNamePerMotclef(String motclef);

    Optional<String> findPonderation(String motclef);
}
