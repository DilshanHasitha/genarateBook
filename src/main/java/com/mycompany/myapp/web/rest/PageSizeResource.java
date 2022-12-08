package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PageSize;
import com.mycompany.myapp.repository.PageSizeRepository;
import com.mycompany.myapp.service.PageSizeQueryService;
import com.mycompany.myapp.service.PageSizeService;
import com.mycompany.myapp.service.criteria.PageSizeCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PageSize}.
 */
@RestController
@RequestMapping("/api")
public class PageSizeResource {

    private final Logger log = LoggerFactory.getLogger(PageSizeResource.class);

    private static final String ENTITY_NAME = "pageSize";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PageSizeService pageSizeService;

    private final PageSizeRepository pageSizeRepository;

    private final PageSizeQueryService pageSizeQueryService;

    public PageSizeResource(
        PageSizeService pageSizeService,
        PageSizeRepository pageSizeRepository,
        PageSizeQueryService pageSizeQueryService
    ) {
        this.pageSizeService = pageSizeService;
        this.pageSizeRepository = pageSizeRepository;
        this.pageSizeQueryService = pageSizeQueryService;
    }

    /**
     * {@code POST  /page-sizes} : Create a new pageSize.
     *
     * @param pageSize the pageSize to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pageSize, or with status {@code 400 (Bad Request)} if the pageSize has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/page-sizes")
    public ResponseEntity<PageSize> createPageSize(@RequestBody PageSize pageSize) throws URISyntaxException {
        log.debug("REST request to save PageSize : {}", pageSize);
        if (pageSize.getId() != null) {
            throw new BadRequestAlertException("A new pageSize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PageSize result = pageSizeService.save(pageSize);
        return ResponseEntity
            .created(new URI("/api/page-sizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /page-sizes/:id} : Updates an existing pageSize.
     *
     * @param id the id of the pageSize to save.
     * @param pageSize the pageSize to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageSize,
     * or with status {@code 400 (Bad Request)} if the pageSize is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pageSize couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/page-sizes/{id}")
    public ResponseEntity<PageSize> updatePageSize(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PageSize pageSize
    ) throws URISyntaxException {
        log.debug("REST request to update PageSize : {}, {}", id, pageSize);
        if (pageSize.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageSize.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PageSize result = pageSizeService.update(pageSize);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pageSize.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /page-sizes/:id} : Partial updates given fields of an existing pageSize, field will ignore if it is null
     *
     * @param id the id of the pageSize to save.
     * @param pageSize the pageSize to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageSize,
     * or with status {@code 400 (Bad Request)} if the pageSize is not valid,
     * or with status {@code 404 (Not Found)} if the pageSize is not found,
     * or with status {@code 500 (Internal Server Error)} if the pageSize couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/page-sizes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PageSize> partialUpdatePageSize(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PageSize pageSize
    ) throws URISyntaxException {
        log.debug("REST request to partial update PageSize partially : {}, {}", id, pageSize);
        if (pageSize.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageSize.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PageSize> result = pageSizeService.partialUpdate(pageSize);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pageSize.getId().toString())
        );
    }

    /**
     * {@code GET  /page-sizes} : get all the pageSizes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pageSizes in body.
     */
    @GetMapping("/page-sizes")
    public ResponseEntity<List<PageSize>> getAllPageSizes(
        PageSizeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PageSizes by criteria: {}", criteria);
        Page<PageSize> page = pageSizeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /page-sizes/count} : count all the pageSizes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/page-sizes/count")
    public ResponseEntity<Long> countPageSizes(PageSizeCriteria criteria) {
        log.debug("REST request to count PageSizes by criteria: {}", criteria);
        return ResponseEntity.ok().body(pageSizeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /page-sizes/:id} : get the "id" pageSize.
     *
     * @param id the id of the pageSize to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageSize, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/page-sizes/{id}")
    public ResponseEntity<PageSize> getPageSize(@PathVariable Long id) {
        log.debug("REST request to get PageSize : {}", id);
        Optional<PageSize> pageSize = pageSizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pageSize);
    }

    /**
     * {@code DELETE  /page-sizes/:id} : delete the "id" pageSize.
     *
     * @param id the id of the pageSize to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/page-sizes/{id}")
    public ResponseEntity<Void> deletePageSize(@PathVariable Long id) {
        log.debug("REST request to delete PageSize : {}", id);
        pageSizeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
