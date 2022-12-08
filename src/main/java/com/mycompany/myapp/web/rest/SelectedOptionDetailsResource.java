package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SelectedOptionDetails;
import com.mycompany.myapp.repository.SelectedOptionDetailsRepository;
import com.mycompany.myapp.service.SelectedOptionDetailsQueryService;
import com.mycompany.myapp.service.SelectedOptionDetailsService;
import com.mycompany.myapp.service.criteria.SelectedOptionDetailsCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SelectedOptionDetails}.
 */
@RestController
@RequestMapping("/api")
public class SelectedOptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SelectedOptionDetailsResource.class);

    private static final String ENTITY_NAME = "selectedOptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SelectedOptionDetailsService selectedOptionDetailsService;

    private final SelectedOptionDetailsRepository selectedOptionDetailsRepository;

    private final SelectedOptionDetailsQueryService selectedOptionDetailsQueryService;

    public SelectedOptionDetailsResource(
        SelectedOptionDetailsService selectedOptionDetailsService,
        SelectedOptionDetailsRepository selectedOptionDetailsRepository,
        SelectedOptionDetailsQueryService selectedOptionDetailsQueryService
    ) {
        this.selectedOptionDetailsService = selectedOptionDetailsService;
        this.selectedOptionDetailsRepository = selectedOptionDetailsRepository;
        this.selectedOptionDetailsQueryService = selectedOptionDetailsQueryService;
    }

    /**
     * {@code POST  /selected-option-details} : Create a new selectedOptionDetails.
     *
     * @param selectedOptionDetails the selectedOptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new selectedOptionDetails, or with status {@code 400 (Bad Request)} if the selectedOptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/selected-option-details")
    public ResponseEntity<SelectedOptionDetails> createSelectedOptionDetails(@RequestBody SelectedOptionDetails selectedOptionDetails)
        throws URISyntaxException {
        log.debug("REST request to save SelectedOptionDetails : {}", selectedOptionDetails);
        if (selectedOptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new selectedOptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SelectedOptionDetails result = selectedOptionDetailsService.save(selectedOptionDetails);
        return ResponseEntity
            .created(new URI("/api/selected-option-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /selected-option-details/:id} : Updates an existing selectedOptionDetails.
     *
     * @param id the id of the selectedOptionDetails to save.
     * @param selectedOptionDetails the selectedOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selectedOptionDetails,
     * or with status {@code 400 (Bad Request)} if the selectedOptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the selectedOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/selected-option-details/{id}")
    public ResponseEntity<SelectedOptionDetails> updateSelectedOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SelectedOptionDetails selectedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update SelectedOptionDetails : {}, {}", id, selectedOptionDetails);
        if (selectedOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selectedOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectedOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SelectedOptionDetails result = selectedOptionDetailsService.update(selectedOptionDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selectedOptionDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /selected-option-details/:id} : Partial updates given fields of an existing selectedOptionDetails, field will ignore if it is null
     *
     * @param id the id of the selectedOptionDetails to save.
     * @param selectedOptionDetails the selectedOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selectedOptionDetails,
     * or with status {@code 400 (Bad Request)} if the selectedOptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the selectedOptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the selectedOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/selected-option-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SelectedOptionDetails> partialUpdateSelectedOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SelectedOptionDetails selectedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update SelectedOptionDetails partially : {}, {}", id, selectedOptionDetails);
        if (selectedOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selectedOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectedOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SelectedOptionDetails> result = selectedOptionDetailsService.partialUpdate(selectedOptionDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selectedOptionDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /selected-option-details} : get all the selectedOptionDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of selectedOptionDetails in body.
     */
    @GetMapping("/selected-option-details")
    public ResponseEntity<List<SelectedOptionDetails>> getAllSelectedOptionDetails(
        SelectedOptionDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SelectedOptionDetails by criteria: {}", criteria);
        Page<SelectedOptionDetails> page = selectedOptionDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /selected-option-details/count} : count all the selectedOptionDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/selected-option-details/count")
    public ResponseEntity<Long> countSelectedOptionDetails(SelectedOptionDetailsCriteria criteria) {
        log.debug("REST request to count SelectedOptionDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(selectedOptionDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /selected-option-details/:id} : get the "id" selectedOptionDetails.
     *
     * @param id the id of the selectedOptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the selectedOptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/selected-option-details/{id}")
    public ResponseEntity<SelectedOptionDetails> getSelectedOptionDetails(@PathVariable Long id) {
        log.debug("REST request to get SelectedOptionDetails : {}", id);
        Optional<SelectedOptionDetails> selectedOptionDetails = selectedOptionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(selectedOptionDetails);
    }

    /**
     * {@code DELETE  /selected-option-details/:id} : delete the "id" selectedOptionDetails.
     *
     * @param id the id of the selectedOptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/selected-option-details/{id}")
    public ResponseEntity<Void> deleteSelectedOptionDetails(@PathVariable Long id) {
        log.debug("REST request to delete SelectedOptionDetails : {}", id);
        selectedOptionDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
