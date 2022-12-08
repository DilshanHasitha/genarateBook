package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.domain.AvatarCharactor;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.Options;
import com.mycompany.myapp.repository.AvatarAttributesRepository;
import com.mycompany.myapp.service.AvatarAttributesService;
import com.mycompany.myapp.service.criteria.AvatarAttributesCriteria;
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
 * Integration tests for the {@link AvatarAttributesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AvatarAttributesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/avatar-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvatarAttributesRepository avatarAttributesRepository;

    @Mock
    private AvatarAttributesRepository avatarAttributesRepositoryMock;

    @Mock
    private AvatarAttributesService avatarAttributesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvatarAttributesMockMvc;

    private AvatarAttributes avatarAttributes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvatarAttributes createEntity(EntityManager em) {
        AvatarAttributes avatarAttributes = new AvatarAttributes()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return avatarAttributes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvatarAttributes createUpdatedEntity(EntityManager em) {
        AvatarAttributes avatarAttributes = new AvatarAttributes()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return avatarAttributes;
    }

    @BeforeEach
    public void initTest() {
        avatarAttributes = createEntity(em);
    }

    @Test
    @Transactional
    void createAvatarAttributes() throws Exception {
        int databaseSizeBeforeCreate = avatarAttributesRepository.findAll().size();
        // Create the AvatarAttributes
        restAvatarAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isCreated());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeCreate + 1);
        AvatarAttributes testAvatarAttributes = avatarAttributesList.get(avatarAttributesList.size() - 1);
        assertThat(testAvatarAttributes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAvatarAttributes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAvatarAttributes.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createAvatarAttributesWithExistingId() throws Exception {
        // Create the AvatarAttributes with an existing ID
        avatarAttributes.setId(1L);

        int databaseSizeBeforeCreate = avatarAttributesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvatarAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = avatarAttributesRepository.findAll().size();
        // set the field null
        avatarAttributes.setDescription(null);

        // Create the AvatarAttributes, which fails.

        restAvatarAttributesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isBadRequest());

        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvatarAttributes() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList
        restAvatarAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avatarAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAvatarAttributesWithEagerRelationshipsIsEnabled() throws Exception {
        when(avatarAttributesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAvatarAttributesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(avatarAttributesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAvatarAttributesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(avatarAttributesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAvatarAttributesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(avatarAttributesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAvatarAttributes() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get the avatarAttributes
        restAvatarAttributesMockMvc
            .perform(get(ENTITY_API_URL_ID, avatarAttributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avatarAttributes.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getAvatarAttributesByIdFiltering() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        Long id = avatarAttributes.getId();

        defaultAvatarAttributesShouldBeFound("id.equals=" + id);
        defaultAvatarAttributesShouldNotBeFound("id.notEquals=" + id);

        defaultAvatarAttributesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAvatarAttributesShouldNotBeFound("id.greaterThan=" + id);

        defaultAvatarAttributesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAvatarAttributesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where code equals to DEFAULT_CODE
        defaultAvatarAttributesShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the avatarAttributesList where code equals to UPDATED_CODE
        defaultAvatarAttributesShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAvatarAttributesShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the avatarAttributesList where code equals to UPDATED_CODE
        defaultAvatarAttributesShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where code is not null
        defaultAvatarAttributesShouldBeFound("code.specified=true");

        // Get all the avatarAttributesList where code is null
        defaultAvatarAttributesShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByCodeContainsSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where code contains DEFAULT_CODE
        defaultAvatarAttributesShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the avatarAttributesList where code contains UPDATED_CODE
        defaultAvatarAttributesShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where code does not contain DEFAULT_CODE
        defaultAvatarAttributesShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the avatarAttributesList where code does not contain UPDATED_CODE
        defaultAvatarAttributesShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where description equals to DEFAULT_DESCRIPTION
        defaultAvatarAttributesShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the avatarAttributesList where description equals to UPDATED_DESCRIPTION
        defaultAvatarAttributesShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAvatarAttributesShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the avatarAttributesList where description equals to UPDATED_DESCRIPTION
        defaultAvatarAttributesShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where description is not null
        defaultAvatarAttributesShouldBeFound("description.specified=true");

        // Get all the avatarAttributesList where description is null
        defaultAvatarAttributesShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where description contains DEFAULT_DESCRIPTION
        defaultAvatarAttributesShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the avatarAttributesList where description contains UPDATED_DESCRIPTION
        defaultAvatarAttributesShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where description does not contain DEFAULT_DESCRIPTION
        defaultAvatarAttributesShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the avatarAttributesList where description does not contain UPDATED_DESCRIPTION
        defaultAvatarAttributesShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where isActive equals to DEFAULT_IS_ACTIVE
        defaultAvatarAttributesShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the avatarAttributesList where isActive equals to UPDATED_IS_ACTIVE
        defaultAvatarAttributesShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultAvatarAttributesShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the avatarAttributesList where isActive equals to UPDATED_IS_ACTIVE
        defaultAvatarAttributesShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        // Get all the avatarAttributesList where isActive is not null
        defaultAvatarAttributesShouldBeFound("isActive.specified=true");

        // Get all the avatarAttributesList where isActive is null
        defaultAvatarAttributesShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByAvatarCharactorIsEqualToSomething() throws Exception {
        AvatarCharactor avatarCharactor;
        if (TestUtil.findAll(em, AvatarCharactor.class).isEmpty()) {
            avatarAttributesRepository.saveAndFlush(avatarAttributes);
            avatarCharactor = AvatarCharactorResourceIT.createEntity(em);
        } else {
            avatarCharactor = TestUtil.findAll(em, AvatarCharactor.class).get(0);
        }
        em.persist(avatarCharactor);
        em.flush();
        avatarAttributes.addAvatarCharactor(avatarCharactor);
        avatarAttributesRepository.saveAndFlush(avatarAttributes);
        Long avatarCharactorId = avatarCharactor.getId();

        // Get all the avatarAttributesList where avatarCharactor equals to avatarCharactorId
        defaultAvatarAttributesShouldBeFound("avatarCharactorId.equals=" + avatarCharactorId);

        // Get all the avatarAttributesList where avatarCharactor equals to (avatarCharactorId + 1)
        defaultAvatarAttributesShouldNotBeFound("avatarCharactorId.equals=" + (avatarCharactorId + 1));
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByOptionIsEqualToSomething() throws Exception {
        Options option;
        if (TestUtil.findAll(em, Options.class).isEmpty()) {
            avatarAttributesRepository.saveAndFlush(avatarAttributes);
            option = OptionsResourceIT.createEntity(em);
        } else {
            option = TestUtil.findAll(em, Options.class).get(0);
        }
        em.persist(option);
        em.flush();
        avatarAttributes.addOption(option);
        avatarAttributesRepository.saveAndFlush(avatarAttributes);
        Long optionId = option.getId();

        // Get all the avatarAttributesList where option equals to optionId
        defaultAvatarAttributesShouldBeFound("optionId.equals=" + optionId);

        // Get all the avatarAttributesList where option equals to (optionId + 1)
        defaultAvatarAttributesShouldNotBeFound("optionId.equals=" + (optionId + 1));
    }

    @Test
    @Transactional
    void getAllAvatarAttributesByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            avatarAttributesRepository.saveAndFlush(avatarAttributes);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        avatarAttributes.addBooks(books);
        avatarAttributesRepository.saveAndFlush(avatarAttributes);
        Long booksId = books.getId();

        // Get all the avatarAttributesList where books equals to booksId
        defaultAvatarAttributesShouldBeFound("booksId.equals=" + booksId);

        // Get all the avatarAttributesList where books equals to (booksId + 1)
        defaultAvatarAttributesShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAvatarAttributesShouldBeFound(String filter) throws Exception {
        restAvatarAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avatarAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restAvatarAttributesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAvatarAttributesShouldNotBeFound(String filter) throws Exception {
        restAvatarAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAvatarAttributesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAvatarAttributes() throws Exception {
        // Get the avatarAttributes
        restAvatarAttributesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvatarAttributes() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();

        // Update the avatarAttributes
        AvatarAttributes updatedAvatarAttributes = avatarAttributesRepository.findById(avatarAttributes.getId()).get();
        // Disconnect from session so that the updates on updatedAvatarAttributes are not directly saved in db
        em.detach(updatedAvatarAttributes);
        updatedAvatarAttributes.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restAvatarAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvatarAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvatarAttributes))
            )
            .andExpect(status().isOk());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
        AvatarAttributes testAvatarAttributes = avatarAttributesList.get(avatarAttributesList.size() - 1);
        assertThat(testAvatarAttributes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAvatarAttributes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvatarAttributes.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingAvatarAttributes() throws Exception {
        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();
        avatarAttributes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvatarAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avatarAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvatarAttributes() throws Exception {
        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();
        avatarAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvatarAttributes() throws Exception {
        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();
        avatarAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarAttributesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvatarAttributesWithPatch() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();

        // Update the avatarAttributes using partial update
        AvatarAttributes partialUpdatedAvatarAttributes = new AvatarAttributes();
        partialUpdatedAvatarAttributes.setId(avatarAttributes.getId());

        partialUpdatedAvatarAttributes.description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restAvatarAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvatarAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvatarAttributes))
            )
            .andExpect(status().isOk());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
        AvatarAttributes testAvatarAttributes = avatarAttributesList.get(avatarAttributesList.size() - 1);
        assertThat(testAvatarAttributes.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAvatarAttributes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvatarAttributes.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateAvatarAttributesWithPatch() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();

        // Update the avatarAttributes using partial update
        AvatarAttributes partialUpdatedAvatarAttributes = new AvatarAttributes();
        partialUpdatedAvatarAttributes.setId(avatarAttributes.getId());

        partialUpdatedAvatarAttributes.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restAvatarAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvatarAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvatarAttributes))
            )
            .andExpect(status().isOk());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
        AvatarAttributes testAvatarAttributes = avatarAttributesList.get(avatarAttributesList.size() - 1);
        assertThat(testAvatarAttributes.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAvatarAttributes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvatarAttributes.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingAvatarAttributes() throws Exception {
        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();
        avatarAttributes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvatarAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avatarAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvatarAttributes() throws Exception {
        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();
        avatarAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvatarAttributes() throws Exception {
        int databaseSizeBeforeUpdate = avatarAttributesRepository.findAll().size();
        avatarAttributes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avatarAttributes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvatarAttributes in the database
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvatarAttributes() throws Exception {
        // Initialize the database
        avatarAttributesRepository.saveAndFlush(avatarAttributes);

        int databaseSizeBeforeDelete = avatarAttributesRepository.findAll().size();

        // Delete the avatarAttributes
        restAvatarAttributesMockMvc
            .perform(delete(ENTITY_API_URL_ID, avatarAttributes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AvatarAttributes> avatarAttributesList = avatarAttributesRepository.findAll();
        assertThat(avatarAttributesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
