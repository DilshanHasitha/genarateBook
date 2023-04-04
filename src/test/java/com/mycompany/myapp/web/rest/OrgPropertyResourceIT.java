package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OrgProperty;
import com.mycompany.myapp.repository.OrgPropertyRepository;
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
 * Integration tests for the {@link OrgPropertyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrgPropertyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/org-properties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrgPropertyRepository orgPropertyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrgPropertyMockMvc;

    private OrgProperty orgProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgProperty createEntity(EntityManager em) {
        OrgProperty orgProperty = new OrgProperty().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).isActive(DEFAULT_IS_ACTIVE);
        return orgProperty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrgProperty createUpdatedEntity(EntityManager em) {
        OrgProperty orgProperty = new OrgProperty().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);
        return orgProperty;
    }

    @BeforeEach
    public void initTest() {
        orgProperty = createEntity(em);
    }

    @Test
    @Transactional
    void createOrgProperty() throws Exception {
        int databaseSizeBeforeCreate = orgPropertyRepository.findAll().size();
        // Create the OrgProperty
        restOrgPropertyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgProperty)))
            .andExpect(status().isCreated());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        OrgProperty testOrgProperty = orgPropertyList.get(orgPropertyList.size() - 1);
        assertThat(testOrgProperty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrgProperty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrgProperty.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createOrgPropertyWithExistingId() throws Exception {
        // Create the OrgProperty with an existing ID
        orgProperty.setId(1L);

        int databaseSizeBeforeCreate = orgPropertyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrgPropertyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgProperty)))
            .andExpect(status().isBadRequest());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrgProperties() throws Exception {
        // Initialize the database
        orgPropertyRepository.saveAndFlush(orgProperty);

        // Get all the orgPropertyList
        restOrgPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orgProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getOrgProperty() throws Exception {
        // Initialize the database
        orgPropertyRepository.saveAndFlush(orgProperty);

        // Get the orgProperty
        restOrgPropertyMockMvc
            .perform(get(ENTITY_API_URL_ID, orgProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orgProperty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrgProperty() throws Exception {
        // Get the orgProperty
        restOrgPropertyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrgProperty() throws Exception {
        // Initialize the database
        orgPropertyRepository.saveAndFlush(orgProperty);

        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();

        // Update the orgProperty
        OrgProperty updatedOrgProperty = orgPropertyRepository.findById(orgProperty.getId()).get();
        // Disconnect from session so that the updates on updatedOrgProperty are not directly saved in db
        em.detach(updatedOrgProperty);
        updatedOrgProperty.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOrgPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrgProperty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrgProperty))
            )
            .andExpect(status().isOk());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
        OrgProperty testOrgProperty = orgPropertyList.get(orgPropertyList.size() - 1);
        assertThat(testOrgProperty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgProperty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrgProperty.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingOrgProperty() throws Exception {
        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();
        orgProperty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orgProperty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrgProperty() throws Exception {
        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();
        orgProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orgProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrgProperty() throws Exception {
        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();
        orgProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPropertyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orgProperty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrgPropertyWithPatch() throws Exception {
        // Initialize the database
        orgPropertyRepository.saveAndFlush(orgProperty);

        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();

        // Update the orgProperty using partial update
        OrgProperty partialUpdatedOrgProperty = new OrgProperty();
        partialUpdatedOrgProperty.setId(orgProperty.getId());

        partialUpdatedOrgProperty.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOrgPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgProperty))
            )
            .andExpect(status().isOk());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
        OrgProperty testOrgProperty = orgPropertyList.get(orgPropertyList.size() - 1);
        assertThat(testOrgProperty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgProperty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrgProperty.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateOrgPropertyWithPatch() throws Exception {
        // Initialize the database
        orgPropertyRepository.saveAndFlush(orgProperty);

        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();

        // Update the orgProperty using partial update
        OrgProperty partialUpdatedOrgProperty = new OrgProperty();
        partialUpdatedOrgProperty.setId(orgProperty.getId());

        partialUpdatedOrgProperty.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOrgPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrgProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrgProperty))
            )
            .andExpect(status().isOk());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
        OrgProperty testOrgProperty = orgPropertyList.get(orgPropertyList.size() - 1);
        assertThat(testOrgProperty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrgProperty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrgProperty.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingOrgProperty() throws Exception {
        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();
        orgProperty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrgPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orgProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrgProperty() throws Exception {
        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();
        orgProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orgProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrgProperty() throws Exception {
        int databaseSizeBeforeUpdate = orgPropertyRepository.findAll().size();
        orgProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrgPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orgProperty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrgProperty in the database
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrgProperty() throws Exception {
        // Initialize the database
        orgPropertyRepository.saveAndFlush(orgProperty);

        int databaseSizeBeforeDelete = orgPropertyRepository.findAll().size();

        // Delete the orgProperty
        restOrgPropertyMockMvc
            .perform(delete(ENTITY_API_URL_ID, orgProperty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrgProperty> orgPropertyList = orgPropertyRepository.findAll();
        assertThat(orgPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
