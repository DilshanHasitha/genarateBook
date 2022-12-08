package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AvatarAttributes;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.BooksAttributes;
import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.domain.BooksRelatedOption;
import com.mycompany.myapp.domain.BooksVariables;
import com.mycompany.myapp.domain.LayerGroup;
import com.mycompany.myapp.domain.PageSize;
import com.mycompany.myapp.domain.PriceRelatedOption;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.service.BooksService;
import com.mycompany.myapp.service.criteria.BooksCriteria;
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
 * Integration tests for the {@link BooksResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BooksResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_NO_OF_PAGES = 1;
    private static final Integer UPDATED_NO_OF_PAGES = 2;
    private static final Integer SMALLER_NO_OF_PAGES = 1 - 1;

    private static final String DEFAULT_STORE_IMG = "AAAAAAAAAA";
    private static final String UPDATED_STORE_IMG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksRepository booksRepository;

    @Mock
    private BooksRepository booksRepositoryMock;

    @Mock
    private BooksService booksServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksMockMvc;

    private Books books;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Books createEntity(EntityManager em) {
        Books books = new Books()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .author(DEFAULT_AUTHOR)
            .isActive(DEFAULT_IS_ACTIVE)
            .noOfPages(DEFAULT_NO_OF_PAGES)
            .storeImg(DEFAULT_STORE_IMG);
        return books;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Books createUpdatedEntity(EntityManager em) {
        Books books = new Books()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .author(UPDATED_AUTHOR)
            .isActive(UPDATED_IS_ACTIVE)
            .noOfPages(UPDATED_NO_OF_PAGES)
            .storeImg(UPDATED_STORE_IMG);
        return books;
    }

    @BeforeEach
    public void initTest() {
        books = createEntity(em);
    }

    @Test
    @Transactional
    void createBooks() throws Exception {
        int databaseSizeBeforeCreate = booksRepository.findAll().size();
        // Create the Books
        restBooksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isCreated());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeCreate + 1);
        Books testBooks = booksList.get(booksList.size() - 1);
        assertThat(testBooks.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooks.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBooks.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBooks.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testBooks.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBooks.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testBooks.getNoOfPages()).isEqualTo(DEFAULT_NO_OF_PAGES);
        assertThat(testBooks.getStoreImg()).isEqualTo(DEFAULT_STORE_IMG);
    }

    @Test
    @Transactional
    void createBooksWithExistingId() throws Exception {
        // Create the Books with an existing ID
        books.setId(1L);

        int databaseSizeBeforeCreate = booksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = booksRepository.findAll().size();
        // set the field null
        books.setName(null);

        // Create the Books, which fails.

        restBooksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isBadRequest());

        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = booksRepository.findAll().size();
        // set the field null
        books.setTitle(null);

        // Create the Books, which fails.

        restBooksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isBadRequest());

        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = booksRepository.findAll().size();
        // set the field null
        books.setIsActive(null);

        // Create the Books, which fails.

        restBooksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isBadRequest());

        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList
        restBooksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(books.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].noOfPages").value(hasItem(DEFAULT_NO_OF_PAGES)))
            .andExpect(jsonPath("$.[*].storeImg").value(hasItem(DEFAULT_STORE_IMG)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(booksServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBooksMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(booksServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(booksServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBooksMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(booksRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get the books
        restBooksMockMvc
            .perform(get(ENTITY_API_URL_ID, books.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(books.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.noOfPages").value(DEFAULT_NO_OF_PAGES))
            .andExpect(jsonPath("$.storeImg").value(DEFAULT_STORE_IMG));
    }

    @Test
    @Transactional
    void getBooksByIdFiltering() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        Long id = books.getId();

        defaultBooksShouldBeFound("id.equals=" + id);
        defaultBooksShouldNotBeFound("id.notEquals=" + id);

        defaultBooksShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where code equals to DEFAULT_CODE
        defaultBooksShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the booksList where code equals to UPDATED_CODE
        defaultBooksShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBooksShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the booksList where code equals to UPDATED_CODE
        defaultBooksShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where code is not null
        defaultBooksShouldBeFound("code.specified=true");

        // Get all the booksList where code is null
        defaultBooksShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByCodeContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where code contains DEFAULT_CODE
        defaultBooksShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the booksList where code contains UPDATED_CODE
        defaultBooksShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where code does not contain DEFAULT_CODE
        defaultBooksShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the booksList where code does not contain UPDATED_CODE
        defaultBooksShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBooksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where name equals to DEFAULT_NAME
        defaultBooksShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the booksList where name equals to UPDATED_NAME
        defaultBooksShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBooksShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the booksList where name equals to UPDATED_NAME
        defaultBooksShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where name is not null
        defaultBooksShouldBeFound("name.specified=true");

        // Get all the booksList where name is null
        defaultBooksShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByNameContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where name contains DEFAULT_NAME
        defaultBooksShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the booksList where name contains UPDATED_NAME
        defaultBooksShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where name does not contain DEFAULT_NAME
        defaultBooksShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the booksList where name does not contain UPDATED_NAME
        defaultBooksShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where title equals to DEFAULT_TITLE
        defaultBooksShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the booksList where title equals to UPDATED_TITLE
        defaultBooksShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBooksShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the booksList where title equals to UPDATED_TITLE
        defaultBooksShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where title is not null
        defaultBooksShouldBeFound("title.specified=true");

        // Get all the booksList where title is null
        defaultBooksShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByTitleContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where title contains DEFAULT_TITLE
        defaultBooksShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the booksList where title contains UPDATED_TITLE
        defaultBooksShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where title does not contain DEFAULT_TITLE
        defaultBooksShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the booksList where title does not contain UPDATED_TITLE
        defaultBooksShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where subTitle equals to DEFAULT_SUB_TITLE
        defaultBooksShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the booksList where subTitle equals to UPDATED_SUB_TITLE
        defaultBooksShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultBooksShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the booksList where subTitle equals to UPDATED_SUB_TITLE
        defaultBooksShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where subTitle is not null
        defaultBooksShouldBeFound("subTitle.specified=true");

        // Get all the booksList where subTitle is null
        defaultBooksShouldNotBeFound("subTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where subTitle contains DEFAULT_SUB_TITLE
        defaultBooksShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the booksList where subTitle contains UPDATED_SUB_TITLE
        defaultBooksShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultBooksShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the booksList where subTitle does not contain UPDATED_SUB_TITLE
        defaultBooksShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where author equals to DEFAULT_AUTHOR
        defaultBooksShouldBeFound("author.equals=" + DEFAULT_AUTHOR);

        // Get all the booksList where author equals to UPDATED_AUTHOR
        defaultBooksShouldNotBeFound("author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllBooksByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where author in DEFAULT_AUTHOR or UPDATED_AUTHOR
        defaultBooksShouldBeFound("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR);

        // Get all the booksList where author equals to UPDATED_AUTHOR
        defaultBooksShouldNotBeFound("author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllBooksByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where author is not null
        defaultBooksShouldBeFound("author.specified=true");

        // Get all the booksList where author is null
        defaultBooksShouldNotBeFound("author.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByAuthorContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where author contains DEFAULT_AUTHOR
        defaultBooksShouldBeFound("author.contains=" + DEFAULT_AUTHOR);

        // Get all the booksList where author contains UPDATED_AUTHOR
        defaultBooksShouldNotBeFound("author.contains=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllBooksByAuthorNotContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where author does not contain DEFAULT_AUTHOR
        defaultBooksShouldNotBeFound("author.doesNotContain=" + DEFAULT_AUTHOR);

        // Get all the booksList where author does not contain UPDATED_AUTHOR
        defaultBooksShouldBeFound("author.doesNotContain=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllBooksByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where isActive is not null
        defaultBooksShouldBeFound("isActive.specified=true");

        // Get all the booksList where isActive is null
        defaultBooksShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages equals to DEFAULT_NO_OF_PAGES
        defaultBooksShouldBeFound("noOfPages.equals=" + DEFAULT_NO_OF_PAGES);

        // Get all the booksList where noOfPages equals to UPDATED_NO_OF_PAGES
        defaultBooksShouldNotBeFound("noOfPages.equals=" + UPDATED_NO_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages in DEFAULT_NO_OF_PAGES or UPDATED_NO_OF_PAGES
        defaultBooksShouldBeFound("noOfPages.in=" + DEFAULT_NO_OF_PAGES + "," + UPDATED_NO_OF_PAGES);

        // Get all the booksList where noOfPages equals to UPDATED_NO_OF_PAGES
        defaultBooksShouldNotBeFound("noOfPages.in=" + UPDATED_NO_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages is not null
        defaultBooksShouldBeFound("noOfPages.specified=true");

        // Get all the booksList where noOfPages is null
        defaultBooksShouldNotBeFound("noOfPages.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages is greater than or equal to DEFAULT_NO_OF_PAGES
        defaultBooksShouldBeFound("noOfPages.greaterThanOrEqual=" + DEFAULT_NO_OF_PAGES);

        // Get all the booksList where noOfPages is greater than or equal to UPDATED_NO_OF_PAGES
        defaultBooksShouldNotBeFound("noOfPages.greaterThanOrEqual=" + UPDATED_NO_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages is less than or equal to DEFAULT_NO_OF_PAGES
        defaultBooksShouldBeFound("noOfPages.lessThanOrEqual=" + DEFAULT_NO_OF_PAGES);

        // Get all the booksList where noOfPages is less than or equal to SMALLER_NO_OF_PAGES
        defaultBooksShouldNotBeFound("noOfPages.lessThanOrEqual=" + SMALLER_NO_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsLessThanSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages is less than DEFAULT_NO_OF_PAGES
        defaultBooksShouldNotBeFound("noOfPages.lessThan=" + DEFAULT_NO_OF_PAGES);

        // Get all the booksList where noOfPages is less than UPDATED_NO_OF_PAGES
        defaultBooksShouldBeFound("noOfPages.lessThan=" + UPDATED_NO_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNoOfPagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where noOfPages is greater than DEFAULT_NO_OF_PAGES
        defaultBooksShouldNotBeFound("noOfPages.greaterThan=" + DEFAULT_NO_OF_PAGES);

        // Get all the booksList where noOfPages is greater than SMALLER_NO_OF_PAGES
        defaultBooksShouldBeFound("noOfPages.greaterThan=" + SMALLER_NO_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByStoreImgIsEqualToSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where storeImg equals to DEFAULT_STORE_IMG
        defaultBooksShouldBeFound("storeImg.equals=" + DEFAULT_STORE_IMG);

        // Get all the booksList where storeImg equals to UPDATED_STORE_IMG
        defaultBooksShouldNotBeFound("storeImg.equals=" + UPDATED_STORE_IMG);
    }

    @Test
    @Transactional
    void getAllBooksByStoreImgIsInShouldWork() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where storeImg in DEFAULT_STORE_IMG or UPDATED_STORE_IMG
        defaultBooksShouldBeFound("storeImg.in=" + DEFAULT_STORE_IMG + "," + UPDATED_STORE_IMG);

        // Get all the booksList where storeImg equals to UPDATED_STORE_IMG
        defaultBooksShouldNotBeFound("storeImg.in=" + UPDATED_STORE_IMG);
    }

    @Test
    @Transactional
    void getAllBooksByStoreImgIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where storeImg is not null
        defaultBooksShouldBeFound("storeImg.specified=true");

        // Get all the booksList where storeImg is null
        defaultBooksShouldNotBeFound("storeImg.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByStoreImgContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where storeImg contains DEFAULT_STORE_IMG
        defaultBooksShouldBeFound("storeImg.contains=" + DEFAULT_STORE_IMG);

        // Get all the booksList where storeImg contains UPDATED_STORE_IMG
        defaultBooksShouldNotBeFound("storeImg.contains=" + UPDATED_STORE_IMG);
    }

    @Test
    @Transactional
    void getAllBooksByStoreImgNotContainsSomething() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        // Get all the booksList where storeImg does not contain DEFAULT_STORE_IMG
        defaultBooksShouldNotBeFound("storeImg.doesNotContain=" + DEFAULT_STORE_IMG);

        // Get all the booksList where storeImg does not contain UPDATED_STORE_IMG
        defaultBooksShouldBeFound("storeImg.doesNotContain=" + UPDATED_STORE_IMG);
    }

    @Test
    @Transactional
    void getAllBooksByPageSizeIsEqualToSomething() throws Exception {
        PageSize pageSize;
        if (TestUtil.findAll(em, PageSize.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            pageSize = PageSizeResourceIT.createEntity(em);
        } else {
            pageSize = TestUtil.findAll(em, PageSize.class).get(0);
        }
        em.persist(pageSize);
        em.flush();
        books.setPageSize(pageSize);
        booksRepository.saveAndFlush(books);
        Long pageSizeId = pageSize.getId();

        // Get all the booksList where pageSize equals to pageSizeId
        defaultBooksShouldBeFound("pageSizeId.equals=" + pageSizeId);

        // Get all the booksList where pageSize equals to (pageSizeId + 1)
        defaultBooksShouldNotBeFound("pageSizeId.equals=" + (pageSizeId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        books.setUser(user);
        booksRepository.saveAndFlush(books);
        Long userId = user.getId();

        // Get all the booksList where user equals to userId
        defaultBooksShouldBeFound("userId.equals=" + userId);

        // Get all the booksList where user equals to (userId + 1)
        defaultBooksShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByBooksPageIsEqualToSomething() throws Exception {
        BooksPage booksPage;
        if (TestUtil.findAll(em, BooksPage.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            booksPage = BooksPageResourceIT.createEntity(em);
        } else {
            booksPage = TestUtil.findAll(em, BooksPage.class).get(0);
        }
        em.persist(booksPage);
        em.flush();
        books.addBooksPage(booksPage);
        booksRepository.saveAndFlush(books);
        Long booksPageId = booksPage.getId();

        // Get all the booksList where booksPage equals to booksPageId
        defaultBooksShouldBeFound("booksPageId.equals=" + booksPageId);

        // Get all the booksList where booksPage equals to (booksPageId + 1)
        defaultBooksShouldNotBeFound("booksPageId.equals=" + (booksPageId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByPriceRelatedOptionIsEqualToSomething() throws Exception {
        PriceRelatedOption priceRelatedOption;
        if (TestUtil.findAll(em, PriceRelatedOption.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            priceRelatedOption = PriceRelatedOptionResourceIT.createEntity(em);
        } else {
            priceRelatedOption = TestUtil.findAll(em, PriceRelatedOption.class).get(0);
        }
        em.persist(priceRelatedOption);
        em.flush();
        books.addPriceRelatedOption(priceRelatedOption);
        booksRepository.saveAndFlush(books);
        Long priceRelatedOptionId = priceRelatedOption.getId();

        // Get all the booksList where priceRelatedOption equals to priceRelatedOptionId
        defaultBooksShouldBeFound("priceRelatedOptionId.equals=" + priceRelatedOptionId);

        // Get all the booksList where priceRelatedOption equals to (priceRelatedOptionId + 1)
        defaultBooksShouldNotBeFound("priceRelatedOptionId.equals=" + (priceRelatedOptionId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByBooksRelatedOptionIsEqualToSomething() throws Exception {
        BooksRelatedOption booksRelatedOption;
        if (TestUtil.findAll(em, BooksRelatedOption.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            booksRelatedOption = BooksRelatedOptionResourceIT.createEntity(em);
        } else {
            booksRelatedOption = TestUtil.findAll(em, BooksRelatedOption.class).get(0);
        }
        em.persist(booksRelatedOption);
        em.flush();
        books.addBooksRelatedOption(booksRelatedOption);
        booksRepository.saveAndFlush(books);
        Long booksRelatedOptionId = booksRelatedOption.getId();

        // Get all the booksList where booksRelatedOption equals to booksRelatedOptionId
        defaultBooksShouldBeFound("booksRelatedOptionId.equals=" + booksRelatedOptionId);

        // Get all the booksList where booksRelatedOption equals to (booksRelatedOptionId + 1)
        defaultBooksShouldNotBeFound("booksRelatedOptionId.equals=" + (booksRelatedOptionId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByBooksAttributesIsEqualToSomething() throws Exception {
        BooksAttributes booksAttributes;
        if (TestUtil.findAll(em, BooksAttributes.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            booksAttributes = BooksAttributesResourceIT.createEntity(em);
        } else {
            booksAttributes = TestUtil.findAll(em, BooksAttributes.class).get(0);
        }
        em.persist(booksAttributes);
        em.flush();
        books.addBooksAttributes(booksAttributes);
        booksRepository.saveAndFlush(books);
        Long booksAttributesId = booksAttributes.getId();

        // Get all the booksList where booksAttributes equals to booksAttributesId
        defaultBooksShouldBeFound("booksAttributesId.equals=" + booksAttributesId);

        // Get all the booksList where booksAttributes equals to (booksAttributesId + 1)
        defaultBooksShouldNotBeFound("booksAttributesId.equals=" + (booksAttributesId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByBooksVariablesIsEqualToSomething() throws Exception {
        BooksVariables booksVariables;
        if (TestUtil.findAll(em, BooksVariables.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            booksVariables = BooksVariablesResourceIT.createEntity(em);
        } else {
            booksVariables = TestUtil.findAll(em, BooksVariables.class).get(0);
        }
        em.persist(booksVariables);
        em.flush();
        books.addBooksVariables(booksVariables);
        booksRepository.saveAndFlush(books);
        Long booksVariablesId = booksVariables.getId();

        // Get all the booksList where booksVariables equals to booksVariablesId
        defaultBooksShouldBeFound("booksVariablesId.equals=" + booksVariablesId);

        // Get all the booksList where booksVariables equals to (booksVariablesId + 1)
        defaultBooksShouldNotBeFound("booksVariablesId.equals=" + (booksVariablesId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByAvatarAttributesIsEqualToSomething() throws Exception {
        AvatarAttributes avatarAttributes;
        if (TestUtil.findAll(em, AvatarAttributes.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            avatarAttributes = AvatarAttributesResourceIT.createEntity(em);
        } else {
            avatarAttributes = TestUtil.findAll(em, AvatarAttributes.class).get(0);
        }
        em.persist(avatarAttributes);
        em.flush();
        books.addAvatarAttributes(avatarAttributes);
        booksRepository.saveAndFlush(books);
        Long avatarAttributesId = avatarAttributes.getId();

        // Get all the booksList where avatarAttributes equals to avatarAttributesId
        defaultBooksShouldBeFound("avatarAttributesId.equals=" + avatarAttributesId);

        // Get all the booksList where avatarAttributes equals to (avatarAttributesId + 1)
        defaultBooksShouldNotBeFound("avatarAttributesId.equals=" + (avatarAttributesId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByLayerGroupIsEqualToSomething() throws Exception {
        LayerGroup layerGroup;
        if (TestUtil.findAll(em, LayerGroup.class).isEmpty()) {
            booksRepository.saveAndFlush(books);
            layerGroup = LayerGroupResourceIT.createEntity(em);
        } else {
            layerGroup = TestUtil.findAll(em, LayerGroup.class).get(0);
        }
        em.persist(layerGroup);
        em.flush();
        books.addLayerGroup(layerGroup);
        booksRepository.saveAndFlush(books);
        Long layerGroupId = layerGroup.getId();

        // Get all the booksList where layerGroup equals to layerGroupId
        defaultBooksShouldBeFound("layerGroupId.equals=" + layerGroupId);

        // Get all the booksList where layerGroup equals to (layerGroupId + 1)
        defaultBooksShouldNotBeFound("layerGroupId.equals=" + (layerGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksShouldBeFound(String filter) throws Exception {
        restBooksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(books.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].noOfPages").value(hasItem(DEFAULT_NO_OF_PAGES)))
            .andExpect(jsonPath("$.[*].storeImg").value(hasItem(DEFAULT_STORE_IMG)));

        // Check, that the count call also returns 1
        restBooksMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksShouldNotBeFound(String filter) throws Exception {
        restBooksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooks() throws Exception {
        // Get the books
        restBooksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        int databaseSizeBeforeUpdate = booksRepository.findAll().size();

        // Update the books
        Books updatedBooks = booksRepository.findById(books.getId()).get();
        // Disconnect from session so that the updates on updatedBooks are not directly saved in db
        em.detach(updatedBooks);
        updatedBooks
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .author(UPDATED_AUTHOR)
            .isActive(UPDATED_IS_ACTIVE)
            .noOfPages(UPDATED_NO_OF_PAGES)
            .storeImg(UPDATED_STORE_IMG);

        restBooksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooks.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooks))
            )
            .andExpect(status().isOk());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
        Books testBooks = booksList.get(booksList.size() - 1);
        assertThat(testBooks.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooks.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBooks.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBooks.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testBooks.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBooks.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testBooks.getNoOfPages()).isEqualTo(UPDATED_NO_OF_PAGES);
        assertThat(testBooks.getStoreImg()).isEqualTo(UPDATED_STORE_IMG);
    }

    @Test
    @Transactional
    void putNonExistingBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();
        books.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, books.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(books))
            )
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();
        books.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(books))
            )
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();
        books.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksWithPatch() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        int databaseSizeBeforeUpdate = booksRepository.findAll().size();

        // Update the books using partial update
        Books partialUpdatedBooks = new Books();
        partialUpdatedBooks.setId(books.getId());

        partialUpdatedBooks.isActive(UPDATED_IS_ACTIVE);

        restBooksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooks))
            )
            .andExpect(status().isOk());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
        Books testBooks = booksList.get(booksList.size() - 1);
        assertThat(testBooks.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBooks.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBooks.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBooks.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testBooks.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBooks.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testBooks.getNoOfPages()).isEqualTo(DEFAULT_NO_OF_PAGES);
        assertThat(testBooks.getStoreImg()).isEqualTo(DEFAULT_STORE_IMG);
    }

    @Test
    @Transactional
    void fullUpdateBooksWithPatch() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        int databaseSizeBeforeUpdate = booksRepository.findAll().size();

        // Update the books using partial update
        Books partialUpdatedBooks = new Books();
        partialUpdatedBooks.setId(books.getId());

        partialUpdatedBooks
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .author(UPDATED_AUTHOR)
            .isActive(UPDATED_IS_ACTIVE)
            .noOfPages(UPDATED_NO_OF_PAGES)
            .storeImg(UPDATED_STORE_IMG);

        restBooksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooks))
            )
            .andExpect(status().isOk());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
        Books testBooks = booksList.get(booksList.size() - 1);
        assertThat(testBooks.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBooks.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBooks.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBooks.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testBooks.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBooks.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testBooks.getNoOfPages()).isEqualTo(UPDATED_NO_OF_PAGES);
        assertThat(testBooks.getStoreImg()).isEqualTo(UPDATED_STORE_IMG);
    }

    @Test
    @Transactional
    void patchNonExistingBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();
        books.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, books.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(books))
            )
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();
        books.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(books))
            )
            .andExpect(status().isBadRequest());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooks() throws Exception {
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();
        books.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(books)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Books in the database
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooks() throws Exception {
        // Initialize the database
        booksRepository.saveAndFlush(books);

        int databaseSizeBeforeDelete = booksRepository.findAll().size();

        // Delete the books
        restBooksMockMvc
            .perform(delete(ENTITY_API_URL_ID, books.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Books> booksList = booksRepository.findAll();
        assertThat(booksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
