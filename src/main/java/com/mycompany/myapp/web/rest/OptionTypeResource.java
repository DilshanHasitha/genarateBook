package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.OptionType;
import com.mycompany.myapp.repository.OptionTypeRepository;
import com.mycompany.myapp.service.OptionTypeQueryService;
import com.mycompany.myapp.service.OptionTypeService;
import com.mycompany.myapp.service.criteria.OptionTypeCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.OptionType}.
 */
@RestController
@RequestMapping("/api")
public class OptionTypeResource {

    private final Logger log = LoggerFactory.getLogger(OptionTypeResource.class);

    private static final String ENTITY_NAME = "optionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OptionTypeService optionTypeService;

    private final OptionTypeRepository optionTypeRepository;

    private final OptionTypeQueryService optionTypeQueryService;

    public OptionTypeResource(
        OptionTypeService optionTypeService,
        OptionTypeRepository optionTypeRepository,
        OptionTypeQueryService optionTypeQueryService
    ) {
        this.optionTypeService = optionTypeService;
        this.optionTypeRepository = optionTypeRepository;
        this.optionTypeQueryService = optionTypeQueryService;
    }

    /**
     * {@code POST  /option-types} : Create a new optionType.
     *
     * @param optionType the optionType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new optionType, or with status {@code 400 (Bad Request)} if the optionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/option-types")
    public ResponseEntity<OptionType> createOptionType(@Valid @RequestBody OptionType optionType) throws URISyntaxException {
        log.debug("REST request to save OptionType : {}", optionType);
        if (optionType.getId() != null) {
            throw new BadRequestAlertException("A new optionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OptionType result = optionTypeService.save(optionType);
        return ResponseEntity
            .created(new URI("/api/option-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /option-types/:id} : Updates an existing optionType.
     *
     * @param id the id of the optionType to save.
     * @param optionType the optionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionType,
     * or with status {@code 400 (Bad Request)} if the optionType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the optionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/option-types/{id}")
    public ResponseEntity<OptionType> updateOptionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OptionType optionType
    ) throws URISyntaxException {
        log.debug("REST request to update OptionType : {}, {}", id, optionType);
        if (optionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OptionType result = optionTypeService.update(optionType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, optionType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /option-types/:id} : Partial updates given fields of an existing optionType, field will ignore if it is null
     *
     * @param id the id of the optionType to save.
     * @param optionType the optionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionType,
     * or with status {@code 400 (Bad Request)} if the optionType is not valid,
     * or with status {@code 404 (Not Found)} if the optionType is not found,
     * or with status {@code 500 (Internal Server Error)} if the optionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/option-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OptionType> partialUpdateOptionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OptionType optionType
    ) throws URISyntaxException {
        log.debug("REST request to partial update OptionType partially : {}, {}", id, optionType);
        if (optionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OptionType> result = optionTypeService.partialUpdate(optionType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, optionType.getId().toString())
        );
    }

    /**
     * {@code GET  /option-types} : get all the optionTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of optionTypes in body.
     */
    @GetMapping("/option-types")
    public ResponseEntity<List<OptionType>> getAllOptionTypes(
        OptionTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OptionTypes by criteria: {}", criteria);
        Page<OptionType> page = optionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /option-types/count} : count all the optionTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/option-types/count")
    public ResponseEntity<Long> countOptionTypes(OptionTypeCriteria criteria) {
        log.debug("REST request to count OptionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(optionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /option-types/:id} : get the "id" optionType.
     *
     * @param id the id of the optionType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the optionType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/option-types/{id}")
    public ResponseEntity<OptionType> getOptionType(@PathVariable Long id) {
        log.debug("REST request to get OptionType : {}", id);
        Optional<OptionType> optionType = optionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionType);
    }

    /**
     * {@code DELETE  /option-types/:id} : delete the "id" optionType.
     *
     * @param id the id of the optionType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/option-types/{id}")
    public ResponseEntity<Void> deleteOptionType(@PathVariable Long id) {
        log.debug("REST request to delete OptionType : {}", id);
        optionTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
