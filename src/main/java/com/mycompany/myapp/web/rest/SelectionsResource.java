package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Selections;
import com.mycompany.myapp.repository.SelectionsRepository;
import com.mycompany.myapp.service.SelectionsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Selections}.
 */
@RestController
@RequestMapping("/api")
public class SelectionsResource {

    private final Logger log = LoggerFactory.getLogger(SelectionsResource.class);

    private static final String ENTITY_NAME = "selections";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SelectionsService selectionsService;

    private final SelectionsRepository selectionsRepository;

    public SelectionsResource(SelectionsService selectionsService, SelectionsRepository selectionsRepository) {
        this.selectionsService = selectionsService;
        this.selectionsRepository = selectionsRepository;
    }

    /**
     * {@code POST  /selections} : Create a new selections.
     *
     * @param selections the selections to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new selections, or with status {@code 400 (Bad Request)} if the selections has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/selections")
    public ResponseEntity<Selections> createSelections(@RequestBody Selections selections) throws URISyntaxException {
        log.debug("REST request to save Selections : {}", selections);
        if (selections.getId() != null) {
            throw new BadRequestAlertException("A new selections cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Selections result = selectionsService.save(selections);
        return ResponseEntity
            .created(new URI("/api/selections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /selections/:id} : Updates an existing selections.
     *
     * @param id the id of the selections to save.
     * @param selections the selections to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selections,
     * or with status {@code 400 (Bad Request)} if the selections is not valid,
     * or with status {@code 500 (Internal Server Error)} if the selections couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/selections/{id}")
    public ResponseEntity<Selections> updateSelections(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Selections selections
    ) throws URISyntaxException {
        log.debug("REST request to update Selections : {}, {}", id, selections);
        if (selections.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selections.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Selections result = selectionsService.update(selections);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selections.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /selections/:id} : Partial updates given fields of an existing selections, field will ignore if it is null
     *
     * @param id the id of the selections to save.
     * @param selections the selections to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selections,
     * or with status {@code 400 (Bad Request)} if the selections is not valid,
     * or with status {@code 404 (Not Found)} if the selections is not found,
     * or with status {@code 500 (Internal Server Error)} if the selections couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/selections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Selections> partialUpdateSelections(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Selections selections
    ) throws URISyntaxException {
        log.debug("REST request to partial update Selections partially : {}, {}", id, selections);
        if (selections.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selections.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Selections> result = selectionsService.partialUpdate(selections);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selections.getId().toString())
        );
    }

    /**
     * {@code GET  /selections} : get all the selections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of selections in body.
     */
    @GetMapping("/selections")
    public List<Selections> getAllSelections() {
        log.debug("REST request to get all Selections");
        return selectionsService.findAll();
    }

    /**
     * {@code GET  /selections/:id} : get the "id" selections.
     *
     * @param id the id of the selections to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the selections, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/selections/{id}")
    public ResponseEntity<Selections> getSelections(@PathVariable Long id) {
        log.debug("REST request to get Selections : {}", id);
        Optional<Selections> selections = selectionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(selections);
    }

    /**
     * {@code DELETE  /selections/:id} : delete the "id" selections.
     *
     * @param id the id of the selections to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/selections/{id}")
    public ResponseEntity<Void> deleteSelections(@PathVariable Long id) {
        log.debug("REST request to delete Selections : {}", id);
        selectionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
