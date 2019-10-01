package com.peploleum.insight.service;

import com.peploleum.insight.service.dto.DictionaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing dictionary.
 */
public interface DictionaryService {

    void delete(String id);

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

    Page<DictionaryDTO> findAll(Pageable pageable);

    Optional<String> findThemeNamePerMotclef(String motclef); // TODO findThemeNameByMotclef!

    Optional<String> findPonderation(String motclef);
}
