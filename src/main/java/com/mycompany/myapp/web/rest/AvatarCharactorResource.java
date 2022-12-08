package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AvatarCharactor;
import com.mycompany.myapp.repository.AvatarCharactorRepository;
import com.mycompany.myapp.service.AvatarCharactorQueryService;
import com.mycompany.myapp.service.AvatarCharactorService;
import com.mycompany.myapp.service.criteria.AvatarCharactorCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AvatarCharactor}.
 */
@RestController
@RequestMapping("/api")
public class AvatarCharactorResource {

    private final Logger log = LoggerFactory.getLogger(AvatarCharactorResource.class);

    private static final String ENTITY_NAME = "avatarCharactor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvatarCharactorService avatarCharactorService;

    private final AvatarCharactorRepository avatarCharactorRepository;

    private final AvatarCharactorQueryService avatarCharactorQueryService;

    public AvatarCharactorResource(
        AvatarCharactorService avatarCharactorService,
        AvatarCharactorRepository avatarCharactorRepository,
        AvatarCharactorQueryService avatarCharactorQueryService
    ) {
        this.avatarCharactorService = avatarCharactorService;
        this.avatarCharactorRepository = avatarCharactorRepository;
        this.avatarCharactorQueryService = avatarCharactorQueryService;
    }

    /**
     * {@code POST  /avatar-charactors} : Create a new avatarCharactor.
     *
     * @param avatarCharactor the avatarCharactor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avatarCharactor, or with status {@code 400 (Bad Request)} if the avatarCharactor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/avatar-charactors")
    public ResponseEntity<AvatarCharactor> createAvatarCharactor(@Valid @RequestBody AvatarCharactor avatarCharactor)
        throws URISyntaxException {
        log.debug("REST request to save AvatarCharactor : {}", avatarCharactor);
        if (avatarCharactor.getId() != null) {
            throw new BadRequestAlertException("A new avatarCharactor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AvatarCharactor result = avatarCharactorService.save(avatarCharactor);
        return ResponseEntity
            .created(new URI("/api/avatar-charactors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /avatar-charactors/:id} : Updates an existing avatarCharactor.
     *
     * @param id the id of the avatarCharactor to save.
     * @param avatarCharactor the avatarCharactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avatarCharactor,
     * or with status {@code 400 (Bad Request)} if the avatarCharactor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avatarCharactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/avatar-charactors/{id}")
    public ResponseEntity<AvatarCharactor> updateAvatarCharactor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AvatarCharactor avatarCharactor
    ) throws URISyntaxException {
        log.debug("REST request to update AvatarCharactor : {}, {}", id, avatarCharactor);
        if (avatarCharactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avatarCharactor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avatarCharactorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AvatarCharactor result = avatarCharactorService.update(avatarCharactor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avatarCharactor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /avatar-charactors/:id} : Partial updates given fields of an existing avatarCharactor, field will ignore if it is null
     *
     * @param id the id of the avatarCharactor to save.
     * @param avatarCharactor the avatarCharactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avatarCharactor,
     * or with status {@code 400 (Bad Request)} if the avatarCharactor is not valid,
     * or with status {@code 404 (Not Found)} if the avatarCharactor is not found,
     * or with status {@code 500 (Internal Server Error)} if the avatarCharactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/avatar-charactors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AvatarCharactor> partialUpdateAvatarCharactor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AvatarCharactor avatarCharactor
    ) throws URISyntaxException {
        log.debug("REST request to partial update AvatarCharactor partially : {}, {}", id, avatarCharactor);
        if (avatarCharactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avatarCharactor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avatarCharactorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AvatarCharactor> result = avatarCharactorService.partialUpdate(avatarCharactor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avatarCharactor.getId().toString())
        );
    }

    /**
     * {@code GET  /avatar-charactors} : get all the avatarCharactors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avatarCharactors in body.
     */
    @GetMapping("/avatar-charactors")
    public ResponseEntity<List<AvatarCharactor>> getAllAvatarCharactors(
        AvatarCharactorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AvatarCharactors by criteria: {}", criteria);
        Page<AvatarCharactor> page = avatarCharactorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avatar-charactors/count} : count all the avatarCharactors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/avatar-charactors/count")
    public ResponseEntity<Long> countAvatarCharactors(AvatarCharactorCriteria criteria) {
        log.debug("REST request to count AvatarCharactors by criteria: {}", criteria);
        return ResponseEntity.ok().body(avatarCharactorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /avatar-charactors/:id} : get the "id" avatarCharactor.
     *
     * @param id the id of the avatarCharactor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avatarCharactor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/avatar-charactors/{id}")
    public ResponseEntity<AvatarCharactor> getAvatarCharactor(@PathVariable Long id) {
        log.debug("REST request to get AvatarCharactor : {}", id);
        Optional<AvatarCharactor> avatarCharactor = avatarCharactorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(avatarCharactor);
    }

    /**
     * {@code DELETE  /avatar-charactors/:id} : delete the "id" avatarCharactor.
     *
     * @param id the id of the avatarCharactor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/avatar-charactors/{id}")
    public ResponseEntity<Void> deleteAvatarCharactor(@PathVariable Long id) {
        log.debug("REST request to delete AvatarCharactor : {}", id);
        avatarCharactorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
