package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.repository.AvatarAttributesRepository;
import com.mycompany.myapp.service.AvatarAttributesQueryService;
import com.mycompany.myapp.service.AvatarAttributesService;
import com.mycompany.myapp.service.criteria.AvatarAttributesCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AvatarAttributes}.
 */
@RestController
@RequestMapping("/api")
public class AvatarAttributesResource {

    private final Logger log = LoggerFactory.getLogger(AvatarAttributesResource.class);

    private static final String ENTITY_NAME = "avatarAttributes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvatarAttributesService avatarAttributesService;

    private final AvatarAttributesRepository avatarAttributesRepository;

    private final AvatarAttributesQueryService avatarAttributesQueryService;

    public AvatarAttributesResource(
        AvatarAttributesService avatarAttributesService,
        AvatarAttributesRepository avatarAttributesRepository,
        AvatarAttributesQueryService avatarAttributesQueryService
    ) {
        this.avatarAttributesService = avatarAttributesService;
        this.avatarAttributesRepository = avatarAttributesRepository;
        this.avatarAttributesQueryService = avatarAttributesQueryService;
    }

    /**
     * {@code POST  /avatar-attributes} : Create a new avatarAttributes.
     *
     * @param avatarAttributes the avatarAttributes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avatarAttributes, or with status {@code 400 (Bad Request)} if the avatarAttributes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/avatar-attributes")
    public ResponseEntity<AvatarAttributes> createAvatarAttributes(@Valid @RequestBody AvatarAttributes avatarAttributes)
        throws URISyntaxException {
        log.debug("REST request to save AvatarAttributes : {}", avatarAttributes);
        if (avatarAttributes.getId() != null) {
            throw new BadRequestAlertException("A new avatarAttributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AvatarAttributes result = avatarAttributesService.save(avatarAttributes);
        return ResponseEntity
            .created(new URI("/api/avatar-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /avatar-attributes/:id} : Updates an existing avatarAttributes.
     *
     * @param id the id of the avatarAttributes to save.
     * @param avatarAttributes the avatarAttributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avatarAttributes,
     * or with status {@code 400 (Bad Request)} if the avatarAttributes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avatarAttributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/avatar-attributes/{id}")
    public ResponseEntity<AvatarAttributes> updateAvatarAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AvatarAttributes avatarAttributes
    ) throws URISyntaxException {
        log.debug("REST request to update AvatarAttributes : {}, {}", id, avatarAttributes);
        if (avatarAttributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avatarAttributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avatarAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AvatarAttributes result = avatarAttributesService.update(avatarAttributes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avatarAttributes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /avatar-attributes/:id} : Partial updates given fields of an existing avatarAttributes, field will ignore if it is null
     *
     * @param id the id of the avatarAttributes to save.
     * @param avatarAttributes the avatarAttributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avatarAttributes,
     * or with status {@code 400 (Bad Request)} if the avatarAttributes is not valid,
     * or with status {@code 404 (Not Found)} if the avatarAttributes is not found,
     * or with status {@code 500 (Internal Server Error)} if the avatarAttributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/avatar-attributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AvatarAttributes> partialUpdateAvatarAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AvatarAttributes avatarAttributes
    ) throws URISyntaxException {
        log.debug("REST request to partial update AvatarAttributes partially : {}, {}", id, avatarAttributes);
        if (avatarAttributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avatarAttributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avatarAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AvatarAttributes> result = avatarAttributesService.partialUpdate(avatarAttributes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avatarAttributes.getId().toString())
        );
    }

    /**
     * {@code GET  /avatar-attributes} : get all the avatarAttributes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avatarAttributes in body.
     */
    @GetMapping("/avatar-attributes")
    public ResponseEntity<List<AvatarAttributes>> getAllAvatarAttributes(
        AvatarAttributesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AvatarAttributes by criteria: {}", criteria);
        Page<AvatarAttributes> page = avatarAttributesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avatar-attributes/count} : count all the avatarAttributes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/avatar-attributes/count")
    public ResponseEntity<Long> countAvatarAttributes(AvatarAttributesCriteria criteria) {
        log.debug("REST request to count AvatarAttributes by criteria: {}", criteria);
        return ResponseEntity.ok().body(avatarAttributesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /avatar-attributes/:id} : get the "id" avatarAttributes.
     *
     * @param id the id of the avatarAttributes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avatarAttributes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/avatar-attributes/{id}")
    public ResponseEntity<AvatarAttributes> getAvatarAttributes(@PathVariable Long id) {
        log.debug("REST request to get AvatarAttributes : {}", id);
        Optional<AvatarAttributes> avatarAttributes = avatarAttributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(avatarAttributes);
    }

    /**
     * {@code DELETE  /avatar-attributes/:id} : delete the "id" avatarAttributes.
     *
     * @param id the id of the avatarAttributes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/avatar-attributes/{id}")
    public ResponseEntity<Void> deleteAvatarAttributes(@PathVariable Long id) {
        log.debug("REST request to delete AvatarAttributes : {}", id);
        avatarAttributesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
