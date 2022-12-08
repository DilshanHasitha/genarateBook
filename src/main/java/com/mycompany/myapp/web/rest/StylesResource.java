package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Styles;
import com.mycompany.myapp.repository.StylesRepository;
import com.mycompany.myapp.service.StylesQueryService;
import com.mycompany.myapp.service.StylesService;
import com.mycompany.myapp.service.criteria.StylesCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Styles}.
 */
@RestController
@RequestMapping("/api")
public class StylesResource {

    private final Logger log = LoggerFactory.getLogger(StylesResource.class);

    private static final String ENTITY_NAME = "styles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StylesService stylesService;

    private final StylesRepository stylesRepository;

    private final StylesQueryService stylesQueryService;

    public StylesResource(StylesService stylesService, StylesRepository stylesRepository, StylesQueryService stylesQueryService) {
        this.stylesService = stylesService;
        this.stylesRepository = stylesRepository;
        this.stylesQueryService = stylesQueryService;
    }

    /**
     * {@code POST  /styles} : Create a new styles.
     *
     * @param styles the styles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new styles, or with status {@code 400 (Bad Request)} if the styles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/styles")
    public ResponseEntity<Styles> createStyles(@Valid @RequestBody Styles styles) throws URISyntaxException {
        log.debug("REST request to save Styles : {}", styles);
        if (styles.getId() != null) {
            throw new BadRequestAlertException("A new styles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Styles result = stylesService.save(styles);
        return ResponseEntity
            .created(new URI("/api/styles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /styles/:id} : Updates an existing styles.
     *
     * @param id the id of the styles to save.
     * @param styles the styles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated styles,
     * or with status {@code 400 (Bad Request)} if the styles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the styles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/styles/{id}")
    public ResponseEntity<Styles> updateStyles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Styles styles
    ) throws URISyntaxException {
        log.debug("REST request to update Styles : {}, {}", id, styles);
        if (styles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, styles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stylesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Styles result = stylesService.update(styles);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, styles.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /styles/:id} : Partial updates given fields of an existing styles, field will ignore if it is null
     *
     * @param id the id of the styles to save.
     * @param styles the styles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated styles,
     * or with status {@code 400 (Bad Request)} if the styles is not valid,
     * or with status {@code 404 (Not Found)} if the styles is not found,
     * or with status {@code 500 (Internal Server Error)} if the styles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/styles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Styles> partialUpdateStyles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Styles styles
    ) throws URISyntaxException {
        log.debug("REST request to partial update Styles partially : {}, {}", id, styles);
        if (styles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, styles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stylesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Styles> result = stylesService.partialUpdate(styles);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, styles.getId().toString())
        );
    }

    /**
     * {@code GET  /styles} : get all the styles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of styles in body.
     */
    @GetMapping("/styles")
    public ResponseEntity<List<Styles>> getAllStyles(
        StylesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Styles by criteria: {}", criteria);
        Page<Styles> page = stylesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /styles/count} : count all the styles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/styles/count")
    public ResponseEntity<Long> countStyles(StylesCriteria criteria) {
        log.debug("REST request to count Styles by criteria: {}", criteria);
        return ResponseEntity.ok().body(stylesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /styles/:id} : get the "id" styles.
     *
     * @param id the id of the styles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the styles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/styles/{id}")
    public ResponseEntity<Styles> getStyles(@PathVariable Long id) {
        log.debug("REST request to get Styles : {}", id);
        Optional<Styles> styles = stylesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(styles);
    }

    /**
     * {@code DELETE  /styles/:id} : delete the "id" styles.
     *
     * @param id the id of the styles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/styles/{id}")
    public ResponseEntity<Void> deleteStyles(@PathVariable Long id) {
        log.debug("REST request to delete Styles : {}", id);
        stylesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
