package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Styles;
import com.mycompany.myapp.repository.StylesRepository;
import com.mycompany.myapp.service.criteria.StylesCriteria;
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
 * Integration tests for the {@link StylesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StylesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

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

    private static final String ENTITY_API_URL = "/api/styles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StylesRepository stylesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStylesMockMvc;

    private Styles styles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Styles createEntity(EntityManager em) {
        Styles styles = new Styles()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .imgURL(DEFAULT_IMG_URL)
            .isActive(DEFAULT_IS_ACTIVE)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .x(DEFAULT_X)
            .y(DEFAULT_Y);
        return styles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Styles createUpdatedEntity(EntityManager em) {
        Styles styles = new Styles()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgURL(UPDATED_IMG_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y);
        return styles;
    }

    @BeforeEach
    public void initTest() {
        styles = createEntity(em);
    }

    @Test
    @Transactional
    void createStyles() throws Exception {
        int databaseSizeBeforeCreate = stylesRepository.findAll().size();
        // Create the Styles
        restStylesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styles)))
            .andExpect(status().isCreated());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeCreate + 1);
        Styles testStyles = stylesList.get(stylesList.size() - 1);
        assertThat(testStyles.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStyles.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStyles.getImgURL()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testStyles.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testStyles.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testStyles.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testStyles.getX()).isEqualTo(DEFAULT_X);
        assertThat(testStyles.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createStylesWithExistingId() throws Exception {
        // Create the Styles with an existing ID
        styles.setId(1L);

        int databaseSizeBeforeCreate = stylesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStylesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styles)))
            .andExpect(status().isBadRequest());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = stylesRepository.findAll().size();
        // set the field null
        styles.setDescription(null);

        // Create the Styles, which fails.

        restStylesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styles)))
            .andExpect(status().isBadRequest());

        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStyles() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList
        restStylesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(styles.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgURL").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));
    }

    @Test
    @Transactional
    void getStyles() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get the styles
        restStylesMockMvc
            .perform(get(ENTITY_API_URL_ID, styles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(styles.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imgURL").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y));
    }

    @Test
    @Transactional
    void getStylesByIdFiltering() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        Long id = styles.getId();

        defaultStylesShouldBeFound("id.equals=" + id);
        defaultStylesShouldNotBeFound("id.notEquals=" + id);

        defaultStylesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStylesShouldNotBeFound("id.greaterThan=" + id);

        defaultStylesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStylesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStylesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where code equals to DEFAULT_CODE
        defaultStylesShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the stylesList where code equals to UPDATED_CODE
        defaultStylesShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllStylesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where code in DEFAULT_CODE or UPDATED_CODE
        defaultStylesShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the stylesList where code equals to UPDATED_CODE
        defaultStylesShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllStylesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where code is not null
        defaultStylesShouldBeFound("code.specified=true");

        // Get all the stylesList where code is null
        defaultStylesShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByCodeContainsSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where code contains DEFAULT_CODE
        defaultStylesShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the stylesList where code contains UPDATED_CODE
        defaultStylesShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllStylesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where code does not contain DEFAULT_CODE
        defaultStylesShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the stylesList where code does not contain UPDATED_CODE
        defaultStylesShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllStylesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where description equals to DEFAULT_DESCRIPTION
        defaultStylesShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the stylesList where description equals to UPDATED_DESCRIPTION
        defaultStylesShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStylesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultStylesShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the stylesList where description equals to UPDATED_DESCRIPTION
        defaultStylesShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStylesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where description is not null
        defaultStylesShouldBeFound("description.specified=true");

        // Get all the stylesList where description is null
        defaultStylesShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where description contains DEFAULT_DESCRIPTION
        defaultStylesShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the stylesList where description contains UPDATED_DESCRIPTION
        defaultStylesShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStylesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where description does not contain DEFAULT_DESCRIPTION
        defaultStylesShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the stylesList where description does not contain UPDATED_DESCRIPTION
        defaultStylesShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllStylesByImgURLIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where imgURL equals to DEFAULT_IMG_URL
        defaultStylesShouldBeFound("imgURL.equals=" + DEFAULT_IMG_URL);

        // Get all the stylesList where imgURL equals to UPDATED_IMG_URL
        defaultStylesShouldNotBeFound("imgURL.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllStylesByImgURLIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where imgURL in DEFAULT_IMG_URL or UPDATED_IMG_URL
        defaultStylesShouldBeFound("imgURL.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL);

        // Get all the stylesList where imgURL equals to UPDATED_IMG_URL
        defaultStylesShouldNotBeFound("imgURL.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllStylesByImgURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where imgURL is not null
        defaultStylesShouldBeFound("imgURL.specified=true");

        // Get all the stylesList where imgURL is null
        defaultStylesShouldNotBeFound("imgURL.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByImgURLContainsSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where imgURL contains DEFAULT_IMG_URL
        defaultStylesShouldBeFound("imgURL.contains=" + DEFAULT_IMG_URL);

        // Get all the stylesList where imgURL contains UPDATED_IMG_URL
        defaultStylesShouldNotBeFound("imgURL.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllStylesByImgURLNotContainsSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where imgURL does not contain DEFAULT_IMG_URL
        defaultStylesShouldNotBeFound("imgURL.doesNotContain=" + DEFAULT_IMG_URL);

        // Get all the stylesList where imgURL does not contain UPDATED_IMG_URL
        defaultStylesShouldBeFound("imgURL.doesNotContain=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllStylesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where isActive equals to DEFAULT_IS_ACTIVE
        defaultStylesShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the stylesList where isActive equals to UPDATED_IS_ACTIVE
        defaultStylesShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStylesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultStylesShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the stylesList where isActive equals to UPDATED_IS_ACTIVE
        defaultStylesShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStylesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where isActive is not null
        defaultStylesShouldBeFound("isActive.specified=true");

        // Get all the stylesList where isActive is null
        defaultStylesShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width equals to DEFAULT_WIDTH
        defaultStylesShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the stylesList where width equals to UPDATED_WIDTH
        defaultStylesShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultStylesShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the stylesList where width equals to UPDATED_WIDTH
        defaultStylesShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width is not null
        defaultStylesShouldBeFound("width.specified=true");

        // Get all the stylesList where width is null
        defaultStylesShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width is greater than or equal to DEFAULT_WIDTH
        defaultStylesShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the stylesList where width is greater than or equal to UPDATED_WIDTH
        defaultStylesShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width is less than or equal to DEFAULT_WIDTH
        defaultStylesShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the stylesList where width is less than or equal to SMALLER_WIDTH
        defaultStylesShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width is less than DEFAULT_WIDTH
        defaultStylesShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the stylesList where width is less than UPDATED_WIDTH
        defaultStylesShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllStylesByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where width is greater than DEFAULT_WIDTH
        defaultStylesShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the stylesList where width is greater than SMALLER_WIDTH
        defaultStylesShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height equals to DEFAULT_HEIGHT
        defaultStylesShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the stylesList where height equals to UPDATED_HEIGHT
        defaultStylesShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultStylesShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the stylesList where height equals to UPDATED_HEIGHT
        defaultStylesShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height is not null
        defaultStylesShouldBeFound("height.specified=true");

        // Get all the stylesList where height is null
        defaultStylesShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height is greater than or equal to DEFAULT_HEIGHT
        defaultStylesShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the stylesList where height is greater than or equal to UPDATED_HEIGHT
        defaultStylesShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height is less than or equal to DEFAULT_HEIGHT
        defaultStylesShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the stylesList where height is less than or equal to SMALLER_HEIGHT
        defaultStylesShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height is less than DEFAULT_HEIGHT
        defaultStylesShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the stylesList where height is less than UPDATED_HEIGHT
        defaultStylesShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllStylesByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where height is greater than DEFAULT_HEIGHT
        defaultStylesShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the stylesList where height is greater than SMALLER_HEIGHT
        defaultStylesShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllStylesByXIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x equals to DEFAULT_X
        defaultStylesShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the stylesList where x equals to UPDATED_X
        defaultStylesShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStylesByXIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x in DEFAULT_X or UPDATED_X
        defaultStylesShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the stylesList where x equals to UPDATED_X
        defaultStylesShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStylesByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x is not null
        defaultStylesShouldBeFound("x.specified=true");

        // Get all the stylesList where x is null
        defaultStylesShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x is greater than or equal to DEFAULT_X
        defaultStylesShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the stylesList where x is greater than or equal to UPDATED_X
        defaultStylesShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStylesByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x is less than or equal to DEFAULT_X
        defaultStylesShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the stylesList where x is less than or equal to SMALLER_X
        defaultStylesShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllStylesByXIsLessThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x is less than DEFAULT_X
        defaultStylesShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the stylesList where x is less than UPDATED_X
        defaultStylesShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    void getAllStylesByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where x is greater than DEFAULT_X
        defaultStylesShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the stylesList where x is greater than SMALLER_X
        defaultStylesShouldBeFound("x.greaterThan=" + SMALLER_X);
    }

    @Test
    @Transactional
    void getAllStylesByYIsEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y equals to DEFAULT_Y
        defaultStylesShouldBeFound("y.equals=" + DEFAULT_Y);

        // Get all the stylesList where y equals to UPDATED_Y
        defaultStylesShouldNotBeFound("y.equals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllStylesByYIsInShouldWork() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y in DEFAULT_Y or UPDATED_Y
        defaultStylesShouldBeFound("y.in=" + DEFAULT_Y + "," + UPDATED_Y);

        // Get all the stylesList where y equals to UPDATED_Y
        defaultStylesShouldNotBeFound("y.in=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllStylesByYIsNullOrNotNull() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y is not null
        defaultStylesShouldBeFound("y.specified=true");

        // Get all the stylesList where y is null
        defaultStylesShouldNotBeFound("y.specified=false");
    }

    @Test
    @Transactional
    void getAllStylesByYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y is greater than or equal to DEFAULT_Y
        defaultStylesShouldBeFound("y.greaterThanOrEqual=" + DEFAULT_Y);

        // Get all the stylesList where y is greater than or equal to UPDATED_Y
        defaultStylesShouldNotBeFound("y.greaterThanOrEqual=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllStylesByYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y is less than or equal to DEFAULT_Y
        defaultStylesShouldBeFound("y.lessThanOrEqual=" + DEFAULT_Y);

        // Get all the stylesList where y is less than or equal to SMALLER_Y
        defaultStylesShouldNotBeFound("y.lessThanOrEqual=" + SMALLER_Y);
    }

    @Test
    @Transactional
    void getAllStylesByYIsLessThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y is less than DEFAULT_Y
        defaultStylesShouldNotBeFound("y.lessThan=" + DEFAULT_Y);

        // Get all the stylesList where y is less than UPDATED_Y
        defaultStylesShouldBeFound("y.lessThan=" + UPDATED_Y);
    }

    @Test
    @Transactional
    void getAllStylesByYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        // Get all the stylesList where y is greater than DEFAULT_Y
        defaultStylesShouldNotBeFound("y.greaterThan=" + DEFAULT_Y);

        // Get all the stylesList where y is greater than SMALLER_Y
        defaultStylesShouldBeFound("y.greaterThan=" + SMALLER_Y);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStylesShouldBeFound(String filter) throws Exception {
        restStylesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(styles.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgURL").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));

        // Check, that the count call also returns 1
        restStylesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStylesShouldNotBeFound(String filter) throws Exception {
        restStylesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStylesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStyles() throws Exception {
        // Get the styles
        restStylesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStyles() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();

        // Update the styles
        Styles updatedStyles = stylesRepository.findById(styles.getId()).get();
        // Disconnect from session so that the updates on updatedStyles are not directly saved in db
        em.detach(updatedStyles);
        updatedStyles
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgURL(UPDATED_IMG_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restStylesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStyles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStyles))
            )
            .andExpect(status().isOk());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
        Styles testStyles = stylesList.get(stylesList.size() - 1);
        assertThat(testStyles.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStyles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStyles.getImgURL()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testStyles.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testStyles.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testStyles.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testStyles.getX()).isEqualTo(UPDATED_X);
        assertThat(testStyles.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingStyles() throws Exception {
        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();
        styles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStylesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, styles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStyles() throws Exception {
        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();
        styles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(styles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStyles() throws Exception {
        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();
        styles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(styles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStylesWithPatch() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();

        // Update the styles using partial update
        Styles partialUpdatedStyles = new Styles();
        partialUpdatedStyles.setId(styles.getId());

        partialUpdatedStyles
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgURL(UPDATED_IMG_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .x(UPDATED_X);

        restStylesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStyles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStyles))
            )
            .andExpect(status().isOk());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
        Styles testStyles = stylesList.get(stylesList.size() - 1);
        assertThat(testStyles.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStyles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStyles.getImgURL()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testStyles.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testStyles.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testStyles.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testStyles.getX()).isEqualTo(UPDATED_X);
        assertThat(testStyles.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void fullUpdateStylesWithPatch() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();

        // Update the styles using partial update
        Styles partialUpdatedStyles = new Styles();
        partialUpdatedStyles.setId(styles.getId());

        partialUpdatedStyles
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgURL(UPDATED_IMG_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restStylesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStyles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStyles))
            )
            .andExpect(status().isOk());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
        Styles testStyles = stylesList.get(stylesList.size() - 1);
        assertThat(testStyles.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStyles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStyles.getImgURL()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testStyles.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testStyles.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testStyles.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testStyles.getX()).isEqualTo(UPDATED_X);
        assertThat(testStyles.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingStyles() throws Exception {
        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();
        styles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStylesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, styles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(styles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStyles() throws Exception {
        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();
        styles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(styles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStyles() throws Exception {
        int databaseSizeBeforeUpdate = stylesRepository.findAll().size();
        styles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStylesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(styles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Styles in the database
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStyles() throws Exception {
        // Initialize the database
        stylesRepository.saveAndFlush(styles);

        int databaseSizeBeforeDelete = stylesRepository.findAll().size();

        // Delete the styles
        restStylesMockMvc
            .perform(delete(ENTITY_API_URL_ID, styles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Styles> stylesList = stylesRepository.findAll();
        assertThat(stylesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
