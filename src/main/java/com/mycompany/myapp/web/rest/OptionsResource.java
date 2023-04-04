package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.domain.OrgProperty;
import com.mycompany.myapp.repository.OptionsRepository;
import com.mycompany.myapp.service.OptionsQueryService;
import com.mycompany.myapp.service.OptionsService;
import com.mycompany.myapp.service.criteria.OptionsCriteria;
import com.mycompany.myapp.service.dto.CustomOptionDTO;
import com.mycompany.myapp.service.dto.wikunum.JwtResponse;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Options}.
 */
@RestController
@RequestMapping("/api")
public class OptionsResource {

    private final Logger log = LoggerFactory.getLogger(OptionsResource.class);

    private static final String ENTITY_NAME = "options";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OptionsService optionsService;

    private final OptionsRepository optionsRepository;

    private final OptionsQueryService optionsQueryService;
    private final UserJWTController userJWTController;

    public OptionsResource(
        OptionsService optionsService,
        OptionsRepository optionsRepository,
        OptionsQueryService optionsQueryService,
        UserJWTController userJWTController
    ) {
        this.optionsService = optionsService;
        this.optionsRepository = optionsRepository;
        this.optionsQueryService = optionsQueryService;
        this.userJWTController = userJWTController;
    }

    /**
     * {@code POST  /options} : Create a new options.
     *
     * @param options the options to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new options, or with status {@code 400 (Bad Request)} if the options has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/options")
    public ResponseEntity<Options> createOptions(@Valid @RequestBody Options options) throws URISyntaxException {
        log.debug("REST request to save Options : {}", options);
        if (options.getId() != null) {
            throw new BadRequestAlertException("A new options cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Options result = optionsService.save(options);
        return ResponseEntity
            .created(new URI("/api/options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /options/:id} : Updates an existing options.
     *
     * @param id the id of the options to save.
     * @param options the options to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated options,
     * or with status {@code 400 (Bad Request)} if the options is not valid,
     * or with status {@code 500 (Internal Server Error)} if the options couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/options/{id}")
    public ResponseEntity<Options> updateOptions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Options options
    ) throws URISyntaxException {
        log.debug("REST request to update Options : {}, {}", id, options);
        if (options.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, options.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Options result = optionsService.update(options);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, options.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /options/:id} : Partial updates given fields of an existing options, field will ignore if it is null
     *
     * @param id the id of the options to save.
     * @param options the options to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated options,
     * or with status {@code 400 (Bad Request)} if the options is not valid,
     * or with status {@code 404 (Not Found)} if the options is not found,
     * or with status {@code 500 (Internal Server Error)} if the options couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Options> partialUpdateOptions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Options options
    ) throws URISyntaxException {
        log.debug("REST request to partial update Options partially : {}, {}", id, options);
        if (options.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, options.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Options> result = optionsService.partialUpdate(options);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, options.getId().toString())
        );
    }

    /**
     * {@code GET  /options} : get all the options.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of options in body.
     */
    @GetMapping("/options")
    public ResponseEntity<List<Options>> getAllOptions(
        OptionsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Options by criteria: {}", criteria);
        Page<Options> page = optionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /options/count} : count all the options.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/options/count")
    public ResponseEntity<Long> countOptions(OptionsCriteria criteria) {
        log.debug("REST request to count Options by criteria: {}", criteria);
        return ResponseEntity.ok().body(optionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /options/:id} : get the "id" options.
     *
     * @param id the id of the options to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the options, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/options/{id}")
    public ResponseEntity<Options> getOptions(@PathVariable Long id) {
        log.debug("REST request to get Options : {}", id);
        Optional<Options> options = optionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(options);
    }

    /**
     * {@code DELETE  /options/:id} : delete the "id" options.
     *
     * @param id the id of the options to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/options/{id}")
    public ResponseEntity<Void> deleteOptions(@PathVariable Long id) {
        log.debug("REST request to delete Options : {}", id);
        optionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/createNewCustomOption")
    public ResponseEntity<CustomOptionDTO> createNewCustomOption(@RequestBody CustomOptionDTO customOptionDTO) throws URISyntaxException {
        log.debug("REST request to save CustomOptionDetails : {}", customOptionDTO);
        JwtResponse token = userJWTController.getJWTAuth();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String url = "http://demo.wikunum.lk/api/createNewCustomOption";
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token.getId_token());
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, customOptionDTO);
        if (200 == response.getStatusCodeValue()) {
            String x = "ss";
        } else {
            String x = "ss";
        }
        return ResponseEntity.ok().body(customOptionDTO);
    }
}
