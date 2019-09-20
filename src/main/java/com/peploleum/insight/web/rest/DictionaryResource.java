package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.DictionaryService;
import com.peploleum.insight.service.dto.DictionaryDTO;
import com.peploleum.insight.service.dto.RawDataDTO;
import com.peploleum.insight.web.rest.errors.BadRequestAlertException;
import com.peploleum.insight.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing Dictionary.
 */
@RestController
@RequestMapping("/api")
public class DictionaryResource {

    private final Logger log = LoggerFactory.getLogger(DictionaryResource.class);

    private static final String ENTITY_NAME = "dictionary";

    private final DictionaryService dictionaryService;

    public DictionaryResource(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

//    public DictionaryResource(DictionaryService dictionaryService) {
//        this.dictionaryService = dictionaryService;
//    }

    /**
     * POST  /dictionary : Create a new dictionary.
     *
     * @param dictionaryDTO the biographicsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dictionaryDTO, or with status 400 (Bad Request) if the dictionaryDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dictionary")
    @Timed
    public ResponseEntity<DictionaryDTO> createDictionary(@Valid @RequestBody DictionaryDTO dictionaryDTO) throws URISyntaxException {
        log.debug("REST request to save the dictionary : {}", dictionaryDTO);
        if (dictionaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new dictionary cannot already have an ID", ENTITY_NAME, "idexists");
        }
//        return ResponseEntity.created(new URI("/api/dictionary/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
        return ResponseEntity.ok()
            .headers(new HttpHeaders())
            .body(dictionaryService.save(dictionaryDTO));
    }


    /**
     * GET  /dictionary : get all the dictionary.
     *
     * @return the ResponseEntity with status 200 (OK) and the dictionary in body
     */
    @GetMapping("/dictionary")
    @Timed
    public ResponseEntity<DictionaryDTO> getDictionary(String id) {
        log.debug("REST request to get the dictionary");
        Optional<DictionaryDTO> dictionaryDTO = dictionaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dictionaryDTO);
    }

    /**
     * DELETE  /dictionary delete the dictionary.
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dictionary")
    @Timed
    public ResponseEntity<Void> deleteDictionary(String id) {
        log.debug("REST request to delete a dictionary");
        dictionaryService.delete(id);
        return ResponseEntity.ok().headers(new HttpHeaders()).build();
    }


}
