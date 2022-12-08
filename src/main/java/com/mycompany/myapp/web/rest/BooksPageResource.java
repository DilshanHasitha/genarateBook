package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.repository.BooksPageRepository;
import com.mycompany.myapp.service.BooksPageQueryService;
import com.mycompany.myapp.service.BooksPageService;
import com.mycompany.myapp.service.criteria.BooksPageCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BooksPage}.
 */
@RestController
@RequestMapping("/api")
public class BooksPageResource {

    private final Logger log = LoggerFactory.getLogger(BooksPageResource.class);

    private static final String ENTITY_NAME = "booksPage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksPageService booksPageService;

    private final BooksPageRepository booksPageRepository;

    private final BooksPageQueryService booksPageQueryService;

    public BooksPageResource(
        BooksPageService booksPageService,
        BooksPageRepository booksPageRepository,
        BooksPageQueryService booksPageQueryService
    ) {
        this.booksPageService = booksPageService;
        this.booksPageRepository = booksPageRepository;
        this.booksPageQueryService = booksPageQueryService;
    }

    /**
     * {@code POST  /books-pages} : Create a new booksPage.
     *
     * @param booksPage the booksPage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksPage, or with status {@code 400 (Bad Request)} if the booksPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books-pages")
    public ResponseEntity<BooksPage> createBooksPage(@Valid @RequestBody BooksPage booksPage) throws URISyntaxException {
        log.debug("REST request to save BooksPage : {}", booksPage);
        if (booksPage.getId() != null) {
            throw new BadRequestAlertException("A new booksPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksPage result = booksPageService.save(booksPage);
        return ResponseEntity
            .created(new URI("/api/books-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books-pages/:id} : Updates an existing booksPage.
     *
     * @param id the id of the booksPage to save.
     * @param booksPage the booksPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksPage,
     * or with status {@code 400 (Bad Request)} if the booksPage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books-pages/{id}")
    public ResponseEntity<BooksPage> updateBooksPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BooksPage booksPage
    ) throws URISyntaxException {
        log.debug("REST request to update BooksPage : {}, {}", id, booksPage);
        if (booksPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksPage result = booksPageService.update(booksPage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksPage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books-pages/:id} : Partial updates given fields of an existing booksPage, field will ignore if it is null
     *
     * @param id the id of the booksPage to save.
     * @param booksPage the booksPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksPage,
     * or with status {@code 400 (Bad Request)} if the booksPage is not valid,
     * or with status {@code 404 (Not Found)} if the booksPage is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books-pages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksPage> partialUpdateBooksPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BooksPage booksPage
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooksPage partially : {}, {}", id, booksPage);
        if (booksPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksPage> result = booksPageService.partialUpdate(booksPage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksPage.getId().toString())
        );
    }

    /**
     * {@code GET  /books-pages} : get all the booksPages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booksPages in body.
     */
    @GetMapping("/books-pages")
    public ResponseEntity<List<BooksPage>> getAllBooksPages(
        BooksPageCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BooksPages by criteria: {}", criteria);
        Page<BooksPage> page = booksPageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books-pages/count} : count all the booksPages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books-pages/count")
    public ResponseEntity<Long> countBooksPages(BooksPageCriteria criteria) {
        log.debug("REST request to count BooksPages by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksPageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books-pages/:id} : get the "id" booksPage.
     *
     * @param id the id of the booksPage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksPage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books-pages/{id}")
    public ResponseEntity<BooksPage> getBooksPage(@PathVariable Long id) {
        log.debug("REST request to get BooksPage : {}", id);
        Optional<BooksPage> booksPage = booksPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksPage);
    }

    /**
     * {@code DELETE  /books-pages/:id} : delete the "id" booksPage.
     *
     * @param id the id of the booksPage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books-pages/{id}")
    public ResponseEntity<Void> deleteBooksPage(@PathVariable Long id) {
        log.debug("REST request to delete BooksPage : {}", id);
        booksPageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
