package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.domain.PageLayers;
import com.mycompany.myapp.repository.BooksPageRepository;
import com.mycompany.myapp.service.BooksPageService;
import com.mycompany.myapp.service.criteria.BooksPageCriteria;
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
 * Integration tests for the {@link BooksPageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BooksPageResourceIT {

    private static final Integer DEFAULT_NUM = 1;
    private static final Integer UPDATED_NUM = 2;
    private static final Integer SMALLER_NUM = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/books-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooksPageRepository booksPageRepository;

    @Mock
    private BooksPageRepository booksPageRepositoryMock;

    @Mock
    private BooksPageService booksPageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBooksPageMockMvc;

    private BooksPage booksPage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksPage createEntity(EntityManager em) {
        BooksPage booksPage = new BooksPage().num(DEFAULT_NUM).isActive(DEFAULT_IS_ACTIVE);
        return booksPage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooksPage createUpdatedEntity(EntityManager em) {
        BooksPage booksPage = new BooksPage().num(UPDATED_NUM).isActive(UPDATED_IS_ACTIVE);
        return booksPage;
    }

    @BeforeEach
    public void initTest() {
        booksPage = createEntity(em);
    }

    @Test
    @Transactional
    void createBooksPage() throws Exception {
        int databaseSizeBeforeCreate = booksPageRepository.findAll().size();
        // Create the BooksPage
        restBooksPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksPage)))
            .andExpect(status().isCreated());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeCreate + 1);
        BooksPage testBooksPage = booksPageList.get(booksPageList.size() - 1);
        assertThat(testBooksPage.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testBooksPage.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBooksPageWithExistingId() throws Exception {
        // Create the BooksPage with an existing ID
        booksPage.setId(1L);

        int databaseSizeBeforeCreate = booksPageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBooksPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksPage)))
            .andExpect(status().isBadRequest());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = booksPageRepository.findAll().size();
        // set the field null
        booksPage.setNum(null);

        // Create the BooksPage, which fails.

        restBooksPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksPage)))
            .andExpect(status().isBadRequest());

        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBooksPages() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList
        restBooksPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].num").value(hasItem(DEFAULT_NUM)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksPagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(booksPageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBooksPageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(booksPageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksPagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(booksPageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBooksPageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(booksPageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBooksPage() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get the booksPage
        restBooksPageMockMvc
            .perform(get(ENTITY_API_URL_ID, booksPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booksPage.getId().intValue()))
            .andExpect(jsonPath("$.num").value(DEFAULT_NUM))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBooksPagesByIdFiltering() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        Long id = booksPage.getId();

        defaultBooksPageShouldBeFound("id.equals=" + id);
        defaultBooksPageShouldNotBeFound("id.notEquals=" + id);

        defaultBooksPageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBooksPageShouldNotBeFound("id.greaterThan=" + id);

        defaultBooksPageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBooksPageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsEqualToSomething() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num equals to DEFAULT_NUM
        defaultBooksPageShouldBeFound("num.equals=" + DEFAULT_NUM);

        // Get all the booksPageList where num equals to UPDATED_NUM
        defaultBooksPageShouldNotBeFound("num.equals=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsInShouldWork() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num in DEFAULT_NUM or UPDATED_NUM
        defaultBooksPageShouldBeFound("num.in=" + DEFAULT_NUM + "," + UPDATED_NUM);

        // Get all the booksPageList where num equals to UPDATED_NUM
        defaultBooksPageShouldNotBeFound("num.in=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num is not null
        defaultBooksPageShouldBeFound("num.specified=true");

        // Get all the booksPageList where num is null
        defaultBooksPageShouldNotBeFound("num.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num is greater than or equal to DEFAULT_NUM
        defaultBooksPageShouldBeFound("num.greaterThanOrEqual=" + DEFAULT_NUM);

        // Get all the booksPageList where num is greater than or equal to UPDATED_NUM
        defaultBooksPageShouldNotBeFound("num.greaterThanOrEqual=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num is less than or equal to DEFAULT_NUM
        defaultBooksPageShouldBeFound("num.lessThanOrEqual=" + DEFAULT_NUM);

        // Get all the booksPageList where num is less than or equal to SMALLER_NUM
        defaultBooksPageShouldNotBeFound("num.lessThanOrEqual=" + SMALLER_NUM);
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsLessThanSomething() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num is less than DEFAULT_NUM
        defaultBooksPageShouldNotBeFound("num.lessThan=" + DEFAULT_NUM);

        // Get all the booksPageList where num is less than UPDATED_NUM
        defaultBooksPageShouldBeFound("num.lessThan=" + UPDATED_NUM);
    }

    @Test
    @Transactional
    void getAllBooksPagesByNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where num is greater than DEFAULT_NUM
        defaultBooksPageShouldNotBeFound("num.greaterThan=" + DEFAULT_NUM);

        // Get all the booksPageList where num is greater than SMALLER_NUM
        defaultBooksPageShouldBeFound("num.greaterThan=" + SMALLER_NUM);
    }

    @Test
    @Transactional
    void getAllBooksPagesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBooksPageShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the booksPageList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksPageShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksPagesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBooksPageShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the booksPageList where isActive equals to UPDATED_IS_ACTIVE
        defaultBooksPageShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBooksPagesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        // Get all the booksPageList where isActive is not null
        defaultBooksPageShouldBeFound("isActive.specified=true");

        // Get all the booksPageList where isActive is null
        defaultBooksPageShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksPagesByPageDetailsIsEqualToSomething() throws Exception {
        PageLayers pageDetails;
        if (TestUtil.findAll(em, PageLayers.class).isEmpty()) {
            booksPageRepository.saveAndFlush(booksPage);
            pageDetails = PageLayersResourceIT.createEntity(em);
        } else {
            pageDetails = TestUtil.findAll(em, PageLayers.class).get(0);
        }
        em.persist(pageDetails);
        em.flush();
        booksPage.addPageDetails(pageDetails);
        booksPageRepository.saveAndFlush(booksPage);
        Long pageDetailsId = pageDetails.getId();

        // Get all the booksPageList where pageDetails equals to pageDetailsId
        defaultBooksPageShouldBeFound("pageDetailsId.equals=" + pageDetailsId);

        // Get all the booksPageList where pageDetails equals to (pageDetailsId + 1)
        defaultBooksPageShouldNotBeFound("pageDetailsId.equals=" + (pageDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllBooksPagesByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            booksPageRepository.saveAndFlush(booksPage);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        booksPage.addBooks(books);
        booksPageRepository.saveAndFlush(booksPage);
        Long booksId = books.getId();

        // Get all the booksPageList where books equals to booksId
        defaultBooksPageShouldBeFound("booksId.equals=" + booksId);

        // Get all the booksPageList where books equals to (booksId + 1)
        defaultBooksPageShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBooksPageShouldBeFound(String filter) throws Exception {
        restBooksPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booksPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].num").value(hasItem(DEFAULT_NUM)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBooksPageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBooksPageShouldNotBeFound(String filter) throws Exception {
        restBooksPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBooksPageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooksPage() throws Exception {
        // Get the booksPage
        restBooksPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooksPage() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();

        // Update the booksPage
        BooksPage updatedBooksPage = booksPageRepository.findById(booksPage.getId()).get();
        // Disconnect from session so that the updates on updatedBooksPage are not directly saved in db
        em.detach(updatedBooksPage);
        updatedBooksPage.num(UPDATED_NUM).isActive(UPDATED_IS_ACTIVE);

        restBooksPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBooksPage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBooksPage))
            )
            .andExpect(status().isOk());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
        BooksPage testBooksPage = booksPageList.get(booksPageList.size() - 1);
        assertThat(testBooksPage.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testBooksPage.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBooksPage() throws Exception {
        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();
        booksPage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, booksPage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooksPage() throws Exception {
        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();
        booksPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(booksPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooksPage() throws Exception {
        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();
        booksPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksPageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(booksPage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBooksPageWithPatch() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();

        // Update the booksPage using partial update
        BooksPage partialUpdatedBooksPage = new BooksPage();
        partialUpdatedBooksPage.setId(booksPage.getId());

        partialUpdatedBooksPage.isActive(UPDATED_IS_ACTIVE);

        restBooksPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksPage))
            )
            .andExpect(status().isOk());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
        BooksPage testBooksPage = booksPageList.get(booksPageList.size() - 1);
        assertThat(testBooksPage.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testBooksPage.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBooksPageWithPatch() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();

        // Update the booksPage using partial update
        BooksPage partialUpdatedBooksPage = new BooksPage();
        partialUpdatedBooksPage.setId(booksPage.getId());

        partialUpdatedBooksPage.num(UPDATED_NUM).isActive(UPDATED_IS_ACTIVE);

        restBooksPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooksPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooksPage))
            )
            .andExpect(status().isOk());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
        BooksPage testBooksPage = booksPageList.get(booksPageList.size() - 1);
        assertThat(testBooksPage.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testBooksPage.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBooksPage() throws Exception {
        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();
        booksPage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBooksPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, booksPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooksPage() throws Exception {
        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();
        booksPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(booksPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooksPage() throws Exception {
        int databaseSizeBeforeUpdate = booksPageRepository.findAll().size();
        booksPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBooksPageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(booksPage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BooksPage in the database
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooksPage() throws Exception {
        // Initialize the database
        booksPageRepository.saveAndFlush(booksPage);

        int databaseSizeBeforeDelete = booksPageRepository.findAll().size();

        // Delete the booksPage
        restBooksPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, booksPage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BooksPage> booksPageList = booksPageRepository.findAll();
        assertThat(booksPageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
