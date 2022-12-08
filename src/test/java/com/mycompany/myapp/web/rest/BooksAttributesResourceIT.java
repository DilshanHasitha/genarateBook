package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.BooksAttributes;
import com.mycompany.myapp.repository.BooksAttributesRepository;
import com.mycompany.myapp.service.criteria.BooksAttributesCriteria;
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
 * Integration tests for the {@link BooksAttributesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BooksAttributesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/books-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksAttributesRepository booksAttributesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksAttributesMockMvc;

    private BooksAttributes booksAttributes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksAttributes createEntity(EntityManager em) {
        BooksAttributes booksAttributes = new BooksAttributes()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return booksAttributes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksAttributes createUpdatedEntity(EntityManager em) {
        BooksAttributes booksAttributes = new BooksAttributes()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return booksAttributes;
    }

    @BeforeEach
    public void initTest() {
        booksAttributes = createEntity(em);
    }

    @Test
    @Transactional
    void createBooksAttributes() throws Exception {
        int databaseSizeBeforeCreate = booksAttributesRepository.findAll().size();
        // Create the BooksAttributes
        restBooksAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isCreated());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeCreate + 1);
        BooksAttributes testBooksAttributes = booksAttributesList.get(booksAttributesList.size() - 1);
        assertThat(testBooksAttributes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksAttributes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBooksAttributes.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBooksAttributesWithExistingId() throws Exception {
        // Create the BooksAttributes with an existing ID
        booksAttributes.setId(1L);

        int databaseSizeBeforeCreate = booksAttributesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBooksAttributes() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList
        restBooksAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getBooksAttributes() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get the booksAttributes
        restBooksAttributesMockMvc
            .perform(get(ENTITY_API_URL_ID, booksAttributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booksAttributes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBooksAttributesByIdFiltering() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        Long id = booksAttributes.getId();

        defaultBooksAttributesShouldBeFound("id.equals=" + id);
        defaultBooksAttributesShouldNotBeFound("id.notEquals=" + id);

        defaultBooksAttributesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksAttributesShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksAttributesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksAttributesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where code equals to DEFAULT_CODE
        defaultBooksAttributesShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the booksAttributesList where code equals to UPDATED_CODE
        defaultBooksAttributesShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBooksAttributesShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the booksAttributesList where code equals to UPDATED_CODE
        defaultBooksAttributesShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where code is not null
        defaultBooksAttributesShouldBeFound("code.specified=true");

        // Get all the booksAttributesList where code is null
        defaultBooksAttributesShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksAttributesByCodeContainsSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where code contains DEFAULT_CODE
        defaultBooksAttributesShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the booksAttributesList where code contains UPDATED_CODE
        defaultBooksAttributesShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where code does not contain DEFAULT_CODE
        defaultBooksAttributesShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the booksAttributesList where code does not contain UPDATED_CODE
        defaultBooksAttributesShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where description equals to DEFAULT_DESCRIPTION
        defaultBooksAttributesShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the booksAttributesList where description equals to UPDATED_DESCRIPTION
        defaultBooksAttributesShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBooksAttributesShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the booksAttributesList where description equals to UPDATED_DESCRIPTION
        defaultBooksAttributesShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where description is not null
        defaultBooksAttributesShouldBeFound("description.specified=true");

        // Get all the booksAttributesList where description is null
        defaultBooksAttributesShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksAttributesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where description contains DEFAULT_DESCRIPTION
        defaultBooksAttributesShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the booksAttributesList where description contains UPDATED_DESCRIPTION
        defaultBooksAttributesShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where description does not contain DEFAULT_DESCRIPTION
        defaultBooksAttributesShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the booksAttributesList where description does not contain UPDATED_DESCRIPTION
        defaultBooksAttributesShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksAttributesShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksAttributesList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksAttributesShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksAttributesShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksAttributesList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksAttributesShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksAttributesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        // Get all the booksAttributesList where isActive is not null
        defaultBooksAttributesShouldBeFound("isActive.specified=true");

        // Get all the booksAttributesList where isActive is null
        defaultBooksAttributesShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksAttributesByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            booksAttributesRepository.saveAndFlush(booksAttributes);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        booksAttributes.addBooks(books);
        booksAttributesRepository.saveAndFlush(booksAttributes);
        Long booksId = books.getId();

        // Get all the booksAttributesList where books equals to booksId
        defaultBooksAttributesShouldBeFound("booksId.equals=" + booksId);

        // Get all the booksAttributesList where books equals to (booksId + 1)
        defaultBooksAttributesShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksAttributesShouldBeFound(String filter) throws Exception {
        restBooksAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBooksAttributesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksAttributesShouldNotBeFound(String filter) throws Exception {
        restBooksAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksAttributesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooksAttributes() throws Exception {
        // Get the booksAttributes
        restBooksAttributesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooksAttributes() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();

        // Update the booksAttributes
        BooksAttributes updatedBooksAttributes = booksAttributesRepository.findById(booksAttributes.getId()).get();
        // Disconnect from session so that the updates on updatedBooksAttributes are not directly saved in db
        em.detach(updatedBooksAttributes);
        updatedBooksAttributes.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooksAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooksAttributes))
            )
            .andExpect(status().isOk());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
        BooksAttributes testBooksAttributes = booksAttributesList.get(booksAttributesList.size() - 1);
        assertThat(testBooksAttributes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksAttributes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksAttributes.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBooksAttributes() throws Exception {
        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();
        booksAttributes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, booksAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooksAttributes() throws Exception {
        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();
        booksAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooksAttributes() throws Exception {
        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();
        booksAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksAttributesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksAttributesWithPatch() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();

        // Update the booksAttributes using partial update
        BooksAttributes partialUpdatedBooksAttributes = new BooksAttributes();
        partialUpdatedBooksAttributes.setId(booksAttributes.getId());

        partialUpdatedBooksAttributes.description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksAttributes))
            )
            .andExpect(status().isOk());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
        BooksAttributes testBooksAttributes = booksAttributesList.get(booksAttributesList.size() - 1);
        assertThat(testBooksAttributes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksAttributes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksAttributes.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBooksAttributesWithPatch() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();

        // Update the booksAttributes using partial update
        BooksAttributes partialUpdatedBooksAttributes = new BooksAttributes();
        partialUpdatedBooksAttributes.setId(booksAttributes.getId());

        partialUpdatedBooksAttributes.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restBooksAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksAttributes))
            )
            .andExpect(status().isOk());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
        BooksAttributes testBooksAttributes = booksAttributesList.get(booksAttributesList.size() - 1);
        assertThat(testBooksAttributes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksAttributes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooksAttributes.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBooksAttributes() throws Exception {
        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();
        booksAttributes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, booksAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooksAttributes() throws Exception {
        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();
        booksAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooksAttributes() throws Exception {
        int databaseSizeBeforeUpdate = booksAttributesRepository.findAll().size();
        booksAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksAttributes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksAttributes in the database
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooksAttributes() throws Exception {
        // Initialize the database
        booksAttributesRepository.saveAndFlush(booksAttributes);

        int databaseSizeBeforeDelete = booksAttributesRepository.findAll().size();

        // Delete the booksAttributes
        restBooksAttributesMockMvc
            .perform(delete(ENTITY_API_URL_ID, booksAttributes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BooksAttributes> booksAttributesList = booksAttributesRepository.findAll();
        assertThat(booksAttributesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
