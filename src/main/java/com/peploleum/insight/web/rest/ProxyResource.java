package com.peploleum.insight.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.peploleum.insight.service.impl.ProxyServiceImpl;
import com.peploleum.insight.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/**
 * REST controller for proxying request to external API.
 */
@RestController
@RequestMapping("/api")
public class ProxyResource {

    private final Logger log = LoggerFactory.getLogger(ProxyResource.class);
    private final ProxyServiceImpl proxyService;

    public ProxyResource(ProxyServiceImpl proxyService) {
        this.proxyService = proxyService;
    }

    /**
     * POST  /houston : Transfert le formulaire 94A vers l'API HOUSTON.
     *
     * @param formulaire the 94A form to post
     * @return the ResponseEntity with status 201 (Successful), or with status 400 (Bad Request)
     */
    @PostMapping("/houston")
    @Timed
    public ResponseEntity<String> sendForm(@RequestBody String formulaire) {
        log.debug("POST formulaire 94A vers Houston");
        try {
            String externalBioId = this.proxyService.postFormToHouston(formulaire);
            return ResponseEntity.ok().headers(new HttpHeaders()).body(externalBioId);
        } catch (RestClientException e) {
            throw new InternalServerErrorException("Failed to post form to Houston API.");
        }
    }

}
