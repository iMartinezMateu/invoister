package com.invoister.web.rest;

import com.invoister.InvoisterApp;

import com.invoister.domain.InvoiceItem;
import com.invoister.repository.InvoiceItemRepository;
import com.invoister.repository.search.InvoiceItemSearchRepository;
import com.invoister.service.InvoiceItemService;
import com.invoister.service.dto.InvoiceItemDTO;
import com.invoister.service.mapper.InvoiceItemMapper;
import com.invoister.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the InvoiceItemResource REST controller.
 *
 * @see InvoiceItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoisterApp.class)
public class InvoiceItemResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Double DEFAULT_GROSS_COST = 1D;
    private static final Double UPDATED_GROSS_COST = 2D;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private InvoiceItemRepository invoiceItemRepositoryMock;

    @Autowired
    private InvoiceItemMapper invoiceItemMapper;
    

    @Mock
    private InvoiceItemService invoiceItemServiceMock;

    @Autowired
    private InvoiceItemService invoiceItemService;

    /**
     * This repository is mocked in the com.invoister.repository.search test package.
     *
     * @see com.invoister.repository.search.InvoiceItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvoiceItemSearchRepository mockInvoiceItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceItemResource invoiceItemResource = new InvoiceItemResource(invoiceItemService);
        this.restInvoiceItemMockMvc = MockMvcBuilders.standaloneSetup(invoiceItemResource)
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
    public static InvoiceItem createEntity(EntityManager em) {
        InvoiceItem invoiceItem = new InvoiceItem()
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .grossCost(DEFAULT_GROSS_COST);
        return invoiceItem;
    }

    @Before
    public void initTest() {
        invoiceItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);
        restInvoiceItemMockMvc.perform(post("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceItem.getGrossCost()).isEqualTo(DEFAULT_GROSS_COST);

        // Validate the InvoiceItem in Elasticsearch
        verify(mockInvoiceItemSearchRepository, times(1)).save(testInvoiceItem);
    }

    @Test
    @Transactional
    public void createInvoiceItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();

        // Create the InvoiceItem with an existing ID
        invoiceItem.setId(1L);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemMockMvc.perform(post("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the InvoiceItem in Elasticsearch
        verify(mockInvoiceItemSearchRepository, times(0)).save(invoiceItem);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setDescription(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc.perform(post("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setQuantity(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc.perform(post("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGrossCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setGrossCost(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc.perform(post("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList
        restInvoiceItemMockMvc.perform(get("/api/invoice-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].grossCost").value(hasItem(DEFAULT_GROSS_COST.doubleValue())));
    }
    
    public void getAllInvoiceItemsWithEagerRelationshipsIsEnabled() throws Exception {
        InvoiceItemResource invoiceItemResource = new InvoiceItemResource(invoiceItemServiceMock);
        when(invoiceItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restInvoiceItemMockMvc = MockMvcBuilders.standaloneSetup(invoiceItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restInvoiceItemMockMvc.perform(get("/api/invoice-items?eagerload=true"))
        .andExpect(status().isOk());

        verify(invoiceItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllInvoiceItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        InvoiceItemResource invoiceItemResource = new InvoiceItemResource(invoiceItemServiceMock);
            when(invoiceItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restInvoiceItemMockMvc = MockMvcBuilders.standaloneSetup(invoiceItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restInvoiceItemMockMvc.perform(get("/api/invoice-items?eagerload=true"))
        .andExpect(status().isOk());

            verify(invoiceItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get("/api/invoice-items/{id}", invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.grossCost").value(DEFAULT_GROSS_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get("/api/invoice-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = invoiceItemRepository.findById(invoiceItem.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceItem are not directly saved in db
        em.detach(updatedInvoiceItem);
        updatedInvoiceItem
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .grossCost(UPDATED_GROSS_COST);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(updatedInvoiceItem);

        restInvoiceItemMockMvc.perform(put("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItem.getGrossCost()).isEqualTo(UPDATED_GROSS_COST);

        // Validate the InvoiceItem in Elasticsearch
        verify(mockInvoiceItemSearchRepository, times(1)).save(testInvoiceItem);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc.perform(put("/api/invoice-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InvoiceItem in Elasticsearch
        verify(mockInvoiceItemSearchRepository, times(0)).save(invoiceItem);
    }

    @Test
    @Transactional
    public void deleteInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeDelete = invoiceItemRepository.findAll().size();

        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(delete("/api/invoice-items/{id}", invoiceItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InvoiceItem in Elasticsearch
        verify(mockInvoiceItemSearchRepository, times(1)).deleteById(invoiceItem.getId());
    }

    @Test
    @Transactional
    public void searchInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);
        when(mockInvoiceItemSearchRepository.search(queryStringQuery("id:" + invoiceItem.getId())))
            .thenReturn(Collections.singletonList(invoiceItem));
        // Search the invoiceItem
        restInvoiceItemMockMvc.perform(get("/api/_search/invoice-items?query=id:" + invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].grossCost").value(hasItem(DEFAULT_GROSS_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItem.class);
        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setId(1L);
        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setId(invoiceItem1.getId());
        assertThat(invoiceItem1).isEqualTo(invoiceItem2);
        invoiceItem2.setId(2L);
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);
        invoiceItem1.setId(null);
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItemDTO.class);
        InvoiceItemDTO invoiceItemDTO1 = new InvoiceItemDTO();
        invoiceItemDTO1.setId(1L);
        InvoiceItemDTO invoiceItemDTO2 = new InvoiceItemDTO();
        assertThat(invoiceItemDTO1).isNotEqualTo(invoiceItemDTO2);
        invoiceItemDTO2.setId(invoiceItemDTO1.getId());
        assertThat(invoiceItemDTO1).isEqualTo(invoiceItemDTO2);
        invoiceItemDTO2.setId(2L);
        assertThat(invoiceItemDTO1).isNotEqualTo(invoiceItemDTO2);
        invoiceItemDTO1.setId(null);
        assertThat(invoiceItemDTO1).isNotEqualTo(invoiceItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceItemMapper.fromId(null)).isNull();
    }
}
