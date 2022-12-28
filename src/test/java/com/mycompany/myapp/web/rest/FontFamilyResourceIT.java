package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FontFamily;
import com.mycompany.myapp.repository.FontFamilyRepository;
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
 * Integration tests for the {@link FontFamilyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FontFamilyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/font-families";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FontFamilyRepository fontFamilyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFontFamilyMockMvc;

    private FontFamily fontFamily;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FontFamily createEntity(EntityManager em) {
        FontFamily fontFamily = new FontFamily().name(DEFAULT_NAME).url(DEFAULT_URL).isActive(DEFAULT_IS_ACTIVE);
        return fontFamily;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FontFamily createUpdatedEntity(EntityManager em) {
        FontFamily fontFamily = new FontFamily().name(UPDATED_NAME).url(UPDATED_URL).isActive(UPDATED_IS_ACTIVE);
        return fontFamily;
    }

    @BeforeEach
    public void initTest() {
        fontFamily = createEntity(em);
    }

    @Test
    @Transactional
    void createFontFamily() throws Exception {
        int databaseSizeBeforeCreate = fontFamilyRepository.findAll().size();
        // Create the FontFamily
        restFontFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fontFamily)))
            .andExpect(status().isCreated());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeCreate + 1);
        FontFamily testFontFamily = fontFamilyList.get(fontFamilyList.size() - 1);
        assertThat(testFontFamily.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFontFamily.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFontFamily.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createFontFamilyWithExistingId() throws Exception {
        // Create the FontFamily with an existing ID
        fontFamily.setId(1L);

        int databaseSizeBeforeCreate = fontFamilyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFontFamilyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fontFamily)))
            .andExpect(status().isBadRequest());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFontFamilies() throws Exception {
        // Initialize the database
        fontFamilyRepository.saveAndFlush(fontFamily);

        // Get all the fontFamilyList
        restFontFamilyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fontFamily.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getFontFamily() throws Exception {
        // Initialize the database
        fontFamilyRepository.saveAndFlush(fontFamily);

        // Get the fontFamily
        restFontFamilyMockMvc
            .perform(get(ENTITY_API_URL_ID, fontFamily.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fontFamily.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFontFamily() throws Exception {
        // Get the fontFamily
        restFontFamilyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFontFamily() throws Exception {
        // Initialize the database
        fontFamilyRepository.saveAndFlush(fontFamily);

        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();

        // Update the fontFamily
        FontFamily updatedFontFamily = fontFamilyRepository.findById(fontFamily.getId()).get();
        // Disconnect from session so that the updates on updatedFontFamily are not directly saved in db
        em.detach(updatedFontFamily);
        updatedFontFamily.name(UPDATED_NAME).url(UPDATED_URL).isActive(UPDATED_IS_ACTIVE);

        restFontFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFontFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFontFamily))
            )
            .andExpect(status().isOk());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
        FontFamily testFontFamily = fontFamilyList.get(fontFamilyList.size() - 1);
        assertThat(testFontFamily.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFontFamily.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFontFamily.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingFontFamily() throws Exception {
        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();
        fontFamily.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFontFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fontFamily.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fontFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFontFamily() throws Exception {
        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();
        fontFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFontFamilyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fontFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFontFamily() throws Exception {
        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();
        fontFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFontFamilyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fontFamily)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFontFamilyWithPatch() throws Exception {
        // Initialize the database
        fontFamilyRepository.saveAndFlush(fontFamily);

        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();

        // Update the fontFamily using partial update
        FontFamily partialUpdatedFontFamily = new FontFamily();
        partialUpdatedFontFamily.setId(fontFamily.getId());

        partialUpdatedFontFamily.name(UPDATED_NAME).url(UPDATED_URL);

        restFontFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFontFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFontFamily))
            )
            .andExpect(status().isOk());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
        FontFamily testFontFamily = fontFamilyList.get(fontFamilyList.size() - 1);
        assertThat(testFontFamily.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFontFamily.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFontFamily.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateFontFamilyWithPatch() throws Exception {
        // Initialize the database
        fontFamilyRepository.saveAndFlush(fontFamily);

        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();

        // Update the fontFamily using partial update
        FontFamily partialUpdatedFontFamily = new FontFamily();
        partialUpdatedFontFamily.setId(fontFamily.getId());

        partialUpdatedFontFamily.name(UPDATED_NAME).url(UPDATED_URL).isActive(UPDATED_IS_ACTIVE);

        restFontFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFontFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFontFamily))
            )
            .andExpect(status().isOk());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
        FontFamily testFontFamily = fontFamilyList.get(fontFamilyList.size() - 1);
        assertThat(testFontFamily.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFontFamily.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFontFamily.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingFontFamily() throws Exception {
        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();
        fontFamily.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFontFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fontFamily.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fontFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFontFamily() throws Exception {
        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();
        fontFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFontFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fontFamily))
            )
            .andExpect(status().isBadRequest());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFontFamily() throws Exception {
        int databaseSizeBeforeUpdate = fontFamilyRepository.findAll().size();
        fontFamily.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFontFamilyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fontFamily))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FontFamily in the database
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFontFamily() throws Exception {
        // Initialize the database
        fontFamilyRepository.saveAndFlush(fontFamily);

        int databaseSizeBeforeDelete = fontFamilyRepository.findAll().size();

        // Delete the fontFamily
        restFontFamilyMockMvc
            .perform(delete(ENTITY_API_URL_ID, fontFamily.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FontFamily> fontFamilyList = fontFamilyRepository.findAll();
        assertThat(fontFamilyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
