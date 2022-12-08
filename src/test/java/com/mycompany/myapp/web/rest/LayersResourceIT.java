package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LayerDetails;
import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayersRepository;
import com.mycompany.myapp.service.LayersService;
import com.mycompany.myapp.service.criteria.LayersCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LayersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LayersResourceIT {

    private static final Integer DEFAULT_LAYER_NO = 1;
    private static final Integer UPDATED_LAYER_NO = 2;
    private static final Integer SMALLER_LAYER_NO = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/layers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LayersRepository layersRepository;

    @Mock
    private LayersRepository layersRepositoryMock;

    @Mock
    private LayersService layersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLayersMockMvc;

    private Layers layers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layers createEntity(EntityManager em) {
        Layers layers = new Layers().layerNo(DEFAULT_LAYER_NO).isActive(DEFAULT_IS_ACTIVE);
        return layers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layers createUpdatedEntity(EntityManager em) {
        Layers layers = new Layers().layerNo(UPDATED_LAYER_NO).isActive(UPDATED_IS_ACTIVE);
        return layers;
    }

    @BeforeEach
    public void initTest() {
        layers = createEntity(em);
    }

    @Test
    @Transactional
    void createLayers() throws Exception {
        int databaseSizeBeforeCreate = layersRepository.findAll().size();
        // Create the Layers
        restLayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layers)))
            .andExpect(status().isCreated());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeCreate + 1);
        Layers testLayers = layersList.get(layersList.size() - 1);
        assertThat(testLayers.getLayerNo()).isEqualTo(DEFAULT_LAYER_NO);
        assertThat(testLayers.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createLayersWithExistingId() throws Exception {
        // Create the Layers with an existing ID
        layers.setId(1L);

        int databaseSizeBeforeCreate = layersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layers)))
            .andExpect(status().isBadRequest());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLayerNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = layersRepository.findAll().size();
        // set the field null
        layers.setLayerNo(null);

        // Create the Layers, which fails.

        restLayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layers)))
            .andExpect(status().isBadRequest());

        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLayers() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList
        restLayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layers.getId().intValue())))
            .andExpect(jsonPath("$.[*].layerNo").value(hasItem(DEFAULT_LAYER_NO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLayersWithEagerRelationshipsIsEnabled() throws Exception {
        when(layersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLayersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(layersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLayersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(layersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLayersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(layersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLayers() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get the layers
        restLayersMockMvc
            .perform(get(ENTITY_API_URL_ID, layers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(layers.getId().intValue()))
            .andExpect(jsonPath("$.layerNo").value(DEFAULT_LAYER_NO))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getLayersByIdFiltering() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        Long id = layers.getId();

        defaultLayersShouldBeFound("id.equals=" + id);
        defaultLayersShouldNotBeFound("id.notEquals=" + id);

        defaultLayersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLayersShouldNotBeFound("id.greaterThan=" + id);

        defaultLayersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLayersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsEqualToSomething() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo equals to DEFAULT_LAYER_NO
        defaultLayersShouldBeFound("layerNo.equals=" + DEFAULT_LAYER_NO);

        // Get all the layersList where layerNo equals to UPDATED_LAYER_NO
        defaultLayersShouldNotBeFound("layerNo.equals=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsInShouldWork() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo in DEFAULT_LAYER_NO or UPDATED_LAYER_NO
        defaultLayersShouldBeFound("layerNo.in=" + DEFAULT_LAYER_NO + "," + UPDATED_LAYER_NO);

        // Get all the layersList where layerNo equals to UPDATED_LAYER_NO
        defaultLayersShouldNotBeFound("layerNo.in=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo is not null
        defaultLayersShouldBeFound("layerNo.specified=true");

        // Get all the layersList where layerNo is null
        defaultLayersShouldNotBeFound("layerNo.specified=false");
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo is greater than or equal to DEFAULT_LAYER_NO
        defaultLayersShouldBeFound("layerNo.greaterThanOrEqual=" + DEFAULT_LAYER_NO);

        // Get all the layersList where layerNo is greater than or equal to UPDATED_LAYER_NO
        defaultLayersShouldNotBeFound("layerNo.greaterThanOrEqual=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo is less than or equal to DEFAULT_LAYER_NO
        defaultLayersShouldBeFound("layerNo.lessThanOrEqual=" + DEFAULT_LAYER_NO);

        // Get all the layersList where layerNo is less than or equal to SMALLER_LAYER_NO
        defaultLayersShouldNotBeFound("layerNo.lessThanOrEqual=" + SMALLER_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsLessThanSomething() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo is less than DEFAULT_LAYER_NO
        defaultLayersShouldNotBeFound("layerNo.lessThan=" + DEFAULT_LAYER_NO);

        // Get all the layersList where layerNo is less than UPDATED_LAYER_NO
        defaultLayersShouldBeFound("layerNo.lessThan=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllLayersByLayerNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where layerNo is greater than DEFAULT_LAYER_NO
        defaultLayersShouldNotBeFound("layerNo.greaterThan=" + DEFAULT_LAYER_NO);

        // Get all the layersList where layerNo is greater than SMALLER_LAYER_NO
        defaultLayersShouldBeFound("layerNo.greaterThan=" + SMALLER_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllLayersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLayersShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the layersList where isActive equals to UPDATED_IS_ACTIVE
        defaultLayersShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLayersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLayersShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the layersList where isActive equals to UPDATED_IS_ACTIVE
        defaultLayersShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLayersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        // Get all the layersList where isActive is not null
        defaultLayersShouldBeFound("isActive.specified=true");

        // Get all the layersList where isActive is null
        defaultLayersShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllLayersByLayerdetailsIsEqualToSomething() throws Exception {
        LayerDetails layerdetails;
        if (TestUtil.findAll(em, LayerDetails.class).isEmpty()) {
            layersRepository.saveAndFlush(layers);
            layerdetails = LayerDetailsResourceIT.createEntity(em);
        } else {
            layerdetails = TestUtil.findAll(em, LayerDetails.class).get(0);
        }
        em.persist(layerdetails);
        em.flush();
        layers.addLayerdetails(layerdetails);
        layersRepository.saveAndFlush(layers);
        Long layerdetailsId = layerdetails.getId();

        // Get all the layersList where layerdetails equals to layerdetailsId
        defaultLayersShouldBeFound("layerdetailsId.equals=" + layerdetailsId);

        // Get all the layersList where layerdetails equals to (layerdetailsId + 1)
        defaultLayersShouldNotBeFound("layerdetailsId.equals=" + (layerdetailsId + 1));
    }

    @Test
    @Transactional
    void getAllLayersByLayerGroupIsEqualToSomething() throws Exception {
        LayerGroup layerGroup;
        if (TestUtil.findAll(em, LayerGroup.class).isEmpty()) {
            layersRepository.saveAndFlush(layers);
            layerGroup = LayerGroupResourceIT.createEntity(em);
        } else {
            layerGroup = TestUtil.findAll(em, LayerGroup.class).get(0);
        }
        em.persist(layerGroup);
        em.flush();
        layers.addLayerGroup(layerGroup);
        layersRepository.saveAndFlush(layers);
        Long layerGroupId = layerGroup.getId();

        // Get all the layersList where layerGroup equals to layerGroupId
        defaultLayersShouldBeFound("layerGroupId.equals=" + layerGroupId);

        // Get all the layersList where layerGroup equals to (layerGroupId + 1)
        defaultLayersShouldNotBeFound("layerGroupId.equals=" + (layerGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLayersShouldBeFound(String filter) throws Exception {
        restLayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layers.getId().intValue())))
            .andExpect(jsonPath("$.[*].layerNo").value(hasItem(DEFAULT_LAYER_NO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restLayersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLayersShouldNotBeFound(String filter) throws Exception {
        restLayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLayersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLayers() throws Exception {
        // Get the layers
        restLayersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLayers() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        int databaseSizeBeforeUpdate = layersRepository.findAll().size();

        // Update the layers
        Layers updatedLayers = layersRepository.findById(layers.getId()).get();
        // Disconnect from session so that the updates on updatedLayers are not directly saved in db
        em.detach(updatedLayers);
        updatedLayers.layerNo(UPDATED_LAYER_NO).isActive(UPDATED_IS_ACTIVE);

        restLayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLayers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLayers))
            )
            .andExpect(status().isOk());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
        Layers testLayers = layersList.get(layersList.size() - 1);
        assertThat(testLayers.getLayerNo()).isEqualTo(UPDATED_LAYER_NO);
        assertThat(testLayers.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingLayers() throws Exception {
        int databaseSizeBeforeUpdate = layersRepository.findAll().size();
        layers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, layers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLayers() throws Exception {
        int databaseSizeBeforeUpdate = layersRepository.findAll().size();
        layers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLayers() throws Exception {
        int databaseSizeBeforeUpdate = layersRepository.findAll().size();
        layers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLayersWithPatch() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        int databaseSizeBeforeUpdate = layersRepository.findAll().size();

        // Update the layers using partial update
        Layers partialUpdatedLayers = new Layers();
        partialUpdatedLayers.setId(layers.getId());

        partialUpdatedLayers.layerNo(UPDATED_LAYER_NO).isActive(UPDATED_IS_ACTIVE);

        restLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayers))
            )
            .andExpect(status().isOk());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
        Layers testLayers = layersList.get(layersList.size() - 1);
        assertThat(testLayers.getLayerNo()).isEqualTo(UPDATED_LAYER_NO);
        assertThat(testLayers.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateLayersWithPatch() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        int databaseSizeBeforeUpdate = layersRepository.findAll().size();

        // Update the layers using partial update
        Layers partialUpdatedLayers = new Layers();
        partialUpdatedLayers.setId(layers.getId());

        partialUpdatedLayers.layerNo(UPDATED_LAYER_NO).isActive(UPDATED_IS_ACTIVE);

        restLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayers))
            )
            .andExpect(status().isOk());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
        Layers testLayers = layersList.get(layersList.size() - 1);
        assertThat(testLayers.getLayerNo()).isEqualTo(UPDATED_LAYER_NO);
        assertThat(testLayers.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingLayers() throws Exception {
        int databaseSizeBeforeUpdate = layersRepository.findAll().size();
        layers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, layers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLayers() throws Exception {
        int databaseSizeBeforeUpdate = layersRepository.findAll().size();
        layers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLayers() throws Exception {
        int databaseSizeBeforeUpdate = layersRepository.findAll().size();
        layers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(layers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Layers in the database
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLayers() throws Exception {
        // Initialize the database
        layersRepository.saveAndFlush(layers);

        int databaseSizeBeforeDelete = layersRepository.findAll().size();

        // Delete the layers
        restLayersMockMvc
            .perform(delete(ENTITY_API_URL_ID, layers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Layers> layersList = layersRepository.findAll();
        assertThat(layersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
