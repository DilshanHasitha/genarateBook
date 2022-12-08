package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.SelectedOption;
import com.mycompany.myapp.domain.SelectedOptionDetails;
import com.mycompany.myapp.repository.SelectedOptionRepository;
import com.mycompany.myapp.service.SelectedOptionService;
import com.mycompany.myapp.service.criteria.SelectedOptionCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SelectedOptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SelectedOptionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/selected-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SelectedOptionRepository selectedOptionRepository;

    @Mock
    private SelectedOptionRepository selectedOptionRepositoryMock;

    @Mock
    private SelectedOptionService selectedOptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSelectedOptionMockMvc;

    private SelectedOption selectedOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelectedOption createEntity(EntityManager em) {
        SelectedOption selectedOption = new SelectedOption().code(DEFAULT_CODE).date(DEFAULT_DATE);
        return selectedOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SelectedOption createUpdatedEntity(EntityManager em) {
        SelectedOption selectedOption = new SelectedOption().code(UPDATED_CODE).date(UPDATED_DATE);
        return selectedOption;
    }

    @BeforeEach
    public void initTest() {
        selectedOption = createEntity(em);
    }

    @Test
    @Transactional
    void createSelectedOption() throws Exception {
        int databaseSizeBeforeCreate = selectedOptionRepository.findAll().size();
        // Create the SelectedOption
        restSelectedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isCreated());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeCreate + 1);
        SelectedOption testSelectedOption = selectedOptionList.get(selectedOptionList.size() - 1);
        assertThat(testSelectedOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSelectedOption.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createSelectedOptionWithExistingId() throws Exception {
        // Create the SelectedOption with an existing ID
        selectedOption.setId(1L);

        int databaseSizeBeforeCreate = selectedOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSelectedOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSelectedOptions() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList
        restSelectedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSelectedOptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(selectedOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSelectedOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(selectedOptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSelectedOptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(selectedOptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSelectedOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(selectedOptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSelectedOption() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get the selectedOption
        restSelectedOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, selectedOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(selectedOption.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getSelectedOptionsByIdFiltering() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        Long id = selectedOption.getId();

        defaultSelectedOptionShouldBeFound("id.equals=" + id);
        defaultSelectedOptionShouldNotBeFound("id.notEquals=" + id);

        defaultSelectedOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSelectedOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultSelectedOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSelectedOptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where code equals to DEFAULT_CODE
        defaultSelectedOptionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the selectedOptionList where code equals to UPDATED_CODE
        defaultSelectedOptionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSelectedOptionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the selectedOptionList where code equals to UPDATED_CODE
        defaultSelectedOptionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where code is not null
        defaultSelectedOptionShouldBeFound("code.specified=true");

        // Get all the selectedOptionList where code is null
        defaultSelectedOptionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where code contains DEFAULT_CODE
        defaultSelectedOptionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the selectedOptionList where code contains UPDATED_CODE
        defaultSelectedOptionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where code does not contain DEFAULT_CODE
        defaultSelectedOptionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the selectedOptionList where code does not contain UPDATED_CODE
        defaultSelectedOptionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date equals to DEFAULT_DATE
        defaultSelectedOptionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the selectedOptionList where date equals to UPDATED_DATE
        defaultSelectedOptionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultSelectedOptionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the selectedOptionList where date equals to UPDATED_DATE
        defaultSelectedOptionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date is not null
        defaultSelectedOptionShouldBeFound("date.specified=true");

        // Get all the selectedOptionList where date is null
        defaultSelectedOptionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date is greater than or equal to DEFAULT_DATE
        defaultSelectedOptionShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the selectedOptionList where date is greater than or equal to UPDATED_DATE
        defaultSelectedOptionShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date is less than or equal to DEFAULT_DATE
        defaultSelectedOptionShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the selectedOptionList where date is less than or equal to SMALLER_DATE
        defaultSelectedOptionShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date is less than DEFAULT_DATE
        defaultSelectedOptionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the selectedOptionList where date is less than UPDATED_DATE
        defaultSelectedOptionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        // Get all the selectedOptionList where date is greater than DEFAULT_DATE
        defaultSelectedOptionShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the selectedOptionList where date is greater than SMALLER_DATE
        defaultSelectedOptionShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByBooksIsEqualToSomething() throws Exception {
        Books books;
        if (TestUtil.findAll(em, Books.class).isEmpty()) {
            selectedOptionRepository.saveAndFlush(selectedOption);
            books = BooksResourceIT.createEntity(em);
        } else {
            books = TestUtil.findAll(em, Books.class).get(0);
        }
        em.persist(books);
        em.flush();
        selectedOption.setBooks(books);
        selectedOptionRepository.saveAndFlush(selectedOption);
        Long booksId = books.getId();

        // Get all the selectedOptionList where books equals to booksId
        defaultSelectedOptionShouldBeFound("booksId.equals=" + booksId);

        // Get all the selectedOptionList where books equals to (booksId + 1)
        defaultSelectedOptionShouldNotBeFound("booksId.equals=" + (booksId + 1));
    }

    @Test
    @Transactional
    void getAllSelectedOptionsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            selectedOptionRepository.saveAndFlush(selectedOption);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        selectedOption.setCustomer(customer);
        selectedOptionRepository.saveAndFlush(selectedOption);
        Long customerId = customer.getId();

        // Get all the selectedOptionList where customer equals to customerId
        defaultSelectedOptionShouldBeFound("customerId.equals=" + customerId);

        // Get all the selectedOptionList where customer equals to (customerId + 1)
        defaultSelectedOptionShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllSelectedOptionsBySelectedOptionDetailsIsEqualToSomething() throws Exception {
        SelectedOptionDetails selectedOptionDetails;
        if (TestUtil.findAll(em, SelectedOptionDetails.class).isEmpty()) {
            selectedOptionRepository.saveAndFlush(selectedOption);
            selectedOptionDetails = SelectedOptionDetailsResourceIT.createEntity(em);
        } else {
            selectedOptionDetails = TestUtil.findAll(em, SelectedOptionDetails.class).get(0);
        }
        em.persist(selectedOptionDetails);
        em.flush();
        selectedOption.addSelectedOptionDetails(selectedOptionDetails);
        selectedOptionRepository.saveAndFlush(selectedOption);
        Long selectedOptionDetailsId = selectedOptionDetails.getId();

        // Get all the selectedOptionList where selectedOptionDetails equals to selectedOptionDetailsId
        defaultSelectedOptionShouldBeFound("selectedOptionDetailsId.equals=" + selectedOptionDetailsId);

        // Get all the selectedOptionList where selectedOptionDetails equals to (selectedOptionDetailsId + 1)
        defaultSelectedOptionShouldNotBeFound("selectedOptionDetailsId.equals=" + (selectedOptionDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSelectedOptionShouldBeFound(String filter) throws Exception {
        restSelectedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectedOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restSelectedOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSelectedOptionShouldNotBeFound(String filter) throws Exception {
        restSelectedOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSelectedOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSelectedOption() throws Exception {
        // Get the selectedOption
        restSelectedOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSelectedOption() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();

        // Update the selectedOption
        SelectedOption updatedSelectedOption = selectedOptionRepository.findById(selectedOption.getId()).get();
        // Disconnect from session so that the updates on updatedSelectedOption are not directly saved in db
        em.detach(updatedSelectedOption);
        updatedSelectedOption.code(UPDATED_CODE).date(UPDATED_DATE);

        restSelectedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSelectedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSelectedOption))
            )
            .andExpect(status().isOk());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
        SelectedOption testSelectedOption = selectedOptionList.get(selectedOptionList.size() - 1);
        assertThat(testSelectedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSelectedOption.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSelectedOption() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();
        selectedOption.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, selectedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSelectedOption() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();
        selectedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSelectedOption() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();
        selectedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(selectedOption)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSelectedOptionWithPatch() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();

        // Update the selectedOption using partial update
        SelectedOption partialUpdatedSelectedOption = new SelectedOption();
        partialUpdatedSelectedOption.setId(selectedOption.getId());

        restSelectedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelectedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSelectedOption))
            )
            .andExpect(status().isOk());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
        SelectedOption testSelectedOption = selectedOptionList.get(selectedOptionList.size() - 1);
        assertThat(testSelectedOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSelectedOption.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSelectedOptionWithPatch() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();

        // Update the selectedOption using partial update
        SelectedOption partialUpdatedSelectedOption = new SelectedOption();
        partialUpdatedSelectedOption.setId(selectedOption.getId());

        partialUpdatedSelectedOption.code(UPDATED_CODE).date(UPDATED_DATE);

        restSelectedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSelectedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSelectedOption))
            )
            .andExpect(status().isOk());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
        SelectedOption testSelectedOption = selectedOptionList.get(selectedOptionList.size() - 1);
        assertThat(testSelectedOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSelectedOption.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSelectedOption() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();
        selectedOption.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSelectedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, selectedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSelectedOption() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();
        selectedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isBadRequest());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSelectedOption() throws Exception {
        int databaseSizeBeforeUpdate = selectedOptionRepository.findAll().size();
        selectedOption.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSelectedOptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(selectedOption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SelectedOption in the database
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSelectedOption() throws Exception {
        // Initialize the database
        selectedOptionRepository.saveAndFlush(selectedOption);

        int databaseSizeBeforeDelete = selectedOptionRepository.findAll().size();

        // Delete the selectedOption
        restSelectedOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, selectedOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SelectedOption> selectedOptionList = selectedOptionRepository.findAll();
        assertThat(selectedOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
