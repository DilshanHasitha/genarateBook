package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ImageStoreType;
import com.mycompany.myapp.repository.ImageStoreTypeRepository;
import com.mycompany.myapp.service.ImageStoreTypeQueryService;
import com.mycompany.myapp.service.ImageStoreTypeService;
import com.mycompany.myapp.service.criteria.ImageStoreTypeCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ImageStoreType}.
 */
@RestController
@RequestMapping("/api")
public class ImageStoreTypeResource {

    private final Logger log = LoggerFactory.getLogger(ImageStoreTypeResource.class);

    private static final String ENTITY_NAME = "imageStoreType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageStoreTypeService imageStoreTypeService;

    private final ImageStoreTypeRepository imageStoreTypeRepository;

    private final ImageStoreTypeQueryService imageStoreTypeQueryService;

    public ImageStoreTypeResource(
        ImageStoreTypeService imageStoreTypeService,
        ImageStoreTypeRepository imageStoreTypeRepository,
        ImageStoreTypeQueryService imageStoreTypeQueryService
    ) {
        this.imageStoreTypeService = imageStoreTypeService;
        this.imageStoreTypeRepository = imageStoreTypeRepository;
        this.imageStoreTypeQueryService = imageStoreTypeQueryService;
    }

    /**
     * {@code POST  /image-store-types} : Create a new imageStoreType.
     *
     * @param imageStoreType the imageStoreType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageStoreType, or with status {@code 400 (Bad Request)} if the imageStoreType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-store-types")
    public ResponseEntity<ImageStoreType> createImageStoreType(@Valid @RequestBody ImageStoreType imageStoreType)
        throws URISyntaxException {
        log.debug("REST request to save ImageStoreType : {}", imageStoreType);
        if (imageStoreType.getId() != null) {
            throw new BadRequestAlertException("A new imageStoreType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageStoreType result = imageStoreTypeService.save(imageStoreType);
        return ResponseEntity
            .created(new URI("/api/image-store-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-store-types/:id} : Updates an existing imageStoreType.
     *
     * @param id the id of the imageStoreType to save.
     * @param imageStoreType the imageStoreType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageStoreType,
     * or with status {@code 400 (Bad Request)} if the imageStoreType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageStoreType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-store-types/{id}")
    public ResponseEntity<ImageStoreType> updateImageStoreType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageStoreType imageStoreType
    ) throws URISyntaxException {
        log.debug("REST request to update ImageStoreType : {}, {}", id, imageStoreType);
        if (imageStoreType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageStoreType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageStoreTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageStoreType result = imageStoreTypeService.update(imageStoreType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imageStoreType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-store-types/:id} : Partial updates given fields of an existing imageStoreType, field will ignore if it is null
     *
     * @param id the id of the imageStoreType to save.
     * @param imageStoreType the imageStoreType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageStoreType,
     * or with status {@code 400 (Bad Request)} if the imageStoreType is not valid,
     * or with status {@code 404 (Not Found)} if the imageStoreType is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageStoreType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-store-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageStoreType> partialUpdateImageStoreType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageStoreType imageStoreType
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageStoreType partially : {}, {}", id, imageStoreType);
        if (imageStoreType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageStoreType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageStoreTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageStoreType> result = imageStoreTypeService.partialUpdate(imageStoreType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imageStoreType.getId().toString())
        );
    }

    /**
     * {@code GET  /image-store-types} : get all the imageStoreTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageStoreTypes in body.
     */
    @GetMapping("/image-store-types")
    public ResponseEntity<List<ImageStoreType>> getAllImageStoreTypes(
        ImageStoreTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ImageStoreTypes by criteria: {}", criteria);
        Page<ImageStoreType> page = imageStoreTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-store-types/count} : count all the imageStoreTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/image-store-types/count")
    public ResponseEntity<Long> countImageStoreTypes(ImageStoreTypeCriteria criteria) {
        log.debug("REST request to count ImageStoreTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageStoreTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-store-types/:id} : get the "id" imageStoreType.
     *
     * @param id the id of the imageStoreType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageStoreType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-store-types/{id}")
    public ResponseEntity<ImageStoreType> getImageStoreType(@PathVariable Long id) {
        log.debug("REST request to get ImageStoreType : {}", id);
        Optional<ImageStoreType> imageStoreType = imageStoreTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageStoreType);
    }

    /**
     * {@code DELETE  /image-store-types/:id} : delete the "id" imageStoreType.
     *
     * @param id the id of the imageStoreType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-store-types/{id}")
    public ResponseEntity<Void> deleteImageStoreType(@PathVariable Long id) {
        log.debug("REST request to delete ImageStoreType : {}", id);
        imageStoreTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
