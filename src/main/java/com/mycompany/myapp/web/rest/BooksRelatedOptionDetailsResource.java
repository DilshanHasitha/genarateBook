package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BooksRelatedOptionDetails;
import com.mycompany.myapp.repository.BooksRelatedOptionDetailsRepository;
import com.mycompany.myapp.service.BooksRelatedOptionDetailsQueryService;
import com.mycompany.myapp.service.BooksRelatedOptionDetailsService;
import com.mycompany.myapp.service.criteria.BooksRelatedOptionDetailsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BooksRelatedOptionDetails}.
 */
@RestController
@RequestMapping("/api")
public class BooksRelatedOptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BooksRelatedOptionDetailsResource.class);

    private static final String ENTITY_NAME = "booksRelatedOptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksRelatedOptionDetailsService booksRelatedOptionDetailsService;

    private final BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository;

    private final BooksRelatedOptionDetailsQueryService booksRelatedOptionDetailsQueryService;

    public BooksRelatedOptionDetailsResource(
        BooksRelatedOptionDetailsService booksRelatedOptionDetailsService,
        BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository,
        BooksRelatedOptionDetailsQueryService booksRelatedOptionDetailsQueryService
    ) {
        this.booksRelatedOptionDetailsService = booksRelatedOptionDetailsService;
        this.booksRelatedOptionDetailsRepository = booksRelatedOptionDetailsRepository;
        this.booksRelatedOptionDetailsQueryService = booksRelatedOptionDetailsQueryService;
    }

    /**
     * {@code POST  /books-related-option-details} : Create a new booksRelatedOptionDetails.
     *
     * @param booksRelatedOptionDetails the booksRelatedOptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksRelatedOptionDetails, or with status {@code 400 (Bad Request)} if the booksRelatedOptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books-related-option-details")
    public ResponseEntity<BooksRelatedOptionDetails> createBooksRelatedOptionDetails(
        @Valid @RequestBody BooksRelatedOptionDetails booksRelatedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to save BooksRelatedOptionDetails : {}", booksRelatedOptionDetails);
        if (booksRelatedOptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new booksRelatedOptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksRelatedOptionDetails result = booksRelatedOptionDetailsService.save(booksRelatedOptionDetails);
        return ResponseEntity
            .created(new URI("/api/books-related-option-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books-related-option-details/:id} : Updates an existing booksRelatedOptionDetails.
     *
     * @param id the id of the booksRelatedOptionDetails to save.
     * @param booksRelatedOptionDetails the booksRelatedOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksRelatedOptionDetails,
     * or with status {@code 400 (Bad Request)} if the booksRelatedOptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksRelatedOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books-related-option-details/{id}")
    public ResponseEntity<BooksRelatedOptionDetails> updateBooksRelatedOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BooksRelatedOptionDetails booksRelatedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update BooksRelatedOptionDetails : {}, {}", id, booksRelatedOptionDetails);
        if (booksRelatedOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksRelatedOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRelatedOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksRelatedOptionDetails result = booksRelatedOptionDetailsService.update(booksRelatedOptionDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksRelatedOptionDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books-related-option-details/:id} : Partial updates given fields of an existing booksRelatedOptionDetails, field will ignore if it is null
     *
     * @param id the id of the booksRelatedOptionDetails to save.
     * @param booksRelatedOptionDetails the booksRelatedOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksRelatedOptionDetails,
     * or with status {@code 400 (Bad Request)} if the booksRelatedOptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the booksRelatedOptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksRelatedOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books-related-option-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksRelatedOptionDetails> partialUpdateBooksRelatedOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BooksRelatedOptionDetails booksRelatedOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooksRelatedOptionDetails partially : {}, {}", id, booksRelatedOptionDetails);
        if (booksRelatedOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksRelatedOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRelatedOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksRelatedOptionDetails> result = booksRelatedOptionDetailsService.partialUpdate(booksRelatedOptionDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksRelatedOptionDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /books-related-option-details} : get all the booksRelatedOptionDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booksRelatedOptionDetails in body.
     */
    @GetMapping("/books-related-option-details")
    public ResponseEntity<List<BooksRelatedOptionDetails>> getAllBooksRelatedOptionDetails(
        BooksRelatedOptionDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BooksRelatedOptionDetails by criteria: {}", criteria);
        Page<BooksRelatedOptionDetails> page = booksRelatedOptionDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books-related-option-details/count} : count all the booksRelatedOptionDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books-related-option-details/count")
    public ResponseEntity<Long> countBooksRelatedOptionDetails(BooksRelatedOptionDetailsCriteria criteria) {
        log.debug("REST request to count BooksRelatedOptionDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksRelatedOptionDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books-related-option-details/:id} : get the "id" booksRelatedOptionDetails.
     *
     * @param id the id of the booksRelatedOptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksRelatedOptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books-related-option-details/{id}")
    public ResponseEntity<BooksRelatedOptionDetails> getBooksRelatedOptionDetails(@PathVariable Long id) {
        log.debug("REST request to get BooksRelatedOptionDetails : {}", id);
        Optional<BooksRelatedOptionDetails> booksRelatedOptionDetails = booksRelatedOptionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksRelatedOptionDetails);
    }

    /**
     * {@code DELETE  /books-related-option-details/:id} : delete the "id" booksRelatedOptionDetails.
     *
     * @param id the id of the booksRelatedOptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books-related-option-details/{id}")
    public ResponseEntity<Void> deleteBooksRelatedOptionDetails(@PathVariable Long id) {
        log.debug("REST request to delete BooksRelatedOptionDetails : {}", id);
        booksRelatedOptionDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
