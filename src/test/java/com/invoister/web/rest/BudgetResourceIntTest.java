package com.invoister.web.rest;

import com.invoister.InvoisterApp;

import com.invoister.domain.Budget;
import com.invoister.repository.BudgetRepository;
import com.invoister.repository.search.BudgetSearchRepository;
import com.invoister.service.BudgetService;
import com.invoister.service.dto.BudgetDTO;
import com.invoister.service.mapper.BudgetMapper;
import com.invoister.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.invoister.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BudgetResource REST controller.
 *
 * @see BudgetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoisterApp.class)
public class BudgetResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Double DEFAULT_GROSS_COST = 1D;
    private static final Double UPDATED_GROSS_COST = 2D;

    private static final Integer DEFAULT_TAX = 1;
    private static final Integer UPDATED_TAX = 2;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;
    
    @Autowired
    private BudgetService budgetService;

    /**
     * This repository is mocked in the com.invoister.repository.search test package.
     *
     * @see com.invoister.repository.search.BudgetSearchRepositoryMockConfiguration
     */
    @Autowired
    private BudgetSearchRepository mockBudgetSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBudgetMockMvc;

    private Budget budget;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BudgetResource budgetResource = new BudgetResource(budgetService);
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createEntity(EntityManager em) {
        Budget budget = new Budget()
            .date(DEFAULT_DATE)
            .body(DEFAULT_BODY)
            .grossCost(DEFAULT_GROSS_COST)
            .tax(DEFAULT_TAX);
        return budget;
    }

    @Before
    public void initTest() {
        budget = createEntity(em);
    }

    @Test
    @Transactional
    public void createBudget() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);
        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isCreated());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate + 1);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBudget.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testBudget.getGrossCost()).isEqualTo(DEFAULT_GROSS_COST);
        assertThat(testBudget.getTax()).isEqualTo(DEFAULT_TAX);

        // Validate the Budget in Elasticsearch
        verify(mockBudgetSearchRepository, times(1)).save(testBudget);
    }

    @Test
    @Transactional
    public void createBudgetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget with an existing ID
        budget.setId(1L);
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeCreate);

        // Validate the Budget in Elasticsearch
        verify(mockBudgetSearchRepository, times(0)).save(budget);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().size();
        // set the field null
        budget.setDate(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGrossCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().size();
        // set the field null
        budget.setGrossCost(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().size();
        // set the field null
        budget.setTax(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc.perform(post("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBudgets() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get all the budgetList
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].grossCost").value(hasItem(DEFAULT_GROSS_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX)));
    }
    
    @Test
    @Transactional
    public void getBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.grossCost").value(DEFAULT_GROSS_COST.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX));
    }

    @Test
    @Transactional
    public void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Update the budget
        Budget updatedBudget = budgetRepository.findById(budget.getId()).get();
        // Disconnect from session so that the updates on updatedBudget are not directly saved in db
        em.detach(updatedBudget);
        updatedBudget
            .date(UPDATED_DATE)
            .body(UPDATED_BODY)
            .grossCost(UPDATED_GROSS_COST)
            .tax(UPDATED_TAX);
        BudgetDTO budgetDTO = budgetMapper.toDto(updatedBudget);

        restBudgetMockMvc.perform(put("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isOk());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgetList.get(budgetList.size() - 1);
        assertThat(testBudget.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBudget.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testBudget.getGrossCost()).isEqualTo(UPDATED_GROSS_COST);
        assertThat(testBudget.getTax()).isEqualTo(UPDATED_TAX);

        // Validate the Budget in Elasticsearch
        verify(mockBudgetSearchRepository, times(1)).save(testBudget);
    }

    @Test
    @Transactional
    public void updateNonExistingBudget() throws Exception {
        int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc.perform(put("/api/budgets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Budget in Elasticsearch
        verify(mockBudgetSearchRepository, times(0)).save(budget);
    }

    @Test
    @Transactional
    public void deleteBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        int databaseSizeBeforeDelete = budgetRepository.findAll().size();

        // Get the budget
        restBudgetMockMvc.perform(delete("/api/budgets/{id}", budget.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Budget> budgetList = budgetRepository.findAll();
        assertThat(budgetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Budget in Elasticsearch
        verify(mockBudgetSearchRepository, times(1)).deleteById(budget.getId());
    }

    @Test
    @Transactional
    public void searchBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);
        when(mockBudgetSearchRepository.search(queryStringQuery("id:" + budget.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(budget), PageRequest.of(0, 1), 1));
        // Search the budget
        restBudgetMockMvc.perform(get("/api/_search/budgets?query=id:" + budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].grossCost").value(hasItem(DEFAULT_GROSS_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Budget.class);
        Budget budget1 = new Budget();
        budget1.setId(1L);
        Budget budget2 = new Budget();
        budget2.setId(budget1.getId());
        assertThat(budget1).isEqualTo(budget2);
        budget2.setId(2L);
        assertThat(budget1).isNotEqualTo(budget2);
        budget1.setId(null);
        assertThat(budget1).isNotEqualTo(budget2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetDTO.class);
        BudgetDTO budgetDTO1 = new BudgetDTO();
        budgetDTO1.setId(1L);
        BudgetDTO budgetDTO2 = new BudgetDTO();
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2);
        budgetDTO2.setId(budgetDTO1.getId());
        assertThat(budgetDTO1).isEqualTo(budgetDTO2);
        budgetDTO2.setId(2L);
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2);
        budgetDTO1.setId(null);
        assertThat(budgetDTO1).isNotEqualTo(budgetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(budgetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(budgetMapper.fromId(null)).isNull();
    }
}
