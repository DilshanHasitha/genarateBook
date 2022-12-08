package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.repository.OptionsRepository;
import com.mycompany.myapp.service.criteria.OptionsCriteria;
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
 * Integration tests for the {@link OptionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionsMockMvc;

    private Options options;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Options createEntity(EntityManager em) {
        Options options = new Options()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .imgURL(DEFAULT_IMG_URL)
            .isActive(DEFAULT_IS_ACTIVE);
        return options;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Options createUpdatedEntity(EntityManager em) {
        Options options = new Options()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgURL(UPDATED_IMG_URL)
            .isActive(UPDATED_IS_ACTIVE);
        return options;
    }

    @BeforeEach
    public void initTest() {
        options = createEntity(em);
    }

    @Test
    @Transactional
    void createOptions() throws Exception {
        int databaseSizeBeforeCreate = optionsRepository.findAll().size();
        // Create the Options
        restOptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(options)))
            .andExpect(status().isCreated());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeCreate + 1);
        Options testOptions = optionsList.get(optionsList.size() - 1);
        assertThat(testOptions.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOptions.getImgURL()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testOptions.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createOptionsWithExistingId() throws Exception {
        // Create the Options with an existing ID
        options.setId(1L);

        int databaseSizeBeforeCreate = optionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(options)))
            .andExpect(status().isBadRequest());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionsRepository.findAll().size();
        // set the field null
        options.setDescription(null);

        // Create the Options, which fails.

        restOptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(options)))
            .andExpect(status().isBadRequest());

        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOptions() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList
        restOptionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(options.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgURL").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getOptions() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get the options
        restOptionsMockMvc
            .perform(get(ENTITY_API_URL_ID, options.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(options.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imgURL").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getOptionsByIdFiltering() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        Long id = options.getId();

        defaultOptionsShouldBeFound("id.equals=" + id);
        defaultOptionsShouldNotBeFound("id.notEquals=" + id);

        defaultOptionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOptionsShouldNotBeFound("id.greaterThan=" + id);

        defaultOptionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOptionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where code equals to DEFAULT_CODE
        defaultOptionsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the optionsList where code equals to UPDATED_CODE
        defaultOptionsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOptionsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the optionsList where code equals to UPDATED_CODE
        defaultOptionsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where code is not null
        defaultOptionsShouldBeFound("code.specified=true");

        // Get all the optionsList where code is null
        defaultOptionsShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where code contains DEFAULT_CODE
        defaultOptionsShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the optionsList where code contains UPDATED_CODE
        defaultOptionsShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where code does not contain DEFAULT_CODE
        defaultOptionsShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the optionsList where code does not contain UPDATED_CODE
        defaultOptionsShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where description equals to DEFAULT_DESCRIPTION
        defaultOptionsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the optionsList where description equals to UPDATED_DESCRIPTION
        defaultOptionsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOptionsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the optionsList where description equals to UPDATED_DESCRIPTION
        defaultOptionsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where description is not null
        defaultOptionsShouldBeFound("description.specified=true");

        // Get all the optionsList where description is null
        defaultOptionsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where description contains DEFAULT_DESCRIPTION
        defaultOptionsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the optionsList where description contains UPDATED_DESCRIPTION
        defaultOptionsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where description does not contain DEFAULT_DESCRIPTION
        defaultOptionsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the optionsList where description does not contain UPDATED_DESCRIPTION
        defaultOptionsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionsByImgURLIsEqualToSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where imgURL equals to DEFAULT_IMG_URL
        defaultOptionsShouldBeFound("imgURL.equals=" + DEFAULT_IMG_URL);

        // Get all the optionsList where imgURL equals to UPDATED_IMG_URL
        defaultOptionsShouldNotBeFound("imgURL.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllOptionsByImgURLIsInShouldWork() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where imgURL in DEFAULT_IMG_URL or UPDATED_IMG_URL
        defaultOptionsShouldBeFound("imgURL.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL);

        // Get all the optionsList where imgURL equals to UPDATED_IMG_URL
        defaultOptionsShouldNotBeFound("imgURL.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllOptionsByImgURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where imgURL is not null
        defaultOptionsShouldBeFound("imgURL.specified=true");

        // Get all the optionsList where imgURL is null
        defaultOptionsShouldNotBeFound("imgURL.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByImgURLContainsSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where imgURL contains DEFAULT_IMG_URL
        defaultOptionsShouldBeFound("imgURL.contains=" + DEFAULT_IMG_URL);

        // Get all the optionsList where imgURL contains UPDATED_IMG_URL
        defaultOptionsShouldNotBeFound("imgURL.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllOptionsByImgURLNotContainsSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where imgURL does not contain DEFAULT_IMG_URL
        defaultOptionsShouldNotBeFound("imgURL.doesNotContain=" + DEFAULT_IMG_URL);

        // Get all the optionsList where imgURL does not contain UPDATED_IMG_URL
        defaultOptionsShouldBeFound("imgURL.doesNotContain=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllOptionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOptionsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the optionsList where isActive equals to UPDATED_IS_ACTIVE
        defaultOptionsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOptionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOptionsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the optionsList where isActive equals to UPDATED_IS_ACTIVE
        defaultOptionsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOptionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        // Get all the optionsList where isActive is not null
        defaultOptionsShouldBeFound("isActive.specified=true");

        // Get all the optionsList where isActive is null
        defaultOptionsShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionsShouldBeFound(String filter) throws Exception {
        restOptionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(options.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgURL").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restOptionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionsShouldNotBeFound(String filter) throws Exception {
        restOptionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOptions() throws Exception {
        // Get the options
        restOptionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOptions() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();

        // Update the options
        Options updatedOptions = optionsRepository.findById(options.getId()).get();
        // Disconnect from session so that the updates on updatedOptions are not directly saved in db
        em.detach(updatedOptions);
        updatedOptions.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).imgURL(UPDATED_IMG_URL).isActive(UPDATED_IS_ACTIVE);

        restOptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOptions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOptions))
            )
            .andExpect(status().isOk());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
        Options testOptions = optionsList.get(optionsList.size() - 1);
        assertThat(testOptions.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOptions.getImgURL()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testOptions.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingOptions() throws Exception {
        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();
        options.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, options.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(options))
            )
            .andExpect(status().isBadRequest());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOptions() throws Exception {
        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();
        options.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(options))
            )
            .andExpect(status().isBadRequest());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOptions() throws Exception {
        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();
        options.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(options)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionsWithPatch() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();

        // Update the options using partial update
        Options partialUpdatedOptions = new Options();
        partialUpdatedOptions.setId(options.getId());

        partialUpdatedOptions.description(UPDATED_DESCRIPTION);

        restOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOptions))
            )
            .andExpect(status().isOk());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
        Options testOptions = optionsList.get(optionsList.size() - 1);
        assertThat(testOptions.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOptions.getImgURL()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testOptions.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateOptionsWithPatch() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();

        // Update the options using partial update
        Options partialUpdatedOptions = new Options();
        partialUpdatedOptions.setId(options.getId());

        partialUpdatedOptions.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).imgURL(UPDATED_IMG_URL).isActive(UPDATED_IS_ACTIVE);

        restOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOptions))
            )
            .andExpect(status().isOk());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
        Options testOptions = optionsList.get(optionsList.size() - 1);
        assertThat(testOptions.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOptions.getImgURL()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testOptions.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingOptions() throws Exception {
        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();
        options.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, options.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(options))
            )
            .andExpect(status().isBadRequest());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOptions() throws Exception {
        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();
        options.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(options))
            )
            .andExpect(status().isBadRequest());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOptions() throws Exception {
        int databaseSizeBeforeUpdate = optionsRepository.findAll().size();
        options.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(options)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Options in the database
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOptions() throws Exception {
        // Initialize the database
        optionsRepository.saveAndFlush(options);

        int databaseSizeBeforeDelete = optionsRepository.findAll().size();

        // Delete the options
        restOptionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, options.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Options> optionsList = optionsRepository.findAll();
        assertThat(optionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
