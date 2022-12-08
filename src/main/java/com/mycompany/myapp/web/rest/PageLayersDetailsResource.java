package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PageLayersDetails;
import com.mycompany.myapp.repository.PageLayersDetailsRepository;
import com.mycompany.myapp.service.PageLayersDetailsQueryService;
import com.mycompany.myapp.service.PageLayersDetailsService;
import com.mycompany.myapp.service.criteria.PageLayersDetailsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PageLayersDetails}.
 */
@RestController
@RequestMapping("/api")
public class PageLayersDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PageLayersDetailsResource.class);

    private static final String ENTITY_NAME = "pageLayersDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PageLayersDetailsService pageLayersDetailsService;

    private final PageLayersDetailsRepository pageLayersDetailsRepository;

    private final PageLayersDetailsQueryService pageLayersDetailsQueryService;

    public PageLayersDetailsResource(
        PageLayersDetailsService pageLayersDetailsService,
        PageLayersDetailsRepository pageLayersDetailsRepository,
        PageLayersDetailsQueryService pageLayersDetailsQueryService
    ) {
        this.pageLayersDetailsService = pageLayersDetailsService;
        this.pageLayersDetailsRepository = pageLayersDetailsRepository;
        this.pageLayersDetailsQueryService = pageLayersDetailsQueryService;
    }

    /**
     * {@code POST  /page-layers-details} : Create a new pageLayersDetails.
     *
     * @param pageLayersDetails the pageLayersDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pageLayersDetails, or with status {@code 400 (Bad Request)} if the pageLayersDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/page-layers-details")
    public ResponseEntity<PageLayersDetails> createPageLayersDetails(@Valid @RequestBody PageLayersDetails pageLayersDetails)
        throws URISyntaxException {
        log.debug("REST request to save PageLayersDetails : {}", pageLayersDetails);
        if (pageLayersDetails.getId() != null) {
            throw new BadRequestAlertException("A new pageLayersDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PageLayersDetails result = pageLayersDetailsService.save(pageLayersDetails);
        return ResponseEntity
            .created(new URI("/api/page-layers-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /page-layers-details/:id} : Updates an existing pageLayersDetails.
     *
     * @param id the id of the pageLayersDetails to save.
     * @param pageLayersDetails the pageLayersDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageLayersDetails,
     * or with status {@code 400 (Bad Request)} if the pageLayersDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pageLayersDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/page-layers-details/{id}")
    public ResponseEntity<PageLayersDetails> updatePageLayersDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PageLayersDetails pageLayersDetails
    ) throws URISyntaxException {
        log.debug("REST request to update PageLayersDetails : {}, {}", id, pageLayersDetails);
        if (pageLayersDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageLayersDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageLayersDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PageLayersDetails result = pageLayersDetailsService.update(pageLayersDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pageLayersDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /page-layers-details/:id} : Partial updates given fields of an existing pageLayersDetails, field will ignore if it is null
     *
     * @param id the id of the pageLayersDetails to save.
     * @param pageLayersDetails the pageLayersDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageLayersDetails,
     * or with status {@code 400 (Bad Request)} if the pageLayersDetails is not valid,
     * or with status {@code 404 (Not Found)} if the pageLayersDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the pageLayersDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/page-layers-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PageLayersDetails> partialUpdatePageLayersDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PageLayersDetails pageLayersDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update PageLayersDetails partially : {}, {}", id, pageLayersDetails);
        if (pageLayersDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageLayersDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageLayersDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PageLayersDetails> result = pageLayersDetailsService.partialUpdate(pageLayersDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pageLayersDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /page-layers-details} : get all the pageLayersDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pageLayersDetails in body.
     */
    @GetMapping("/page-layers-details")
    public ResponseEntity<List<PageLayersDetails>> getAllPageLayersDetails(
        PageLayersDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PageLayersDetails by criteria: {}", criteria);
        Page<PageLayersDetails> page = pageLayersDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /page-layers-details/count} : count all the pageLayersDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/page-layers-details/count")
    public ResponseEntity<Long> countPageLayersDetails(PageLayersDetailsCriteria criteria) {
        log.debug("REST request to count PageLayersDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(pageLayersDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /page-layers-details/:id} : get the "id" pageLayersDetails.
     *
     * @param id the id of the pageLayersDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageLayersDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/page-layers-details/{id}")
    public ResponseEntity<PageLayersDetails> getPageLayersDetails(@PathVariable Long id) {
        log.debug("REST request to get PageLayersDetails : {}", id);
        Optional<PageLayersDetails> pageLayersDetails = pageLayersDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pageLayersDetails);
    }

    /**
     * {@code DELETE  /page-layers-details/:id} : delete the "id" pageLayersDetails.
     *
     * @param id the id of the pageLayersDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/page-layers-details/{id}")
    public ResponseEntity<Void> deletePageLayersDetails(@PathVariable Long id) {
        log.debug("REST request to delete PageLayersDetails : {}", id);
        pageLayersDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
