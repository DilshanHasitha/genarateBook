package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayerDetailsRepository;
import com.mycompany.myapp.service.criteria.LayerDetailsCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LayerDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LayerDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/layer-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LayerDetailsRepository layerDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLayerDetailsMockMvc;

    private LayerDetails layerDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LayerDetails createEntity(EntityManager em) {
        LayerDetails layerDetails = new LayerDetails().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return layerDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LayerDetails createUpdatedEntity(EntityManager em) {
        LayerDetails layerDetails = new LayerDetails().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return layerDetails;
    }

    @BeforeEach
    public void initTest() {
        layerDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createLayerDetails() throws Exception {
        int databaseSizeBeforeCreate = layerDetailsRepository.findAll().size();
        // Create the LayerDetails
        restLayerDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerDetails)))
            .andExpect(status().isCreated());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        LayerDetails testLayerDetails = layerDetailsList.get(layerDetailsList.size() - 1);
        assertThat(testLayerDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLayerDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createLayerDetailsWithExistingId() throws Exception {
        // Create the LayerDetails with an existing ID
        layerDetails.setId(1L);

        int databaseSizeBeforeCreate = layerDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLayerDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerDetails)))
            .andExpect(status().isBadRequest());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = layerDetailsRepository.findAll().size();
        // set the field null
        layerDetails.setName(null);

        // Create the LayerDetails, which fails.

        restLayerDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerDetails)))
            .andExpect(status().isBadRequest());

        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLayerDetails() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList
        restLayerDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getLayerDetails() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get the layerDetails
        restLayerDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, layerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(layerDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getLayerDetailsByIdFiltering() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        Long id = layerDetails.getId();

        defaultLayerDetailsShouldBeFound("id.equals=" + id);
        defaultLayerDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultLayerDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLayerDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultLayerDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLayerDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where name equals to DEFAULT_NAME
        defaultLayerDetailsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the layerDetailsList where name equals to UPDATED_NAME
        defaultLayerDetailsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLayerDetailsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the layerDetailsList where name equals to UPDATED_NAME
        defaultLayerDetailsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where name is not null
        defaultLayerDetailsShouldBeFound("name.specified=true");

        // Get all the layerDetailsList where name is null
        defaultLayerDetailsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllLayerDetailsByNameContainsSomething() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where name contains DEFAULT_NAME
        defaultLayerDetailsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the layerDetailsList where name contains UPDATED_NAME
        defaultLayerDetailsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where name does not contain DEFAULT_NAME
        defaultLayerDetailsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the layerDetailsList where name does not contain UPDATED_NAME
        defaultLayerDetailsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultLayerDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the layerDetailsList where description equals to UPDATED_DESCRIPTION
        defaultLayerDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLayerDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the layerDetailsList where description equals to UPDATED_DESCRIPTION
        defaultLayerDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where description is not null
        defaultLayerDetailsShouldBeFound("description.specified=true");

        // Get all the layerDetailsList where description is null
        defaultLayerDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllLayerDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where description contains DEFAULT_DESCRIPTION
        defaultLayerDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the layerDetailsList where description contains UPDATED_DESCRIPTION
        defaultLayerDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        // Get all the layerDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultLayerDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the layerDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultLayerDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerDetailsByLayersIsEqualToSomething() throws Exception {
        Layers layers;
        if (TestUtil.findAll(em, Layers.class).isEmpty()) {
            layerDetailsRepository.saveAndFlush(layerDetails);
            layers = LayersResourceIT.createEntity(em);
        } else {
            layers = TestUtil.findAll(em, Layers.class).get(0);
        }
        em.persist(layers);
        em.flush();
        layerDetails.addLayers(layers);
        layerDetailsRepository.saveAndFlush(layerDetails);
        Long layersId = layers.getId();

        // Get all the layerDetailsList where layers equals to layersId
        defaultLayerDetailsShouldBeFound("layersId.equals=" + layersId);

        // Get all the layerDetailsList where layers equals to (layersId + 1)
        defaultLayerDetailsShouldNotBeFound("layersId.equals=" + (layersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLayerDetailsShouldBeFound(String filter) throws Exception {
        restLayerDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLayerDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLayerDetailsShouldNotBeFound(String filter) throws Exception {
        restLayerDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLayerDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLayerDetails() throws Exception {
        // Get the layerDetails
        restLayerDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLayerDetails() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();

        // Update the layerDetails
        LayerDetails updatedLayerDetails = layerDetailsRepository.findById(layerDetails.getId()).get();
        // Disconnect from session so that the updates on updatedLayerDetails are not directly saved in db
        em.detach(updatedLayerDetails);
        updatedLayerDetails.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restLayerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLayerDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLayerDetails))
            )
            .andExpect(status().isOk());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
        LayerDetails testLayerDetails = layerDetailsList.get(layerDetailsList.size() - 1);
        assertThat(testLayerDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLayerDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingLayerDetails() throws Exception {
        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();
        layerDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, layerDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLayerDetails() throws Exception {
        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();
        layerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLayerDetails() throws Exception {
        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();
        layerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLayerDetailsWithPatch() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();

        // Update the layerDetails using partial update
        LayerDetails partialUpdatedLayerDetails = new LayerDetails();
        partialUpdatedLayerDetails.setId(layerDetails.getId());

        partialUpdatedLayerDetails.name(UPDATED_NAME);

        restLayerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayerDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayerDetails))
            )
            .andExpect(status().isOk());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
        LayerDetails testLayerDetails = layerDetailsList.get(layerDetailsList.size() - 1);
        assertThat(testLayerDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLayerDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLayerDetailsWithPatch() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();

        // Update the layerDetails using partial update
        LayerDetails partialUpdatedLayerDetails = new LayerDetails();
        partialUpdatedLayerDetails.setId(layerDetails.getId());

        partialUpdatedLayerDetails.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restLayerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayerDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayerDetails))
            )
            .andExpect(status().isOk());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
        LayerDetails testLayerDetails = layerDetailsList.get(layerDetailsList.size() - 1);
        assertThat(testLayerDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLayerDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLayerDetails() throws Exception {
        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();
        layerDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, layerDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLayerDetails() throws Exception {
        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();
        layerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLayerDetails() throws Exception {
        int databaseSizeBeforeUpdate = layerDetailsRepository.findAll().size();
        layerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(layerDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LayerDetails in the database
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLayerDetails() throws Exception {
        // Initialize the database
        layerDetailsRepository.saveAndFlush(layerDetails);

        int databaseSizeBeforeDelete = layerDetailsRepository.findAll().size();

        // Delete the layerDetails
        restLayerDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, layerDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LayerDetails> layerDetailsList = layerDetailsRepository.findAll();
        assertThat(layerDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
