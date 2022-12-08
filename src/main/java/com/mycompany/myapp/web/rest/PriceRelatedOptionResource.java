package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PriceRelatedOption;
import com.mycompany.myapp.repository.PriceRelatedOptionRepository;
import com.mycompany.myapp.service.PriceRelatedOptionQueryService;
import com.mycompany.myapp.service.PriceRelatedOptionService;
import com.mycompany.myapp.service.criteria.PriceRelatedOptionCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PriceRelatedOption}.
 */
@RestController
@RequestMapping("/api")
public class PriceRelatedOptionResource {

    private final Logger log = LoggerFactory.getLogger(PriceRelatedOptionResource.class);

    private static final String ENTITY_NAME = "priceRelatedOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceRelatedOptionService priceRelatedOptionService;

    private final PriceRelatedOptionRepository priceRelatedOptionRepository;

    private final PriceRelatedOptionQueryService priceRelatedOptionQueryService;

    public PriceRelatedOptionResource(
        PriceRelatedOptionService priceRelatedOptionService,
        PriceRelatedOptionRepository priceRelatedOptionRepository,
        PriceRelatedOptionQueryService priceRelatedOptionQueryService
    ) {
        this.priceRelatedOptionService = priceRelatedOptionService;
        this.priceRelatedOptionRepository = priceRelatedOptionRepository;
        this.priceRelatedOptionQueryService = priceRelatedOptionQueryService;
    }

    /**
     * {@code POST  /price-related-options} : Create a new priceRelatedOption.
     *
     * @param priceRelatedOption the priceRelatedOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceRelatedOption, or with status {@code 400 (Bad Request)} if the priceRelatedOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/price-related-options")
    public ResponseEntity<PriceRelatedOption> createPriceRelatedOption(@Valid @RequestBody PriceRelatedOption priceRelatedOption)
        throws URISyntaxException {
        log.debug("REST request to save PriceRelatedOption : {}", priceRelatedOption);
        if (priceRelatedOption.getId() != null) {
            throw new BadRequestAlertException("A new priceRelatedOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceRelatedOption result = priceRelatedOptionService.save(priceRelatedOption);
        return ResponseEntity
            .created(new URI("/api/price-related-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-related-options/:id} : Updates an existing priceRelatedOption.
     *
     * @param id the id of the priceRelatedOption to save.
     * @param priceRelatedOption the priceRelatedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceRelatedOption,
     * or with status {@code 400 (Bad Request)} if the priceRelatedOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceRelatedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/price-related-options/{id}")
    public ResponseEntity<PriceRelatedOption> updatePriceRelatedOption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriceRelatedOption priceRelatedOption
    ) throws URISyntaxException {
        log.debug("REST request to update PriceRelatedOption : {}, {}", id, priceRelatedOption);
        if (priceRelatedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceRelatedOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRelatedOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriceRelatedOption result = priceRelatedOptionService.update(priceRelatedOption);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceRelatedOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /price-related-options/:id} : Partial updates given fields of an existing priceRelatedOption, field will ignore if it is null
     *
     * @param id the id of the priceRelatedOption to save.
     * @param priceRelatedOption the priceRelatedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceRelatedOption,
     * or with status {@code 400 (Bad Request)} if the priceRelatedOption is not valid,
     * or with status {@code 404 (Not Found)} if the priceRelatedOption is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceRelatedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/price-related-options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceRelatedOption> partialUpdatePriceRelatedOption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriceRelatedOption priceRelatedOption
    ) throws URISyntaxException {
        log.debug("REST request to partial update PriceRelatedOption partially : {}, {}", id, priceRelatedOption);
        if (priceRelatedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceRelatedOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRelatedOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceRelatedOption> result = priceRelatedOptionService.partialUpdate(priceRelatedOption);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceRelatedOption.getId().toString())
        );
    }

    /**
     * {@code GET  /price-related-options} : get all the priceRelatedOptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceRelatedOptions in body.
     */
    @GetMapping("/price-related-options")
    public ResponseEntity<List<PriceRelatedOption>> getAllPriceRelatedOptions(
        PriceRelatedOptionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PriceRelatedOptions by criteria: {}", criteria);
        Page<PriceRelatedOption> page = priceRelatedOptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /price-related-options/count} : count all the priceRelatedOptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/price-related-options/count")
    public ResponseEntity<Long> countPriceRelatedOptions(PriceRelatedOptionCriteria criteria) {
        log.debug("REST request to count PriceRelatedOptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(priceRelatedOptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /price-related-options/:id} : get the "id" priceRelatedOption.
     *
     * @param id the id of the priceRelatedOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceRelatedOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/price-related-options/{id}")
    public ResponseEntity<PriceRelatedOption> getPriceRelatedOption(@PathVariable Long id) {
        log.debug("REST request to get PriceRelatedOption : {}", id);
        Optional<PriceRelatedOption> priceRelatedOption = priceRelatedOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceRelatedOption);
    }

    /**
     * {@code DELETE  /price-related-options/:id} : delete the "id" priceRelatedOption.
     *
     * @param id the id of the priceRelatedOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/price-related-options/{id}")
    public ResponseEntity<Void> deletePriceRelatedOption(@PathVariable Long id) {
        log.debug("REST request to delete PriceRelatedOption : {}", id);
        priceRelatedOptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
