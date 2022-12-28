package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FontFamily;
import com.mycompany.myapp.repository.FontFamilyRepository;
import com.mycompany.myapp.service.FontFamilyService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FontFamily}.
 */
@RestController
@RequestMapping("/api")
public class FontFamilyResource {

    private final Logger log = LoggerFactory.getLogger(FontFamilyResource.class);

    private static final String ENTITY_NAME = "fontFamily";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FontFamilyService fontFamilyService;

    private final FontFamilyRepository fontFamilyRepository;

    public FontFamilyResource(FontFamilyService fontFamilyService, FontFamilyRepository fontFamilyRepository) {
        this.fontFamilyService = fontFamilyService;
        this.fontFamilyRepository = fontFamilyRepository;
    }

    /**
     * {@code POST  /font-families} : Create a new fontFamily.
     *
     * @param fontFamily the fontFamily to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fontFamily, or with status {@code 400 (Bad Request)} if the fontFamily has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/font-families")
    public ResponseEntity<FontFamily> createFontFamily(@RequestBody FontFamily fontFamily) throws URISyntaxException {
        log.debug("REST request to save FontFamily : {}", fontFamily);
        if (fontFamily.getId() != null) {
            throw new BadRequestAlertException("A new fontFamily cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FontFamily result = fontFamilyService.save(fontFamily);
        return ResponseEntity
            .created(new URI("/api/font-families/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /font-families/:id} : Updates an existing fontFamily.
     *
     * @param id the id of the fontFamily to save.
     * @param fontFamily the fontFamily to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fontFamily,
     * or with status {@code 400 (Bad Request)} if the fontFamily is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fontFamily couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/font-families/{id}")
    public ResponseEntity<FontFamily> updateFontFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FontFamily fontFamily
    ) throws URISyntaxException {
        log.debug("REST request to update FontFamily : {}, {}", id, fontFamily);
        if (fontFamily.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fontFamily.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fontFamilyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FontFamily result = fontFamilyService.update(fontFamily);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fontFamily.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /font-families/:id} : Partial updates given fields of an existing fontFamily, field will ignore if it is null
     *
     * @param id the id of the fontFamily to save.
     * @param fontFamily the fontFamily to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fontFamily,
     * or with status {@code 400 (Bad Request)} if the fontFamily is not valid,
     * or with status {@code 404 (Not Found)} if the fontFamily is not found,
     * or with status {@code 500 (Internal Server Error)} if the fontFamily couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/font-families/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FontFamily> partialUpdateFontFamily(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FontFamily fontFamily
    ) throws URISyntaxException {
        log.debug("REST request to partial update FontFamily partially : {}, {}", id, fontFamily);
        if (fontFamily.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fontFamily.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fontFamilyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FontFamily> result = fontFamilyService.partialUpdate(fontFamily);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fontFamily.getId().toString())
        );
    }

    /**
     * {@code GET  /font-families} : get all the fontFamilies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fontFamilies in body.
     */
    @GetMapping("/font-families")
    public List<FontFamily> getAllFontFamilies() {
        log.debug("REST request to get all FontFamilies");
        return fontFamilyService.findAll();
    }

    /**
     * {@code GET  /font-families/:id} : get the "id" fontFamily.
     *
     * @param id the id of the fontFamily to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fontFamily, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/font-families/{id}")
    public ResponseEntity<FontFamily> getFontFamily(@PathVariable Long id) {
        log.debug("REST request to get FontFamily : {}", id);
        Optional<FontFamily> fontFamily = fontFamilyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fontFamily);
    }

    /**
     * {@code DELETE  /font-families/:id} : delete the "id" fontFamily.
     *
     * @param id the id of the fontFamily to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/font-families/{id}")
    public ResponseEntity<Void> deleteFontFamily(@PathVariable Long id) {
        log.debug("REST request to delete FontFamily : {}", id);
        fontFamilyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
