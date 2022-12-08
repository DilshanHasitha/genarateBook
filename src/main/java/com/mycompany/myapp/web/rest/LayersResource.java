package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayersRepository;
import com.mycompany.myapp.service.LayersQueryService;
import com.mycompany.myapp.service.LayersService;
import com.mycompany.myapp.service.criteria.LayersCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Layers}.
 */
@RestController
@RequestMapping("/api")
public class LayersResource {

    private final Logger log = LoggerFactory.getLogger(LayersResource.class);

    private static final String ENTITY_NAME = "layers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LayersService layersService;

    private final LayersRepository layersRepository;

    private final LayersQueryService layersQueryService;

    public LayersResource(LayersService layersService, LayersRepository layersRepository, LayersQueryService layersQueryService) {
        this.layersService = layersService;
        this.layersRepository = layersRepository;
        this.layersQueryService = layersQueryService;
    }

    /**
     * {@code POST  /layers} : Create a new layers.
     *
     * @param layers the layers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new layers, or with status {@code 400 (Bad Request)} if the layers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/layers")
    public ResponseEntity<Layers> createLayers(@Valid @RequestBody Layers layers) throws URISyntaxException {
        log.debug("REST request to save Layers : {}", layers);
        if (layers.getId() != null) {
            throw new BadRequestAlertException("A new layers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Layers result = layersService.save(layers);
        return ResponseEntity
            .created(new URI("/api/layers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /layers/:id} : Updates an existing layers.
     *
     * @param id the id of the layers to save.
     * @param layers the layers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layers,
     * or with status {@code 400 (Bad Request)} if the layers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the layers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/layers/{id}")
    public ResponseEntity<Layers> updateLayers(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Layers layers
    ) throws URISyntaxException {
        log.debug("REST request to update Layers : {}, {}", id, layers);
        if (layers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Layers result = layersService.update(layers);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layers.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /layers/:id} : Partial updates given fields of an existing layers, field will ignore if it is null
     *
     * @param id the id of the layers to save.
     * @param layers the layers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layers,
     * or with status {@code 400 (Bad Request)} if the layers is not valid,
     * or with status {@code 404 (Not Found)} if the layers is not found,
     * or with status {@code 500 (Internal Server Error)} if the layers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/layers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Layers> partialUpdateLayers(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Layers layers
    ) throws URISyntaxException {
        log.debug("REST request to partial update Layers partially : {}, {}", id, layers);
        if (layers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Layers> result = layersService.partialUpdate(layers);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layers.getId().toString())
        );
    }

    /**
     * {@code GET  /layers} : get all the layers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of layers in body.
     */
    @GetMapping("/layers")
    public ResponseEntity<List<Layers>> getAllLayers(
        LayersCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Layers by criteria: {}", criteria);
        Page<Layers> page = layersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /layers/count} : count all the layers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/layers/count")
    public ResponseEntity<Long> countLayers(LayersCriteria criteria) {
        log.debug("REST request to count Layers by criteria: {}", criteria);
        return ResponseEntity.ok().body(layersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /layers/:id} : get the "id" layers.
     *
     * @param id the id of the layers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the layers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/layers/{id}")
    public ResponseEntity<Layers> getLayers(@PathVariable Long id) {
        log.debug("REST request to get Layers : {}", id);
        Optional<Layers> layers = layersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(layers);
    }

    /**
     * {@code DELETE  /layers/:id} : delete the "id" layers.
     *
     * @param id the id of the layers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/layers/{id}")
    public ResponseEntity<Void> deleteLayers(@PathVariable Long id) {
        log.debug("REST request to delete Layers : {}", id);
        layersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
