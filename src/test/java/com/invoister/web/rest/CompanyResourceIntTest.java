package com.invoister.web.rest;

import com.invoister.InvoisterApp;

import com.invoister.domain.Company;
import com.invoister.repository.CompanyRepository;
import com.invoister.repository.search.CompanySearchRepository;
import com.invoister.service.CompanyService;
import com.invoister.service.dto.CompanyDTO;
import com.invoister.service.mapper.CompanyMapper;
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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoisterApp.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_C_ID = "AAAAAAAAAA";
    private static final String UPDATED_C_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "lM@O}.PD";
    private static final String UPDATED_EMAIL = "Q@J.G";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYPAL_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_PAYPAL_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_SECONDARY_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SECONDARY_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SECONDARY_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SECONDARY_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_STAMP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_STAMP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_STAMP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_STAMP_CONTENT_TYPE = "image/png";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;
    
    @Autowired
    private CompanyService companyService;

    /**
     * This repository is mocked in the com.invoister.repository.search test package.
     *
     * @see com.invoister.repository.search.CompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanySearchRepository mockCompanySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .title(DEFAULT_TITLE)
            .cId(DEFAULT_C_ID)
            .mainPhoneNumber(DEFAULT_MAIN_PHONE_NUMBER)
            .secondaryPhoneNumber(DEFAULT_SECONDARY_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .paypalAccount(DEFAULT_PAYPAL_ACCOUNT)
            .bankAccount(DEFAULT_BANK_ACCOUNT)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .secondaryLogo(DEFAULT_SECONDARY_LOGO)
            .secondaryLogoContentType(DEFAULT_SECONDARY_LOGO_CONTENT_TYPE)
            .stamp(DEFAULT_STAMP)
            .stampContentType(DEFAULT_STAMP_CONTENT_TYPE);
        return company;
    }

    @Before
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCompany.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testCompany.getMainPhoneNumber()).isEqualTo(DEFAULT_MAIN_PHONE_NUMBER);
        assertThat(testCompany.getSecondaryPhoneNumber()).isEqualTo(DEFAULT_SECONDARY_PHONE_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getPaypalAccount()).isEqualTo(DEFAULT_PAYPAL_ACCOUNT);
        assertThat(testCompany.getBankAccount()).isEqualTo(DEFAULT_BANK_ACCOUNT);
        assertThat(testCompany.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCompany.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testCompany.getSecondaryLogo()).isEqualTo(DEFAULT_SECONDARY_LOGO);
        assertThat(testCompany.getSecondaryLogoContentType()).isEqualTo(DEFAULT_SECONDARY_LOGO_CONTENT_TYPE);
        assertThat(testCompany.getStamp()).isEqualTo(DEFAULT_STAMP);
        assertThat(testCompany.getStampContentType()).isEqualTo(DEFAULT_STAMP_CONTENT_TYPE);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setTitle(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkcIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setcId(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMainPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setMainPhoneNumber(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondaryPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setSecondaryPhoneNumber(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setEmail(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setAddress(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.toString())))
            .andExpect(jsonPath("$.[*].mainPhoneNumber").value(hasItem(DEFAULT_MAIN_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].secondaryPhoneNumber").value(hasItem(DEFAULT_SECONDARY_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].paypalAccount").value(hasItem(DEFAULT_PAYPAL_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].secondaryLogoContentType").value(hasItem(DEFAULT_SECONDARY_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].secondaryLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_SECONDARY_LOGO))))
            .andExpect(jsonPath("$.[*].stampContentType").value(hasItem(DEFAULT_STAMP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].stamp").value(hasItem(Base64Utils.encodeToString(DEFAULT_STAMP))));
    }
    
    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.cId").value(DEFAULT_C_ID.toString()))
            .andExpect(jsonPath("$.mainPhoneNumber").value(DEFAULT_MAIN_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.secondaryPhoneNumber").value(DEFAULT_SECONDARY_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.paypalAccount").value(DEFAULT_PAYPAL_ACCOUNT.toString()))
            .andExpect(jsonPath("$.bankAccount").value(DEFAULT_BANK_ACCOUNT.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.secondaryLogoContentType").value(DEFAULT_SECONDARY_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.secondaryLogo").value(Base64Utils.encodeToString(DEFAULT_SECONDARY_LOGO)))
            .andExpect(jsonPath("$.stampContentType").value(DEFAULT_STAMP_CONTENT_TYPE))
            .andExpect(jsonPath("$.stamp").value(Base64Utils.encodeToString(DEFAULT_STAMP)));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .title(UPDATED_TITLE)
            .cId(UPDATED_C_ID)
            .mainPhoneNumber(UPDATED_MAIN_PHONE_NUMBER)
            .secondaryPhoneNumber(UPDATED_SECONDARY_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .paypalAccount(UPDATED_PAYPAL_ACCOUNT)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .secondaryLogo(UPDATED_SECONDARY_LOGO)
            .secondaryLogoContentType(UPDATED_SECONDARY_LOGO_CONTENT_TYPE)
            .stamp(UPDATED_STAMP)
            .stampContentType(UPDATED_STAMP_CONTENT_TYPE);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCompany.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testCompany.getMainPhoneNumber()).isEqualTo(UPDATED_MAIN_PHONE_NUMBER);
        assertThat(testCompany.getSecondaryPhoneNumber()).isEqualTo(UPDATED_SECONDARY_PHONE_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getPaypalAccount()).isEqualTo(UPDATED_PAYPAL_ACCOUNT);
        assertThat(testCompany.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testCompany.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCompany.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testCompany.getSecondaryLogo()).isEqualTo(UPDATED_SECONDARY_LOGO);
        assertThat(testCompany.getSecondaryLogoContentType()).isEqualTo(UPDATED_SECONDARY_LOGO_CONTENT_TYPE);
        assertThat(testCompany.getStamp()).isEqualTo(UPDATED_STAMP);
        assertThat(testCompany.getStampContentType()).isEqualTo(UPDATED_STAMP_CONTENT_TYPE);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).deleteById(company.getId());
    }

    @Test
    @Transactional
    public void searchCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        when(mockCompanySearchRepository.search(queryStringQuery("id:" + company.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(company), PageRequest.of(0, 1), 1));
        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.toString())))
            .andExpect(jsonPath("$.[*].mainPhoneNumber").value(hasItem(DEFAULT_MAIN_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].secondaryPhoneNumber").value(hasItem(DEFAULT_SECONDARY_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].paypalAccount").value(hasItem(DEFAULT_PAYPAL_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].secondaryLogoContentType").value(hasItem(DEFAULT_SECONDARY_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].secondaryLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_SECONDARY_LOGO))))
            .andExpect(jsonPath("$.[*].stampContentType").value(hasItem(DEFAULT_STAMP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].stamp").value(hasItem(Base64Utils.encodeToString(DEFAULT_STAMP))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = new Company();
        company1.setId(1L);
        Company company2 = new Company();
        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);
        company2.setId(2L);
        assertThat(company1).isNotEqualTo(company2);
        company1.setId(null);
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyDTO.class);
        CompanyDTO companyDTO1 = new CompanyDTO();
        companyDTO1.setId(1L);
        CompanyDTO companyDTO2 = new CompanyDTO();
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO2.setId(companyDTO1.getId());
        assertThat(companyDTO1).isEqualTo(companyDTO2);
        companyDTO2.setId(2L);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO1.setId(null);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(companyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(companyMapper.fromId(null)).isNull();
    }
}
