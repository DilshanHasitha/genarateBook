package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BooksRelatedOption;
import com.mycompany.myapp.repository.BooksRelatedOptionRepository;
import com.mycompany.myapp.service.BooksRelatedOptionQueryService;
import com.mycompany.myapp.service.BooksRelatedOptionService;
import com.mycompany.myapp.service.criteria.BooksRelatedOptionCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BooksRelatedOption}.
 */
@RestController
@RequestMapping("/api")
public class BooksRelatedOptionResource {

    private final Logger log = LoggerFactory.getLogger(BooksRelatedOptionResource.class);

    private static final String ENTITY_NAME = "booksRelatedOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksRelatedOptionService booksRelatedOptionService;

    private final BooksRelatedOptionRepository booksRelatedOptionRepository;

    private final BooksRelatedOptionQueryService booksRelatedOptionQueryService;

    public BooksRelatedOptionResource(
        BooksRelatedOptionService booksRelatedOptionService,
        BooksRelatedOptionRepository booksRelatedOptionRepository,
        BooksRelatedOptionQueryService booksRelatedOptionQueryService
    ) {
        this.booksRelatedOptionService = booksRelatedOptionService;
        this.booksRelatedOptionRepository = booksRelatedOptionRepository;
        this.booksRelatedOptionQueryService = booksRelatedOptionQueryService;
    }

    /**
     * {@code POST  /books-related-options} : Create a new booksRelatedOption.
     *
     * @param booksRelatedOption the booksRelatedOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksRelatedOption, or with status {@code 400 (Bad Request)} if the booksRelatedOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books-related-options")
    public ResponseEntity<BooksRelatedOption> createBooksRelatedOption(@Valid @RequestBody BooksRelatedOption booksRelatedOption)
        throws URISyntaxException {
        log.debug("REST request to save BooksRelatedOption : {}", booksRelatedOption);
        if (booksRelatedOption.getId() != null) {
            throw new BadRequestAlertException("A new booksRelatedOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksRelatedOption result = booksRelatedOptionService.save(booksRelatedOption);
        return ResponseEntity
            .created(new URI("/api/books-related-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books-related-options/:id} : Updates an existing booksRelatedOption.
     *
     * @param id the id of the booksRelatedOption to save.
     * @param booksRelatedOption the booksRelatedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksRelatedOption,
     * or with status {@code 400 (Bad Request)} if the booksRelatedOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksRelatedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books-related-options/{id}")
    public ResponseEntity<BooksRelatedOption> updateBooksRelatedOption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BooksRelatedOption booksRelatedOption
    ) throws URISyntaxException {
        log.debug("REST request to update BooksRelatedOption : {}, {}", id, booksRelatedOption);
        if (booksRelatedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksRelatedOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRelatedOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksRelatedOption result = booksRelatedOptionService.update(booksRelatedOption);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksRelatedOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books-related-options/:id} : Partial updates given fields of an existing booksRelatedOption, field will ignore if it is null
     *
     * @param id the id of the booksRelatedOption to save.
     * @param booksRelatedOption the booksRelatedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksRelatedOption,
     * or with status {@code 400 (Bad Request)} if the booksRelatedOption is not valid,
     * or with status {@code 404 (Not Found)} if the booksRelatedOption is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksRelatedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books-related-options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksRelatedOption> partialUpdateBooksRelatedOption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BooksRelatedOption booksRelatedOption
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooksRelatedOption partially : {}, {}", id, booksRelatedOption);
        if (booksRelatedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksRelatedOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRelatedOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksRelatedOption> result = booksRelatedOptionService.partialUpdate(booksRelatedOption);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booksRelatedOption.getId().toString())
        );
    }

    /**
     * {@code GET  /books-related-options} : get all the booksRelatedOptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booksRelatedOptions in body.
     */
    @GetMapping("/books-related-options")
    public ResponseEntity<List<BooksRelatedOption>> getAllBooksRelatedOptions(
        BooksRelatedOptionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BooksRelatedOptions by criteria: {}", criteria);
        Page<BooksRelatedOption> page = booksRelatedOptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books-related-options/count} : count all the booksRelatedOptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/books-related-options/count")
    public ResponseEntity<Long> countBooksRelatedOptions(BooksRelatedOptionCriteria criteria) {
        log.debug("REST request to count BooksRelatedOptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(booksRelatedOptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /books-related-options/:id} : get the "id" booksRelatedOption.
     *
     * @param id the id of the booksRelatedOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksRelatedOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books-related-options/{id}")
    public ResponseEntity<BooksRelatedOption> getBooksRelatedOption(@PathVariable Long id) {
        log.debug("REST request to get BooksRelatedOption : {}", id);
        Optional<BooksRelatedOption> booksRelatedOption = booksRelatedOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksRelatedOption);
    }

    /**
     * {@code DELETE  /books-related-options/:id} : delete the "id" booksRelatedOption.
     *
     * @param id the id of the booksRelatedOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books-related-options/{id}")
    public ResponseEntity<Void> deleteBooksRelatedOption(@PathVariable Long id) {
        log.debug("REST request to delete BooksRelatedOption : {}", id);
        booksRelatedOptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
