package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.repository.LayerDetailsRepository;
import com.mycompany.myapp.service.LayerDetailsQueryService;
import com.mycompany.myapp.service.LayerDetailsService;
import com.mycompany.myapp.service.criteria.LayerDetailsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LayerDetails}.
 */
@RestController
@RequestMapping("/api")
public class LayerDetailsResource {

    private final Logger log = LoggerFactory.getLogger(LayerDetailsResource.class);

    private static final String ENTITY_NAME = "layerDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LayerDetailsService layerDetailsService;

    private final LayerDetailsRepository layerDetailsRepository;

    private final LayerDetailsQueryService layerDetailsQueryService;

    public LayerDetailsResource(
        LayerDetailsService layerDetailsService,
        LayerDetailsRepository layerDetailsRepository,
        LayerDetailsQueryService layerDetailsQueryService
    ) {
        this.layerDetailsService = layerDetailsService;
        this.layerDetailsRepository = layerDetailsRepository;
        this.layerDetailsQueryService = layerDetailsQueryService;
    }

    /**
     * {@code POST  /layer-details} : Create a new layerDetails.
     *
     * @param layerDetails the layerDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new layerDetails, or with status {@code 400 (Bad Request)} if the layerDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/layer-details")
    public ResponseEntity<LayerDetails> createLayerDetails(@Valid @RequestBody LayerDetails layerDetails) throws URISyntaxException {
        log.debug("REST request to save LayerDetails : {}", layerDetails);
        if (layerDetails.getId() != null) {
            throw new BadRequestAlertException("A new layerDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LayerDetails result = layerDetailsService.save(layerDetails);
        return ResponseEntity
            .created(new URI("/api/layer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /layer-details/:id} : Updates an existing layerDetails.
     *
     * @param id the id of the layerDetails to save.
     * @param layerDetails the layerDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layerDetails,
     * or with status {@code 400 (Bad Request)} if the layerDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the layerDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/layer-details/{id}")
    public ResponseEntity<LayerDetails> updateLayerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LayerDetails layerDetails
    ) throws URISyntaxException {
        log.debug("REST request to update LayerDetails : {}, {}", id, layerDetails);
        if (layerDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layerDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LayerDetails result = layerDetailsService.update(layerDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layerDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /layer-details/:id} : Partial updates given fields of an existing layerDetails, field will ignore if it is null
     *
     * @param id the id of the layerDetails to save.
     * @param layerDetails the layerDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layerDetails,
     * or with status {@code 400 (Bad Request)} if the layerDetails is not valid,
     * or with status {@code 404 (Not Found)} if the layerDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the layerDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/layer-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LayerDetails> partialUpdateLayerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LayerDetails layerDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update LayerDetails partially : {}, {}", id, layerDetails);
        if (layerDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layerDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LayerDetails> result = layerDetailsService.partialUpdate(layerDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layerDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /layer-details} : get all the layerDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of layerDetails in body.
     */
    @GetMapping("/layer-details")
    public ResponseEntity<List<LayerDetails>> getAllLayerDetails(
        LayerDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LayerDetails by criteria: {}", criteria);
        Page<LayerDetails> page = layerDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /layer-details/count} : count all the layerDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/layer-details/count")
    public ResponseEntity<Long> countLayerDetails(LayerDetailsCriteria criteria) {
        log.debug("REST request to count LayerDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(layerDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /layer-details/:id} : get the "id" layerDetails.
     *
     * @param id the id of the layerDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the layerDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/layer-details/{id}")
    public ResponseEntity<LayerDetails> getLayerDetails(@PathVariable Long id) {
        log.debug("REST request to get LayerDetails : {}", id);
        Optional<LayerDetails> layerDetails = layerDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(layerDetails);
    }

    /**
     * {@code DELETE  /layer-details/:id} : delete the "id" layerDetails.
     *
     * @param id the id of the layerDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/layer-details/{id}")
    public ResponseEntity<Void> deleteLayerDetails(@PathVariable Long id) {
        log.debug("REST request to delete LayerDetails : {}", id);
        layerDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
