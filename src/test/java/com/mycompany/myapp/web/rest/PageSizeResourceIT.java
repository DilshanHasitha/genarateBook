package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PageSize;
import com.mycompany.myapp.repository.PageSizeRepository;
import com.mycompany.myapp.service.criteria.PageSizeCriteria;
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
 * Integration tests for the {@link PageSizeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PageSizeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/page-sizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PageSizeRepository pageSizeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPageSizeMockMvc;

    private PageSize pageSize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageSize createEntity(EntityManager em) {
        PageSize pageSize = new PageSize()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT);
        return pageSize;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageSize createUpdatedEntity(EntityManager em) {
        PageSize pageSize = new PageSize()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT);
        return pageSize;
    }

    @BeforeEach
    public void initTest() {
        pageSize = createEntity(em);
    }

    @Test
    @Transactional
    void createPageSize() throws Exception {
        int databaseSizeBeforeCreate = pageSizeRepository.findAll().size();
        // Create the PageSize
        restPageSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageSize)))
            .andExpect(status().isCreated());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeCreate + 1);
        PageSize testPageSize = pageSizeList.get(pageSizeList.size() - 1);
        assertThat(testPageSize.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPageSize.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPageSize.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPageSize.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPageSize.getHeight()).isEqualTo(DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void createPageSizeWithExistingId() throws Exception {
        // Create the PageSize with an existing ID
        pageSize.setId(1L);

        int databaseSizeBeforeCreate = pageSizeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageSize)))
            .andExpect(status().isBadRequest());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPageSizes() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList
        restPageSizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)));
    }

    @Test
    @Transactional
    void getPageSize() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get the pageSize
        restPageSizeMockMvc
            .perform(get(ENTITY_API_URL_ID, pageSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pageSize.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT));
    }

    @Test
    @Transactional
    void getPageSizesByIdFiltering() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        Long id = pageSize.getId();

        defaultPageSizeShouldBeFound("id.equals=" + id);
        defaultPageSizeShouldNotBeFound("id.notEquals=" + id);

        defaultPageSizeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPageSizeShouldNotBeFound("id.greaterThan=" + id);

        defaultPageSizeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPageSizeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPageSizesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where code equals to DEFAULT_CODE
        defaultPageSizeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the pageSizeList where code equals to UPDATED_CODE
        defaultPageSizeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPageSizesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPageSizeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the pageSizeList where code equals to UPDATED_CODE
        defaultPageSizeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPageSizesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where code is not null
        defaultPageSizeShouldBeFound("code.specified=true");

        // Get all the pageSizeList where code is null
        defaultPageSizeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllPageSizesByCodeContainsSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where code contains DEFAULT_CODE
        defaultPageSizeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the pageSizeList where code contains UPDATED_CODE
        defaultPageSizeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPageSizesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where code does not contain DEFAULT_CODE
        defaultPageSizeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the pageSizeList where code does not contain UPDATED_CODE
        defaultPageSizeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPageSizesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where description equals to DEFAULT_DESCRIPTION
        defaultPageSizeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the pageSizeList where description equals to UPDATED_DESCRIPTION
        defaultPageSizeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageSizesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPageSizeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the pageSizeList where description equals to UPDATED_DESCRIPTION
        defaultPageSizeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageSizesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where description is not null
        defaultPageSizeShouldBeFound("description.specified=true");

        // Get all the pageSizeList where description is null
        defaultPageSizeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPageSizesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where description contains DEFAULT_DESCRIPTION
        defaultPageSizeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the pageSizeList where description contains UPDATED_DESCRIPTION
        defaultPageSizeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageSizesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where description does not contain DEFAULT_DESCRIPTION
        defaultPageSizeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the pageSizeList where description does not contain UPDATED_DESCRIPTION
        defaultPageSizeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageSizesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPageSizeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the pageSizeList where isActive equals to UPDATED_IS_ACTIVE
        defaultPageSizeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPageSizesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPageSizeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the pageSizeList where isActive equals to UPDATED_IS_ACTIVE
        defaultPageSizeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPageSizesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where isActive is not null
        defaultPageSizeShouldBeFound("isActive.specified=true");

        // Get all the pageSizeList where isActive is null
        defaultPageSizeShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width equals to DEFAULT_WIDTH
        defaultPageSizeShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the pageSizeList where width equals to UPDATED_WIDTH
        defaultPageSizeShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultPageSizeShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the pageSizeList where width equals to UPDATED_WIDTH
        defaultPageSizeShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width is not null
        defaultPageSizeShouldBeFound("width.specified=true");

        // Get all the pageSizeList where width is null
        defaultPageSizeShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width is greater than or equal to DEFAULT_WIDTH
        defaultPageSizeShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the pageSizeList where width is greater than or equal to UPDATED_WIDTH
        defaultPageSizeShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width is less than or equal to DEFAULT_WIDTH
        defaultPageSizeShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the pageSizeList where width is less than or equal to SMALLER_WIDTH
        defaultPageSizeShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width is less than DEFAULT_WIDTH
        defaultPageSizeShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the pageSizeList where width is less than UPDATED_WIDTH
        defaultPageSizeShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllPageSizesByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where width is greater than DEFAULT_WIDTH
        defaultPageSizeShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the pageSizeList where width is greater than SMALLER_WIDTH
        defaultPageSizeShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height equals to DEFAULT_HEIGHT
        defaultPageSizeShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the pageSizeList where height equals to UPDATED_HEIGHT
        defaultPageSizeShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultPageSizeShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the pageSizeList where height equals to UPDATED_HEIGHT
        defaultPageSizeShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height is not null
        defaultPageSizeShouldBeFound("height.specified=true");

        // Get all the pageSizeList where height is null
        defaultPageSizeShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height is greater than or equal to DEFAULT_HEIGHT
        defaultPageSizeShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the pageSizeList where height is greater than or equal to UPDATED_HEIGHT
        defaultPageSizeShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height is less than or equal to DEFAULT_HEIGHT
        defaultPageSizeShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the pageSizeList where height is less than or equal to SMALLER_HEIGHT
        defaultPageSizeShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height is less than DEFAULT_HEIGHT
        defaultPageSizeShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the pageSizeList where height is less than UPDATED_HEIGHT
        defaultPageSizeShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllPageSizesByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        // Get all the pageSizeList where height is greater than DEFAULT_HEIGHT
        defaultPageSizeShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the pageSizeList where height is greater than SMALLER_HEIGHT
        defaultPageSizeShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPageSizeShouldBeFound(String filter) throws Exception {
        restPageSizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)));

        // Check, that the count call also returns 1
        restPageSizeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPageSizeShouldNotBeFound(String filter) throws Exception {
        restPageSizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPageSizeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPageSize() throws Exception {
        // Get the pageSize
        restPageSizeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPageSize() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();

        // Update the pageSize
        PageSize updatedPageSize = pageSizeRepository.findById(pageSize.getId()).get();
        // Disconnect from session so that the updates on updatedPageSize are not directly saved in db
        em.detach(updatedPageSize);
        updatedPageSize
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT);

        restPageSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPageSize.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPageSize))
            )
            .andExpect(status().isOk());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
        PageSize testPageSize = pageSizeList.get(pageSizeList.size() - 1);
        assertThat(testPageSize.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPageSize.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageSize.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPageSize.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPageSize.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingPageSize() throws Exception {
        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();
        pageSize.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageSize.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPageSize() throws Exception {
        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();
        pageSize.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPageSize() throws Exception {
        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();
        pageSize.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageSizeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageSize)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePageSizeWithPatch() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();

        // Update the pageSize using partial update
        PageSize partialUpdatedPageSize = new PageSize();
        partialUpdatedPageSize.setId(pageSize.getId());

        partialUpdatedPageSize.description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE).height(UPDATED_HEIGHT);

        restPageSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageSize))
            )
            .andExpect(status().isOk());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
        PageSize testPageSize = pageSizeList.get(pageSizeList.size() - 1);
        assertThat(testPageSize.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPageSize.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageSize.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPageSize.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPageSize.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void fullUpdatePageSizeWithPatch() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();

        // Update the pageSize using partial update
        PageSize partialUpdatedPageSize = new PageSize();
        partialUpdatedPageSize.setId(pageSize.getId());

        partialUpdatedPageSize
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT);

        restPageSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageSize))
            )
            .andExpect(status().isOk());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
        PageSize testPageSize = pageSizeList.get(pageSizeList.size() - 1);
        assertThat(testPageSize.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPageSize.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageSize.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPageSize.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPageSize.getHeight()).isEqualTo(UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingPageSize() throws Exception {
        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();
        pageSize.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pageSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPageSize() throws Exception {
        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();
        pageSize.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPageSize() throws Exception {
        int databaseSizeBeforeUpdate = pageSizeRepository.findAll().size();
        pageSize.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageSizeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pageSize)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageSize in the database
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePageSize() throws Exception {
        // Initialize the database
        pageSizeRepository.saveAndFlush(pageSize);

        int databaseSizeBeforeDelete = pageSizeRepository.findAll().size();

        // Delete the pageSize
        restPageSizeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pageSize.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PageSize> pageSizeList = pageSizeRepository.findAll();
        assertThat(pageSizeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
