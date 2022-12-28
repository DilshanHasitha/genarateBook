package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ImageStoreType;
import com.mycompany.myapp.repository.ImageStoreTypeRepository;
import com.mycompany.myapp.service.criteria.ImageStoreTypeCriteria;
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
 * Integration tests for the {@link ImageStoreTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageStoreTypeResourceIT {

    private static final String DEFAULT_IMAGE_STORE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_STORE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_STORE_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/image-store-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageStoreTypeRepository imageStoreTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageStoreTypeMockMvc;

    private ImageStoreType imageStoreType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageStoreType createEntity(EntityManager em) {
        ImageStoreType imageStoreType = new ImageStoreType()
            .imageStoreTypeCode(DEFAULT_IMAGE_STORE_TYPE_CODE)
            .imageStoreTypeDescription(DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return imageStoreType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageStoreType createUpdatedEntity(EntityManager em) {
        ImageStoreType imageStoreType = new ImageStoreType()
            .imageStoreTypeCode(UPDATED_IMAGE_STORE_TYPE_CODE)
            .imageStoreTypeDescription(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return imageStoreType;
    }

    @BeforeEach
    public void initTest() {
        imageStoreType = createEntity(em);
    }

    @Test
    @Transactional
    void createImageStoreType() throws Exception {
        int databaseSizeBeforeCreate = imageStoreTypeRepository.findAll().size();
        // Create the ImageStoreType
        restImageStoreTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isCreated());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ImageStoreType testImageStoreType = imageStoreTypeList.get(imageStoreTypeList.size() - 1);
        assertThat(testImageStoreType.getImageStoreTypeCode()).isEqualTo(DEFAULT_IMAGE_STORE_TYPE_CODE);
        assertThat(testImageStoreType.getImageStoreTypeDescription()).isEqualTo(DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION);
        assertThat(testImageStoreType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createImageStoreTypeWithExistingId() throws Exception {
        // Create the ImageStoreType with an existing ID
        imageStoreType.setId(1L);

        int databaseSizeBeforeCreate = imageStoreTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageStoreTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImageStoreTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageStoreTypeRepository.findAll().size();
        // set the field null
        imageStoreType.setImageStoreTypeCode(null);

        // Create the ImageStoreType, which fails.

        restImageStoreTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageStoreTypeDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageStoreTypeRepository.findAll().size();
        // set the field null
        imageStoreType.setImageStoreTypeDescription(null);

        // Create the ImageStoreType, which fails.

        restImageStoreTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImageStoreTypes() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList
        restImageStoreTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageStoreType.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageStoreTypeCode").value(hasItem(DEFAULT_IMAGE_STORE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].imageStoreTypeDescription").value(hasItem(DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getImageStoreType() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get the imageStoreType
        restImageStoreTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, imageStoreType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageStoreType.getId().intValue()))
            .andExpect(jsonPath("$.imageStoreTypeCode").value(DEFAULT_IMAGE_STORE_TYPE_CODE))
            .andExpect(jsonPath("$.imageStoreTypeDescription").value(DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getImageStoreTypesByIdFiltering() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        Long id = imageStoreType.getId();

        defaultImageStoreTypeShouldBeFound("id.equals=" + id);
        defaultImageStoreTypeShouldNotBeFound("id.notEquals=" + id);

        defaultImageStoreTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImageStoreTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultImageStoreTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImageStoreTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeCode equals to DEFAULT_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldBeFound("imageStoreTypeCode.equals=" + DEFAULT_IMAGE_STORE_TYPE_CODE);

        // Get all the imageStoreTypeList where imageStoreTypeCode equals to UPDATED_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeCode.equals=" + UPDATED_IMAGE_STORE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeCode in DEFAULT_IMAGE_STORE_TYPE_CODE or UPDATED_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldBeFound("imageStoreTypeCode.in=" + DEFAULT_IMAGE_STORE_TYPE_CODE + "," + UPDATED_IMAGE_STORE_TYPE_CODE);

        // Get all the imageStoreTypeList where imageStoreTypeCode equals to UPDATED_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeCode.in=" + UPDATED_IMAGE_STORE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeCode is not null
        defaultImageStoreTypeShouldBeFound("imageStoreTypeCode.specified=true");

        // Get all the imageStoreTypeList where imageStoreTypeCode is null
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeCode contains DEFAULT_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldBeFound("imageStoreTypeCode.contains=" + DEFAULT_IMAGE_STORE_TYPE_CODE);

        // Get all the imageStoreTypeList where imageStoreTypeCode contains UPDATED_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeCode.contains=" + UPDATED_IMAGE_STORE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeCode does not contain DEFAULT_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeCode.doesNotContain=" + DEFAULT_IMAGE_STORE_TYPE_CODE);

        // Get all the imageStoreTypeList where imageStoreTypeCode does not contain UPDATED_IMAGE_STORE_TYPE_CODE
        defaultImageStoreTypeShouldBeFound("imageStoreTypeCode.doesNotContain=" + UPDATED_IMAGE_STORE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeDescription equals to DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldBeFound("imageStoreTypeDescription.equals=" + DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION);

        // Get all the imageStoreTypeList where imageStoreTypeDescription equals to UPDATED_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeDescription.equals=" + UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeDescription in DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION or UPDATED_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldBeFound(
            "imageStoreTypeDescription.in=" + DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION + "," + UPDATED_IMAGE_STORE_TYPE_DESCRIPTION
        );

        // Get all the imageStoreTypeList where imageStoreTypeDescription equals to UPDATED_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeDescription.in=" + UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeDescription is not null
        defaultImageStoreTypeShouldBeFound("imageStoreTypeDescription.specified=true");

        // Get all the imageStoreTypeList where imageStoreTypeDescription is null
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeDescription contains DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldBeFound("imageStoreTypeDescription.contains=" + DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION);

        // Get all the imageStoreTypeList where imageStoreTypeDescription contains UPDATED_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeDescription.contains=" + UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByImageStoreTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where imageStoreTypeDescription does not contain DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldNotBeFound("imageStoreTypeDescription.doesNotContain=" + DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION);

        // Get all the imageStoreTypeList where imageStoreTypeDescription does not contain UPDATED_IMAGE_STORE_TYPE_DESCRIPTION
        defaultImageStoreTypeShouldBeFound("imageStoreTypeDescription.doesNotContain=" + UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultImageStoreTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the imageStoreTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultImageStoreTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultImageStoreTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the imageStoreTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultImageStoreTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllImageStoreTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        // Get all the imageStoreTypeList where isActive is not null
        defaultImageStoreTypeShouldBeFound("isActive.specified=true");

        // Get all the imageStoreTypeList where isActive is null
        defaultImageStoreTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageStoreTypeShouldBeFound(String filter) throws Exception {
        restImageStoreTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageStoreType.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageStoreTypeCode").value(hasItem(DEFAULT_IMAGE_STORE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].imageStoreTypeDescription").value(hasItem(DEFAULT_IMAGE_STORE_TYPE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restImageStoreTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageStoreTypeShouldNotBeFound(String filter) throws Exception {
        restImageStoreTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageStoreTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageStoreType() throws Exception {
        // Get the imageStoreType
        restImageStoreTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageStoreType() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();

        // Update the imageStoreType
        ImageStoreType updatedImageStoreType = imageStoreTypeRepository.findById(imageStoreType.getId()).get();
        // Disconnect from session so that the updates on updatedImageStoreType are not directly saved in db
        em.detach(updatedImageStoreType);
        updatedImageStoreType
            .imageStoreTypeCode(UPDATED_IMAGE_STORE_TYPE_CODE)
            .imageStoreTypeDescription(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restImageStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImageStoreType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImageStoreType))
            )
            .andExpect(status().isOk());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
        ImageStoreType testImageStoreType = imageStoreTypeList.get(imageStoreTypeList.size() - 1);
        assertThat(testImageStoreType.getImageStoreTypeCode()).isEqualTo(UPDATED_IMAGE_STORE_TYPE_CODE);
        assertThat(testImageStoreType.getImageStoreTypeDescription()).isEqualTo(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
        assertThat(testImageStoreType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingImageStoreType() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();
        imageStoreType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageStoreType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageStoreType() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();
        imageStoreType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageStoreType() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();
        imageStoreType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageStoreType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageStoreTypeWithPatch() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();

        // Update the imageStoreType using partial update
        ImageStoreType partialUpdatedImageStoreType = new ImageStoreType();
        partialUpdatedImageStoreType.setId(imageStoreType.getId());

        partialUpdatedImageStoreType
            .imageStoreTypeCode(UPDATED_IMAGE_STORE_TYPE_CODE)
            .imageStoreTypeDescription(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restImageStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageStoreType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageStoreType))
            )
            .andExpect(status().isOk());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
        ImageStoreType testImageStoreType = imageStoreTypeList.get(imageStoreTypeList.size() - 1);
        assertThat(testImageStoreType.getImageStoreTypeCode()).isEqualTo(UPDATED_IMAGE_STORE_TYPE_CODE);
        assertThat(testImageStoreType.getImageStoreTypeDescription()).isEqualTo(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
        assertThat(testImageStoreType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateImageStoreTypeWithPatch() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();

        // Update the imageStoreType using partial update
        ImageStoreType partialUpdatedImageStoreType = new ImageStoreType();
        partialUpdatedImageStoreType.setId(imageStoreType.getId());

        partialUpdatedImageStoreType
            .imageStoreTypeCode(UPDATED_IMAGE_STORE_TYPE_CODE)
            .imageStoreTypeDescription(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restImageStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageStoreType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageStoreType))
            )
            .andExpect(status().isOk());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
        ImageStoreType testImageStoreType = imageStoreTypeList.get(imageStoreTypeList.size() - 1);
        assertThat(testImageStoreType.getImageStoreTypeCode()).isEqualTo(UPDATED_IMAGE_STORE_TYPE_CODE);
        assertThat(testImageStoreType.getImageStoreTypeDescription()).isEqualTo(UPDATED_IMAGE_STORE_TYPE_DESCRIPTION);
        assertThat(testImageStoreType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingImageStoreType() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();
        imageStoreType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageStoreType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageStoreType() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();
        imageStoreType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageStoreType() throws Exception {
        int databaseSizeBeforeUpdate = imageStoreTypeRepository.findAll().size();
        imageStoreType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageStoreTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageStoreType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageStoreType in the database
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageStoreType() throws Exception {
        // Initialize the database
        imageStoreTypeRepository.saveAndFlush(imageStoreType);

        int databaseSizeBeforeDelete = imageStoreTypeRepository.findAll().size();

        // Delete the imageStoreType
        restImageStoreTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageStoreType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageStoreType> imageStoreTypeList = imageStoreTypeRepository.findAll();
        assertThat(imageStoreTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
