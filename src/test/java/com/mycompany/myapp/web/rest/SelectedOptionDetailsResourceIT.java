package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SelectedOption;
import com.mycompany.myapp.domain.SelectedOptionDetails;
import com.mycompany.myapp.repository.SelectedOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.SelectedOptionDetailsCriteria;
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
 * Integration tests for the {@link SelectedOptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SelectedOptionDetailsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_SELECTED_STYLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_STYLE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTED_OPTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_OPTION_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/selected-option-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SelectedOptionDetailsRepository selectedOptionDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSelectedOptionDetailsMockMvc;

    private SelectedOptionDetails selectedOptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelectedOptionDetails createEntity(EntityManager em) {
        SelectedOptionDetails selectedOptionDetails = new SelectedOptionDetails()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .selectedValue(DEFAULT_SELECTED_VALUE)
            .isActive(DEFAULT_IS_ACTIVE)
            .selectedStyleCode(DEFAULT_SELECTED_STYLE_CODE)
            .selectedOptionCode(DEFAULT_SELECTED_OPTION_CODE);
        return selectedOptionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelectedOptionDetails createUpdatedEntity(EntityManager em) {
        SelectedOptionDetails selectedOptionDetails = new SelectedOptionDetails()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .selectedValue(UPDATED_SELECTED_VALUE)
            .isActive(UPDATED_IS_ACTIVE)
            .selectedStyleCode(UPDATED_SELECTED_STYLE_CODE)
            .selectedOptionCode(UPDATED_SELECTED_OPTION_CODE);
        return selectedOptionDetails;
    }

    @BeforeEach
    public void initTest() {
        selectedOptionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeCreate = selectedOptionDetailsRepository.findAll().size();
        // Create the SelectedOptionDetails
        restSelectedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isCreated());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        SelectedOptionDetails testSelectedOptionDetails = selectedOptionDetailsList.get(selectedOptionDetailsList.size() - 1);
        assertThat(testSelectedOptionDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSelectedOptionDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSelectedOptionDetails.getSelectedValue()).isEqualTo(DEFAULT_SELECTED_VALUE);
        assertThat(testSelectedOptionDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSelectedOptionDetails.getSelectedStyleCode()).isEqualTo(DEFAULT_SELECTED_STYLE_CODE);
        assertThat(testSelectedOptionDetails.getSelectedOptionCode()).isEqualTo(DEFAULT_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void createSelectedOptionDetailsWithExistingId() throws Exception {
        // Create the SelectedOptionDetails with an existing ID
        selectedOptionDetails.setId(1L);

        int databaseSizeBeforeCreate = selectedOptionDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSelectedOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetails() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList
        restSelectedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectedOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].selectedValue").value(hasItem(DEFAULT_SELECTED_VALUE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].selectedStyleCode").value(hasItem(DEFAULT_SELECTED_STYLE_CODE)))
            .andExpect(jsonPath("$.[*].selectedOptionCode").value(hasItem(DEFAULT_SELECTED_OPTION_CODE)));
    }

    @Test
    @Transactional
    void getSelectedOptionDetails() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get the selectedOptionDetails
        restSelectedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, selectedOptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(selectedOptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.selectedValue").value(DEFAULT_SELECTED_VALUE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.selectedStyleCode").value(DEFAULT_SELECTED_STYLE_CODE))
            .andExpect(jsonPath("$.selectedOptionCode").value(DEFAULT_SELECTED_OPTION_CODE));
    }

    @Test
    @Transactional
    void getSelectedOptionDetailsByIdFiltering() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        Long id = selectedOptionDetails.getId();

        defaultSelectedOptionDetailsShouldBeFound("id.equals=" + id);
        defaultSelectedOptionDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultSelectedOptionDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSelectedOptionDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultSelectedOptionDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSelectedOptionDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where code equals to DEFAULT_CODE
        defaultSelectedOptionDetailsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the selectedOptionDetailsList where code equals to UPDATED_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSelectedOptionDetailsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the selectedOptionDetailsList where code equals to UPDATED_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where code is not null
        defaultSelectedOptionDetailsShouldBeFound("code.specified=true");

        // Get all the selectedOptionDetailsList where code is null
        defaultSelectedOptionDetailsShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByCodeContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where code contains DEFAULT_CODE
        defaultSelectedOptionDetailsShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the selectedOptionDetailsList where code contains UPDATED_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where code does not contain DEFAULT_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the selectedOptionDetailsList where code does not contain UPDATED_CODE
        defaultSelectedOptionDetailsShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where name equals to DEFAULT_NAME
        defaultSelectedOptionDetailsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the selectedOptionDetailsList where name equals to UPDATED_NAME
        defaultSelectedOptionDetailsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSelectedOptionDetailsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the selectedOptionDetailsList where name equals to UPDATED_NAME
        defaultSelectedOptionDetailsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where name is not null
        defaultSelectedOptionDetailsShouldBeFound("name.specified=true");

        // Get all the selectedOptionDetailsList where name is null
        defaultSelectedOptionDetailsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByNameContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where name contains DEFAULT_NAME
        defaultSelectedOptionDetailsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the selectedOptionDetailsList where name contains UPDATED_NAME
        defaultSelectedOptionDetailsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where name does not contain DEFAULT_NAME
        defaultSelectedOptionDetailsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the selectedOptionDetailsList where name does not contain UPDATED_NAME
        defaultSelectedOptionDetailsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedValueIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedValue equals to DEFAULT_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldBeFound("selectedValue.equals=" + DEFAULT_SELECTED_VALUE);

        // Get all the selectedOptionDetailsList where selectedValue equals to UPDATED_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedValue.equals=" + UPDATED_SELECTED_VALUE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedValueIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedValue in DEFAULT_SELECTED_VALUE or UPDATED_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldBeFound("selectedValue.in=" + DEFAULT_SELECTED_VALUE + "," + UPDATED_SELECTED_VALUE);

        // Get all the selectedOptionDetailsList where selectedValue equals to UPDATED_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedValue.in=" + UPDATED_SELECTED_VALUE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedValue is not null
        defaultSelectedOptionDetailsShouldBeFound("selectedValue.specified=true");

        // Get all the selectedOptionDetailsList where selectedValue is null
        defaultSelectedOptionDetailsShouldNotBeFound("selectedValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedValueContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedValue contains DEFAULT_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldBeFound("selectedValue.contains=" + DEFAULT_SELECTED_VALUE);

        // Get all the selectedOptionDetailsList where selectedValue contains UPDATED_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedValue.contains=" + UPDATED_SELECTED_VALUE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedValueNotContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedValue does not contain DEFAULT_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedValue.doesNotContain=" + DEFAULT_SELECTED_VALUE);

        // Get all the selectedOptionDetailsList where selectedValue does not contain UPDATED_SELECTED_VALUE
        defaultSelectedOptionDetailsShouldBeFound("selectedValue.doesNotContain=" + UPDATED_SELECTED_VALUE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultSelectedOptionDetailsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the selectedOptionDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultSelectedOptionDetailsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultSelectedOptionDetailsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the selectedOptionDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultSelectedOptionDetailsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where isActive is not null
        defaultSelectedOptionDetailsShouldBeFound("isActive.specified=true");

        // Get all the selectedOptionDetailsList where isActive is null
        defaultSelectedOptionDetailsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedStyleCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedStyleCode equals to DEFAULT_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldBeFound("selectedStyleCode.equals=" + DEFAULT_SELECTED_STYLE_CODE);

        // Get all the selectedOptionDetailsList where selectedStyleCode equals to UPDATED_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedStyleCode.equals=" + UPDATED_SELECTED_STYLE_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedStyleCodeIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedStyleCode in DEFAULT_SELECTED_STYLE_CODE or UPDATED_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldBeFound(
            "selectedStyleCode.in=" + DEFAULT_SELECTED_STYLE_CODE + "," + UPDATED_SELECTED_STYLE_CODE
        );

        // Get all the selectedOptionDetailsList where selectedStyleCode equals to UPDATED_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedStyleCode.in=" + UPDATED_SELECTED_STYLE_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedStyleCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedStyleCode is not null
        defaultSelectedOptionDetailsShouldBeFound("selectedStyleCode.specified=true");

        // Get all the selectedOptionDetailsList where selectedStyleCode is null
        defaultSelectedOptionDetailsShouldNotBeFound("selectedStyleCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedStyleCodeContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedStyleCode contains DEFAULT_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldBeFound("selectedStyleCode.contains=" + DEFAULT_SELECTED_STYLE_CODE);

        // Get all the selectedOptionDetailsList where selectedStyleCode contains UPDATED_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedStyleCode.contains=" + UPDATED_SELECTED_STYLE_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedStyleCodeNotContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedStyleCode does not contain DEFAULT_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedStyleCode.doesNotContain=" + DEFAULT_SELECTED_STYLE_CODE);

        // Get all the selectedOptionDetailsList where selectedStyleCode does not contain UPDATED_SELECTED_STYLE_CODE
        defaultSelectedOptionDetailsShouldBeFound("selectedStyleCode.doesNotContain=" + UPDATED_SELECTED_STYLE_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedOptionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedOptionCode equals to DEFAULT_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldBeFound("selectedOptionCode.equals=" + DEFAULT_SELECTED_OPTION_CODE);

        // Get all the selectedOptionDetailsList where selectedOptionCode equals to UPDATED_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedOptionCode.equals=" + UPDATED_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedOptionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedOptionCode in DEFAULT_SELECTED_OPTION_CODE or UPDATED_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldBeFound(
            "selectedOptionCode.in=" + DEFAULT_SELECTED_OPTION_CODE + "," + UPDATED_SELECTED_OPTION_CODE
        );

        // Get all the selectedOptionDetailsList where selectedOptionCode equals to UPDATED_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedOptionCode.in=" + UPDATED_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedOptionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedOptionCode is not null
        defaultSelectedOptionDetailsShouldBeFound("selectedOptionCode.specified=true");

        // Get all the selectedOptionDetailsList where selectedOptionCode is null
        defaultSelectedOptionDetailsShouldNotBeFound("selectedOptionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedOptionCodeContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedOptionCode contains DEFAULT_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldBeFound("selectedOptionCode.contains=" + DEFAULT_SELECTED_OPTION_CODE);

        // Get all the selectedOptionDetailsList where selectedOptionCode contains UPDATED_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedOptionCode.contains=" + UPDATED_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedOptionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        // Get all the selectedOptionDetailsList where selectedOptionCode does not contain DEFAULT_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldNotBeFound("selectedOptionCode.doesNotContain=" + DEFAULT_SELECTED_OPTION_CODE);

        // Get all the selectedOptionDetailsList where selectedOptionCode does not contain UPDATED_SELECTED_OPTION_CODE
        defaultSelectedOptionDetailsShouldBeFound("selectedOptionCode.doesNotContain=" + UPDATED_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionDetailsBySelectedOptionIsEqualToSomething() throws Exception {
        SelectedOption selectedOption;
        if (TestUtil.findAll(em, SelectedOption.class).isEmpty()) {
            selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);
            selectedOption = SelectedOptionResourceIT.createEntity(em);
        } else {
            selectedOption = TestUtil.findAll(em, SelectedOption.class).get(0);
        }
        em.persist(selectedOption);
        em.flush();
        selectedOptionDetails.addSelectedOption(selectedOption);
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);
        Long selectedOptionId = selectedOption.getId();

        // Get all the selectedOptionDetailsList where selectedOption equals to selectedOptionId
        defaultSelectedOptionDetailsShouldBeFound("selectedOptionId.equals=" + selectedOptionId);

        // Get all the selectedOptionDetailsList where selectedOption equals to (selectedOptionId + 1)
        defaultSelectedOptionDetailsShouldNotBeFound("selectedOptionId.equals=" + (selectedOptionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSelectedOptionDetailsShouldBeFound(String filter) throws Exception {
        restSelectedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectedOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].selectedValue").value(hasItem(DEFAULT_SELECTED_VALUE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].selectedStyleCode").value(hasItem(DEFAULT_SELECTED_STYLE_CODE)))
            .andExpect(jsonPath("$.[*].selectedOptionCode").value(hasItem(DEFAULT_SELECTED_OPTION_CODE)));

        // Check, that the count call also returns 1
        restSelectedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSelectedOptionDetailsShouldNotBeFound(String filter) throws Exception {
        restSelectedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSelectedOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSelectedOptionDetails() throws Exception {
        // Get the selectedOptionDetails
        restSelectedOptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSelectedOptionDetails() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();

        // Update the selectedOptionDetails
        SelectedOptionDetails updatedSelectedOptionDetails = selectedOptionDetailsRepository.findById(selectedOptionDetails.getId()).get();
        // Disconnect from session so that the updates on updatedSelectedOptionDetails are not directly saved in db
        em.detach(updatedSelectedOptionDetails);
        updatedSelectedOptionDetails
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .selectedValue(UPDATED_SELECTED_VALUE)
            .isActive(UPDATED_IS_ACTIVE)
            .selectedStyleCode(UPDATED_SELECTED_STYLE_CODE)
            .selectedOptionCode(UPDATED_SELECTED_OPTION_CODE);

        restSelectedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSelectedOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSelectedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        SelectedOptionDetails testSelectedOptionDetails = selectedOptionDetailsList.get(selectedOptionDetailsList.size() - 1);
        assertThat(testSelectedOptionDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSelectedOptionDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSelectedOptionDetails.getSelectedValue()).isEqualTo(UPDATED_SELECTED_VALUE);
        assertThat(testSelectedOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSelectedOptionDetails.getSelectedStyleCode()).isEqualTo(UPDATED_SELECTED_STYLE_CODE);
        assertThat(testSelectedOptionDetails.getSelectedOptionCode()).isEqualTo(UPDATED_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void putNonExistingSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();
        selectedOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, selectedOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();
        selectedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();
        selectedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSelectedOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();

        // Update the selectedOptionDetails using partial update
        SelectedOptionDetails partialUpdatedSelectedOptionDetails = new SelectedOptionDetails();
        partialUpdatedSelectedOptionDetails.setId(selectedOptionDetails.getId());

        restSelectedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelectedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSelectedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        SelectedOptionDetails testSelectedOptionDetails = selectedOptionDetailsList.get(selectedOptionDetailsList.size() - 1);
        assertThat(testSelectedOptionDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSelectedOptionDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSelectedOptionDetails.getSelectedValue()).isEqualTo(DEFAULT_SELECTED_VALUE);
        assertThat(testSelectedOptionDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSelectedOptionDetails.getSelectedStyleCode()).isEqualTo(DEFAULT_SELECTED_STYLE_CODE);
        assertThat(testSelectedOptionDetails.getSelectedOptionCode()).isEqualTo(DEFAULT_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void fullUpdateSelectedOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();

        // Update the selectedOptionDetails using partial update
        SelectedOptionDetails partialUpdatedSelectedOptionDetails = new SelectedOptionDetails();
        partialUpdatedSelectedOptionDetails.setId(selectedOptionDetails.getId());

        partialUpdatedSelectedOptionDetails
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .selectedValue(UPDATED_SELECTED_VALUE)
            .isActive(UPDATED_IS_ACTIVE)
            .selectedStyleCode(UPDATED_SELECTED_STYLE_CODE)
            .selectedOptionCode(UPDATED_SELECTED_OPTION_CODE);

        restSelectedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelectedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSelectedOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        SelectedOptionDetails testSelectedOptionDetails = selectedOptionDetailsList.get(selectedOptionDetailsList.size() - 1);
        assertThat(testSelectedOptionDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSelectedOptionDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSelectedOptionDetails.getSelectedValue()).isEqualTo(UPDATED_SELECTED_VALUE);
        assertThat(testSelectedOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSelectedOptionDetails.getSelectedStyleCode()).isEqualTo(UPDATED_SELECTED_STYLE_CODE);
        assertThat(testSelectedOptionDetails.getSelectedOptionCode()).isEqualTo(UPDATED_SELECTED_OPTION_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();
        selectedOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, selectedOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();
        selectedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSelectedOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionDetailsRepository.findAll().size();
        selectedOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selectedOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SelectedOptionDetails in the database
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSelectedOptionDetails() throws Exception {
        // Initialize the database
        selectedOptionDetailsRepository.saveAndFlush(selectedOptionDetails);

        int databaseSizeBeforeDelete = selectedOptionDetailsRepository.findAll().size();

        // Delete the selectedOptionDetails
        restSelectedOptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, selectedOptionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SelectedOptionDetails> selectedOptionDetailsList = selectedOptionDetailsRepository.findAll();
        assertThat(selectedOptionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
