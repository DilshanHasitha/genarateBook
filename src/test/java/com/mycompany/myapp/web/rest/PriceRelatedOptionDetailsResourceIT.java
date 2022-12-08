package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PriceRelatedOption;
import com.mycompany.myapp.domain.PriceRelatedOptionDetails;
import com.mycompany.myapp.repository.PriceRelatedOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.PriceRelatedOptionDetailsCriteria;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PriceRelatedOptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceRelatedOptionDetailsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/price-related-option-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceRelatedOptionDetailsRepository priceRelatedOptionDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceRelatedOptionDetailsMockMvc;

    private PriceRelatedOptionDetails priceRelatedOptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceRelatedOptionDetails createEntity(EntityManager em) {
        PriceRelatedOptionDetails priceRelatedOptionDetails = new PriceRelatedOptionDetails()
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE);
        return priceRelatedOptionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceRelatedOptionDetails createUpdatedEntity(EntityManager em) {
        PriceRelatedOptionDetails priceRelatedOptionDetails = new PriceRelatedOptionDetails()
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE);
        return priceRelatedOptionDetails;
    }

    @BeforeEach
    public void initTest() {
        priceRelatedOptionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeCreate = priceRelatedOptionDetailsRepository.findAll().size();
        // Create the PriceRelatedOptionDetails
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isCreated());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PriceRelatedOptionDetails testPriceRelatedOptionDetails = priceRelatedOptionDetailsList.get(
            priceRelatedOptionDetailsList.size() - 1
        );
        assertThat(testPriceRelatedOptionDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPriceRelatedOptionDetails.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createPriceRelatedOptionDetailsWithExistingId() throws Exception {
        // Create the PriceRelatedOptionDetails with an existing ID
        priceRelatedOptionDetails.setId(1L);

        int databaseSizeBeforeCreate = priceRelatedOptionDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRelatedOptionDetailsRepository.findAll().size();
        // set the field null
        priceRelatedOptionDetails.setDescription(null);

        // Create the PriceRelatedOptionDetails, which fails.

        restPriceRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRelatedOptionDetailsRepository.findAll().size();
        // set the field null
        priceRelatedOptionDetails.setPrice(null);

        // Create the PriceRelatedOptionDetails, which fails.

        restPriceRelatedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetails() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList
        restPriceRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceRelatedOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getPriceRelatedOptionDetails() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get the priceRelatedOptionDetails
        restPriceRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, priceRelatedOptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceRelatedOptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getPriceRelatedOptionDetailsByIdFiltering() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        Long id = priceRelatedOptionDetails.getId();

        defaultPriceRelatedOptionDetailsShouldBeFound("id.equals=" + id);
        defaultPriceRelatedOptionDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultPriceRelatedOptionDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPriceRelatedOptionDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultPriceRelatedOptionDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPriceRelatedOptionDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the priceRelatedOptionDetailsList where description equals to UPDATED_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the priceRelatedOptionDetailsList where description equals to UPDATED_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where description is not null
        defaultPriceRelatedOptionDetailsShouldBeFound("description.specified=true");

        // Get all the priceRelatedOptionDetailsList where description is null
        defaultPriceRelatedOptionDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where description contains DEFAULT_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the priceRelatedOptionDetailsList where description contains UPDATED_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the priceRelatedOptionDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultPriceRelatedOptionDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price equals to DEFAULT_PRICE
        defaultPriceRelatedOptionDetailsShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the priceRelatedOptionDetailsList where price equals to UPDATED_PRICE
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultPriceRelatedOptionDetailsShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the priceRelatedOptionDetailsList where price equals to UPDATED_PRICE
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price is not null
        defaultPriceRelatedOptionDetailsShouldBeFound("price.specified=true");

        // Get all the priceRelatedOptionDetailsList where price is null
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price is greater than or equal to DEFAULT_PRICE
        defaultPriceRelatedOptionDetailsShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the priceRelatedOptionDetailsList where price is greater than or equal to UPDATED_PRICE
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price is less than or equal to DEFAULT_PRICE
        defaultPriceRelatedOptionDetailsShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the priceRelatedOptionDetailsList where price is less than or equal to SMALLER_PRICE
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price is less than DEFAULT_PRICE
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the priceRelatedOptionDetailsList where price is less than UPDATED_PRICE
        defaultPriceRelatedOptionDetailsShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        // Get all the priceRelatedOptionDetailsList where price is greater than DEFAULT_PRICE
        defaultPriceRelatedOptionDetailsShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the priceRelatedOptionDetailsList where price is greater than SMALLER_PRICE
        defaultPriceRelatedOptionDetailsShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionDetailsByPriceRelatedOptionIsEqualToSomething() throws Exception {
        PriceRelatedOption priceRelatedOption;
        if (TestUtil.findAll(em, PriceRelatedOption.class).isEmpty()) {
            priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);
            priceRelatedOption = PriceRelatedOptionResourceIT.createEntity(em);
        } else {
            priceRelatedOption = TestUtil.findAll(em, PriceRelatedOption.class).get(0);
        }
        em.persist(priceRelatedOption);
        em.flush();
        priceRelatedOptionDetails.addPriceRelatedOption(priceRelatedOption);
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);
        Long priceRelatedOptionId = priceRelatedOption.getId();

        // Get all the priceRelatedOptionDetailsList where priceRelatedOption equals to priceRelatedOptionId
        defaultPriceRelatedOptionDetailsShouldBeFound("priceRelatedOptionId.equals=" + priceRelatedOptionId);

        // Get all the priceRelatedOptionDetailsList where priceRelatedOption equals to (priceRelatedOptionId + 1)
        defaultPriceRelatedOptionDetailsShouldNotBeFound("priceRelatedOptionId.equals=" + (priceRelatedOptionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriceRelatedOptionDetailsShouldBeFound(String filter) throws Exception {
        restPriceRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceRelatedOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));

        // Check, that the count call also returns 1
        restPriceRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriceRelatedOptionDetailsShouldNotBeFound(String filter) throws Exception {
        restPriceRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriceRelatedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPriceRelatedOptionDetails() throws Exception {
        // Get the priceRelatedOptionDetails
        restPriceRelatedOptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPriceRelatedOptionDetails() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();

        // Update the priceRelatedOptionDetails
        PriceRelatedOptionDetails updatedPriceRelatedOptionDetails = priceRelatedOptionDetailsRepository
            .findById(priceRelatedOptionDetails.getId())
            .get();
        // Disconnect from session so that the updates on updatedPriceRelatedOptionDetails are not directly saved in db
        em.detach(updatedPriceRelatedOptionDetails);
        updatedPriceRelatedOptionDetails.description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restPriceRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPriceRelatedOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPriceRelatedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        PriceRelatedOptionDetails testPriceRelatedOptionDetails = priceRelatedOptionDetailsList.get(
            priceRelatedOptionDetailsList.size() - 1
        );
        assertThat(testPriceRelatedOptionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPriceRelatedOptionDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();
        priceRelatedOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceRelatedOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();
        priceRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();
        priceRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceRelatedOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();

        // Update the priceRelatedOptionDetails using partial update
        PriceRelatedOptionDetails partialUpdatedPriceRelatedOptionDetails = new PriceRelatedOptionDetails();
        partialUpdatedPriceRelatedOptionDetails.setId(priceRelatedOptionDetails.getId());

        partialUpdatedPriceRelatedOptionDetails.description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restPriceRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceRelatedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceRelatedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        PriceRelatedOptionDetails testPriceRelatedOptionDetails = priceRelatedOptionDetailsList.get(
            priceRelatedOptionDetailsList.size() - 1
        );
        assertThat(testPriceRelatedOptionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPriceRelatedOptionDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdatePriceRelatedOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();

        // Update the priceRelatedOptionDetails using partial update
        PriceRelatedOptionDetails partialUpdatedPriceRelatedOptionDetails = new PriceRelatedOptionDetails();
        partialUpdatedPriceRelatedOptionDetails.setId(priceRelatedOptionDetails.getId());

        partialUpdatedPriceRelatedOptionDetails.description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restPriceRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceRelatedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceRelatedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        PriceRelatedOptionDetails testPriceRelatedOptionDetails = priceRelatedOptionDetailsList.get(
            priceRelatedOptionDetailsList.size() - 1
        );
        assertThat(testPriceRelatedOptionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPriceRelatedOptionDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();
        priceRelatedOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceRelatedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();
        priceRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriceRelatedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionDetailsRepository.findAll().size();
        priceRelatedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceRelatedOptionDetails in the database
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriceRelatedOptionDetails() throws Exception {
        // Initialize the database
        priceRelatedOptionDetailsRepository.saveAndFlush(priceRelatedOptionDetails);

        int databaseSizeBeforeDelete = priceRelatedOptionDetailsRepository.findAll().size();

        // Delete the priceRelatedOptionDetails
        restPriceRelatedOptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceRelatedOptionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriceRelatedOptionDetails> priceRelatedOptionDetailsList = priceRelatedOptionDetailsRepository.findAll();
        assertThat(priceRelatedOptionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
