package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StylesDetails;
import com.mycompany.myapp.repository.StylesDetailsRepository;
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
 * Integration tests for the {@link StylesDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StylesDetailsResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_TEMPLATE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_REPLACE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_REPLACE_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/styles-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StylesDetailsRepository stylesDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStylesDetailsMockMvc;

    private StylesDetails stylesDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StylesDetails createEntity(EntityManager em) {
        StylesDetails stylesDetails = new StylesDetails()
            .isActive(DEFAULT_IS_ACTIVE)
            .templateValue(DEFAULT_TEMPLATE_VALUE)
            .replaceValue(DEFAULT_REPLACE_VALUE);
        return stylesDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StylesDetails createUpdatedEntity(EntityManager em) {
        StylesDetails stylesDetails = new StylesDetails()
            .isActive(UPDATED_IS_ACTIVE)
            .templateValue(UPDATED_TEMPLATE_VALUE)
            .replaceValue(UPDATED_REPLACE_VALUE);
        return stylesDetails;
    }

    @BeforeEach
    public void initTest() {
        stylesDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createStylesDetails() throws Exception {
        int databaseSizeBeforeCreate = stylesDetailsRepository.findAll().size();
        // Create the StylesDetails
        restStylesDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stylesDetails)))
            .andExpect(status().isCreated());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        StylesDetails testStylesDetails = stylesDetailsList.get(stylesDetailsList.size() - 1);
        assertThat(testStylesDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testStylesDetails.getTemplateValue()).isEqualTo(DEFAULT_TEMPLATE_VALUE);
        assertThat(testStylesDetails.getReplaceValue()).isEqualTo(DEFAULT_REPLACE_VALUE);
    }

    @Test
    @Transactional
    void createStylesDetailsWithExistingId() throws Exception {
        // Create the StylesDetails with an existing ID
        stylesDetails.setId(1L);

        int databaseSizeBeforeCreate = stylesDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStylesDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stylesDetails)))
            .andExpect(status().isBadRequest());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStylesDetails() throws Exception {
        // Initialize the database
        stylesDetailsRepository.saveAndFlush(stylesDetails);

        // Get all the stylesDetailsList
        restStylesDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stylesDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].templateValue").value(hasItem(DEFAULT_TEMPLATE_VALUE)))
            .andExpect(jsonPath("$.[*].replaceValue").value(hasItem(DEFAULT_REPLACE_VALUE)));
    }

    @Test
    @Transactional
    void getStylesDetails() throws Exception {
        // Initialize the database
        stylesDetailsRepository.saveAndFlush(stylesDetails);

        // Get the stylesDetails
        restStylesDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, stylesDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stylesDetails.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.templateValue").value(DEFAULT_TEMPLATE_VALUE))
            .andExpect(jsonPath("$.replaceValue").value(DEFAULT_REPLACE_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingStylesDetails() throws Exception {
        // Get the stylesDetails
        restStylesDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStylesDetails() throws Exception {
        // Initialize the database
        stylesDetailsRepository.saveAndFlush(stylesDetails);

        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();

        // Update the stylesDetails
        StylesDetails updatedStylesDetails = stylesDetailsRepository.findById(stylesDetails.getId()).get();
        // Disconnect from session so that the updates on updatedStylesDetails are not directly saved in db
        em.detach(updatedStylesDetails);
        updatedStylesDetails.isActive(UPDATED_IS_ACTIVE).templateValue(UPDATED_TEMPLATE_VALUE).replaceValue(UPDATED_REPLACE_VALUE);

        restStylesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStylesDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStylesDetails))
            )
            .andExpect(status().isOk());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
        StylesDetails testStylesDetails = stylesDetailsList.get(stylesDetailsList.size() - 1);
        assertThat(testStylesDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testStylesDetails.getTemplateValue()).isEqualTo(UPDATED_TEMPLATE_VALUE);
        assertThat(testStylesDetails.getReplaceValue()).isEqualTo(UPDATED_REPLACE_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingStylesDetails() throws Exception {
        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();
        stylesDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStylesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stylesDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stylesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStylesDetails() throws Exception {
        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();
        stylesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stylesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStylesDetails() throws Exception {
        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();
        stylesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stylesDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStylesDetailsWithPatch() throws Exception {
        // Initialize the database
        stylesDetailsRepository.saveAndFlush(stylesDetails);

        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();

        // Update the stylesDetails using partial update
        StylesDetails partialUpdatedStylesDetails = new StylesDetails();
        partialUpdatedStylesDetails.setId(stylesDetails.getId());

        partialUpdatedStylesDetails.isActive(UPDATED_IS_ACTIVE).templateValue(UPDATED_TEMPLATE_VALUE).replaceValue(UPDATED_REPLACE_VALUE);

        restStylesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStylesDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStylesDetails))
            )
            .andExpect(status().isOk());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
        StylesDetails testStylesDetails = stylesDetailsList.get(stylesDetailsList.size() - 1);
        assertThat(testStylesDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testStylesDetails.getTemplateValue()).isEqualTo(UPDATED_TEMPLATE_VALUE);
        assertThat(testStylesDetails.getReplaceValue()).isEqualTo(UPDATED_REPLACE_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateStylesDetailsWithPatch() throws Exception {
        // Initialize the database
        stylesDetailsRepository.saveAndFlush(stylesDetails);

        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();

        // Update the stylesDetails using partial update
        StylesDetails partialUpdatedStylesDetails = new StylesDetails();
        partialUpdatedStylesDetails.setId(stylesDetails.getId());

        partialUpdatedStylesDetails.isActive(UPDATED_IS_ACTIVE).templateValue(UPDATED_TEMPLATE_VALUE).replaceValue(UPDATED_REPLACE_VALUE);

        restStylesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStylesDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStylesDetails))
            )
            .andExpect(status().isOk());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
        StylesDetails testStylesDetails = stylesDetailsList.get(stylesDetailsList.size() - 1);
        assertThat(testStylesDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testStylesDetails.getTemplateValue()).isEqualTo(UPDATED_TEMPLATE_VALUE);
        assertThat(testStylesDetails.getReplaceValue()).isEqualTo(UPDATED_REPLACE_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingStylesDetails() throws Exception {
        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();
        stylesDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStylesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stylesDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stylesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStylesDetails() throws Exception {
        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();
        stylesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stylesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStylesDetails() throws Exception {
        int databaseSizeBeforeUpdate = stylesDetailsRepository.findAll().size();
        stylesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stylesDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StylesDetails in the database
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStylesDetails() throws Exception {
        // Initialize the database
        stylesDetailsRepository.saveAndFlush(stylesDetails);

        int databaseSizeBeforeDelete = stylesDetailsRepository.findAll().size();

        // Delete the stylesDetails
        restStylesDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, stylesDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StylesDetails> stylesDetailsList = stylesDetailsRepository.findAll();
        assertThat(stylesDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
