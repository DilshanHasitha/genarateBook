package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BooksVariables;
import com.mycompany.myapp.repository.BooksVariablesRepository;
import com.mycompany.myapp.service.BooksVariablesQueryService;
import com.mycompany.myapp.service.BooksVariablesService;
import com.mycompany.myapp.service.criteria.BooksVariablesCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BooksVariables}.
 */
@RestController
@RequestMapping("/api")
public class BooksVariablesResource {

    private final Logger log = LoggerFactory.getLogger(BooksVariablesResource.class);

    private static final String ENTITY_NAME = "booksVariables";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksVariablesService booksVariablesService;

    private final BooksVariablesRepository booksVariablesRepository;

    private final BooksVariablesQueryService booksVariablesQueryService;

    public BooksVariablesResource(
        BooksVariablesService booksVariablesService,
        BooksVariablesRepository booksVariablesRepository,
        BooksVariablesQueryService booksVariablesQueryService
    ) {
        this.booksVariablesService = booksVariablesService;
        this.booksVariablesRepository = booksVariablesRepository;
        this.booksVariablesQueryService = booksVariablesQueryService;
    }

    /**
     * {@code POST  /books-variables} : Create a new booksVariables.
     *
     * @param booksVariables the booksVariables to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksVariables, or with status {@code 400 (Bad Request)} if the booksVariables has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books-variables")
    public ResponseEntity<BooksVariables> createBooksVariables(@RequestBody BooksVariables booksVariables) throws URISyntaxException {
        log.debug("REST request to save BooksVariables : {}", booksVariables);
        if (booksVariables.getId() != null) {
            throw new BadRequestAlertException("A new booksVariables cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksVariables result = booksVariablesService.save(booksVariables);
        return ResponseEntity
            .created(new URI("/api/books-variables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books-variables/:id} : Updates an existing booksVariables.
     *
     * @param id the id of the booksVariables to save.
     * @param booksVariables the booksVariables to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksVariables,
     * or with status {@code 400 (Bad Request)} if the booksVariables is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksVariables couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books-variables/{id}")
    public ResponseEntity<BooksVariables> updateBooksVariables(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksVariables booksVariables
    ) throws URISyntaxException {
        log.debug("REST request to update BooksVariables : {}, {}", id, booksVariables);
        if (booksVariables.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksVariables.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksVariablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksVariables result = booksVariablesService.update(booksVariables);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksVariables.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books-variables/:id} : Partial updates given fields of an existing booksVariables, field will ignore if it is null
     *
     * @param id the id of the booksVariables to save.
     * @param booksVariables the booksVariables to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksVariables,
     * or with status {@code 400 (Bad Request)} if the booksVariables is not valid,
     * or with status {@code 404 (Not Found)} if the booksVariables is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksVariables couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books-variables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksVariables> partialUpdateBooksVariables(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksVariables booksVariables
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooksVariables partially : {}, {}", id, booksVariables);
        if (booksVariables.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksVariables.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksVariablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksVariables> result = booksVariablesService.partialUpdate(booksVariables);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksVariables.getId().toString())
        );
    }

    /**
     * {@code GET  /books-variables} : get all the booksVariables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booksVariables in body.
     */
    @GetMapping("/books-variables")
    public ResponseEntity<List<BooksVariables>> getAllBooksVariables(
        BooksVariablesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BooksVariables by criteria: {}", criteria);
        Page<BooksVariables> page = booksVariablesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books-variables/count} : count all the booksVariables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books-variables/count")
    public ResponseEntity<Long> countBooksVariables(BooksVariablesCriteria criteria) {
        log.debug("REST request to count BooksVariables by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksVariablesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books-variables/:id} : get the "id" booksVariables.
     *
     * @param id the id of the booksVariables to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksVariables, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books-variables/{id}")
    public ResponseEntity<BooksVariables> getBooksVariables(@PathVariable Long id) {
        log.debug("REST request to get BooksVariables : {}", id);
        Optional<BooksVariables> booksVariables = booksVariablesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksVariables);
    }

    /**
     * {@code DELETE  /books-variables/:id} : delete the "id" booksVariables.
     *
     * @param id the id of the booksVariables to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books-variables/{id}")
    public ResponseEntity<Void> deleteBooksVariables(@PathVariable Long id) {
        log.debug("REST request to delete BooksVariables : {}", id);
        booksVariablesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
