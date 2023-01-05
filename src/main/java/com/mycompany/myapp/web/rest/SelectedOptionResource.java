package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.SelectedOptionRepository;
import com.mycompany.myapp.security.CommonUtils;
import com.mycompany.myapp.service.BooksService;
import com.mycompany.myapp.service.SelectedOptionDetailsService;
import com.mycompany.myapp.service.SelectedOptionQueryService;
import com.mycompany.myapp.service.SelectedOptionService;
import com.mycompany.myapp.service.criteria.SelectedOptionCriteria;
import com.mycompany.myapp.service.dto.SelectedOptionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SelectedOption}.
 */
@RestController
@RequestMapping("/api")
public class SelectedOptionResource {

    private final Logger log = LoggerFactory.getLogger(SelectedOptionResource.class);

    private static final String ENTITY_NAME = "selectedOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SelectedOptionService selectedOptionService;

    private final SelectedOptionRepository selectedOptionRepository;

    private final SelectedOptionQueryService selectedOptionQueryService;
    private final BooksService booksService;
    private final SelectedOptionDetailsService selectedOptionDetailsService;

    public SelectedOptionResource(
        SelectedOptionService selectedOptionService,
        SelectedOptionRepository selectedOptionRepository,
        SelectedOptionQueryService selectedOptionQueryService,
        BooksService booksService,
        SelectedOptionDetailsService selectedOptionDetailsService
    ) {
        this.selectedOptionService = selectedOptionService;
        this.selectedOptionRepository = selectedOptionRepository;
        this.selectedOptionQueryService = selectedOptionQueryService;
        this.booksService = booksService;
        this.selectedOptionDetailsService = selectedOptionDetailsService;
    }

    /**
     * {@code POST  /selected-options} : Create a new selectedOption.
     *
     * @param selectedOption the selectedOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new selectedOption, or with status {@code 400 (Bad Request)} if the selectedOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/selected-options")
    public ResponseEntity<SelectedOption> createSelectedOption(@RequestBody SelectedOption selectedOption) throws URISyntaxException {
        log.debug("REST request to save SelectedOption : {}", selectedOption);
        if (selectedOption.getId() != null) {
            throw new BadRequestAlertException("A new selectedOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SelectedOption result = selectedOptionService.save(selectedOption);
        return ResponseEntity
            .created(new URI("/api/selected-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /selected-options/:id} : Updates an existing selectedOption.
     *
     * @param id the id of the selectedOption to save.
     * @param selectedOption the selectedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selectedOption,
     * or with status {@code 400 (Bad Request)} if the selectedOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the selectedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/selected-options/{id}")
    public ResponseEntity<SelectedOption> updateSelectedOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SelectedOption selectedOption
    ) throws URISyntaxException {
        log.debug("REST request to update SelectedOption : {}, {}", id, selectedOption);
        if (selectedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selectedOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectedOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SelectedOption result = selectedOptionService.update(selectedOption);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selectedOption.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /selected-options/:id} : Partial updates given fields of an existing selectedOption, field will ignore if it is null
     *
     * @param id the id of the selectedOption to save.
     * @param selectedOption the selectedOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated selectedOption,
     * or with status {@code 400 (Bad Request)} if the selectedOption is not valid,
     * or with status {@code 404 (Not Found)} if the selectedOption is not found,
     * or with status {@code 500 (Internal Server Error)} if the selectedOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/selected-options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SelectedOption> partialUpdateSelectedOption(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SelectedOption selectedOption
    ) throws URISyntaxException {
        log.debug("REST request to partial update SelectedOption partially : {}, {}", id, selectedOption);
        if (selectedOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, selectedOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!selectedOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SelectedOption> result = selectedOptionService.partialUpdate(selectedOption);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, selectedOption.getId().toString())
        );
    }

    /**
     * {@code GET  /selected-options} : get all the selectedOptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of selectedOptions in body.
     */
    @GetMapping("/selected-options")
    public ResponseEntity<List<SelectedOption>> getAllSelectedOptions(
        SelectedOptionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SelectedOptions by criteria: {}", criteria);
        Page<SelectedOption> page = selectedOptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /selected-options/count} : count all the selectedOptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/selected-options/count")
    public ResponseEntity<Long> countSelectedOptions(SelectedOptionCriteria criteria) {
        log.debug("REST request to count SelectedOptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(selectedOptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /selected-options/:id} : get the "id" selectedOption.
     *
     * @param id the id of the selectedOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the selectedOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/selected-options/{id}")
    public ResponseEntity<SelectedOption> getSelectedOption(@PathVariable Long id) {
        log.debug("REST request to get SelectedOption : {}", id);
        Optional<SelectedOption> selectedOption = selectedOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(selectedOption);
    }

    /**
     * {@code DELETE  /selected-options/:id} : delete the "id" selectedOption.
     *
     * @param id the id of the selectedOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/selected-options/{id}")
    public ResponseEntity<Void> deleteSelectedOption(@PathVariable Long id) {
        log.debug("REST request to delete SelectedOption : {}", id);
        selectedOptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/addSelectedOptions")
    public SelectedOption addSelectedOptions(@Valid @RequestBody SelectedOptionDTO selectedOptionDTO) throws URISyntaxException {
        Optional<Books> books = booksService.findOneByCode(selectedOptionDTO.getBookCode());
        if (!books.isPresent()) {
            throw new BadRequestAlertException("Invalid book", ENTITY_NAME, "idexists");
        }
        Set<AvatarAttributes> avatarAttributes = books.get().getAvatarAttributes();
        for (SelectedOptionDetails selectedOptionDetails : selectedOptionDTO.getSelectedOptionDetails()) {
            String AvatarAttributesCode = selectedOptionDetails.getCode();
            for (AvatarAttributes avatarAttribute : avatarAttributes) {
                if (avatarAttribute.getCode().equals(AvatarAttributesCode)) {
                    if (avatarAttribute.getTemplateText() != null) {
                        selectedOptionDetails.setName(avatarAttribute.getTemplateText());
                    } else {
                        selectedOptionDetails.setName(selectedOptionDetails.getCode());
                        selectedOptionDetails.setCode(avatarAttribute.getAvatarCharactors().iterator().next().getCharacter().getCode());
                    }
                }
            }
            selectedOptionDetailsService.save(selectedOptionDetails);
        }
        SelectedOption selectedOption = new SelectedOption();
        String code = CommonUtils.generateCode(selectedOptionDTO.getCode());
        selectedOption.setCode(code);
        selectedOption.selectedOptionDetails(selectedOptionDTO.getSelectedOptionDetails());
        selectedOption.setBooks(books.get());

        return selectedOptionService.save(selectedOption);
    }
}
