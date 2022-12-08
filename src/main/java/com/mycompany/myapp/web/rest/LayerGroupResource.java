package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayerGroupRepository;
import com.mycompany.myapp.service.LayerGroupQueryService;
import com.mycompany.myapp.service.LayerGroupService;
import com.mycompany.myapp.service.PDFGenarator;
import com.mycompany.myapp.service.criteria.LayerGroupCriteria;
import com.mycompany.myapp.service.dto.RequestTransDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.sf.jasperreports.engine.JRException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LayerGroup}.
 */
@RestController
@RequestMapping("/api")
public class LayerGroupResource {

    private final Logger log = LoggerFactory.getLogger(LayerGroupResource.class);

    private static final String ENTITY_NAME = "layerGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LayerGroupService layerGroupService;

    private final LayerGroupRepository layerGroupRepository;

    private final LayerGroupQueryService layerGroupQueryService;

    private final PDFGenarator pdfGenarator;

    public LayerGroupResource(
        LayerGroupService layerGroupService,
        LayerGroupRepository layerGroupRepository,
        LayerGroupQueryService layerGroupQueryService,
        PDFGenarator pdfGenarator
    ) {
        this.layerGroupService = layerGroupService;
        this.layerGroupRepository = layerGroupRepository;
        this.layerGroupQueryService = layerGroupQueryService;
        this.pdfGenarator = pdfGenarator;
    }

    /**
     * {@code POST  /layer-groups} : Create a new layerGroup.
     *
     * @param layerGroup the layerGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new layerGroup, or with status {@code 400 (Bad Request)} if the layerGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/layer-groups")
    public ResponseEntity<LayerGroup> createLayerGroup(@Valid @RequestBody LayerGroup layerGroup) throws URISyntaxException {
        log.debug("REST request to save LayerGroup : {}", layerGroup);
        if (layerGroup.getId() != null) {
            throw new BadRequestAlertException("A new layerGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LayerGroup result = layerGroupService.save(layerGroup);
        return ResponseEntity
            .created(new URI("/api/layer-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /layer-groups/:id} : Updates an existing layerGroup.
     *
     * @param id the id of the layerGroup to save.
     * @param layerGroup the layerGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layerGroup,
     * or with status {@code 400 (Bad Request)} if the layerGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the layerGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/layer-groups/{id}")
    public ResponseEntity<LayerGroup> updateLayerGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LayerGroup layerGroup
    ) throws URISyntaxException {
        log.debug("REST request to update LayerGroup : {}, {}", id, layerGroup);
        if (layerGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layerGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layerGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LayerGroup result = layerGroupService.update(layerGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layerGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /layer-groups/:id} : Partial updates given fields of an existing layerGroup, field will ignore if it is null
     *
     * @param id the id of the layerGroup to save.
     * @param layerGroup the layerGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layerGroup,
     * or with status {@code 400 (Bad Request)} if the layerGroup is not valid,
     * or with status {@code 404 (Not Found)} if the layerGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the layerGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/layer-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LayerGroup> partialUpdateLayerGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LayerGroup layerGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update LayerGroup partially : {}, {}", id, layerGroup);
        if (layerGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layerGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layerGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LayerGroup> result = layerGroupService.partialUpdate(layerGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, layerGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /layer-groups} : get all the layerGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of layerGroups in body.
     */
    @GetMapping("/layer-groups")
    public ResponseEntity<List<LayerGroup>> getAllLayerGroups(
        LayerGroupCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LayerGroups by criteria: {}", criteria);
        Page<LayerGroup> page = layerGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /layer-groups/count} : count all the layerGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/layer-groups/count")
    public ResponseEntity<Long> countLayerGroups(LayerGroupCriteria criteria) {
        log.debug("REST request to count LayerGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(layerGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /layer-groups/:id} : get the "id" layerGroup.
     *
     * @param id the id of the layerGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the layerGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/layer-groups/{id}")
    public ResponseEntity<LayerGroup> getLayerGroup(@PathVariable Long id) {
        log.debug("REST request to get LayerGroup : {}", id);
        Optional<LayerGroup> layerGroup = layerGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(layerGroup);
    }

    /**
     * {@code DELETE  /layer-groups/:id} : delete the "id" layerGroup.
     *
     * @param id the id of the layerGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/layer-groups/{id}")
    public ResponseEntity<Void> deleteLayerGroup(@PathVariable Long id) {
        log.debug("REST request to delete LayerGroup : {}", id);
        layerGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    //    @GetMapping("/printReceipts")
    //    public byte[] receipt(String orderId, String storeCode) throws IOException, JRException {
    //        LayerGroup layerGroup = layerGroupRepository.findOneByCode("TEST");
    //
    ////Sort the array in ascending order
    //        List<Layers> layers = new ArrayList<>(layerGroup.getLayers());
    //        Collections.sort(layers, new Comparator<Layers>(){
    //            public int compare(Layers o1, Layers o2)
    //            {
    //                return o1.getLayerNo().compareTo(o2.getLayerNo());
    //            }
    //        });
    //        Set<Layers> targetSet = new HashSet<>(layers);
    //        layerGroup.setLayers(targetSet);
    //
    //        byte[] receipt = pdfGenarator.pdfCreator(layerGroup);
    //        return receipt;
    //    }
}
