package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BooksPage;
import com.mycompany.myapp.domain.PageLayers;
import com.mycompany.myapp.domain.PageLayersDetails;
import com.mycompany.myapp.repository.PageLayersRepository;
import com.mycompany.myapp.service.PageLayersService;
import com.mycompany.myapp.service.criteria.PageLayersCriteria;
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
 * Integration tests for the {@link PageLayersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PageLayersResourceIT {

    private static final Integer DEFAULT_LAYER_NO = 1;
    private static final Integer UPDATED_LAYER_NO = 2;
    private static final Integer SMALLER_LAYER_NO = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_EDITABLE = false;
    private static final Boolean UPDATED_IS_EDITABLE = true;

    private static final Boolean DEFAULT_IS_TEXT = false;
    private static final Boolean UPDATED_IS_TEXT = true;

    private static final String ENTITY_API_URL = "/api/page-layers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PageLayersRepository pageLayersRepository;

    @Mock
    private PageLayersRepository pageLayersRepositoryMock;

    @Mock
    private PageLayersService pageLayersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPageLayersMockMvc;

    private PageLayers pageLayers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageLayers createEntity(EntityManager em) {
        PageLayers pageLayers = new PageLayers()
            .layerNo(DEFAULT_LAYER_NO)
            .isActive(DEFAULT_IS_ACTIVE)
            .isEditable(DEFAULT_IS_EDITABLE)
            .isText(DEFAULT_IS_TEXT);
        return pageLayers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageLayers createUpdatedEntity(EntityManager em) {
        PageLayers pageLayers = new PageLayers()
            .layerNo(UPDATED_LAYER_NO)
            .isActive(UPDATED_IS_ACTIVE)
            .isEditable(UPDATED_IS_EDITABLE)
            .isText(UPDATED_IS_TEXT);
        return pageLayers;
    }

    @BeforeEach
    public void initTest() {
        pageLayers = createEntity(em);
    }

    @Test
    @Transactional
    void createPageLayers() throws Exception {
        int databaseSizeBeforeCreate = pageLayersRepository.findAll().size();
        // Create the PageLayers
        restPageLayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayers)))
            .andExpect(status().isCreated());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeCreate + 1);
        PageLayers testPageLayers = pageLayersList.get(pageLayersList.size() - 1);
        assertThat(testPageLayers.getLayerNo()).isEqualTo(DEFAULT_LAYER_NO);
        assertThat(testPageLayers.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPageLayers.getIsEditable()).isEqualTo(DEFAULT_IS_EDITABLE);
        assertThat(testPageLayers.getIsText()).isEqualTo(DEFAULT_IS_TEXT);
    }

    @Test
    @Transactional
    void createPageLayersWithExistingId() throws Exception {
        // Create the PageLayers with an existing ID
        pageLayers.setId(1L);

        int databaseSizeBeforeCreate = pageLayersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageLayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayers)))
            .andExpect(status().isBadRequest());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLayerNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageLayersRepository.findAll().size();
        // set the field null
        pageLayers.setLayerNo(null);

        // Create the PageLayers, which fails.

        restPageLayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayers)))
            .andExpect(status().isBadRequest());

        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPageLayers() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList
        restPageLayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageLayers.getId().intValue())))
            .andExpect(jsonPath("$.[*].layerNo").value(hasItem(DEFAULT_LAYER_NO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isEditable").value(hasItem(DEFAULT_IS_EDITABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isText").value(hasItem(DEFAULT_IS_TEXT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPageLayersWithEagerRelationshipsIsEnabled() throws Exception {
        when(pageLayersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPageLayersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pageLayersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPageLayersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pageLayersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPageLayersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pageLayersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPageLayers() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get the pageLayers
        restPageLayersMockMvc
            .perform(get(ENTITY_API_URL_ID, pageLayers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pageLayers.getId().intValue()))
            .andExpect(jsonPath("$.layerNo").value(DEFAULT_LAYER_NO))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isEditable").value(DEFAULT_IS_EDITABLE.booleanValue()))
            .andExpect(jsonPath("$.isText").value(DEFAULT_IS_TEXT.booleanValue()));
    }

    @Test
    @Transactional
    void getPageLayersByIdFiltering() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        Long id = pageLayers.getId();

        defaultPageLayersShouldBeFound("id.equals=" + id);
        defaultPageLayersShouldNotBeFound("id.notEquals=" + id);

        defaultPageLayersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPageLayersShouldNotBeFound("id.greaterThan=" + id);

        defaultPageLayersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPageLayersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo equals to DEFAULT_LAYER_NO
        defaultPageLayersShouldBeFound("layerNo.equals=" + DEFAULT_LAYER_NO);

        // Get all the pageLayersList where layerNo equals to UPDATED_LAYER_NO
        defaultPageLayersShouldNotBeFound("layerNo.equals=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo in DEFAULT_LAYER_NO or UPDATED_LAYER_NO
        defaultPageLayersShouldBeFound("layerNo.in=" + DEFAULT_LAYER_NO + "," + UPDATED_LAYER_NO);

        // Get all the pageLayersList where layerNo equals to UPDATED_LAYER_NO
        defaultPageLayersShouldNotBeFound("layerNo.in=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo is not null
        defaultPageLayersShouldBeFound("layerNo.specified=true");

        // Get all the pageLayersList where layerNo is null
        defaultPageLayersShouldNotBeFound("layerNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo is greater than or equal to DEFAULT_LAYER_NO
        defaultPageLayersShouldBeFound("layerNo.greaterThanOrEqual=" + DEFAULT_LAYER_NO);

        // Get all the pageLayersList where layerNo is greater than or equal to UPDATED_LAYER_NO
        defaultPageLayersShouldNotBeFound("layerNo.greaterThanOrEqual=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo is less than or equal to DEFAULT_LAYER_NO
        defaultPageLayersShouldBeFound("layerNo.lessThanOrEqual=" + DEFAULT_LAYER_NO);

        // Get all the pageLayersList where layerNo is less than or equal to SMALLER_LAYER_NO
        defaultPageLayersShouldNotBeFound("layerNo.lessThanOrEqual=" + SMALLER_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsLessThanSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo is less than DEFAULT_LAYER_NO
        defaultPageLayersShouldNotBeFound("layerNo.lessThan=" + DEFAULT_LAYER_NO);

        // Get all the pageLayersList where layerNo is less than UPDATED_LAYER_NO
        defaultPageLayersShouldBeFound("layerNo.lessThan=" + UPDATED_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllPageLayersByLayerNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where layerNo is greater than DEFAULT_LAYER_NO
        defaultPageLayersShouldNotBeFound("layerNo.greaterThan=" + DEFAULT_LAYER_NO);

        // Get all the pageLayersList where layerNo is greater than SMALLER_LAYER_NO
        defaultPageLayersShouldBeFound("layerNo.greaterThan=" + SMALLER_LAYER_NO);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPageLayersShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the pageLayersList where isActive equals to UPDATED_IS_ACTIVE
        defaultPageLayersShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPageLayersShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the pageLayersList where isActive equals to UPDATED_IS_ACTIVE
        defaultPageLayersShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isActive is not null
        defaultPageLayersShouldBeFound("isActive.specified=true");

        // Get all the pageLayersList where isActive is null
        defaultPageLayersShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersByIsEditableIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isEditable equals to DEFAULT_IS_EDITABLE
        defaultPageLayersShouldBeFound("isEditable.equals=" + DEFAULT_IS_EDITABLE);

        // Get all the pageLayersList where isEditable equals to UPDATED_IS_EDITABLE
        defaultPageLayersShouldNotBeFound("isEditable.equals=" + UPDATED_IS_EDITABLE);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsEditableIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isEditable in DEFAULT_IS_EDITABLE or UPDATED_IS_EDITABLE
        defaultPageLayersShouldBeFound("isEditable.in=" + DEFAULT_IS_EDITABLE + "," + UPDATED_IS_EDITABLE);

        // Get all the pageLayersList where isEditable equals to UPDATED_IS_EDITABLE
        defaultPageLayersShouldNotBeFound("isEditable.in=" + UPDATED_IS_EDITABLE);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsEditableIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isEditable is not null
        defaultPageLayersShouldBeFound("isEditable.specified=true");

        // Get all the pageLayersList where isEditable is null
        defaultPageLayersShouldNotBeFound("isEditable.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersByIsTextIsEqualToSomething() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isText equals to DEFAULT_IS_TEXT
        defaultPageLayersShouldBeFound("isText.equals=" + DEFAULT_IS_TEXT);

        // Get all the pageLayersList where isText equals to UPDATED_IS_TEXT
        defaultPageLayersShouldNotBeFound("isText.equals=" + UPDATED_IS_TEXT);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsTextIsInShouldWork() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isText in DEFAULT_IS_TEXT or UPDATED_IS_TEXT
        defaultPageLayersShouldBeFound("isText.in=" + DEFAULT_IS_TEXT + "," + UPDATED_IS_TEXT);

        // Get all the pageLayersList where isText equals to UPDATED_IS_TEXT
        defaultPageLayersShouldNotBeFound("isText.in=" + UPDATED_IS_TEXT);
    }

    @Test
    @Transactional
    void getAllPageLayersByIsTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        // Get all the pageLayersList where isText is not null
        defaultPageLayersShouldBeFound("isText.specified=true");

        // Get all the pageLayersList where isText is null
        defaultPageLayersShouldNotBeFound("isText.specified=false");
    }

    @Test
    @Transactional
    void getAllPageLayersByPageElementDetailsIsEqualToSomething() throws Exception {
        PageLayersDetails pageElementDetails;
        if (TestUtil.findAll(em, PageLayersDetails.class).isEmpty()) {
            pageLayersRepository.saveAndFlush(pageLayers);
            pageElementDetails = PageLayersDetailsResourceIT.createEntity(em);
        } else {
            pageElementDetails = TestUtil.findAll(em, PageLayersDetails.class).get(0);
        }
        em.persist(pageElementDetails);
        em.flush();
        pageLayers.addPageElementDetails(pageElementDetails);
        pageLayersRepository.saveAndFlush(pageLayers);
        Long pageElementDetailsId = pageElementDetails.getId();

        // Get all the pageLayersList where pageElementDetails equals to pageElementDetailsId
        defaultPageLayersShouldBeFound("pageElementDetailsId.equals=" + pageElementDetailsId);

        // Get all the pageLayersList where pageElementDetails equals to (pageElementDetailsId + 1)
        defaultPageLayersShouldNotBeFound("pageElementDetailsId.equals=" + (pageElementDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllPageLayersByBooksPageIsEqualToSomething() throws Exception {
        BooksPage booksPage;
        if (TestUtil.findAll(em, BooksPage.class).isEmpty()) {
            pageLayersRepository.saveAndFlush(pageLayers);
            booksPage = BooksPageResourceIT.createEntity(em);
        } else {
            booksPage = TestUtil.findAll(em, BooksPage.class).get(0);
        }
        em.persist(booksPage);
        em.flush();
        pageLayers.addBooksPage(booksPage);
        pageLayersRepository.saveAndFlush(pageLayers);
        Long booksPageId = booksPage.getId();

        // Get all the pageLayersList where booksPage equals to booksPageId
        defaultPageLayersShouldBeFound("booksPageId.equals=" + booksPageId);

        // Get all the pageLayersList where booksPage equals to (booksPageId + 1)
        defaultPageLayersShouldNotBeFound("booksPageId.equals=" + (booksPageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPageLayersShouldBeFound(String filter) throws Exception {
        restPageLayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageLayers.getId().intValue())))
            .andExpect(jsonPath("$.[*].layerNo").value(hasItem(DEFAULT_LAYER_NO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isEditable").value(hasItem(DEFAULT_IS_EDITABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isText").value(hasItem(DEFAULT_IS_TEXT.booleanValue())));

        // Check, that the count call also returns 1
        restPageLayersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPageLayersShouldNotBeFound(String filter) throws Exception {
        restPageLayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPageLayersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPageLayers() throws Exception {
        // Get the pageLayers
        restPageLayersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPageLayers() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();

        // Update the pageLayers
        PageLayers updatedPageLayers = pageLayersRepository.findById(pageLayers.getId()).get();
        // Disconnect from session so that the updates on updatedPageLayers are not directly saved in db
        em.detach(updatedPageLayers);
        updatedPageLayers.layerNo(UPDATED_LAYER_NO).isActive(UPDATED_IS_ACTIVE).isEditable(UPDATED_IS_EDITABLE).isText(UPDATED_IS_TEXT);

        restPageLayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPageLayers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPageLayers))
            )
            .andExpect(status().isOk());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
        PageLayers testPageLayers = pageLayersList.get(pageLayersList.size() - 1);
        assertThat(testPageLayers.getLayerNo()).isEqualTo(UPDATED_LAYER_NO);
        assertThat(testPageLayers.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPageLayers.getIsEditable()).isEqualTo(UPDATED_IS_EDITABLE);
        assertThat(testPageLayers.getIsText()).isEqualTo(UPDATED_IS_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingPageLayers() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();
        pageLayers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageLayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageLayers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageLayers))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPageLayers() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();
        pageLayers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageLayers))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPageLayers() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();
        pageLayers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageLayers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePageLayersWithPatch() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();

        // Update the pageLayers using partial update
        PageLayers partialUpdatedPageLayers = new PageLayers();
        partialUpdatedPageLayers.setId(pageLayers.getId());

        partialUpdatedPageLayers.layerNo(UPDATED_LAYER_NO).isEditable(UPDATED_IS_EDITABLE);

        restPageLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageLayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageLayers))
            )
            .andExpect(status().isOk());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
        PageLayers testPageLayers = pageLayersList.get(pageLayersList.size() - 1);
        assertThat(testPageLayers.getLayerNo()).isEqualTo(UPDATED_LAYER_NO);
        assertThat(testPageLayers.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPageLayers.getIsEditable()).isEqualTo(UPDATED_IS_EDITABLE);
        assertThat(testPageLayers.getIsText()).isEqualTo(DEFAULT_IS_TEXT);
    }

    @Test
    @Transactional
    void fullUpdatePageLayersWithPatch() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();

        // Update the pageLayers using partial update
        PageLayers partialUpdatedPageLayers = new PageLayers();
        partialUpdatedPageLayers.setId(pageLayers.getId());

        partialUpdatedPageLayers
            .layerNo(UPDATED_LAYER_NO)
            .isActive(UPDATED_IS_ACTIVE)
            .isEditable(UPDATED_IS_EDITABLE)
            .isText(UPDATED_IS_TEXT);

        restPageLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageLayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageLayers))
            )
            .andExpect(status().isOk());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
        PageLayers testPageLayers = pageLayersList.get(pageLayersList.size() - 1);
        assertThat(testPageLayers.getLayerNo()).isEqualTo(UPDATED_LAYER_NO);
        assertThat(testPageLayers.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPageLayers.getIsEditable()).isEqualTo(UPDATED_IS_EDITABLE);
        assertThat(testPageLayers.getIsText()).isEqualTo(UPDATED_IS_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingPageLayers() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();
        pageLayers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pageLayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageLayers))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPageLayers() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();
        pageLayers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageLayers))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPageLayers() throws Exception {
        int databaseSizeBeforeUpdate = pageLayersRepository.findAll().size();
        pageLayers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageLayersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pageLayers))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageLayers in the database
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePageLayers() throws Exception {
        // Initialize the database
        pageLayersRepository.saveAndFlush(pageLayers);

        int databaseSizeBeforeDelete = pageLayersRepository.findAll().size();

        // Delete the pageLayers
        restPageLayersMockMvc
            .perform(delete(ENTITY_API_URL_ID, pageLayers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PageLayers> pageLayersList = pageLayersRepository.findAll();
        assertThat(pageLayersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
