package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.domain.AvatarCharactor;
import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.repository.AvatarCharactorRepository;
import com.mycompany.myapp.service.criteria.AvatarCharactorCriteria;
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
 * Integration tests for the {@link AvatarCharactorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvatarCharactorResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;
    private static final Integer SMALLER_X = 1 - 1;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;
    private static final Integer SMALLER_Y = 1 - 1;

    private static final String ENTITY_API_URL = "/api/avatar-charactors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvatarCharactorRepository avatarCharactorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvatarCharactorMockMvc;

    private AvatarCharactor avatarCharactor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvatarCharactor createEntity(EntityManager em) {
        AvatarCharactor avatarCharactor = new AvatarCharactor()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .imgUrl(DEFAULT_IMG_URL)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .x(DEFAULT_X)
            .y(DEFAULT_Y);
        return avatarCharactor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvatarCharactor createUpdatedEntity(EntityManager em) {
        AvatarCharactor avatarCharactor = new AvatarCharactor()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .imgUrl(UPDATED_IMG_URL)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y);
        return avatarCharactor;
    }

    @BeforeEach
    public void initTest() {
        avatarCharactor = createEntity(em);
    }

    @Test
    @Transactional
    void createAvatarCharactor() throws Exception {
        int databaseSizeBeforeCreate = avatarCharactorRepository.findAll().size();
        // Create the AvatarCharactor
        restAvatarCharactorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isCreated());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeCreate + 1);
        AvatarCharactor testAvatarCharactor = avatarCharactorList.get(avatarCharactorList.size() - 1);
        assertThat(testAvatarCharactor.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAvatarCharactor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAvatarCharactor.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testAvatarCharactor.getImgUrl()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testAvatarCharactor.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testAvatarCharactor.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testAvatarCharactor.getX()).isEqualTo(DEFAULT_X);
        assertThat(testAvatarCharactor.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createAvatarCharactorWithExistingId() throws Exception {
        // Create the AvatarCharactor with an existing ID
        avatarCharactor.setId(1L);

        int databaseSizeBeforeCreate = avatarCharactorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvatarCharactorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = avatarCharactorRepository.findAll().size();
        // set the field null
        avatarCharactor.setDescription(null);

        // Create the AvatarCharactor, which fails.

        restAvatarCharactorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isBadRequest());

        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvatarCharactors() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList
        restAvatarCharactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avatarCharactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));
    }

    @Test
    @Transactional
    void getAvatarCharactor() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get the avatarCharactor
        restAvatarCharactorMockMvc
            .perform(get(ENTITY_API_URL_ID, avatarCharactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avatarCharactor.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y));
    }

    @Test
    @Transactional
    void getAvatarCharactorsByIdFiltering() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        Long id = avatarCharactor.getId();

        defaultAvatarCharactorShouldBeFound("id.equals=" + id);
        defaultAvatarCharactorShouldNotBeFound("id.notEquals=" + id);

        defaultAvatarCharactorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAvatarCharactorShouldNotBeFound("id.greaterThan=" + id);

        defaultAvatarCharactorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAvatarCharactorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where code equals to DEFAULT_CODE
        defaultAvatarCharactorShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the avatarCharactorList where code equals to UPDATED_CODE
        defaultAvatarCharactorShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAvatarCharactorShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the avatarCharactorList where code equals to UPDATED_CODE
        defaultAvatarCharactorShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where code is not null
        defaultAvatarCharactorShouldBeFound("code.specified=true");

        // Get all the avatarCharactorList where code is null
        defaultAvatarCharactorShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByCodeContainsSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where code contains DEFAULT_CODE
        defaultAvatarCharactorShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the avatarCharactorList where code contains UPDATED_CODE
        defaultAvatarCharactorShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where code does not contain DEFAULT_CODE
        defaultAvatarCharactorShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the avatarCharactorList where code does not contain UPDATED_CODE
        defaultAvatarCharactorShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where description equals to DEFAULT_DESCRIPTION
        defaultAvatarCharactorShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the avatarCharactorList where description equals to UPDATED_DESCRIPTION
        defaultAvatarCharactorShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAvatarCharactorShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the avatarCharactorList where description equals to UPDATED_DESCRIPTION
        defaultAvatarCharactorShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where description is not null
        defaultAvatarCharactorShouldBeFound("description.specified=true");

        // Get all the avatarCharactorList where description is null
        defaultAvatarCharactorShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where description contains DEFAULT_DESCRIPTION
        defaultAvatarCharactorShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the avatarCharactorList where description contains UPDATED_DESCRIPTION
        defaultAvatarCharactorShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where description does not contain DEFAULT_DESCRIPTION
        defaultAvatarCharactorShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the avatarCharactorList where description does not contain UPDATED_DESCRIPTION
        defaultAvatarCharactorShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where isActive equals to DEFAULT_IS_ACTIVE
        defaultAvatarCharactorShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the avatarCharactorList where isActive equals to UPDATED_IS_ACTIVE
        defaultAvatarCharactorShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultAvatarCharactorShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the avatarCharactorList where isActive equals to UPDATED_IS_ACTIVE
        defaultAvatarCharactorShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where isActive is not null
        defaultAvatarCharactorShouldBeFound("isActive.specified=true");

        // Get all the avatarCharactorList where isActive is null
        defaultAvatarCharactorShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where imgUrl equals to DEFAULT_IMG_URL
        defaultAvatarCharactorShouldBeFound("imgUrl.equals=" + DEFAULT_IMG_URL);

        // Get all the avatarCharactorList where imgUrl equals to UPDATED_IMG_URL
        defaultAvatarCharactorShouldNotBeFound("imgUrl.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where imgUrl in DEFAULT_IMG_URL or UPDATED_IMG_URL
        defaultAvatarCharactorShouldBeFound("imgUrl.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL);

        // Get all the avatarCharactorList where imgUrl equals to UPDATED_IMG_URL
        defaultAvatarCharactorShouldNotBeFound("imgUrl.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where imgUrl is not null
        defaultAvatarCharactorShouldBeFound("imgUrl.specified=true");

        // Get all the avatarCharactorList where imgUrl is null
        defaultAvatarCharactorShouldNotBeFound("imgUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByImgUrlContainsSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where imgUrl contains DEFAULT_IMG_URL
        defaultAvatarCharactorShouldBeFound("imgUrl.contains=" + DEFAULT_IMG_URL);

        // Get all the avatarCharactorList where imgUrl contains UPDATED_IMG_URL
        defaultAvatarCharactorShouldNotBeFound("imgUrl.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where imgUrl does not contain DEFAULT_IMG_URL
        defaultAvatarCharactorShouldNotBeFound("imgUrl.doesNotContain=" + DEFAULT_IMG_URL);

        // Get all the avatarCharactorList where imgUrl does not contain UPDATED_IMG_URL
        defaultAvatarCharactorShouldBeFound("imgUrl.doesNotContain=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width equals to DEFAULT_WIDTH
        defaultAvatarCharactorShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the avatarCharactorList where width equals to UPDATED_WIDTH
        defaultAvatarCharactorShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultAvatarCharactorShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the avatarCharactorList where width equals to UPDATED_WIDTH
        defaultAvatarCharactorShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width is not null
        defaultAvatarCharactorShouldBeFound("width.specified=true");

        // Get all the avatarCharactorList where width is null
        defaultAvatarCharactorShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width is greater than or equal to DEFAULT_WIDTH
        defaultAvatarCharactorShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the avatarCharactorList where width is greater than or equal to UPDATED_WIDTH
        defaultAvatarCharactorShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width is less than or equal to DEFAULT_WIDTH
        defaultAvatarCharactorShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the avatarCharactorList where width is less than or equal to SMALLER_WIDTH
        defaultAvatarCharactorShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width is less than DEFAULT_WIDTH
        defaultAvatarCharactorShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the avatarCharactorList where width is less than UPDATED_WIDTH
        defaultAvatarCharactorShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where width is greater than DEFAULT_WIDTH
        defaultAvatarCharactorShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the avatarCharactorList where width is greater than SMALLER_WIDTH
        defaultAvatarCharactorShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height equals to DEFAULT_HEIGHT
        defaultAvatarCharactorShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the avatarCharactorList where height equals to UPDATED_HEIGHT
        defaultAvatarCharactorShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultAvatarCharactorShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the avatarCharactorList where height equals to UPDATED_HEIGHT
        defaultAvatarCharactorShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height is not null
        defaultAvatarCharactorShouldBeFound("height.specified=true");

        // Get all the avatarCharactorList where height is null
        defaultAvatarCharactorShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height is greater than or equal to DEFAULT_HEIGHT
        defaultAvatarCharactorShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the avatarCharactorList where height is greater than or equal to UPDATED_HEIGHT
        defaultAvatarCharactorShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height is less than or equal to DEFAULT_HEIGHT
        defaultAvatarCharactorShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the avatarCharactorList where height is less than or equal to SMALLER_HEIGHT
        defaultAvatarCharactorShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height is less than DEFAULT_HEIGHT
        defaultAvatarCharactorShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the avatarCharactorList where height is less than UPDATED_HEIGHT
        defaultAvatarCharactorShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where height is greater than DEFAULT_HEIGHT
        defaultAvatarCharactorShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the avatarCharactorList where height is greater than SMALLER_HEIGHT
        defaultAvatarCharactorShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x equals to DEFAULT_X
        defaultAvatarCharactorShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the avatarCharactorList where x equals to UPDATED_X
        defaultAvatarCharactorShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x in DEFAULT_X or UPDATED_X
        defaultAvatarCharactorShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the avatarCharactorList where x equals to UPDATED_X
        defaultAvatarCharactorShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x is not null
        defaultAvatarCharactorShouldBeFound("x.specified=true");

        // Get all the avatarCharactorList where x is null
        defaultAvatarCharactorShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x is greater than or equal to DEFAULT_X
        defaultAvatarCharactorShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the avatarCharactorList where x is greater than or equal to UPDATED_X
        defaultAvatarCharactorShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x is less than or equal to DEFAULT_X
        defaultAvatarCharactorShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the avatarCharactorList where x is less than or equal to SMALLER_X
        defaultAvatarCharactorShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsLessThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x is less than DEFAULT_X
        defaultAvatarCharactorShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the avatarCharactorList where x is less than UPDATED_X
        defaultAvatarCharactorShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where x is greater than DEFAULT_X
        defaultAvatarCharactorShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the avatarCharactorList where x is greater than SMALLER_X
        defaultAvatarCharactorShouldBeFound("x.greaterThan=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y equals to DEFAULT_Y
        defaultAvatarCharactorShouldBeFound("y.equals=" + DEFAULT_Y);

        // Get all the avatarCharactorList where y equals to UPDATED_Y
        defaultAvatarCharactorShouldNotBeFound("y.equals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsInShouldWork() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y in DEFAULT_Y or UPDATED_Y
        defaultAvatarCharactorShouldBeFound("y.in=" + DEFAULT_Y + "," + UPDATED_Y);

        // Get all the avatarCharactorList where y equals to UPDATED_Y
        defaultAvatarCharactorShouldNotBeFound("y.in=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsNullOrNotNull() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y is not null
        defaultAvatarCharactorShouldBeFound("y.specified=true");

        // Get all the avatarCharactorList where y is null
        defaultAvatarCharactorShouldNotBeFound("y.specified=false");
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y is greater than or equal to DEFAULT_Y
        defaultAvatarCharactorShouldBeFound("y.greaterThanOrEqual=" + DEFAULT_Y);

        // Get all the avatarCharactorList where y is greater than or equal to UPDATED_Y
        defaultAvatarCharactorShouldNotBeFound("y.greaterThanOrEqual=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y is less than or equal to DEFAULT_Y
        defaultAvatarCharactorShouldBeFound("y.lessThanOrEqual=" + DEFAULT_Y);

        // Get all the avatarCharactorList where y is less than or equal to SMALLER_Y
        defaultAvatarCharactorShouldNotBeFound("y.lessThanOrEqual=" + SMALLER_Y);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsLessThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y is less than DEFAULT_Y
        defaultAvatarCharactorShouldNotBeFound("y.lessThan=" + DEFAULT_Y);

        // Get all the avatarCharactorList where y is less than UPDATED_Y
        defaultAvatarCharactorShouldBeFound("y.lessThan=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        // Get all the avatarCharactorList where y is greater than DEFAULT_Y
        defaultAvatarCharactorShouldNotBeFound("y.greaterThan=" + DEFAULT_Y);

        // Get all the avatarCharactorList where y is greater than SMALLER_Y
        defaultAvatarCharactorShouldBeFound("y.greaterThan=" + SMALLER_Y);
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByAvatarAttributesIsEqualToSomething() throws Exception {
        AvatarAttributes avatarAttributes;
        if (TestUtil.findAll(em, AvatarAttributes.class).isEmpty()) {
            avatarCharactorRepository.saveAndFlush(avatarCharactor);
            avatarAttributes = AvatarAttributesResourceIT.createEntity(em);
        } else {
            avatarAttributes = TestUtil.findAll(em, AvatarAttributes.class).get(0);
        }
        em.persist(avatarAttributes);
        em.flush();
        avatarCharactor.addAvatarAttributes(avatarAttributes);
        avatarCharactorRepository.saveAndFlush(avatarCharactor);
        Long avatarAttributesId = avatarAttributes.getId();

        // Get all the avatarCharactorList where avatarAttributes equals to avatarAttributesId
        defaultAvatarCharactorShouldBeFound("avatarAttributesId.equals=" + avatarAttributesId);

        // Get all the avatarCharactorList where avatarAttributes equals to (avatarAttributesId + 1)
        defaultAvatarCharactorShouldNotBeFound("avatarAttributesId.equals=" + (avatarAttributesId + 1));
    }

    @Test
    @Transactional
    void getAllAvatarCharactorsByLayerGroupIsEqualToSomething() throws Exception {
        LayerGroup layerGroup;
        if (TestUtil.findAll(em, LayerGroup.class).isEmpty()) {
            avatarCharactorRepository.saveAndFlush(avatarCharactor);
            layerGroup = LayerGroupResourceIT.createEntity(em);
        } else {
            layerGroup = TestUtil.findAll(em, LayerGroup.class).get(0);
        }
        em.persist(layerGroup);
        em.flush();
        avatarCharactor.setLayerGroup(layerGroup);
        avatarCharactorRepository.saveAndFlush(avatarCharactor);
        Long layerGroupId = layerGroup.getId();

        // Get all the avatarCharactorList where layerGroup equals to layerGroupId
        defaultAvatarCharactorShouldBeFound("layerGroupId.equals=" + layerGroupId);

        // Get all the avatarCharactorList where layerGroup equals to (layerGroupId + 1)
        defaultAvatarCharactorShouldNotBeFound("layerGroupId.equals=" + (layerGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAvatarCharactorShouldBeFound(String filter) throws Exception {
        restAvatarCharactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avatarCharactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));

        // Check, that the count call also returns 1
        restAvatarCharactorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAvatarCharactorShouldNotBeFound(String filter) throws Exception {
        restAvatarCharactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAvatarCharactorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAvatarCharactor() throws Exception {
        // Get the avatarCharactor
        restAvatarCharactorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvatarCharactor() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();

        // Update the avatarCharactor
        AvatarCharactor updatedAvatarCharactor = avatarCharactorRepository.findById(avatarCharactor.getId()).get();
        // Disconnect from session so that the updates on updatedAvatarCharactor are not directly saved in db
        em.detach(updatedAvatarCharactor);
        updatedAvatarCharactor
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .imgUrl(UPDATED_IMG_URL)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restAvatarCharactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvatarCharactor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvatarCharactor))
            )
            .andExpect(status().isOk());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
        AvatarCharactor testAvatarCharactor = avatarCharactorList.get(avatarCharactorList.size() - 1);
        assertThat(testAvatarCharactor.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAvatarCharactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvatarCharactor.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAvatarCharactor.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testAvatarCharactor.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testAvatarCharactor.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testAvatarCharactor.getX()).isEqualTo(UPDATED_X);
        assertThat(testAvatarCharactor.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingAvatarCharactor() throws Exception {
        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();
        avatarCharactor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvatarCharactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avatarCharactor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvatarCharactor() throws Exception {
        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();
        avatarCharactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarCharactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvatarCharactor() throws Exception {
        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();
        avatarCharactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarCharactorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvatarCharactorWithPatch() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();

        // Update the avatarCharactor using partial update
        AvatarCharactor partialUpdatedAvatarCharactor = new AvatarCharactor();
        partialUpdatedAvatarCharactor.setId(avatarCharactor.getId());

        partialUpdatedAvatarCharactor.code(UPDATED_CODE).imgUrl(UPDATED_IMG_URL);

        restAvatarCharactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvatarCharactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvatarCharactor))
            )
            .andExpect(status().isOk());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
        AvatarCharactor testAvatarCharactor = avatarCharactorList.get(avatarCharactorList.size() - 1);
        assertThat(testAvatarCharactor.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAvatarCharactor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAvatarCharactor.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testAvatarCharactor.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testAvatarCharactor.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testAvatarCharactor.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testAvatarCharactor.getX()).isEqualTo(DEFAULT_X);
        assertThat(testAvatarCharactor.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void fullUpdateAvatarCharactorWithPatch() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();

        // Update the avatarCharactor using partial update
        AvatarCharactor partialUpdatedAvatarCharactor = new AvatarCharactor();
        partialUpdatedAvatarCharactor.setId(avatarCharactor.getId());

        partialUpdatedAvatarCharactor
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .imgUrl(UPDATED_IMG_URL)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restAvatarCharactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvatarCharactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvatarCharactor))
            )
            .andExpect(status().isOk());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
        AvatarCharactor testAvatarCharactor = avatarCharactorList.get(avatarCharactorList.size() - 1);
        assertThat(testAvatarCharactor.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAvatarCharactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAvatarCharactor.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAvatarCharactor.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testAvatarCharactor.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testAvatarCharactor.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testAvatarCharactor.getX()).isEqualTo(UPDATED_X);
        assertThat(testAvatarCharactor.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingAvatarCharactor() throws Exception {
        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();
        avatarCharactor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvatarCharactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avatarCharactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvatarCharactor() throws Exception {
        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();
        avatarCharactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarCharactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvatarCharactor() throws Exception {
        int databaseSizeBeforeUpdate = avatarCharactorRepository.findAll().size();
        avatarCharactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvatarCharactorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avatarCharactor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvatarCharactor in the database
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvatarCharactor() throws Exception {
        // Initialize the database
        avatarCharactorRepository.saveAndFlush(avatarCharactor);

        int databaseSizeBeforeDelete = avatarCharactorRepository.findAll().size();

        // Delete the avatarCharactor
        restAvatarCharactorMockMvc
            .perform(delete(ENTITY_API_URL_ID, avatarCharactor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AvatarCharactor> avatarCharactorList = avatarCharactorRepository.findAll();
        assertThat(avatarCharactorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
