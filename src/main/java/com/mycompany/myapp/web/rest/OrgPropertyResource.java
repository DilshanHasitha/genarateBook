package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.OrgProperty;
import com.mycompany.myapp.repository.OrgPropertyRepository;
import com.mycompany.myapp.service.OrgPropertyService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.OrgProperty}.
 */
@RestController
@RequestMapping("/api")
public class OrgPropertyResource {

    private final Logger log = LoggerFactory.getLogger(OrgPropertyResource.class);

    private static final String ENTITY_NAME = "orgProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrgPropertyService orgPropertyService;

    private final OrgPropertyRepository orgPropertyRepository;

    public OrgPropertyResource(OrgPropertyService orgPropertyService, OrgPropertyRepository orgPropertyRepository) {
        this.orgPropertyService = orgPropertyService;
        this.orgPropertyRepository = orgPropertyRepository;
    }

    /**
     * {@code POST  /org-properties} : Create a new orgProperty.
     *
     * @param orgProperty the orgProperty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orgProperty, or with status {@code 400 (Bad Request)} if the orgProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/org-properties")
    public ResponseEntity<OrgProperty> createOrgProperty(@RequestBody OrgProperty orgProperty) throws URISyntaxException {
        log.debug("REST request to save OrgProperty : {}", orgProperty);
        if (orgProperty.getId() != null) {
            throw new BadRequestAlertException("A new orgProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrgProperty result = orgPropertyService.save(orgProperty);
        return ResponseEntity
            .created(new URI("/api/org-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /org-properties/:id} : Updates an existing orgProperty.
     *
     * @param id the id of the orgProperty to save.
     * @param orgProperty the orgProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgProperty,
     * or with status {@code 400 (Bad Request)} if the orgProperty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orgProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/org-properties/{id}")
    public ResponseEntity<OrgProperty> updateOrgProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgProperty orgProperty
    ) throws URISyntaxException {
        log.debug("REST request to update OrgProperty : {}, {}", id, orgProperty);
        if (orgProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgProperty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrgProperty result = orgPropertyService.update(orgProperty);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgProperty.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /org-properties/:id} : Partial updates given fields of an existing orgProperty, field will ignore if it is null
     *
     * @param id the id of the orgProperty to save.
     * @param orgProperty the orgProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orgProperty,
     * or with status {@code 400 (Bad Request)} if the orgProperty is not valid,
     * or with status {@code 404 (Not Found)} if the orgProperty is not found,
     * or with status {@code 500 (Internal Server Error)} if the orgProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/org-properties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrgProperty> partialUpdateOrgProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrgProperty orgProperty
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrgProperty partially : {}, {}", id, orgProperty);
        if (orgProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orgProperty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orgPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrgProperty> result = orgPropertyService.partialUpdate(orgProperty);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orgProperty.getId().toString())
        );
    }

    /**
     * {@code GET  /org-properties} : get all the orgProperties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orgProperties in body.
     */
    @GetMapping("/org-properties")
    public List<OrgProperty> getAllOrgProperties() {
        log.debug("REST request to get all OrgProperties");
        return orgPropertyService.findAll();
    }

    /**
     * {@code GET  /org-properties/:id} : get the "id" orgProperty.
     *
     * @param id the id of the orgProperty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orgProperty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/org-properties/{id}")
    public ResponseEntity<OrgProperty> getOrgProperty(@PathVariable Long id) {
        log.debug("REST request to get OrgProperty : {}", id);
        Optional<OrgProperty> orgProperty = orgPropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orgProperty);
    }

    /**
     * {@code DELETE  /org-properties/:id} : delete the "id" orgProperty.
     *
     * @param id the id of the orgProperty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/org-properties/{id}")
    public ResponseEntity<Void> deleteOrgProperty(@PathVariable Long id) {
        log.debug("REST request to delete OrgProperty : {}", id);
        orgPropertyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
