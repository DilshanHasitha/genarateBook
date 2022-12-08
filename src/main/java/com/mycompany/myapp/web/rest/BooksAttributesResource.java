package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BooksAttributes;
import com.mycompany.myapp.repository.BooksAttributesRepository;
import com.mycompany.myapp.service.BooksAttributesQueryService;
import com.mycompany.myapp.service.BooksAttributesService;
import com.mycompany.myapp.service.criteria.BooksAttributesCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BooksAttributes}.
 */
@RestController
@RequestMapping("/api")
public class BooksAttributesResource {

    private final Logger log = LoggerFactory.getLogger(BooksAttributesResource.class);

    private static final String ENTITY_NAME = "booksAttributes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksAttributesService booksAttributesService;

    private final BooksAttributesRepository booksAttributesRepository;

    private final BooksAttributesQueryService booksAttributesQueryService;

    public BooksAttributesResource(
        BooksAttributesService booksAttributesService,
        BooksAttributesRepository booksAttributesRepository,
        BooksAttributesQueryService booksAttributesQueryService
    ) {
        this.booksAttributesService = booksAttributesService;
        this.booksAttributesRepository = booksAttributesRepository;
        this.booksAttributesQueryService = booksAttributesQueryService;
    }

    /**
     * {@code POST  /books-attributes} : Create a new booksAttributes.
     *
     * @param booksAttributes the booksAttributes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksAttributes, or with status {@code 400 (Bad Request)} if the booksAttributes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books-attributes")
    public ResponseEntity<BooksAttributes> createBooksAttributes(@RequestBody BooksAttributes booksAttributes) throws URISyntaxException {
        log.debug("REST request to save BooksAttributes : {}", booksAttributes);
        if (booksAttributes.getId() != null) {
            throw new BadRequestAlertException("A new booksAttributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksAttributes result = booksAttributesService.save(booksAttributes);
        return ResponseEntity
            .created(new URI("/api/books-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books-attributes/:id} : Updates an existing booksAttributes.
     *
     * @param id the id of the booksAttributes to save.
     * @param booksAttributes the booksAttributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksAttributes,
     * or with status {@code 400 (Bad Request)} if the booksAttributes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksAttributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books-attributes/{id}")
    public ResponseEntity<BooksAttributes> updateBooksAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksAttributes booksAttributes
    ) throws URISyntaxException {
        log.debug("REST request to update BooksAttributes : {}, {}", id, booksAttributes);
        if (booksAttributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksAttributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksAttributes result = booksAttributesService.update(booksAttributes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksAttributes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books-attributes/:id} : Partial updates given fields of an existing booksAttributes, field will ignore if it is null
     *
     * @param id the id of the booksAttributes to save.
     * @param booksAttributes the booksAttributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksAttributes,
     * or with status {@code 400 (Bad Request)} if the booksAttributes is not valid,
     * or with status {@code 404 (Not Found)} if the booksAttributes is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksAttributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books-attributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksAttributes> partialUpdateBooksAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksAttributes booksAttributes
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooksAttributes partially : {}, {}", id, booksAttributes);
        if (booksAttributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksAttributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksAttributes> result = booksAttributesService.partialUpdate(booksAttributes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksAttributes.getId().toString())
        );
    }

    /**
     * {@code GET  /books-attributes} : get all the booksAttributes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booksAttributes in body.
     */
    @GetMapping("/books-attributes")
    public ResponseEntity<List<BooksAttributes>> getAllBooksAttributes(
        BooksAttributesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BooksAttributes by criteria: {}", criteria);
        Page<BooksAttributes> page = booksAttributesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books-attributes/count} : count all the booksAttributes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books-attributes/count")
    public ResponseEntity<Long> countBooksAttributes(BooksAttributesCriteria criteria) {
        log.debug("REST request to count BooksAttributes by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksAttributesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books-attributes/:id} : get the "id" booksAttributes.
     *
     * @param id the id of the booksAttributes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksAttributes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books-attributes/{id}")
    public ResponseEntity<BooksAttributes> getBooksAttributes(@PathVariable Long id) {
        log.debug("REST request to get BooksAttributes : {}", id);
        Optional<BooksAttributes> booksAttributes = booksAttributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksAttributes);
    }

    /**
     * {@code DELETE  /books-attributes/:id} : delete the "id" booksAttributes.
     *
     * @param id the id of the booksAttributes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books-attributes/{id}")
    public ResponseEntity<Void> deleteBooksAttributes(@PathVariable Long id) {
        log.debug("REST request to delete BooksAttributes : {}", id);
        booksAttributesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
