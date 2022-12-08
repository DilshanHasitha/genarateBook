package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.BooksOptionDetails;
import com.mycompany.myapp.repository.BooksOptionDetailsRepository;
import com.mycompany.myapp.service.criteria.BooksOptionDetailsCriteria;
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
 * Integration tests for the {@link BooksOptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BooksOptionDetailsResourceIT {

    private static final String DEFAULT_AVATAR_ATTRIBUTES = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_ATTRIBUTES = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR_CHARACTOR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_CHARACTOR = "BBBBBBBBBB";

    private static final String DEFAULT_STYLE = "AAAAAAAAAA";
    private static final String UPDATED_STYLE = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/books-option-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksOptionDetailsRepository booksOptionDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksOptionDetailsMockMvc;

    private BooksOptionDetails booksOptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksOptionDetails createEntity(EntityManager em) {
        BooksOptionDetails booksOptionDetails = new BooksOptionDetails()
            .avatarAttributes(DEFAULT_AVATAR_ATTRIBUTES)
            .avatarCharactor(DEFAULT_AVATAR_CHARACTOR)
            .style(DEFAULT_STYLE)
            .option(DEFAULT_OPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return booksOptionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksOptionDetails createUpdatedEntity(EntityManager em) {
        BooksOptionDetails booksOptionDetails = new BooksOptionDetails()
            .avatarAttributes(UPDATED_AVATAR_ATTRIBUTES)
            .avatarCharactor(UPDATED_AVATAR_CHARACTOR)
            .style(UPDATED_STYLE)
            .option(UPDATED_OPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return booksOptionDetails;
    }

    @BeforeEach
    public void initTest() {
        booksOptionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createBooksOptionDetails() throws Exception {
        int databaseSizeBeforeCreate = booksOptionDetailsRepository.findAll().size();
        // Create the BooksOptionDetails
        restBooksOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isCreated());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BooksOptionDetails testBooksOptionDetails = booksOptionDetailsList.get(booksOptionDetailsList.size() - 1);
        assertThat(testBooksOptionDetails.getAvatarAttributes()).isEqualTo(DEFAULT_AVATAR_ATTRIBUTES);
        assertThat(testBooksOptionDetails.getAvatarCharactor()).isEqualTo(DEFAULT_AVATAR_CHARACTOR);
        assertThat(testBooksOptionDetails.getStyle()).isEqualTo(DEFAULT_STYLE);
        assertThat(testBooksOptionDetails.getOption()).isEqualTo(DEFAULT_OPTION);
        assertThat(testBooksOptionDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBooksOptionDetailsWithExistingId() throws Exception {
        // Create the BooksOptionDetails with an existing ID
        booksOptionDetails.setId(1L);

        int databaseSizeBeforeCreate = booksOptionDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksOptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetails() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList
        restBooksOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarAttributes").value(hasItem(DEFAULT_AVATAR_ATTRIBUTES)))
            .andExpect(jsonPath("$.[*].avatarCharactor").value(hasItem(DEFAULT_AVATAR_CHARACTOR)))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE)))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getBooksOptionDetails() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get the booksOptionDetails
        restBooksOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, booksOptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booksOptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.avatarAttributes").value(DEFAULT_AVATAR_ATTRIBUTES))
            .andExpect(jsonPath("$.avatarCharactor").value(DEFAULT_AVATAR_CHARACTOR))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE))
            .andExpect(jsonPath("$.option").value(DEFAULT_OPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBooksOptionDetailsByIdFiltering() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        Long id = booksOptionDetails.getId();

        defaultBooksOptionDetailsShouldBeFound("id.equals=" + id);
        defaultBooksOptionDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultBooksOptionDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksOptionDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksOptionDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksOptionDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarAttributesIsEqualToSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarAttributes equals to DEFAULT_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldBeFound("avatarAttributes.equals=" + DEFAULT_AVATAR_ATTRIBUTES);

        // Get all the booksOptionDetailsList where avatarAttributes equals to UPDATED_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldNotBeFound("avatarAttributes.equals=" + UPDATED_AVATAR_ATTRIBUTES);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarAttributesIsInShouldWork() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarAttributes in DEFAULT_AVATAR_ATTRIBUTES or UPDATED_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldBeFound("avatarAttributes.in=" + DEFAULT_AVATAR_ATTRIBUTES + "," + UPDATED_AVATAR_ATTRIBUTES);

        // Get all the booksOptionDetailsList where avatarAttributes equals to UPDATED_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldNotBeFound("avatarAttributes.in=" + UPDATED_AVATAR_ATTRIBUTES);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarAttributesIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarAttributes is not null
        defaultBooksOptionDetailsShouldBeFound("avatarAttributes.specified=true");

        // Get all the booksOptionDetailsList where avatarAttributes is null
        defaultBooksOptionDetailsShouldNotBeFound("avatarAttributes.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarAttributesContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarAttributes contains DEFAULT_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldBeFound("avatarAttributes.contains=" + DEFAULT_AVATAR_ATTRIBUTES);

        // Get all the booksOptionDetailsList where avatarAttributes contains UPDATED_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldNotBeFound("avatarAttributes.contains=" + UPDATED_AVATAR_ATTRIBUTES);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarAttributesNotContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarAttributes does not contain DEFAULT_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldNotBeFound("avatarAttributes.doesNotContain=" + DEFAULT_AVATAR_ATTRIBUTES);

        // Get all the booksOptionDetailsList where avatarAttributes does not contain UPDATED_AVATAR_ATTRIBUTES
        defaultBooksOptionDetailsShouldBeFound("avatarAttributes.doesNotContain=" + UPDATED_AVATAR_ATTRIBUTES);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarCharactorIsEqualToSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarCharactor equals to DEFAULT_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldBeFound("avatarCharactor.equals=" + DEFAULT_AVATAR_CHARACTOR);

        // Get all the booksOptionDetailsList where avatarCharactor equals to UPDATED_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldNotBeFound("avatarCharactor.equals=" + UPDATED_AVATAR_CHARACTOR);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarCharactorIsInShouldWork() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarCharactor in DEFAULT_AVATAR_CHARACTOR or UPDATED_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldBeFound("avatarCharactor.in=" + DEFAULT_AVATAR_CHARACTOR + "," + UPDATED_AVATAR_CHARACTOR);

        // Get all the booksOptionDetailsList where avatarCharactor equals to UPDATED_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldNotBeFound("avatarCharactor.in=" + UPDATED_AVATAR_CHARACTOR);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarCharactorIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarCharactor is not null
        defaultBooksOptionDetailsShouldBeFound("avatarCharactor.specified=true");

        // Get all the booksOptionDetailsList where avatarCharactor is null
        defaultBooksOptionDetailsShouldNotBeFound("avatarCharactor.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarCharactorContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarCharactor contains DEFAULT_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldBeFound("avatarCharactor.contains=" + DEFAULT_AVATAR_CHARACTOR);

        // Get all the booksOptionDetailsList where avatarCharactor contains UPDATED_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldNotBeFound("avatarCharactor.contains=" + UPDATED_AVATAR_CHARACTOR);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByAvatarCharactorNotContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where avatarCharactor does not contain DEFAULT_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldNotBeFound("avatarCharactor.doesNotContain=" + DEFAULT_AVATAR_CHARACTOR);

        // Get all the booksOptionDetailsList where avatarCharactor does not contain UPDATED_AVATAR_CHARACTOR
        defaultBooksOptionDetailsShouldBeFound("avatarCharactor.doesNotContain=" + UPDATED_AVATAR_CHARACTOR);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByStyleIsEqualToSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where style equals to DEFAULT_STYLE
        defaultBooksOptionDetailsShouldBeFound("style.equals=" + DEFAULT_STYLE);

        // Get all the booksOptionDetailsList where style equals to UPDATED_STYLE
        defaultBooksOptionDetailsShouldNotBeFound("style.equals=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByStyleIsInShouldWork() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where style in DEFAULT_STYLE or UPDATED_STYLE
        defaultBooksOptionDetailsShouldBeFound("style.in=" + DEFAULT_STYLE + "," + UPDATED_STYLE);

        // Get all the booksOptionDetailsList where style equals to UPDATED_STYLE
        defaultBooksOptionDetailsShouldNotBeFound("style.in=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByStyleIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where style is not null
        defaultBooksOptionDetailsShouldBeFound("style.specified=true");

        // Get all the booksOptionDetailsList where style is null
        defaultBooksOptionDetailsShouldNotBeFound("style.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByStyleContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where style contains DEFAULT_STYLE
        defaultBooksOptionDetailsShouldBeFound("style.contains=" + DEFAULT_STYLE);

        // Get all the booksOptionDetailsList where style contains UPDATED_STYLE
        defaultBooksOptionDetailsShouldNotBeFound("style.contains=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByStyleNotContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where style does not contain DEFAULT_STYLE
        defaultBooksOptionDetailsShouldNotBeFound("style.doesNotContain=" + DEFAULT_STYLE);

        // Get all the booksOptionDetailsList where style does not contain UPDATED_STYLE
        defaultBooksOptionDetailsShouldBeFound("style.doesNotContain=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByOptionIsEqualToSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where option equals to DEFAULT_OPTION
        defaultBooksOptionDetailsShouldBeFound("option.equals=" + DEFAULT_OPTION);

        // Get all the booksOptionDetailsList where option equals to UPDATED_OPTION
        defaultBooksOptionDetailsShouldNotBeFound("option.equals=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByOptionIsInShouldWork() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where option in DEFAULT_OPTION or UPDATED_OPTION
        defaultBooksOptionDetailsShouldBeFound("option.in=" + DEFAULT_OPTION + "," + UPDATED_OPTION);

        // Get all the booksOptionDetailsList where option equals to UPDATED_OPTION
        defaultBooksOptionDetailsShouldNotBeFound("option.in=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByOptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where option is not null
        defaultBooksOptionDetailsShouldBeFound("option.specified=true");

        // Get all the booksOptionDetailsList where option is null
        defaultBooksOptionDetailsShouldNotBeFound("option.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByOptionContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where option contains DEFAULT_OPTION
        defaultBooksOptionDetailsShouldBeFound("option.contains=" + DEFAULT_OPTION);

        // Get all the booksOptionDetailsList where option contains UPDATED_OPTION
        defaultBooksOptionDetailsShouldNotBeFound("option.contains=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByOptionNotContainsSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where option does not contain DEFAULT_OPTION
        defaultBooksOptionDetailsShouldNotBeFound("option.doesNotContain=" + DEFAULT_OPTION);

        // Get all the booksOptionDetailsList where option does not contain UPDATED_OPTION
        defaultBooksOptionDetailsShouldBeFound("option.doesNotContain=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksOptionDetailsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksOptionDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksOptionDetailsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksOptionDetailsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksOptionDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksOptionDetailsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        // Get all the booksOptionDetailsList where isActive is not null
        defaultBooksOptionDetailsShouldBeFound("isActive.specified=true");

        // Get all the booksOptionDetailsList where isActive is null
        defaultBooksOptionDetailsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksOptionDetailsByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        booksOptionDetails.setBooks(books);
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);
        Long booksId = books.getId();

        // Get all the booksOptionDetailsList where books equals to booksId
        defaultBooksOptionDetailsShouldBeFound("booksId.equals=" + booksId);

        // Get all the booksOptionDetailsList where books equals to (booksId + 1)
        defaultBooksOptionDetailsShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksOptionDetailsShouldBeFound(String filter) throws Exception {
        restBooksOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksOptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarAttributes").value(hasItem(DEFAULT_AVATAR_ATTRIBUTES)))
            .andExpect(jsonPath("$.[*].avatarCharactor").value(hasItem(DEFAULT_AVATAR_CHARACTOR)))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE)))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBooksOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksOptionDetailsShouldNotBeFound(String filter) throws Exception {
        restBooksOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksOptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooksOptionDetails() throws Exception {
        // Get the booksOptionDetails
        restBooksOptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooksOptionDetails() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();

        // Update the booksOptionDetails
        BooksOptionDetails updatedBooksOptionDetails = booksOptionDetailsRepository.findById(booksOptionDetails.getId()).get();
        // Disconnect from session so that the updates on updatedBooksOptionDetails are not directly saved in db
        em.detach(updatedBooksOptionDetails);
        updatedBooksOptionDetails
            .avatarAttributes(UPDATED_AVATAR_ATTRIBUTES)
            .avatarCharactor(UPDATED_AVATAR_CHARACTOR)
            .style(UPDATED_STYLE)
            .option(UPDATED_OPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restBooksOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooksOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooksOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        BooksOptionDetails testBooksOptionDetails = booksOptionDetailsList.get(booksOptionDetailsList.size() - 1);
        assertThat(testBooksOptionDetails.getAvatarAttributes()).isEqualTo(UPDATED_AVATAR_ATTRIBUTES);
        assertThat(testBooksOptionDetails.getAvatarCharactor()).isEqualTo(UPDATED_AVATAR_CHARACTOR);
        assertThat(testBooksOptionDetails.getStyle()).isEqualTo(UPDATED_STYLE);
        assertThat(testBooksOptionDetails.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testBooksOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBooksOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();
        booksOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, booksOptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooksOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();
        booksOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooksOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();
        booksOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksOptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();

        // Update the booksOptionDetails using partial update
        BooksOptionDetails partialUpdatedBooksOptionDetails = new BooksOptionDetails();
        partialUpdatedBooksOptionDetails.setId(booksOptionDetails.getId());

        partialUpdatedBooksOptionDetails.style(UPDATED_STYLE).option(UPDATED_OPTION);

        restBooksOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        BooksOptionDetails testBooksOptionDetails = booksOptionDetailsList.get(booksOptionDetailsList.size() - 1);
        assertThat(testBooksOptionDetails.getAvatarAttributes()).isEqualTo(DEFAULT_AVATAR_ATTRIBUTES);
        assertThat(testBooksOptionDetails.getAvatarCharactor()).isEqualTo(DEFAULT_AVATAR_CHARACTOR);
        assertThat(testBooksOptionDetails.getStyle()).isEqualTo(UPDATED_STYLE);
        assertThat(testBooksOptionDetails.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testBooksOptionDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBooksOptionDetailsWithPatch() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();

        // Update the booksOptionDetails using partial update
        BooksOptionDetails partialUpdatedBooksOptionDetails = new BooksOptionDetails();
        partialUpdatedBooksOptionDetails.setId(booksOptionDetails.getId());

        partialUpdatedBooksOptionDetails
            .avatarAttributes(UPDATED_AVATAR_ATTRIBUTES)
            .avatarCharactor(UPDATED_AVATAR_CHARACTOR)
            .style(UPDATED_STYLE)
            .option(UPDATED_OPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restBooksOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksOptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        BooksOptionDetails testBooksOptionDetails = booksOptionDetailsList.get(booksOptionDetailsList.size() - 1);
        assertThat(testBooksOptionDetails.getAvatarAttributes()).isEqualTo(UPDATED_AVATAR_ATTRIBUTES);
        assertThat(testBooksOptionDetails.getAvatarCharactor()).isEqualTo(UPDATED_AVATAR_CHARACTOR);
        assertThat(testBooksOptionDetails.getStyle()).isEqualTo(UPDATED_STYLE);
        assertThat(testBooksOptionDetails.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testBooksOptionDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBooksOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();
        booksOptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, booksOptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooksOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();
        booksOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooksOptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = booksOptionDetailsRepository.findAll().size();
        booksOptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksOptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksOptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksOptionDetails in the database
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooksOptionDetails() throws Exception {
        // Initialize the database
        booksOptionDetailsRepository.saveAndFlush(booksOptionDetails);

        int databaseSizeBeforeDelete = booksOptionDetailsRepository.findAll().size();

        // Delete the booksOptionDetails
        restBooksOptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, booksOptionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BooksOptionDetails> booksOptionDetailsList = booksOptionDetailsRepository.findAll();
        assertThat(booksOptionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
