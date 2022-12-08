package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PriceRelatedOptionDetails;
import com.mycompany.myapp.repository.PriceRelatedOptionDetailsRepository;
import com.mycompany.myapp.service.PriceRelatedOptionDetailsQueryService;
import com.mycompany.myapp.service.PriceRelatedOptionDetailsService;
import com.mycompany.myapp.service.criteria.PriceRelatedOptionDetailsCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PriceRelatedOptionDetails}.
 */
@RestController
@RequestMapping("/api")
public class PriceRelatedOptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PriceRelatedOptionDetailsResource.class);

    private static final String ENTITY_NAME = "priceRelatedOptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceRelatedOptionDetailsService priceRelatedOptionDetailsService;

    private final PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository;

    private final PriceRelatedOptionDetailsQueryService priceRelatedOptionDetailsQueryService;

    public PriceRelatedOptionDetailsResource(
        PriceRelatedOptionDetailsService priceRelatedOptionDetailsService,
        PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository,
        PriceRelatedOptionDetailsQueryService priceRelatedOptionDetailsQueryService
    ) {
        this.priceRelatedOptionDetailsService = priceRelatedOptionDetailsService;
        this.priceRelatedOptionDetailsRepository = priceRelatedOptionDetailsRepository;
        this.priceRelatedOptionDetailsQueryService = priceRelatedOptionDetailsQueryService;
    }

    /**
     * {@code POST  /price-related-option-details} : Create a new priceRelatedOptionDetails.
     *
     * @param priceRelatedOptionDetails the priceRelatedOptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceRelatedOptionDetails, or with status {@code 400 (Bad Request)} if the priceRelatedOptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/price-related-option-details")
    public ResponseEntity<PriceRelatedOptionDetails> createPriceRelatedOptionDetails(
        @Valid @RequestBody PriceRelatedOptionDetails priceRelatedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to save PriceRelatedOptionDetails : {}", priceRelatedOptionDetails);
        if (priceRelatedOptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new priceRelatedOptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceRelatedOptionDetails result = priceRelatedOptionDetailsService.save(priceRelatedOptionDetails);
        return ResponseEntity
            .created(new URI("/api/price-related-option-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-related-option-details/:id} : Updates an existing priceRelatedOptionDetails.
     *
     * @param id the id of the priceRelatedOptionDetails to save.
     * @param priceRelatedOptionDetails the priceRelatedOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceRelatedOptionDetails,
     * or with status {@code 400 (Bad Request)} if the priceRelatedOptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceRelatedOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/price-related-option-details/{id}")
    public ResponseEntity<PriceRelatedOptionDetails> updatePriceRelatedOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriceRelatedOptionDetails priceRelatedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update PriceRelatedOptionDetails : {}, {}", id, priceRelatedOptionDetails);
        if (priceRelatedOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceRelatedOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRelatedOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriceRelatedOptionDetails result = priceRelatedOptionDetailsService.update(priceRelatedOptionDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceRelatedOptionDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /price-related-option-details/:id} : Partial updates given fields of an existing priceRelatedOptionDetails, field will ignore if it is null
     *
     * @param id the id of the priceRelatedOptionDetails to save.
     * @param priceRelatedOptionDetails the priceRelatedOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceRelatedOptionDetails,
     * or with status {@code 400 (Bad Request)} if the priceRelatedOptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the priceRelatedOptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceRelatedOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/price-related-option-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceRelatedOptionDetails> partialUpdatePriceRelatedOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriceRelatedOptionDetails priceRelatedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update PriceRelatedOptionDetails partially : {}, {}", id, priceRelatedOptionDetails);
        if (priceRelatedOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceRelatedOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRelatedOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceRelatedOptionDetails> result = priceRelatedOptionDetailsService.partialUpdate(priceRelatedOptionDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceRelatedOptionDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /price-related-option-details} : get all the priceRelatedOptionDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceRelatedOptionDetails in body.
     */
    @GetMapping("/price-related-option-details")
    public ResponseEntity<List<PriceRelatedOptionDetails>> getAllPriceRelatedOptionDetails(
        PriceRelatedOptionDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PriceRelatedOptionDetails by criteria: {}", criteria);
        Page<PriceRelatedOptionDetails> page = priceRelatedOptionDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /price-related-option-details/count} : count all the priceRelatedOptionDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/price-related-option-details/count")
    public ResponseEntity<Long> countPriceRelatedOptionDetails(PriceRelatedOptionDetailsCriteria criteria) {
        log.debug("REST request to count PriceRelatedOptionDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(priceRelatedOptionDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /price-related-option-details/:id} : get the "id" priceRelatedOptionDetails.
     *
     * @param id the id of the priceRelatedOptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceRelatedOptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/price-related-option-details/{id}")
    public ResponseEntity<PriceRelatedOptionDetails> getPriceRelatedOptionDetails(@PathVariable Long id) {
        log.debug("REST request to get PriceRelatedOptionDetails : {}", id);
        Optional<PriceRelatedOptionDetails> priceRelatedOptionDetails = priceRelatedOptionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceRelatedOptionDetails);
    }

    /**
     * {@code DELETE  /price-related-option-details/:id} : delete the "id" priceRelatedOptionDetails.
     *
     * @param id the id of the priceRelatedOptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/price-related-option-details/{id}")
    public ResponseEntity<Void> deletePriceRelatedOptionDetails(@PathVariable Long id) {
        log.debug("REST request to delete PriceRelatedOptionDetails : {}", id);
        priceRelatedOptionDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
