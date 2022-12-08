package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.BooksRelatedOption;
import com.mycompany.myapp.domain.BooksRelatedOptionDetails;
import com.mycompany.myapp.repository.BooksRelatedOptionRepository;
import com.mycompany.myapp.service.BooksRelatedOptionService;
import com.mycompany.myapp.service.criteria.BooksRelatedOptionCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BooksRelatedOptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BooksRelatedOptionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/books-related-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksRelatedOptionRepository booksRelatedOptionRepository;

    @Mock
    private BooksRelatedOptionRepository booksRelatedOptionRepositoryMock;

    @Mock
    private BooksRelatedOptionService booksRelatedOptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksRelatedOptionMockMvc;

    private BooksRelatedOption booksRelatedOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksRelatedOption createEntity(EntityManager em) {
        BooksRelatedOption booksRelatedOption = new BooksRelatedOption().code(DEFAULT_CODE).name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return booksRelatedOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksRelatedOption createUpdatedEntity(EntityManager em) {
        BooksRelatedOption booksRelatedOption = new BooksRelatedOption().code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        return booksRelatedOption;
    }

    @BeforeEach
    public void initTest() {
        booksRelatedOption = createEntity(em);
    }

    @Test
    @Transactional
    void createBooksRelatedOption() throws Exception {
        int databaseSizeBeforeCreate = booksRelatedOptionRepository.findAll().size();
        // Create the BooksRelatedOption
        restBooksRelatedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isCreated());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeCreate + 1);
        BooksRelatedOption testBooksRelatedOption = booksRelatedOptionList.get(booksRelatedOptionList.size() - 1);
        assertThat(testBooksRelatedOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksRelatedOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBooksRelatedOption.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBooksRelatedOptionWithExistingId() throws Exception {
        // Create the BooksRelatedOption with an existing ID
        booksRelatedOption.setId(1L);

        int databaseSizeBeforeCreate = booksRelatedOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksRelatedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = booksRelatedOptionRepository.findAll().size();
        // set the field null
        booksRelatedOption.setName(null);

        // Create the BooksRelatedOption, which fails.

        restBooksRelatedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isBadRequest());

        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptions() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList
        restBooksRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksRelatedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksRelatedOptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(booksRelatedOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBooksRelatedOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(booksRelatedOptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksRelatedOptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(booksRelatedOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBooksRelatedOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(booksRelatedOptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBooksRelatedOption() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get the booksRelatedOption
        restBooksRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, booksRelatedOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booksRelatedOption.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBooksRelatedOptionsByIdFiltering() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        Long id = booksRelatedOption.getId();

        defaultBooksRelatedOptionShouldBeFound("id.equals=" + id);
        defaultBooksRelatedOptionShouldNotBeFound("id.notEquals=" + id);

        defaultBooksRelatedOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksRelatedOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksRelatedOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksRelatedOptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where code equals to DEFAULT_CODE
        defaultBooksRelatedOptionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the booksRelatedOptionList where code equals to UPDATED_CODE
        defaultBooksRelatedOptionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBooksRelatedOptionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the booksRelatedOptionList where code equals to UPDATED_CODE
        defaultBooksRelatedOptionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where code is not null
        defaultBooksRelatedOptionShouldBeFound("code.specified=true");

        // Get all the booksRelatedOptionList where code is null
        defaultBooksRelatedOptionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where code contains DEFAULT_CODE
        defaultBooksRelatedOptionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the booksRelatedOptionList where code contains UPDATED_CODE
        defaultBooksRelatedOptionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where code does not contain DEFAULT_CODE
        defaultBooksRelatedOptionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the booksRelatedOptionList where code does not contain UPDATED_CODE
        defaultBooksRelatedOptionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where name equals to DEFAULT_NAME
        defaultBooksRelatedOptionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the booksRelatedOptionList where name equals to UPDATED_NAME
        defaultBooksRelatedOptionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBooksRelatedOptionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the booksRelatedOptionList where name equals to UPDATED_NAME
        defaultBooksRelatedOptionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where name is not null
        defaultBooksRelatedOptionShouldBeFound("name.specified=true");

        // Get all the booksRelatedOptionList where name is null
        defaultBooksRelatedOptionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where name contains DEFAULT_NAME
        defaultBooksRelatedOptionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the booksRelatedOptionList where name contains UPDATED_NAME
        defaultBooksRelatedOptionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where name does not contain DEFAULT_NAME
        defaultBooksRelatedOptionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the booksRelatedOptionList where name does not contain UPDATED_NAME
        defaultBooksRelatedOptionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksRelatedOptionShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksRelatedOptionList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksRelatedOptionShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksRelatedOptionShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksRelatedOptionList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksRelatedOptionShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        // Get all the booksRelatedOptionList where isActive is not null
        defaultBooksRelatedOptionShouldBeFound("isActive.specified=true");

        // Get all the booksRelatedOptionList where isActive is null
        defaultBooksRelatedOptionShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByBooksRelatedOptionDetailsIsEqualToSomething() throws Exception {
        BooksRelatedOptionDetails booksRelatedOptionDetails;
        if (TestUtil.findAll(em, BooksRelatedOptionDetails.class).isEmpty()) {
            booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);
            booksRelatedOptionDetails = BooksRelatedOptionDetailsResourceIT.createEntity(em);
        } else {
            booksRelatedOptionDetails = TestUtil.findAll(em, BooksRelatedOptionDetails.class).get(0);
        }
        em.persist(booksRelatedOptionDetails);
        em.flush();
        booksRelatedOption.addBooksRelatedOptionDetails(booksRelatedOptionDetails);
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);
        Long booksRelatedOptionDetailsId = booksRelatedOptionDetails.getId();

        // Get all the booksRelatedOptionList where booksRelatedOptionDetails equals to booksRelatedOptionDetailsId
        defaultBooksRelatedOptionShouldBeFound("booksRelatedOptionDetailsId.equals=" + booksRelatedOptionDetailsId);

        // Get all the booksRelatedOptionList where booksRelatedOptionDetails equals to (booksRelatedOptionDetailsId + 1)
        defaultBooksRelatedOptionShouldNotBeFound("booksRelatedOptionDetailsId.equals=" + (booksRelatedOptionDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllBooksRelatedOptionsByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        booksRelatedOption.addBooks(books);
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);
        Long booksId = books.getId();

        // Get all the booksRelatedOptionList where books equals to booksId
        defaultBooksRelatedOptionShouldBeFound("booksId.equals=" + booksId);

        // Get all the booksRelatedOptionList where books equals to (booksId + 1)
        defaultBooksRelatedOptionShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksRelatedOptionShouldBeFound(String filter) throws Exception {
        restBooksRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksRelatedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBooksRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksRelatedOptionShouldNotBeFound(String filter) throws Exception {
        restBooksRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooksRelatedOption() throws Exception {
        // Get the booksRelatedOption
        restBooksRelatedOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooksRelatedOption() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();

        // Update the booksRelatedOption
        BooksRelatedOption updatedBooksRelatedOption = booksRelatedOptionRepository.findById(booksRelatedOption.getId()).get();
        // Disconnect from session so that the updates on updatedBooksRelatedOption are not directly saved in db
        em.detach(updatedBooksRelatedOption);
        updatedBooksRelatedOption.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restBooksRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooksRelatedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooksRelatedOption))
            )
            .andExpect(status().isOk());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
        BooksRelatedOption testBooksRelatedOption = booksRelatedOptionList.get(booksRelatedOptionList.size() - 1);
        assertThat(testBooksRelatedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksRelatedOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBooksRelatedOption.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBooksRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();
        booksRelatedOption.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, booksRelatedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooksRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();
        booksRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooksRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();
        booksRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksRelatedOptionWithPatch() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();

        // Update the booksRelatedOption using partial update
        BooksRelatedOption partialUpdatedBooksRelatedOption = new BooksRelatedOption();
        partialUpdatedBooksRelatedOption.setId(booksRelatedOption.getId());

        partialUpdatedBooksRelatedOption.name(UPDATED_NAME);

        restBooksRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksRelatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksRelatedOption))
            )
            .andExpect(status().isOk());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
        BooksRelatedOption testBooksRelatedOption = booksRelatedOptionList.get(booksRelatedOptionList.size() - 1);
        assertThat(testBooksRelatedOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooksRelatedOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBooksRelatedOption.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBooksRelatedOptionWithPatch() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();

        // Update the booksRelatedOption using partial update
        BooksRelatedOption partialUpdatedBooksRelatedOption = new BooksRelatedOption();
        partialUpdatedBooksRelatedOption.setId(booksRelatedOption.getId());

        partialUpdatedBooksRelatedOption.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restBooksRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksRelatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksRelatedOption))
            )
            .andExpect(status().isOk());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
        BooksRelatedOption testBooksRelatedOption = booksRelatedOptionList.get(booksRelatedOptionList.size() - 1);
        assertThat(testBooksRelatedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooksRelatedOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBooksRelatedOption.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBooksRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();
        booksRelatedOption.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, booksRelatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooksRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();
        booksRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooksRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = booksRelatedOptionRepository.findAll().size();
        booksRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksRelatedOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksRelatedOption in the database
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooksRelatedOption() throws Exception {
        // Initialize the database
        booksRelatedOptionRepository.saveAndFlush(booksRelatedOption);

        int databaseSizeBeforeDelete = booksRelatedOptionRepository.findAll().size();

        // Delete the booksRelatedOption
        restBooksRelatedOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, booksRelatedOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BooksRelatedOption> booksRelatedOptionList = booksRelatedOptionRepository.findAll();
        assertThat(booksRelatedOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
