package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BooksRelatedOption;
import com.mycompany.myapp.domain.BooksRelatedOptionDetails;
import com.mycompany.myapp.repository.BooksRelatedOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.BooksRelatedOptionDetailsCriteria;
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
 * Integration tests for the {@link BooksRelatedOptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BooksRelatedOptionDetailsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/books-related-option-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksRelatedOptionDetailsRepository booksRelatedOptionDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksRelatedOptionDetailsMockMvc;

    private BooksRelatedOptionDetails booksRelatedOptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksRelatedOptionDetails createEntity(EntityManager em) {
        BooksRelatedOptionDetails booksRelatedOptionDetails = new BooksRelatedOptionDetails()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return booksRelatedOptionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksRelatedOptionDetails createUpdatedEntity(EntityManager em) {
        BooksRelatedOptionDetails booksRelatedOptionDetails = new BooksRelatedOptionDetails()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return booksRelatedOptionDetails;
    }

    @BeforeEach
    public void initTest() {
        booksRelatedOptionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeCreate = booksRelatedOptionDetailsRepository.findAll().size();
        // Create the BooksRelatedOptionDetails
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isCreated());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BooksRelatedOptionDetails testBooksRelatedOptionDetails = booksRelatedOptionDetailsList.get(
            booksRelatedOptionDetailsList.size() - 1
        );
        assertThat(testBooksRelatedOptionDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksRelatedOptionDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBooksRelatedOptionDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBooksRelatedOptionDetailsWithExistingId() throws Exception {
        // Create the BooksRelatedOptionDetails with an existing ID
        booksRelatedOptionDetails.setId(1L);

        int databaseSizeBeforeCreate = booksRelatedOptionDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = booksRelatedOptionDetailsRepository.findAll().size();
        // set the field null
        booksRelatedOptionDetails.setDescription(null);

        // Create the BooksRelatedOptionDetails, which fails.

        restBooksRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetails() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList
        restBooksRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksRelatedOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getBooksRelatedOptionDetails() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get the booksRelatedOptionDetails
        restBooksRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, booksRelatedOptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booksRelatedOptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBooksRelatedOptionDetailsByIdFiltering() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        Long id = booksRelatedOptionDetails.getId();

        defaultBooksRelatedOptionDetailsShouldBeFound("id.equals=" + id);
        defaultBooksRelatedOptionDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultBooksRelatedOptionDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksRelatedOptionDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksRelatedOptionDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksRelatedOptionDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where code equals to DEFAULT_CODE
        defaultBooksRelatedOptionDetailsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the booksRelatedOptionDetailsList where code equals to UPDATED_CODE
        defaultBooksRelatedOptionDetailsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBooksRelatedOptionDetailsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the booksRelatedOptionDetailsList where code equals to UPDATED_CODE
        defaultBooksRelatedOptionDetailsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where code is not null
        defaultBooksRelatedOptionDetailsShouldBeFound("code.specified=true");

        // Get all the booksRelatedOptionDetailsList where code is null
        defaultBooksRelatedOptionDetailsShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByCodeContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where code contains DEFAULT_CODE
        defaultBooksRelatedOptionDetailsShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the booksRelatedOptionDetailsList where code contains UPDATED_CODE
        defaultBooksRelatedOptionDetailsShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where code does not contain DEFAULT_CODE
        defaultBooksRelatedOptionDetailsShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the booksRelatedOptionDetailsList where code does not contain UPDATED_CODE
        defaultBooksRelatedOptionDetailsShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the booksRelatedOptionDetailsList where description equals to UPDATED_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the booksRelatedOptionDetailsList where description equals to UPDATED_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where description is not null
        defaultBooksRelatedOptionDetailsShouldBeFound("description.specified=true");

        // Get all the booksRelatedOptionDetailsList where description is null
        defaultBooksRelatedOptionDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where description contains DEFAULT_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the booksRelatedOptionDetailsList where description contains UPDATED_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the booksRelatedOptionDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultBooksRelatedOptionDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksRelatedOptionDetailsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksRelatedOptionDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksRelatedOptionDetailsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksRelatedOptionDetailsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksRelatedOptionDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksRelatedOptionDetailsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        // Get all the booksRelatedOptionDetailsList where isActive is not null
        defaultBooksRelatedOptionDetailsShouldBeFound("isActive.specified=true");

        // Get all the booksRelatedOptionDetailsList where isActive is null
        defaultBooksRelatedOptionDetailsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionDetailsByBooksRelatedOptionIsEqualToSomething() throws Exception {
        BooksRelatedOption booksRelatedOption;
        if (TestUtil.findAll(em, BooksRelatedOption.class).isEmpty()) {
            booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);
            booksRelatedOption = BooksRelatedOptionResourceIT.createEntity(em);
        } else {
            booksRelatedOption = TestUtil.findAll(em, BooksRelatedOption.class).get(0);
        }
        em.persist(booksRelatedOption);
        em.flush();
        booksRelatedOptionDetails.addBooksRelatedOption(booksRelatedOption);
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);
        Long booksRelatedOptionId = booksRelatedOption.getId();

        // Get all the booksRelatedOptionDetailsList where booksRelatedOption equals to booksRelatedOptionId
        defaultBooksRelatedOptionDetailsShouldBeFound("booksRelatedOptionId.equals=" + booksRelatedOptionId);

        // Get all the booksRelatedOptionDetailsList where booksRelatedOption equals to (booksRelatedOptionId + 1)
        defaultBooksRelatedOptionDetailsShouldNotBeFound("booksRelatedOptionId.equals=" + (booksRelatedOptionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksRelatedOptionDetailsShouldBeFound(String filter) throws Exception {
        restBooksRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksRelatedOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBooksRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksRelatedOptionDetailsShouldNotBeFound(String filter) throws Exception {
        restBooksRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooksRelatedOptionDetails() throws Exception {
        // Get the booksRelatedOptionDetails
        restBooksRelatedOptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooksRelatedOptionDetails() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();

        // Update the booksRelatedOptionDetails
        BooksRelatedOptionDetails updatedBooksRelatedOptionDetails = booksRelatedOptionDetailsRepository
            .findById(booksRelatedOptionDetails.getId())
            .get();
        // Disconnect from session so that the updates on updatedBooksRelatedOptionDetails are not directly saved in db
        em.detach(updatedBooksRelatedOptionDetails);
        updatedBooksRelatedOptionDetails.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooksRelatedOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooksRelatedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        BooksRelatedOptionDetails testBooksRelatedOptionDetails = booksRelatedOptionDetailsList.get(
            booksRelatedOptionDetailsList.size() - 1
        );
        assertThat(testBooksRelatedOptionDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksRelatedOptionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksRelatedOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();
        booksRelatedOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, booksRelatedOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();
        booksRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();
        booksRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksRelatedOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();

        // Update the booksRelatedOptionDetails using partial update
        BooksRelatedOptionDetails partialUpdatedBooksRelatedOptionDetails = new BooksRelatedOptionDetails();
        partialUpdatedBooksRelatedOptionDetails.setId(booksRelatedOptionDetails.getId());

        partialUpdatedBooksRelatedOptionDetails.isActive(UPDATED_IS_ACTIVE);

        restBooksRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksRelatedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksRelatedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        BooksRelatedOptionDetails testBooksRelatedOptionDetails = booksRelatedOptionDetailsList.get(
            booksRelatedOptionDetailsList.size() - 1
        );
        assertThat(testBooksRelatedOptionDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksRelatedOptionDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBooksRelatedOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBooksRelatedOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();

        // Update the booksRelatedOptionDetails using partial update
        BooksRelatedOptionDetails partialUpdatedBooksRelatedOptionDetails = new BooksRelatedOptionDetails();
        partialUpdatedBooksRelatedOptionDetails.setId(booksRelatedOptionDetails.getId());

        partialUpdatedBooksRelatedOptionDetails.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksRelatedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksRelatedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        BooksRelatedOptionDetails testBooksRelatedOptionDetails = booksRelatedOptionDetailsList.get(
            booksRelatedOptionDetailsList.size() - 1
        );
        assertThat(testBooksRelatedOptionDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksRelatedOptionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksRelatedOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();
        booksRelatedOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, booksRelatedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();
        booksRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooksRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionDetailsRepository.findAll().size();
        booksRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksRelatedOptionDetails in the database
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooksRelatedOptionDetails() throws Exception {
        // Initialize the database
        booksRelatedOptionDetailsRepository.saveAndFlush(booksRelatedOptionDetails);

        int databaseSizeBeforeDelete = booksRelatedOptionDetailsRepository.findAll().size();

        // Delete the booksRelatedOptionDetails
        restBooksRelatedOptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, booksRelatedOptionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BooksRelatedOptionDetails> booksRelatedOptionDetailsList = booksRelatedOptionDetailsRepository.findAll();
        assertThat(booksRelatedOptionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
