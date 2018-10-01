package com.invoister.web.rest;

import com.invoister.InvoisterApp;

import com.invoister.domain.Costumer;
import com.invoister.repository.CostumerRepository;
import com.invoister.repository.search.CostumerSearchRepository;
import com.invoister.service.CostumerService;
import com.invoister.service.dto.CostumerDTO;
import com.invoister.service.mapper.CostumerMapper;
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

import javax.persistence.EntityManager;
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
 * Test class for the CostumerResource REST controller.
 *
 * @see CostumerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoisterApp.class)
public class CostumerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_C_ID = "AAAAAAAAAA";
    private static final String UPDATED_C_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Y&@t.4";
    private static final String UPDATED_EMAIL = "(@J.q_";

    @Autowired
    private CostumerRepository costumerRepository;

    @Autowired
    private CostumerMapper costumerMapper;
    
    @Autowired
    private CostumerService costumerService;

    /**
     * This repository is mocked in the com.invoister.repository.search test package.
     *
     * @see com.invoister.repository.search.CostumerSearchRepositoryMockConfiguration
     */
    @Autowired
    private CostumerSearchRepository mockCostumerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCostumerMockMvc;

    private Costumer costumer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CostumerResource costumerResource = new CostumerResource(costumerService);
        this.restCostumerMockMvc = MockMvcBuilders.standaloneSetup(costumerResource)
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
    public static Costumer createEntity(EntityManager em) {
        Costumer costumer = new Costumer()
            .name(DEFAULT_NAME)
            .cId(DEFAULT_C_ID)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return costumer;
    }

    @Before
    public void initTest() {
        costumer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCostumer() throws Exception {
        int databaseSizeBeforeCreate = costumerRepository.findAll().size();

        // Create the Costumer
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);
        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isCreated());

        // Validate the Costumer in the database
        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeCreate + 1);
        Costumer testCostumer = costumerList.get(costumerList.size() - 1);
        assertThat(testCostumer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCostumer.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testCostumer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCostumer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCostumer.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Costumer in Elasticsearch
        verify(mockCostumerSearchRepository, times(1)).save(testCostumer);
    }

    @Test
    @Transactional
    public void createCostumerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = costumerRepository.findAll().size();

        // Create the Costumer with an existing ID
        costumer.setId(1L);
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Costumer in the database
        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Costumer in Elasticsearch
        verify(mockCostumerSearchRepository, times(0)).save(costumer);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = costumerRepository.findAll().size();
        // set the field null
        costumer.setName(null);

        // Create the Costumer, which fails.
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkcIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = costumerRepository.findAll().size();
        // set the field null
        costumer.setcId(null);

        // Create the Costumer, which fails.
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = costumerRepository.findAll().size();
        // set the field null
        costumer.setAddress(null);

        // Create the Costumer, which fails.
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = costumerRepository.findAll().size();
        // set the field null
        costumer.setPhone(null);

        // Create the Costumer, which fails.
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = costumerRepository.findAll().size();
        // set the field null
        costumer.setEmail(null);

        // Create the Costumer, which fails.
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        restCostumerMockMvc.perform(post("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCostumers() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

        // Get all the costumerList
        restCostumerMockMvc.perform(get("/api/costumers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

        // Get the costumer
        restCostumerMockMvc.perform(get("/api/costumers/{id}", costumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(costumer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cId").value(DEFAULT_C_ID.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCostumer() throws Exception {
        // Get the costumer
        restCostumerMockMvc.perform(get("/api/costumers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

        int databaseSizeBeforeUpdate = costumerRepository.findAll().size();

        // Update the costumer
        Costumer updatedCostumer = costumerRepository.findById(costumer.getId()).get();
        // Disconnect from session so that the updates on updatedCostumer are not directly saved in db
        em.detach(updatedCostumer);
        updatedCostumer
            .name(UPDATED_NAME)
            .cId(UPDATED_C_ID)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        CostumerDTO costumerDTO = costumerMapper.toDto(updatedCostumer);

        restCostumerMockMvc.perform(put("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isOk());

        // Validate the Costumer in the database
        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeUpdate);
        Costumer testCostumer = costumerList.get(costumerList.size() - 1);
        assertThat(testCostumer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCostumer.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testCostumer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCostumer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCostumer.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Costumer in Elasticsearch
        verify(mockCostumerSearchRepository, times(1)).save(testCostumer);
    }

    @Test
    @Transactional
    public void updateNonExistingCostumer() throws Exception {
        int databaseSizeBeforeUpdate = costumerRepository.findAll().size();

        // Create the Costumer
        CostumerDTO costumerDTO = costumerMapper.toDto(costumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostumerMockMvc.perform(put("/api/costumers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(costumerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Costumer in the database
        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Costumer in Elasticsearch
        verify(mockCostumerSearchRepository, times(0)).save(costumer);
    }

    @Test
    @Transactional
    public void deleteCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);

        int databaseSizeBeforeDelete = costumerRepository.findAll().size();

        // Get the costumer
        restCostumerMockMvc.perform(delete("/api/costumers/{id}", costumer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Costumer> costumerList = costumerRepository.findAll();
        assertThat(costumerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Costumer in Elasticsearch
        verify(mockCostumerSearchRepository, times(1)).deleteById(costumer.getId());
    }

    @Test
    @Transactional
    public void searchCostumer() throws Exception {
        // Initialize the database
        costumerRepository.saveAndFlush(costumer);
        when(mockCostumerSearchRepository.search(queryStringQuery("id:" + costumer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(costumer), PageRequest.of(0, 1), 1));
        // Search the costumer
        restCostumerMockMvc.perform(get("/api/_search/costumers?query=id:" + costumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Costumer.class);
        Costumer costumer1 = new Costumer();
        costumer1.setId(1L);
        Costumer costumer2 = new Costumer();
        costumer2.setId(costumer1.getId());
        assertThat(costumer1).isEqualTo(costumer2);
        costumer2.setId(2L);
        assertThat(costumer1).isNotEqualTo(costumer2);
        costumer1.setId(null);
        assertThat(costumer1).isNotEqualTo(costumer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostumerDTO.class);
        CostumerDTO costumerDTO1 = new CostumerDTO();
        costumerDTO1.setId(1L);
        CostumerDTO costumerDTO2 = new CostumerDTO();
        assertThat(costumerDTO1).isNotEqualTo(costumerDTO2);
        costumerDTO2.setId(costumerDTO1.getId());
        assertThat(costumerDTO1).isEqualTo(costumerDTO2);
        costumerDTO2.setId(2L);
        assertThat(costumerDTO1).isNotEqualTo(costumerDTO2);
        costumerDTO1.setId(null);
        assertThat(costumerDTO1).isNotEqualTo(costumerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(costumerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(costumerMapper.fromId(null)).isNull();
    }
}
