package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StylesDetails;
import com.mycompany.myapp.repository.StylesDetailsRepository;
import com.mycompany.myapp.service.StylesDetailsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StylesDetails}.
 */
@RestController
@RequestMapping("/api")
public class StylesDetailsResource {

    private final Logger log = LoggerFactory.getLogger(StylesDetailsResource.class);

    private static final String ENTITY_NAME = "stylesDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StylesDetailsService stylesDetailsService;

    private final StylesDetailsRepository stylesDetailsRepository;

    public StylesDetailsResource(StylesDetailsService stylesDetailsService, StylesDetailsRepository stylesDetailsRepository) {
        this.stylesDetailsService = stylesDetailsService;
        this.stylesDetailsRepository = stylesDetailsRepository;
    }

    /**
     * {@code POST  /styles-details} : Create a new stylesDetails.
     *
     * @param stylesDetails the stylesDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stylesDetails, or with status {@code 400 (Bad Request)} if the stylesDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/styles-details")
    public ResponseEntity<StylesDetails> createStylesDetails(@RequestBody StylesDetails stylesDetails) throws URISyntaxException {
        log.debug("REST request to save StylesDetails : {}", stylesDetails);
        if (stylesDetails.getId() != null) {
            throw new BadRequestAlertException("A new stylesDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StylesDetails result = stylesDetailsService.save(stylesDetails);
        return ResponseEntity
            .created(new URI("/api/styles-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /styles-details/:id} : Updates an existing stylesDetails.
     *
     * @param id the id of the stylesDetails to save.
     * @param stylesDetails the stylesDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stylesDetails,
     * or with status {@code 400 (Bad Request)} if the stylesDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stylesDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/styles-details/{id}")
    public ResponseEntity<StylesDetails> updateStylesDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StylesDetails stylesDetails
    ) throws URISyntaxException {
        log.debug("REST request to update StylesDetails : {}, {}", id, stylesDetails);
        if (stylesDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stylesDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stylesDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StylesDetails result = stylesDetailsService.update(stylesDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stylesDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /styles-details/:id} : Partial updates given fields of an existing stylesDetails, field will ignore if it is null
     *
     * @param id the id of the stylesDetails to save.
     * @param stylesDetails the stylesDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stylesDetails,
     * or with status {@code 400 (Bad Request)} if the stylesDetails is not valid,
     * or with status {@code 404 (Not Found)} if the stylesDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the stylesDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/styles-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StylesDetails> partialUpdateStylesDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StylesDetails stylesDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update StylesDetails partially : {}, {}", id, stylesDetails);
        if (stylesDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stylesDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stylesDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StylesDetails> result = stylesDetailsService.partialUpdate(stylesDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stylesDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /styles-details} : get all the stylesDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stylesDetails in body.
     */
    @GetMapping("/styles-details")
    public List<StylesDetails> getAllStylesDetails() {
        log.debug("REST request to get all StylesDetails");
        return stylesDetailsService.findAll();
    }

    /**
     * {@code GET  /styles-details/:id} : get the "id" stylesDetails.
     *
     * @param id the id of the stylesDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stylesDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/styles-details/{id}")
    public ResponseEntity<StylesDetails> getStylesDetails(@PathVariable Long id) {
        log.debug("REST request to get StylesDetails : {}", id);
        Optional<StylesDetails> stylesDetails = stylesDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stylesDetails);
    }

    /**
     * {@code DELETE  /styles-details/:id} : delete the "id" stylesDetails.
     *
     * @param id the id of the stylesDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/styles-details/{id}")
    public ResponseEntity<Void> deleteStylesDetails(@PathVariable Long id) {
        log.debug("REST request to delete StylesDetails : {}", id);
        stylesDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
