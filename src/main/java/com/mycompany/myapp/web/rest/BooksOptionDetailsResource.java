package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BooksOptionDetails;
import com.mycompany.myapp.repository.BooksOptionDetailsRepository;
import com.mycompany.myapp.service.BooksOptionDetailsQueryService;
import com.mycompany.myapp.service.BooksOptionDetailsService;
import com.mycompany.myapp.service.criteria.BooksOptionDetailsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BooksOptionDetails}.
 */
@RestController
@RequestMapping("/api")
public class BooksOptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BooksOptionDetailsResource.class);

    private static final String ENTITY_NAME = "booksOptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksOptionDetailsService booksOptionDetailsService;

    private final BooksOptionDetailsRepository booksOptionDetailsRepository;

    private final BooksOptionDetailsQueryService booksOptionDetailsQueryService;

    public BooksOptionDetailsResource(
        BooksOptionDetailsService booksOptionDetailsService,
        BooksOptionDetailsRepository booksOptionDetailsRepository,
        BooksOptionDetailsQueryService booksOptionDetailsQueryService
    ) {
        this.booksOptionDetailsService = booksOptionDetailsService;
        this.booksOptionDetailsRepository = booksOptionDetailsRepository;
        this.booksOptionDetailsQueryService = booksOptionDetailsQueryService;
    }

    /**
     * {@code POST  /books-option-details} : Create a new booksOptionDetails.
     *
     * @param booksOptionDetails the booksOptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksOptionDetails, or with status {@code 400 (Bad Request)} if the booksOptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books-option-details")
    public ResponseEntity<BooksOptionDetails> createBooksOptionDetails(@RequestBody BooksOptionDetails booksOptionDetails)
        throws URISyntaxException {
        log.debug("REST request to save BooksOptionDetails : {}", booksOptionDetails);
        if (booksOptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new booksOptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksOptionDetails result = booksOptionDetailsService.save(booksOptionDetails);
        return ResponseEntity
            .created(new URI("/api/books-option-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books-option-details/:id} : Updates an existing booksOptionDetails.
     *
     * @param id the id of the booksOptionDetails to save.
     * @param booksOptionDetails the booksOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksOptionDetails,
     * or with status {@code 400 (Bad Request)} if the booksOptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books-option-details/{id}")
    public ResponseEntity<BooksOptionDetails> updateBooksOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksOptionDetails booksOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update BooksOptionDetails : {}, {}", id, booksOptionDetails);
        if (booksOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksOptionDetails result = booksOptionDetailsService.update(booksOptionDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksOptionDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books-option-details/:id} : Partial updates given fields of an existing booksOptionDetails, field will ignore if it is null
     *
     * @param id the id of the booksOptionDetails to save.
     * @param booksOptionDetails the booksOptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksOptionDetails,
     * or with status {@code 400 (Bad Request)} if the booksOptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the booksOptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksOptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books-option-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksOptionDetails> partialUpdateBooksOptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksOptionDetails booksOptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooksOptionDetails partially : {}, {}", id, booksOptionDetails);
        if (booksOptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksOptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksOptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksOptionDetails> result = booksOptionDetailsService.partialUpdate(booksOptionDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksOptionDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /books-option-details} : get all the booksOptionDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booksOptionDetails in body.
     */
    @GetMapping("/books-option-details")
    public ResponseEntity<List<BooksOptionDetails>> getAllBooksOptionDetails(
        BooksOptionDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BooksOptionDetails by criteria: {}", criteria);
        Page<BooksOptionDetails> page = booksOptionDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books-option-details/count} : count all the booksOptionDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books-option-details/count")
    public ResponseEntity<Long> countBooksOptionDetails(BooksOptionDetailsCriteria criteria) {
        log.debug("REST request to count BooksOptionDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksOptionDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books-option-details/:id} : get the "id" booksOptionDetails.
     *
     * @param id the id of the booksOptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksOptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books-option-details/{id}")
    public ResponseEntity<BooksOptionDetails> getBooksOptionDetails(@PathVariable Long id) {
        log.debug("REST request to get BooksOptionDetails : {}", id);
        Optional<BooksOptionDetails> booksOptionDetails = booksOptionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksOptionDetails);
    }

    /**
     * {@code DELETE  /books-option-details/:id} : delete the "id" booksOptionDetails.
     *
     * @param id the id of the booksOptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books-option-details/{id}")
    public ResponseEntity<Void> deleteBooksOptionDetails(@PathVariable Long id) {
        log.debug("REST request to delete BooksOptionDetails : {}", id);
        booksOptionDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
