package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.OptionType;
import com.mycompany.myapp.domain.PriceRelatedOption;
import com.mycompany.myapp.domain.PriceRelatedOptionDetails;
import com.mycompany.myapp.repository.PriceRelatedOptionRepository;
import com.mycompany.myapp.service.PriceRelatedOptionService;
import com.mycompany.myapp.service.criteria.PriceRelatedOptionCriteria;
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
 * Integration tests for the {@link PriceRelatedOptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PriceRelatedOptionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/price-related-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceRelatedOptionRepository priceRelatedOptionRepository;

    @Mock
    private PriceRelatedOptionRepository priceRelatedOptionRepositoryMock;

    @Mock
    private PriceRelatedOptionService priceRelatedOptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceRelatedOptionMockMvc;

    private PriceRelatedOption priceRelatedOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceRelatedOption createEntity(EntityManager em) {
        PriceRelatedOption priceRelatedOption = new PriceRelatedOption().code(DEFAULT_CODE).name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return priceRelatedOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceRelatedOption createUpdatedEntity(EntityManager em) {
        PriceRelatedOption priceRelatedOption = new PriceRelatedOption().code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        return priceRelatedOption;
    }

    @BeforeEach
    public void initTest() {
        priceRelatedOption = createEntity(em);
    }

    @Test
    @Transactional
    void createPriceRelatedOption() throws Exception {
        int databaseSizeBeforeCreate = priceRelatedOptionRepository.findAll().size();
        // Create the PriceRelatedOption
        restPriceRelatedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isCreated());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeCreate + 1);
        PriceRelatedOption testPriceRelatedOption = priceRelatedOptionList.get(priceRelatedOptionList.size() - 1);
        assertThat(testPriceRelatedOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPriceRelatedOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPriceRelatedOption.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createPriceRelatedOptionWithExistingId() throws Exception {
        // Create the PriceRelatedOption with an existing ID
        priceRelatedOption.setId(1L);

        int databaseSizeBeforeCreate = priceRelatedOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceRelatedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRelatedOptionRepository.findAll().size();
        // set the field null
        priceRelatedOption.setName(null);

        // Create the PriceRelatedOption, which fails.

        restPriceRelatedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isBadRequest());

        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptions() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList
        restPriceRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceRelatedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPriceRelatedOptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(priceRelatedOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPriceRelatedOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(priceRelatedOptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPriceRelatedOptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(priceRelatedOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPriceRelatedOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(priceRelatedOptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPriceRelatedOption() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get the priceRelatedOption
        restPriceRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, priceRelatedOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceRelatedOption.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getPriceRelatedOptionsByIdFiltering() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        Long id = priceRelatedOption.getId();

        defaultPriceRelatedOptionShouldBeFound("id.equals=" + id);
        defaultPriceRelatedOptionShouldNotBeFound("id.notEquals=" + id);

        defaultPriceRelatedOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPriceRelatedOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultPriceRelatedOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPriceRelatedOptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where code equals to DEFAULT_CODE
        defaultPriceRelatedOptionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the priceRelatedOptionList where code equals to UPDATED_CODE
        defaultPriceRelatedOptionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPriceRelatedOptionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the priceRelatedOptionList where code equals to UPDATED_CODE
        defaultPriceRelatedOptionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where code is not null
        defaultPriceRelatedOptionShouldBeFound("code.specified=true");

        // Get all the priceRelatedOptionList where code is null
        defaultPriceRelatedOptionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where code contains DEFAULT_CODE
        defaultPriceRelatedOptionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the priceRelatedOptionList where code contains UPDATED_CODE
        defaultPriceRelatedOptionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where code does not contain DEFAULT_CODE
        defaultPriceRelatedOptionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the priceRelatedOptionList where code does not contain UPDATED_CODE
        defaultPriceRelatedOptionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where name equals to DEFAULT_NAME
        defaultPriceRelatedOptionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the priceRelatedOptionList where name equals to UPDATED_NAME
        defaultPriceRelatedOptionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPriceRelatedOptionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the priceRelatedOptionList where name equals to UPDATED_NAME
        defaultPriceRelatedOptionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where name is not null
        defaultPriceRelatedOptionShouldBeFound("name.specified=true");

        // Get all the priceRelatedOptionList where name is null
        defaultPriceRelatedOptionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where name contains DEFAULT_NAME
        defaultPriceRelatedOptionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the priceRelatedOptionList where name contains UPDATED_NAME
        defaultPriceRelatedOptionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where name does not contain DEFAULT_NAME
        defaultPriceRelatedOptionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the priceRelatedOptionList where name does not contain UPDATED_NAME
        defaultPriceRelatedOptionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPriceRelatedOptionShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the priceRelatedOptionList where isActive equals to UPDATED_IS_ACTIVE
        defaultPriceRelatedOptionShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPriceRelatedOptionShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the priceRelatedOptionList where isActive equals to UPDATED_IS_ACTIVE
        defaultPriceRelatedOptionShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        // Get all the priceRelatedOptionList where isActive is not null
        defaultPriceRelatedOptionShouldBeFound("isActive.specified=true");

        // Get all the priceRelatedOptionList where isActive is null
        defaultPriceRelatedOptionShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByOptionTypeIsEqualToSomething() throws Exception {
        OptionType optionType;
        if (TestUtil.findAll(em, OptionType.class).isEmpty()) {
            priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);
            optionType = OptionTypeResourceIT.createEntity(em);
        } else {
            optionType = TestUtil.findAll(em, OptionType.class).get(0);
        }
        em.persist(optionType);
        em.flush();
        priceRelatedOption.setOptionType(optionType);
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);
        Long optionTypeId = optionType.getId();

        // Get all the priceRelatedOptionList where optionType equals to optionTypeId
        defaultPriceRelatedOptionShouldBeFound("optionTypeId.equals=" + optionTypeId);

        // Get all the priceRelatedOptionList where optionType equals to (optionTypeId + 1)
        defaultPriceRelatedOptionShouldNotBeFound("optionTypeId.equals=" + (optionTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByPriceRelatedOptionDetailsIsEqualToSomething() throws Exception {
        PriceRelatedOptionDetails priceRelatedOptionDetails;
        if (TestUtil.findAll(em, PriceRelatedOptionDetails.class).isEmpty()) {
            priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);
            priceRelatedOptionDetails = PriceRelatedOptionDetailsResourceIT.createEntity(em);
        } else {
            priceRelatedOptionDetails = TestUtil.findAll(em, PriceRelatedOptionDetails.class).get(0);
        }
        em.persist(priceRelatedOptionDetails);
        em.flush();
        priceRelatedOption.addPriceRelatedOptionDetails(priceRelatedOptionDetails);
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);
        Long priceRelatedOptionDetailsId = priceRelatedOptionDetails.getId();

        // Get all the priceRelatedOptionList where priceRelatedOptionDetails equals to priceRelatedOptionDetailsId
        defaultPriceRelatedOptionShouldBeFound("priceRelatedOptionDetailsId.equals=" + priceRelatedOptionDetailsId);

        // Get all the priceRelatedOptionList where priceRelatedOptionDetails equals to (priceRelatedOptionDetailsId + 1)
        defaultPriceRelatedOptionShouldNotBeFound("priceRelatedOptionDetailsId.equals=" + (priceRelatedOptionDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllPriceRelatedOptionsByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        priceRelatedOption.addBooks(books);
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);
        Long booksId = books.getId();

        // Get all the priceRelatedOptionList where books equals to booksId
        defaultPriceRelatedOptionShouldBeFound("booksId.equals=" + booksId);

        // Get all the priceRelatedOptionList where books equals to (booksId + 1)
        defaultPriceRelatedOptionShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriceRelatedOptionShouldBeFound(String filter) throws Exception {
        restPriceRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceRelatedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPriceRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriceRelatedOptionShouldNotBeFound(String filter) throws Exception {
        restPriceRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriceRelatedOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPriceRelatedOption() throws Exception {
        // Get the priceRelatedOption
        restPriceRelatedOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPriceRelatedOption() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();

        // Update the priceRelatedOption
        PriceRelatedOption updatedPriceRelatedOption = priceRelatedOptionRepository.findById(priceRelatedOption.getId()).get();
        // Disconnect from session so that the updates on updatedPriceRelatedOption are not directly saved in db
        em.detach(updatedPriceRelatedOption);
        updatedPriceRelatedOption.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restPriceRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPriceRelatedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPriceRelatedOption))
            )
            .andExpect(status().isOk());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
        PriceRelatedOption testPriceRelatedOption = priceRelatedOptionList.get(priceRelatedOptionList.size() - 1);
        assertThat(testPriceRelatedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPriceRelatedOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriceRelatedOption.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingPriceRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();
        priceRelatedOption.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceRelatedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriceRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();
        priceRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriceRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();
        priceRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceRelatedOptionWithPatch() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();

        // Update the priceRelatedOption using partial update
        PriceRelatedOption partialUpdatedPriceRelatedOption = new PriceRelatedOption();
        partialUpdatedPriceRelatedOption.setId(priceRelatedOption.getId());

        partialUpdatedPriceRelatedOption.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restPriceRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceRelatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceRelatedOption))
            )
            .andExpect(status().isOk());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
        PriceRelatedOption testPriceRelatedOption = priceRelatedOptionList.get(priceRelatedOptionList.size() - 1);
        assertThat(testPriceRelatedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPriceRelatedOption.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPriceRelatedOption.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdatePriceRelatedOptionWithPatch() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();

        // Update the priceRelatedOption using partial update
        PriceRelatedOption partialUpdatedPriceRelatedOption = new PriceRelatedOption();
        partialUpdatedPriceRelatedOption.setId(priceRelatedOption.getId());

        partialUpdatedPriceRelatedOption.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restPriceRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceRelatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceRelatedOption))
            )
            .andExpect(status().isOk());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
        PriceRelatedOption testPriceRelatedOption = priceRelatedOptionList.get(priceRelatedOptionList.size() - 1);
        assertThat(testPriceRelatedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPriceRelatedOption.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriceRelatedOption.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingPriceRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();
        priceRelatedOption.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceRelatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriceRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();
        priceRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriceRelatedOption() throws Exception {
        int databaseSizeBeforeUpdate = priceRelatedOptionRepository.findAll().size();
        priceRelatedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRelatedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRelatedOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceRelatedOption in the database
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriceRelatedOption() throws Exception {
        // Initialize the database
        priceRelatedOptionRepository.saveAndFlush(priceRelatedOption);

        int databaseSizeBeforeDelete = priceRelatedOptionRepository.findAll().size();

        // Delete the priceRelatedOption
        restPriceRelatedOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceRelatedOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriceRelatedOption> priceRelatedOptionList = priceRelatedOptionRepository.findAll();
        assertThat(priceRelatedOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
