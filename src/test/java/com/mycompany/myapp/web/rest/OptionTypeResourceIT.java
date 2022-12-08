package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OptionType;
import com.mycompany.myapp.repository.OptionTypeRepository;
import com.mycompany.myapp.service.criteria.OptionTypeCriteria;
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
 * Integration tests for the {@link OptionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/option-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OptionTypeRepository optionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionTypeMockMvc;

    private OptionType optionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionType createEntity(EntityManager em) {
        OptionType optionType = new OptionType().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).isActive(DEFAULT_IS_ACTIVE);
        return optionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionType createUpdatedEntity(EntityManager em) {
        OptionType optionType = new OptionType().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);
        return optionType;
    }

    @BeforeEach
    public void initTest() {
        optionType = createEntity(em);
    }

    @Test
    @Transactional
    void createOptionType() throws Exception {
        int databaseSizeBeforeCreate = optionTypeRepository.findAll().size();
        // Create the OptionType
        restOptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionType)))
            .andExpect(status().isCreated());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        OptionType testOptionType = optionTypeList.get(optionTypeList.size() - 1);
        assertThat(testOptionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOptionType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createOptionTypeWithExistingId() throws Exception {
        // Create the OptionType with an existing ID
        optionType.setId(1L);

        int databaseSizeBeforeCreate = optionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionType)))
            .andExpect(status().isBadRequest());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionTypeRepository.findAll().size();
        // set the field null
        optionType.setDescription(null);

        // Create the OptionType, which fails.

        restOptionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionType)))
            .andExpect(status().isBadRequest());

        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOptionTypes() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList
        restOptionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getOptionType() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get the optionType
        restOptionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, optionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(optionType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getOptionTypesByIdFiltering() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        Long id = optionType.getId();

        defaultOptionTypeShouldBeFound("id.equals=" + id);
        defaultOptionTypeShouldNotBeFound("id.notEquals=" + id);

        defaultOptionTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOptionTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultOptionTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOptionTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOptionTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where code equals to DEFAULT_CODE
        defaultOptionTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the optionTypeList where code equals to UPDATED_CODE
        defaultOptionTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOptionTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the optionTypeList where code equals to UPDATED_CODE
        defaultOptionTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where code is not null
        defaultOptionTypeShouldBeFound("code.specified=true");

        // Get all the optionTypeList where code is null
        defaultOptionTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where code contains DEFAULT_CODE
        defaultOptionTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the optionTypeList where code contains UPDATED_CODE
        defaultOptionTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where code does not contain DEFAULT_CODE
        defaultOptionTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the optionTypeList where code does not contain UPDATED_CODE
        defaultOptionTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where description equals to DEFAULT_DESCRIPTION
        defaultOptionTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the optionTypeList where description equals to UPDATED_DESCRIPTION
        defaultOptionTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOptionTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the optionTypeList where description equals to UPDATED_DESCRIPTION
        defaultOptionTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where description is not null
        defaultOptionTypeShouldBeFound("description.specified=true");

        // Get all the optionTypeList where description is null
        defaultOptionTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where description contains DEFAULT_DESCRIPTION
        defaultOptionTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the optionTypeList where description contains UPDATED_DESCRIPTION
        defaultOptionTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultOptionTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the optionTypeList where description does not contain UPDATED_DESCRIPTION
        defaultOptionTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOptionTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOptionTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the optionTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultOptionTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOptionTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOptionTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the optionTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultOptionTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOptionTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        // Get all the optionTypeList where isActive is not null
        defaultOptionTypeShouldBeFound("isActive.specified=true");

        // Get all the optionTypeList where isActive is null
        defaultOptionTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionTypeShouldBeFound(String filter) throws Exception {
        restOptionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restOptionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionTypeShouldNotBeFound(String filter) throws Exception {
        restOptionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOptionType() throws Exception {
        // Get the optionType
        restOptionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOptionType() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();

        // Update the optionType
        OptionType updatedOptionType = optionTypeRepository.findById(optionType.getId()).get();
        // Disconnect from session so that the updates on updatedOptionType are not directly saved in db
        em.detach(updatedOptionType);
        updatedOptionType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOptionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOptionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOptionType))
            )
            .andExpect(status().isOk());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
        OptionType testOptionType = optionTypeList.get(optionTypeList.size() - 1);
        assertThat(testOptionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOptionType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingOptionType() throws Exception {
        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();
        optionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOptionType() throws Exception {
        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();
        optionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOptionType() throws Exception {
        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();
        optionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionTypeWithPatch() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();

        // Update the optionType using partial update
        OptionType partialUpdatedOptionType = new OptionType();
        partialUpdatedOptionType.setId(optionType.getId());

        restOptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOptionType))
            )
            .andExpect(status().isOk());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
        OptionType testOptionType = optionTypeList.get(optionTypeList.size() - 1);
        assertThat(testOptionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOptionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOptionType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateOptionTypeWithPatch() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();

        // Update the optionType using partial update
        OptionType partialUpdatedOptionType = new OptionType();
        partialUpdatedOptionType.setId(optionType.getId());

        partialUpdatedOptionType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOptionType))
            )
            .andExpect(status().isOk());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
        OptionType testOptionType = optionTypeList.get(optionTypeList.size() - 1);
        assertThat(testOptionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOptionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOptionType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingOptionType() throws Exception {
        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();
        optionType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(optionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOptionType() throws Exception {
        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();
        optionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(optionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOptionType() throws Exception {
        int databaseSizeBeforeUpdate = optionTypeRepository.findAll().size();
        optionType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(optionType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionType in the database
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOptionType() throws Exception {
        // Initialize the database
        optionTypeRepository.saveAndFlush(optionType);

        int databaseSizeBeforeDelete = optionTypeRepository.findAll().size();

        // Delete the optionType
        restOptionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, optionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OptionType> optionTypeList = optionTypeRepository.findAll();
        assertThat(optionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
