package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.BooksVariables;
import com.mycompany.myapp.repository.BooksVariablesRepository;
import com.mycompany.myapp.service.criteria.BooksVariablesCriteria;
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
 * Integration tests for the {@link BooksVariablesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BooksVariablesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/books-variables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksVariablesRepository booksVariablesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksVariablesMockMvc;

    private BooksVariables booksVariables;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksVariables createEntity(EntityManager em) {
        BooksVariables booksVariables = new BooksVariables()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return booksVariables;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksVariables createUpdatedEntity(EntityManager em) {
        BooksVariables booksVariables = new BooksVariables()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return booksVariables;
    }

    @BeforeEach
    public void initTest() {
        booksVariables = createEntity(em);
    }

    @Test
    @Transactional
    void createBooksVariables() throws Exception {
        int databaseSizeBeforeCreate = booksVariablesRepository.findAll().size();
        // Create the BooksVariables
        restBooksVariablesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isCreated());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeCreate + 1);
        BooksVariables testBooksVariables = booksVariablesList.get(booksVariablesList.size() - 1);
        assertThat(testBooksVariables.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksVariables.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBooksVariables.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBooksVariablesWithExistingId() throws Exception {
        // Create the BooksVariables with an existing ID
        booksVariables.setId(1L);

        int databaseSizeBeforeCreate = booksVariablesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksVariablesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBooksVariables() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList
        restBooksVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksVariables.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getBooksVariables() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get the booksVariables
        restBooksVariablesMockMvc
            .perform(get(ENTITY_API_URL_ID, booksVariables.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booksVariables.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBooksVariablesByIdFiltering() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        Long id = booksVariables.getId();

        defaultBooksVariablesShouldBeFound("id.equals=" + id);
        defaultBooksVariablesShouldNotBeFound("id.notEquals=" + id);

        defaultBooksVariablesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksVariablesShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksVariablesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksVariablesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where code equals to DEFAULT_CODE
        defaultBooksVariablesShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the booksVariablesList where code equals to UPDATED_CODE
        defaultBooksVariablesShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBooksVariablesShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the booksVariablesList where code equals to UPDATED_CODE
        defaultBooksVariablesShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where code is not null
        defaultBooksVariablesShouldBeFound("code.specified=true");

        // Get all the booksVariablesList where code is null
        defaultBooksVariablesShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksVariablesByCodeContainsSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where code contains DEFAULT_CODE
        defaultBooksVariablesShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the booksVariablesList where code contains UPDATED_CODE
        defaultBooksVariablesShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where code does not contain DEFAULT_CODE
        defaultBooksVariablesShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the booksVariablesList where code does not contain UPDATED_CODE
        defaultBooksVariablesShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where description equals to DEFAULT_DESCRIPTION
        defaultBooksVariablesShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the booksVariablesList where description equals to UPDATED_DESCRIPTION
        defaultBooksVariablesShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBooksVariablesShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the booksVariablesList where description equals to UPDATED_DESCRIPTION
        defaultBooksVariablesShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where description is not null
        defaultBooksVariablesShouldBeFound("description.specified=true");

        // Get all the booksVariablesList where description is null
        defaultBooksVariablesShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksVariablesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where description contains DEFAULT_DESCRIPTION
        defaultBooksVariablesShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the booksVariablesList where description contains UPDATED_DESCRIPTION
        defaultBooksVariablesShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where description does not contain DEFAULT_DESCRIPTION
        defaultBooksVariablesShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the booksVariablesList where description does not contain UPDATED_DESCRIPTION
        defaultBooksVariablesShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksVariablesShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksVariablesList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksVariablesShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksVariablesShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksVariablesList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksVariablesShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksVariablesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        // Get all the booksVariablesList where isActive is not null
        defaultBooksVariablesShouldBeFound("isActive.specified=true");

        // Get all the booksVariablesList where isActive is null
        defaultBooksVariablesShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksVariablesByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            booksVariablesRepository.saveAndFlush(booksVariables);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        booksVariables.addBooks(books);
        booksVariablesRepository.saveAndFlush(booksVariables);
        Long booksId = books.getId();

        // Get all the booksVariablesList where books equals to booksId
        defaultBooksVariablesShouldBeFound("booksId.equals=" + booksId);

        // Get all the booksVariablesList where books equals to (booksId + 1)
        defaultBooksVariablesShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksVariablesShouldBeFound(String filter) throws Exception {
        restBooksVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksVariables.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBooksVariablesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksVariablesShouldNotBeFound(String filter) throws Exception {
        restBooksVariablesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksVariablesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooksVariables() throws Exception {
        // Get the booksVariables
        restBooksVariablesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooksVariables() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();

        // Update the booksVariables
        BooksVariables updatedBooksVariables = booksVariablesRepository.findById(booksVariables.getId()).get();
        // Disconnect from session so that the updates on updatedBooksVariables are not directly saved in db
        em.detach(updatedBooksVariables);
        updatedBooksVariables.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooksVariables.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooksVariables))
            )
            .andExpect(status().isOk());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
        BooksVariables testBooksVariables = booksVariablesList.get(booksVariablesList.size() - 1);
        assertThat(testBooksVariables.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksVariables.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksVariables.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBooksVariables() throws Exception {
        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();
        booksVariables.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, booksVariables.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooksVariables() throws Exception {
        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();
        booksVariables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksVariablesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooksVariables() throws Exception {
        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();
        booksVariables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksVariablesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksVariables)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksVariablesWithPatch() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();

        // Update the booksVariables using partial update
        BooksVariables partialUpdatedBooksVariables = new BooksVariables();
        partialUpdatedBooksVariables.setId(booksVariables.getId());

        partialUpdatedBooksVariables.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restBooksVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksVariables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksVariables))
            )
            .andExpect(status().isOk());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
        BooksVariables testBooksVariables = booksVariablesList.get(booksVariablesList.size() - 1);
        assertThat(testBooksVariables.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksVariables.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksVariables.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBooksVariablesWithPatch() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();

        // Update the booksVariables using partial update
        BooksVariables partialUpdatedBooksVariables = new BooksVariables();
        partialUpdatedBooksVariables.setId(booksVariables.getId());

        partialUpdatedBooksVariables.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksVariables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksVariables))
            )
            .andExpect(status().isOk());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
        BooksVariables testBooksVariables = booksVariablesList.get(booksVariablesList.size() - 1);
        assertThat(testBooksVariables.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksVariables.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksVariables.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBooksVariables() throws Exception {
        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();
        booksVariables.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, booksVariables.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooksVariables() throws Exception {
        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();
        booksVariables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooksVariables() throws Exception {
        int databaseSizeBeforeUpdate = booksVariablesRepository.findAll().size();
        booksVariables.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksVariablesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(booksVariables))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksVariables in the database
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooksVariables() throws Exception {
        // Initialize the database
        booksVariablesRepository.saveAndFlush(booksVariables);

        int databaseSizeBeforeDelete = booksVariablesRepository.findAll().size();

        // Delete the booksVariables
        restBooksVariablesMockMvc
            .perform(delete(ENTITY_API_URL_ID, booksVariables.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BooksVariables> booksVariablesList = booksVariablesRepository.findAll();
        assertThat(booksVariablesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
