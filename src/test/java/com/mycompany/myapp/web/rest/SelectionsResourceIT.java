package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Selections;
import com.mycompany.myapp.repository.SelectionsRepository;
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
 * Integration tests for the {@link SelectionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SelectionsResourceIT {

    private static final String DEFAULT_AVATAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STYLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STYLE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final String DEFAULT_AVATAR_ATTRIBUTES_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_ATTRIBUTES_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/selections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SelectionsRepository selectionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSelectionsMockMvc;

    private Selections selections;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Selections createEntity(EntityManager em) {
        Selections selections = new Selections()
            .avatarCode(DEFAULT_AVATAR_CODE)
            .styleCode(DEFAULT_STYLE_CODE)
            .optionCode(DEFAULT_OPTION_CODE)
            .image(DEFAULT_IMAGE)
            .height(DEFAULT_HEIGHT)
            .x(DEFAULT_X)
            .y(DEFAULT_Y)
            .isActive(DEFAULT_IS_ACTIVE)
            .width(DEFAULT_WIDTH)
            .avatarAttributesCode(DEFAULT_AVATAR_ATTRIBUTES_CODE);
        return selections;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Selections createUpdatedEntity(EntityManager em) {
        Selections selections = new Selections()
            .avatarCode(UPDATED_AVATAR_CODE)
            .styleCode(UPDATED_STYLE_CODE)
            .optionCode(UPDATED_OPTION_CODE)
            .image(UPDATED_IMAGE)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .avatarAttributesCode(UPDATED_AVATAR_ATTRIBUTES_CODE);
        return selections;
    }

    @BeforeEach
    public void initTest() {
        selections = createEntity(em);
    }

    @Test
    @Transactional
    void createSelections() throws Exception {
        int databaseSizeBeforeCreate = selectionsRepository.findAll().size();
        // Create the Selections
        restSelectionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(selections)))
            .andExpect(status().isCreated());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeCreate + 1);
        Selections testSelections = selectionsList.get(selectionsList.size() - 1);
        assertThat(testSelections.getAvatarCode()).isEqualTo(DEFAULT_AVATAR_CODE);
        assertThat(testSelections.getStyleCode()).isEqualTo(DEFAULT_STYLE_CODE);
        assertThat(testSelections.getOptionCode()).isEqualTo(DEFAULT_OPTION_CODE);
        assertThat(testSelections.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSelections.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testSelections.getX()).isEqualTo(DEFAULT_X);
        assertThat(testSelections.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testSelections.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSelections.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testSelections.getAvatarAttributesCode()).isEqualTo(DEFAULT_AVATAR_ATTRIBUTES_CODE);
    }

    @Test
    @Transactional
    void createSelectionsWithExistingId() throws Exception {
        // Create the Selections with an existing ID
        selections.setId(1L);

        int databaseSizeBeforeCreate = selectionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSelectionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(selections)))
            .andExpect(status().isBadRequest());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSelections() throws Exception {
        // Initialize the database
        selectionsRepository.saveAndFlush(selections);

        // Get all the selectionsList
        restSelectionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selections.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarCode").value(hasItem(DEFAULT_AVATAR_CODE)))
            .andExpect(jsonPath("$.[*].styleCode").value(hasItem(DEFAULT_STYLE_CODE)))
            .andExpect(jsonPath("$.[*].optionCode").value(hasItem(DEFAULT_OPTION_CODE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].avatarAttributesCode").value(hasItem(DEFAULT_AVATAR_ATTRIBUTES_CODE)));
    }

    @Test
    @Transactional
    void getSelections() throws Exception {
        // Initialize the database
        selectionsRepository.saveAndFlush(selections);

        // Get the selections
        restSelectionsMockMvc
            .perform(get(ENTITY_API_URL_ID, selections.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(selections.getId().intValue()))
            .andExpect(jsonPath("$.avatarCode").value(DEFAULT_AVATAR_CODE))
            .andExpect(jsonPath("$.styleCode").value(DEFAULT_STYLE_CODE))
            .andExpect(jsonPath("$.optionCode").value(DEFAULT_OPTION_CODE))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.avatarAttributesCode").value(DEFAULT_AVATAR_ATTRIBUTES_CODE));
    }

    @Test
    @Transactional
    void getNonExistingSelections() throws Exception {
        // Get the selections
        restSelectionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSelections() throws Exception {
        // Initialize the database
        selectionsRepository.saveAndFlush(selections);

        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();

        // Update the selections
        Selections updatedSelections = selectionsRepository.findById(selections.getId()).get();
        // Disconnect from session so that the updates on updatedSelections are not directly saved in db
        em.detach(updatedSelections);
        updatedSelections
            .avatarCode(UPDATED_AVATAR_CODE)
            .styleCode(UPDATED_STYLE_CODE)
            .optionCode(UPDATED_OPTION_CODE)
            .image(UPDATED_IMAGE)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .avatarAttributesCode(UPDATED_AVATAR_ATTRIBUTES_CODE);

        restSelectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSelections.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSelections))
            )
            .andExpect(status().isOk());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
        Selections testSelections = selectionsList.get(selectionsList.size() - 1);
        assertThat(testSelections.getAvatarCode()).isEqualTo(UPDATED_AVATAR_CODE);
        assertThat(testSelections.getStyleCode()).isEqualTo(UPDATED_STYLE_CODE);
        assertThat(testSelections.getOptionCode()).isEqualTo(UPDATED_OPTION_CODE);
        assertThat(testSelections.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSelections.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testSelections.getX()).isEqualTo(UPDATED_X);
        assertThat(testSelections.getY()).isEqualTo(UPDATED_Y);
        assertThat(testSelections.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSelections.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testSelections.getAvatarAttributesCode()).isEqualTo(UPDATED_AVATAR_ATTRIBUTES_CODE);
    }

    @Test
    @Transactional
    void putNonExistingSelections() throws Exception {
        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();
        selections.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, selections.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSelections() throws Exception {
        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();
        selections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSelections() throws Exception {
        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();
        selections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(selections)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSelectionsWithPatch() throws Exception {
        // Initialize the database
        selectionsRepository.saveAndFlush(selections);

        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();

        // Update the selections using partial update
        Selections partialUpdatedSelections = new Selections();
        partialUpdatedSelections.setId(selections.getId());

        partialUpdatedSelections
            .avatarCode(UPDATED_AVATAR_CODE)
            .optionCode(UPDATED_OPTION_CODE)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH);

        restSelectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSelections))
            )
            .andExpect(status().isOk());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
        Selections testSelections = selectionsList.get(selectionsList.size() - 1);
        assertThat(testSelections.getAvatarCode()).isEqualTo(UPDATED_AVATAR_CODE);
        assertThat(testSelections.getStyleCode()).isEqualTo(DEFAULT_STYLE_CODE);
        assertThat(testSelections.getOptionCode()).isEqualTo(UPDATED_OPTION_CODE);
        assertThat(testSelections.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSelections.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testSelections.getX()).isEqualTo(UPDATED_X);
        assertThat(testSelections.getY()).isEqualTo(UPDATED_Y);
        assertThat(testSelections.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSelections.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testSelections.getAvatarAttributesCode()).isEqualTo(DEFAULT_AVATAR_ATTRIBUTES_CODE);
    }

    @Test
    @Transactional
    void fullUpdateSelectionsWithPatch() throws Exception {
        // Initialize the database
        selectionsRepository.saveAndFlush(selections);

        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();

        // Update the selections using partial update
        Selections partialUpdatedSelections = new Selections();
        partialUpdatedSelections.setId(selections.getId());

        partialUpdatedSelections
            .avatarCode(UPDATED_AVATAR_CODE)
            .styleCode(UPDATED_STYLE_CODE)
            .optionCode(UPDATED_OPTION_CODE)
            .image(UPDATED_IMAGE)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .avatarAttributesCode(UPDATED_AVATAR_ATTRIBUTES_CODE);

        restSelectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSelections))
            )
            .andExpect(status().isOk());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
        Selections testSelections = selectionsList.get(selectionsList.size() - 1);
        assertThat(testSelections.getAvatarCode()).isEqualTo(UPDATED_AVATAR_CODE);
        assertThat(testSelections.getStyleCode()).isEqualTo(UPDATED_STYLE_CODE);
        assertThat(testSelections.getOptionCode()).isEqualTo(UPDATED_OPTION_CODE);
        assertThat(testSelections.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSelections.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testSelections.getX()).isEqualTo(UPDATED_X);
        assertThat(testSelections.getY()).isEqualTo(UPDATED_Y);
        assertThat(testSelections.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSelections.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testSelections.getAvatarAttributesCode()).isEqualTo(UPDATED_AVATAR_ATTRIBUTES_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingSelections() throws Exception {
        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();
        selections.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, selections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSelections() throws Exception {
        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();
        selections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSelections() throws Exception {
        int databaseSizeBeforeUpdate = selectionsRepository.findAll().size();
        selections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(selections))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Selections in the database
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSelections() throws Exception {
        // Initialize the database
        selectionsRepository.saveAndFlush(selections);

        int databaseSizeBeforeDelete = selectionsRepository.findAll().size();

        // Delete the selections
        restSelectionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, selections.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Selections> selectionsList = selectionsRepository.findAll();
        assertThat(selectionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
