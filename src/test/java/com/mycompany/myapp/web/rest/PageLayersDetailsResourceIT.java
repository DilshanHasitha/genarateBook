package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PageLayers;
import com.mycompany.myapp.domain.PageLayersDetails;
import com.mycompany.myapp.repository.PageLayersDetailsRepository;
import com.mycompany.myapp.service.criteria.PageLayersDetailsCriteria;
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
 * Integration tests for the {@link PageLayersDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PageLayersDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/page-layers-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PageLayersDetailsRepository pageLayersDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPageLayersDetailsMockMvc;

    private PageLayersDetails pageLayersDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageLayersDetails createEntity(EntityManager em) {
        PageLayersDetails pageLayersDetails = new PageLayersDetails()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return pageLayersDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageLayersDetails createUpdatedEntity(EntityManager em) {
        PageLayersDetails pageLayersDetails = new PageLayersDetails()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return pageLayersDetails;
    }

    @BeforeEach
    public void initTest() {
        pageLayersDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPageLayersDetails() throws Exception {
        int databaseSizeBeforeCreate = pageLayersDetailsRepository.findAll().size();
        // Create the PageLayersDetails
        restPageLayersDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isCreated());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PageLayersDetails testPageLayersDetails = pageLayersDetailsList.get(pageLayersDetailsList.size() - 1);
        assertThat(testPageLayersDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPageLayersDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPageLayersDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createPageLayersDetailsWithExistingId() throws Exception {
        // Create the PageLayersDetails with an existing ID
        pageLayersDetails.setId(1L);

        int databaseSizeBeforeCreate = pageLayersDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageLayersDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageLayersDetailsRepository.findAll().size();
        // set the field null
        pageLayersDetails.setName(null);

        // Create the PageLayersDetails, which fails.

        restPageLayersDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isBadRequest());

        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPageLayersDetails() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList
        restPageLayersDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageLayersDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getPageLayersDetails() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get the pageLayersDetails
        restPageLayersDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, pageLayersDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pageLayersDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getPageLayersDetailsByIdFiltering() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        Long id = pageLayersDetails.getId();

        defaultPageLayersDetailsShouldBeFound("id.equals=" + id);
        defaultPageLayersDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultPageLayersDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPageLayersDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultPageLayersDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPageLayersDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where name equals to DEFAULT_NAME
        defaultPageLayersDetailsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pageLayersDetailsList where name equals to UPDATED_NAME
        defaultPageLayersDetailsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPageLayersDetailsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pageLayersDetailsList where name equals to UPDATED_NAME
        defaultPageLayersDetailsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where name is not null
        defaultPageLayersDetailsShouldBeFound("name.specified=true");

        // Get all the pageLayersDetailsList where name is null
        defaultPageLayersDetailsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByNameContainsSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where name contains DEFAULT_NAME
        defaultPageLayersDetailsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pageLayersDetailsList where name contains UPDATED_NAME
        defaultPageLayersDetailsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where name does not contain DEFAULT_NAME
        defaultPageLayersDetailsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pageLayersDetailsList where name does not contain UPDATED_NAME
        defaultPageLayersDetailsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultPageLayersDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the pageLayersDetailsList where description equals to UPDATED_DESCRIPTION
        defaultPageLayersDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPageLayersDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the pageLayersDetailsList where description equals to UPDATED_DESCRIPTION
        defaultPageLayersDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where description is not null
        defaultPageLayersDetailsShouldBeFound("description.specified=true");

        // Get all the pageLayersDetailsList where description is null
        defaultPageLayersDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where description contains DEFAULT_DESCRIPTION
        defaultPageLayersDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the pageLayersDetailsList where description contains UPDATED_DESCRIPTION
        defaultPageLayersDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultPageLayersDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the pageLayersDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultPageLayersDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPageLayersDetailsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the pageLayersDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultPageLayersDetailsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPageLayersDetailsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the pageLayersDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultPageLayersDetailsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        // Get all the pageLayersDetailsList where isActive is not null
        defaultPageLayersDetailsShouldBeFound("isActive.specified=true");

        // Get all the pageLayersDetailsList where isActive is null
        defaultPageLayersDetailsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersDetailsByPageElementIsEqualToSomething() throws Exception {
        PageLayers pageElement;
        if (TestUtil.findAll(em, PageLayers.class).isEmpty()) {
            pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);
            pageElement = PageLayersResourceIT.createEntity(em);
        } else {
            pageElement = TestUtil.findAll(em, PageLayers.class).get(0);
        }
        em.persist(pageElement);
        em.flush();
        pageLayersDetails.addPageElement(pageElement);
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);
        Long pageElementId = pageElement.getId();

        // Get all the pageLayersDetailsList where pageElement equals to pageElementId
        defaultPageLayersDetailsShouldBeFound("pageElementId.equals=" + pageElementId);

        // Get all the pageLayersDetailsList where pageElement equals to (pageElementId + 1)
        defaultPageLayersDetailsShouldNotBeFound("pageElementId.equals=" + (pageElementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPageLayersDetailsShouldBeFound(String filter) throws Exception {
        restPageLayersDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageLayersDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPageLayersDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPageLayersDetailsShouldNotBeFound(String filter) throws Exception {
        restPageLayersDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPageLayersDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPageLayersDetails() throws Exception {
        // Get the pageLayersDetails
        restPageLayersDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPageLayersDetails() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();

        // Update the pageLayersDetails
        PageLayersDetails updatedPageLayersDetails = pageLayersDetailsRepository.findById(pageLayersDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPageLayersDetails are not directly saved in db
        em.detach(updatedPageLayersDetails);
        updatedPageLayersDetails.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restPageLayersDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPageLayersDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPageLayersDetails))
            )
            .andExpect(status().isOk());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
        PageLayersDetails testPageLayersDetails = pageLayersDetailsList.get(pageLayersDetailsList.size() - 1);
        assertThat(testPageLayersDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPageLayersDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageLayersDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingPageLayersDetails() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();
        pageLayersDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageLayersDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageLayersDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPageLayersDetails() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();
        pageLayersDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPageLayersDetails() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();
        pageLayersDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePageLayersDetailsWithPatch() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();

        // Update the pageLayersDetails using partial update
        PageLayersDetails partialUpdatedPageLayersDetails = new PageLayersDetails();
        partialUpdatedPageLayersDetails.setId(pageLayersDetails.getId());

        partialUpdatedPageLayersDetails.description(UPDATED_DESCRIPTION);

        restPageLayersDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageLayersDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageLayersDetails))
            )
            .andExpect(status().isOk());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
        PageLayersDetails testPageLayersDetails = pageLayersDetailsList.get(pageLayersDetailsList.size() - 1);
        assertThat(testPageLayersDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPageLayersDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageLayersDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdatePageLayersDetailsWithPatch() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();

        // Update the pageLayersDetails using partial update
        PageLayersDetails partialUpdatedPageLayersDetails = new PageLayersDetails();
        partialUpdatedPageLayersDetails.setId(pageLayersDetails.getId());

        partialUpdatedPageLayersDetails.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restPageLayersDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageLayersDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageLayersDetails))
            )
            .andExpect(status().isOk());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
        PageLayersDetails testPageLayersDetails = pageLayersDetailsList.get(pageLayersDetailsList.size() - 1);
        assertThat(testPageLayersDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPageLayersDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageLayersDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingPageLayersDetails() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();
        pageLayersDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageLayersDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pageLayersDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPageLayersDetails() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();
        pageLayersDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPageLayersDetails() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersDetailsRepository.findAll().size();
        pageLayersDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageLayersDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageLayersDetails in the database
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePageLayersDetails() throws Exception {
        // Initialize the database
        pageLayersDetailsRepository.saveAndFlush(pageLayersDetails);

        int databaseSizeBeforeDelete = pageLayersDetailsRepository.findAll().size();

        // Delete the pageLayersDetails
        restPageLayersDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, pageLayersDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PageLayersDetails> pageLayersDetailsList = pageLayersDetailsRepository.findAll();
        assertThat(pageLayersDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
