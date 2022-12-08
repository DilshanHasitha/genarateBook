package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.domain.Layers;
import com.mycompany.myapp.repository.LayerGroupRepository;
import com.mycompany.myapp.service.LayerGroupService;
import com.mycompany.myapp.service.criteria.LayerGroupCriteria;
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
 * Integration tests for the {@link LayerGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LayerGroupResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/layer-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LayerGroupRepository layerGroupRepository;

    @Mock
    private LayerGroupRepository layerGroupRepositoryMock;

    @Mock
    private LayerGroupService layerGroupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLayerGroupMockMvc;

    private LayerGroup layerGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LayerGroup createEntity(EntityManager em) {
        LayerGroup layerGroup = new LayerGroup().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).isActive(DEFAULT_IS_ACTIVE);
        return layerGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LayerGroup createUpdatedEntity(EntityManager em) {
        LayerGroup layerGroup = new LayerGroup().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);
        return layerGroup;
    }

    @BeforeEach
    public void initTest() {
        layerGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createLayerGroup() throws Exception {
        int databaseSizeBeforeCreate = layerGroupRepository.findAll().size();
        // Create the LayerGroup
        restLayerGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerGroup)))
            .andExpect(status().isCreated());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeCreate + 1);
        LayerGroup testLayerGroup = layerGroupList.get(layerGroupList.size() - 1);
        assertThat(testLayerGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLayerGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLayerGroup.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createLayerGroupWithExistingId() throws Exception {
        // Create the LayerGroup with an existing ID
        layerGroup.setId(1L);

        int databaseSizeBeforeCreate = layerGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLayerGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerGroup)))
            .andExpect(status().isBadRequest());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = layerGroupRepository.findAll().size();
        // set the field null
        layerGroup.setDescription(null);

        // Create the LayerGroup, which fails.

        restLayerGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerGroup)))
            .andExpect(status().isBadRequest());

        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLayerGroups() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList
        restLayerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLayerGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(layerGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLayerGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(layerGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLayerGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(layerGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLayerGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(layerGroupRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLayerGroup() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get the layerGroup
        restLayerGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, layerGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(layerGroup.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getLayerGroupsByIdFiltering() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        Long id = layerGroup.getId();

        defaultLayerGroupShouldBeFound("id.equals=" + id);
        defaultLayerGroupShouldNotBeFound("id.notEquals=" + id);

        defaultLayerGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLayerGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultLayerGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLayerGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where code equals to DEFAULT_CODE
        defaultLayerGroupShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the layerGroupList where code equals to UPDATED_CODE
        defaultLayerGroupShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where code in DEFAULT_CODE or UPDATED_CODE
        defaultLayerGroupShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the layerGroupList where code equals to UPDATED_CODE
        defaultLayerGroupShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where code is not null
        defaultLayerGroupShouldBeFound("code.specified=true");

        // Get all the layerGroupList where code is null
        defaultLayerGroupShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllLayerGroupsByCodeContainsSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where code contains DEFAULT_CODE
        defaultLayerGroupShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the layerGroupList where code contains UPDATED_CODE
        defaultLayerGroupShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where code does not contain DEFAULT_CODE
        defaultLayerGroupShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the layerGroupList where code does not contain UPDATED_CODE
        defaultLayerGroupShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where description equals to DEFAULT_DESCRIPTION
        defaultLayerGroupShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the layerGroupList where description equals to UPDATED_DESCRIPTION
        defaultLayerGroupShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLayerGroupShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the layerGroupList where description equals to UPDATED_DESCRIPTION
        defaultLayerGroupShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where description is not null
        defaultLayerGroupShouldBeFound("description.specified=true");

        // Get all the layerGroupList where description is null
        defaultLayerGroupShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllLayerGroupsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where description contains DEFAULT_DESCRIPTION
        defaultLayerGroupShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the layerGroupList where description contains UPDATED_DESCRIPTION
        defaultLayerGroupShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where description does not contain DEFAULT_DESCRIPTION
        defaultLayerGroupShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the layerGroupList where description does not contain UPDATED_DESCRIPTION
        defaultLayerGroupShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLayerGroupShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the layerGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultLayerGroupShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLayerGroupShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the layerGroupList where isActive equals to UPDATED_IS_ACTIVE
        defaultLayerGroupShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLayerGroupsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        // Get all the layerGroupList where isActive is not null
        defaultLayerGroupShouldBeFound("isActive.specified=true");

        // Get all the layerGroupList where isActive is null
        defaultLayerGroupShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllLayerGroupsByLayersIsEqualToSomething() throws Exception {
        Layers layers;
        if (TestUtil.findAll(em, Layers.class).isEmpty()) {
            layerGroupRepository.saveAndFlush(layerGroup);
            layers = LayersResourceIT.createEntity(em);
        } else {
            layers = TestUtil.findAll(em, Layers.class).get(0);
        }
        em.persist(layers);
        em.flush();
        layerGroup.addLayers(layers);
        layerGroupRepository.saveAndFlush(layerGroup);
        Long layersId = layers.getId();

        // Get all the layerGroupList where layers equals to layersId
        defaultLayerGroupShouldBeFound("layersId.equals=" + layersId);

        // Get all the layerGroupList where layers equals to (layersId + 1)
        defaultLayerGroupShouldNotBeFound("layersId.equals=" + (layersId + 1));
    }

    @Test
    @Transactional
    void getAllLayerGroupsByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            layerGroupRepository.saveAndFlush(layerGroup);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        layerGroup.addBooks(books);
        layerGroupRepository.saveAndFlush(layerGroup);
        Long booksId = books.getId();

        // Get all the layerGroupList where books equals to booksId
        defaultLayerGroupShouldBeFound("booksId.equals=" + booksId);

        // Get all the layerGroupList where books equals to (booksId + 1)
        defaultLayerGroupShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLayerGroupShouldBeFound(String filter) throws Exception {
        restLayerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restLayerGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLayerGroupShouldNotBeFound(String filter) throws Exception {
        restLayerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLayerGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLayerGroup() throws Exception {
        // Get the layerGroup
        restLayerGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLayerGroup() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();

        // Update the layerGroup
        LayerGroup updatedLayerGroup = layerGroupRepository.findById(layerGroup.getId()).get();
        // Disconnect from session so that the updates on updatedLayerGroup are not directly saved in db
        em.detach(updatedLayerGroup);
        updatedLayerGroup.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restLayerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLayerGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLayerGroup))
            )
            .andExpect(status().isOk());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
        LayerGroup testLayerGroup = layerGroupList.get(layerGroupList.size() - 1);
        assertThat(testLayerGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLayerGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLayerGroup.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingLayerGroup() throws Exception {
        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();
        layerGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, layerGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layerGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLayerGroup() throws Exception {
        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();
        layerGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layerGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLayerGroup() throws Exception {
        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();
        layerGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layerGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLayerGroupWithPatch() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();

        // Update the layerGroup using partial update
        LayerGroup partialUpdatedLayerGroup = new LayerGroup();
        partialUpdatedLayerGroup.setId(layerGroup.getId());

        partialUpdatedLayerGroup.isActive(UPDATED_IS_ACTIVE);

        restLayerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayerGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayerGroup))
            )
            .andExpect(status().isOk());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
        LayerGroup testLayerGroup = layerGroupList.get(layerGroupList.size() - 1);
        assertThat(testLayerGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLayerGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLayerGroup.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateLayerGroupWithPatch() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();

        // Update the layerGroup using partial update
        LayerGroup partialUpdatedLayerGroup = new LayerGroup();
        partialUpdatedLayerGroup.setId(layerGroup.getId());

        partialUpdatedLayerGroup.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restLayerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayerGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayerGroup))
            )
            .andExpect(status().isOk());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
        LayerGroup testLayerGroup = layerGroupList.get(layerGroupList.size() - 1);
        assertThat(testLayerGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLayerGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLayerGroup.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingLayerGroup() throws Exception {
        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();
        layerGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, layerGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layerGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLayerGroup() throws Exception {
        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();
        layerGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layerGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLayerGroup() throws Exception {
        int databaseSizeBeforeUpdate = layerGroupRepository.findAll().size();
        layerGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(layerGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LayerGroup in the database
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLayerGroup() throws Exception {
        // Initialize the database
        layerGroupRepository.saveAndFlush(layerGroup);

        int databaseSizeBeforeDelete = layerGroupRepository.findAll().size();

        // Delete the layerGroup
        restLayerGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, layerGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LayerGroup> layerGroupList = layerGroupRepository.findAll();
        assertThat(layerGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
