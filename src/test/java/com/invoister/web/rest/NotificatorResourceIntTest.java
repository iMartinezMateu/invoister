package com.invoister.web.rest;

import com.invoister.InvoisterApp;

import com.invoister.domain.Notificator;
import com.invoister.repository.NotificatorRepository;
import com.invoister.repository.search.NotificatorSearchRepository;
import com.invoister.service.NotificatorService;
import com.invoister.service.dto.NotificatorDTO;
import com.invoister.service.mapper.NotificatorMapper;
import com.invoister.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import com.invoister.domain.enumeration.SecureConnection;
/**
 * Test class for the NotificatorResource REST controller.
 *
 * @see NotificatorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoisterApp.class)
public class NotificatorResourceIntTest {

    private static final String DEFAULT_SMTP_HOST = "AAAAAAAAAA";
    private static final String UPDATED_SMTP_HOST = "BBBBBBBBBB";

    private static final Integer DEFAULT_SMTP_PORT = 1;
    private static final Integer UPDATED_SMTP_PORT = 2;

    private static final Boolean DEFAULT_SMTP_AUTH = false;
    private static final Boolean UPDATED_SMTP_AUTH = true;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final SecureConnection DEFAULT_SECURE_CONNECTION = SecureConnection.NONE;
    private static final SecureConnection UPDATED_SECURE_CONNECTION = SecureConnection.SSL;

    @Autowired
    private NotificatorRepository notificatorRepository;

    @Autowired
    private NotificatorMapper notificatorMapper;
    
    @Autowired
    private NotificatorService notificatorService;

    /**
     * This repository is mocked in the com.invoister.repository.search test package.
     *
     * @see com.invoister.repository.search.NotificatorSearchRepositoryMockConfiguration
     */
    @Autowired
    private NotificatorSearchRepository mockNotificatorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificatorMockMvc;

    private Notificator notificator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificatorResource notificatorResource = new NotificatorResource(notificatorService);
        this.restNotificatorMockMvc = MockMvcBuilders.standaloneSetup(notificatorResource)
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
    public static Notificator createEntity(EntityManager em) {
        Notificator notificator = new Notificator()
            .smtpHost(DEFAULT_SMTP_HOST)
            .smtpPort(DEFAULT_SMTP_PORT)
            .smtpAuth(DEFAULT_SMTP_AUTH)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .secureConnection(DEFAULT_SECURE_CONNECTION);
        return notificator;
    }

    @Before
    public void initTest() {
        notificator = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificator() throws Exception {
        int databaseSizeBeforeCreate = notificatorRepository.findAll().size();

        // Create the Notificator
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);
        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isCreated());

        // Validate the Notificator in the database
        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeCreate + 1);
        Notificator testNotificator = notificatorList.get(notificatorList.size() - 1);
        assertThat(testNotificator.getSmtpHost()).isEqualTo(DEFAULT_SMTP_HOST);
        assertThat(testNotificator.getSmtpPort()).isEqualTo(DEFAULT_SMTP_PORT);
        assertThat(testNotificator.isSmtpAuth()).isEqualTo(DEFAULT_SMTP_AUTH);
        assertThat(testNotificator.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testNotificator.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testNotificator.getSecureConnection()).isEqualTo(DEFAULT_SECURE_CONNECTION);

        // Validate the Notificator in Elasticsearch
        verify(mockNotificatorSearchRepository, times(1)).save(testNotificator);
    }

    @Test
    @Transactional
    public void createNotificatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificatorRepository.findAll().size();

        // Create the Notificator with an existing ID
        notificator.setId(1L);
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notificator in the database
        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Notificator in Elasticsearch
        verify(mockNotificatorSearchRepository, times(0)).save(notificator);
    }

    @Test
    @Transactional
    public void checkSmtpHostIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificatorRepository.findAll().size();
        // set the field null
        notificator.setSmtpHost(null);

        // Create the Notificator, which fails.
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSmtpPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificatorRepository.findAll().size();
        // set the field null
        notificator.setSmtpPort(null);

        // Create the Notificator, which fails.
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSmtpAuthIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificatorRepository.findAll().size();
        // set the field null
        notificator.setSmtpAuth(null);

        // Create the Notificator, which fails.
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificatorRepository.findAll().size();
        // set the field null
        notificator.setUsername(null);

        // Create the Notificator, which fails.
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificatorRepository.findAll().size();
        // set the field null
        notificator.setPassword(null);

        // Create the Notificator, which fails.
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecureConnectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificatorRepository.findAll().size();
        // set the field null
        notificator.setSecureConnection(null);

        // Create the Notificator, which fails.
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        restNotificatorMockMvc.perform(post("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificators() throws Exception {
        // Initialize the database
        notificatorRepository.saveAndFlush(notificator);

        // Get all the notificatorList
        restNotificatorMockMvc.perform(get("/api/notificators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificator.getId().intValue())))
            .andExpect(jsonPath("$.[*].smtpHost").value(hasItem(DEFAULT_SMTP_HOST.toString())))
            .andExpect(jsonPath("$.[*].smtpPort").value(hasItem(DEFAULT_SMTP_PORT)))
            .andExpect(jsonPath("$.[*].smtpAuth").value(hasItem(DEFAULT_SMTP_AUTH.booleanValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].secureConnection").value(hasItem(DEFAULT_SECURE_CONNECTION.toString())));
    }
    
    @Test
    @Transactional
    public void getNotificator() throws Exception {
        // Initialize the database
        notificatorRepository.saveAndFlush(notificator);

        // Get the notificator
        restNotificatorMockMvc.perform(get("/api/notificators/{id}", notificator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificator.getId().intValue()))
            .andExpect(jsonPath("$.smtpHost").value(DEFAULT_SMTP_HOST.toString()))
            .andExpect(jsonPath("$.smtpPort").value(DEFAULT_SMTP_PORT))
            .andExpect(jsonPath("$.smtpAuth").value(DEFAULT_SMTP_AUTH.booleanValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.secureConnection").value(DEFAULT_SECURE_CONNECTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificator() throws Exception {
        // Get the notificator
        restNotificatorMockMvc.perform(get("/api/notificators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificator() throws Exception {
        // Initialize the database
        notificatorRepository.saveAndFlush(notificator);

        int databaseSizeBeforeUpdate = notificatorRepository.findAll().size();

        // Update the notificator
        Notificator updatedNotificator = notificatorRepository.findById(notificator.getId()).get();
        // Disconnect from session so that the updates on updatedNotificator are not directly saved in db
        em.detach(updatedNotificator);
        updatedNotificator
            .smtpHost(UPDATED_SMTP_HOST)
            .smtpPort(UPDATED_SMTP_PORT)
            .smtpAuth(UPDATED_SMTP_AUTH)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .secureConnection(UPDATED_SECURE_CONNECTION);
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(updatedNotificator);

        restNotificatorMockMvc.perform(put("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isOk());

        // Validate the Notificator in the database
        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeUpdate);
        Notificator testNotificator = notificatorList.get(notificatorList.size() - 1);
        assertThat(testNotificator.getSmtpHost()).isEqualTo(UPDATED_SMTP_HOST);
        assertThat(testNotificator.getSmtpPort()).isEqualTo(UPDATED_SMTP_PORT);
        assertThat(testNotificator.isSmtpAuth()).isEqualTo(UPDATED_SMTP_AUTH);
        assertThat(testNotificator.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testNotificator.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testNotificator.getSecureConnection()).isEqualTo(UPDATED_SECURE_CONNECTION);

        // Validate the Notificator in Elasticsearch
        verify(mockNotificatorSearchRepository, times(1)).save(testNotificator);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificator() throws Exception {
        int databaseSizeBeforeUpdate = notificatorRepository.findAll().size();

        // Create the Notificator
        NotificatorDTO notificatorDTO = notificatorMapper.toDto(notificator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificatorMockMvc.perform(put("/api/notificators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notificator in the database
        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Notificator in Elasticsearch
        verify(mockNotificatorSearchRepository, times(0)).save(notificator);
    }

    @Test
    @Transactional
    public void deleteNotificator() throws Exception {
        // Initialize the database
        notificatorRepository.saveAndFlush(notificator);

        int databaseSizeBeforeDelete = notificatorRepository.findAll().size();

        // Get the notificator
        restNotificatorMockMvc.perform(delete("/api/notificators/{id}", notificator.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Notificator> notificatorList = notificatorRepository.findAll();
        assertThat(notificatorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Notificator in Elasticsearch
        verify(mockNotificatorSearchRepository, times(1)).deleteById(notificator.getId());
    }

    @Test
    @Transactional
    public void searchNotificator() throws Exception {
        // Initialize the database
        notificatorRepository.saveAndFlush(notificator);
        when(mockNotificatorSearchRepository.search(queryStringQuery("id:" + notificator.getId())))
            .thenReturn(Collections.singletonList(notificator));
        // Search the notificator
        restNotificatorMockMvc.perform(get("/api/_search/notificators?query=id:" + notificator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificator.getId().intValue())))
            .andExpect(jsonPath("$.[*].smtpHost").value(hasItem(DEFAULT_SMTP_HOST.toString())))
            .andExpect(jsonPath("$.[*].smtpPort").value(hasItem(DEFAULT_SMTP_PORT)))
            .andExpect(jsonPath("$.[*].smtpAuth").value(hasItem(DEFAULT_SMTP_AUTH.booleanValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].secureConnection").value(hasItem(DEFAULT_SECURE_CONNECTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notificator.class);
        Notificator notificator1 = new Notificator();
        notificator1.setId(1L);
        Notificator notificator2 = new Notificator();
        notificator2.setId(notificator1.getId());
        assertThat(notificator1).isEqualTo(notificator2);
        notificator2.setId(2L);
        assertThat(notificator1).isNotEqualTo(notificator2);
        notificator1.setId(null);
        assertThat(notificator1).isNotEqualTo(notificator2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificatorDTO.class);
        NotificatorDTO notificatorDTO1 = new NotificatorDTO();
        notificatorDTO1.setId(1L);
        NotificatorDTO notificatorDTO2 = new NotificatorDTO();
        assertThat(notificatorDTO1).isNotEqualTo(notificatorDTO2);
        notificatorDTO2.setId(notificatorDTO1.getId());
        assertThat(notificatorDTO1).isEqualTo(notificatorDTO2);
        notificatorDTO2.setId(2L);
        assertThat(notificatorDTO1).isNotEqualTo(notificatorDTO2);
        notificatorDTO1.setId(null);
        assertThat(notificatorDTO1).isNotEqualTo(notificatorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(notificatorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(notificatorMapper.fromId(null)).isNull();
    }
}
