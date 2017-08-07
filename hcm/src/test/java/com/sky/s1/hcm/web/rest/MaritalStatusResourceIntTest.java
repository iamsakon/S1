package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.MaritalStatus;
import com.sky.s1.hcm.repository.MaritalStatusRepository;
import com.sky.s1.hcm.service.MaritalStatusService;
import com.sky.s1.hcm.repository.search.MaritalStatusSearchRepository;
import com.sky.s1.hcm.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MaritalStatusResource REST controller.
 *
 * @see MaritalStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class MaritalStatusResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_FLAG = false;
    private static final Boolean UPDATED_ACTIVE_FLAG = true;

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_COMPANY_CODE = 1L;
    private static final Long UPDATED_COMPANY_CODE = 2L;

    @Autowired
    private MaritalStatusRepository maritalStatusRepository;

    @Autowired
    private MaritalStatusService maritalStatusService;

    @Autowired
    private MaritalStatusSearchRepository maritalStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaritalStatusMockMvc;

    private MaritalStatus maritalStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MaritalStatusResource maritalStatusResource = new MaritalStatusResource(maritalStatusService);
        this.restMaritalStatusMockMvc = MockMvcBuilders.standaloneSetup(maritalStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaritalStatus createEntity(EntityManager em) {
        MaritalStatus maritalStatus = new MaritalStatus()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE);
        return maritalStatus;
    }

    @Before
    public void initTest() {
        maritalStatusSearchRepository.deleteAll();
        maritalStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaritalStatus() throws Exception {
        int databaseSizeBeforeCreate = maritalStatusRepository.findAll().size();

        // Create the MaritalStatus
        restMaritalStatusMockMvc.perform(post("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isCreated());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate + 1);
        MaritalStatus testMaritalStatus = maritalStatusList.get(maritalStatusList.size() - 1);
        assertThat(testMaritalStatus.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMaritalStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaritalStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaritalStatus.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testMaritalStatus.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testMaritalStatus.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);

        // Validate the MaritalStatus in Elasticsearch
        MaritalStatus maritalStatusEs = maritalStatusSearchRepository.findOne(testMaritalStatus.getId());
        assertThat(maritalStatusEs).isEqualToComparingFieldByField(testMaritalStatus);
    }

    @Test
    @Transactional
    public void createMaritalStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maritalStatusRepository.findAll().size();

        // Create the MaritalStatus with an existing ID
        maritalStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaritalStatusMockMvc.perform(post("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = maritalStatusRepository.findAll().size();
        // set the field null
        maritalStatus.setCode(null);

        // Create the MaritalStatus, which fails.

        restMaritalStatusMockMvc.perform(post("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isBadRequest());

        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = maritalStatusRepository.findAll().size();
        // set the field null
        maritalStatus.setActiveFlag(null);

        // Create the MaritalStatus, which fails.

        restMaritalStatusMockMvc.perform(post("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isBadRequest());

        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaritalStatuses() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);

        // Get all the maritalStatusList
        restMaritalStatusMockMvc.perform(get("/api/marital-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maritalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void getMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);

        // Get the maritalStatus
        restMaritalStatusMockMvc.perform(get("/api/marital-statuses/{id}", maritalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(maritalStatus.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMaritalStatus() throws Exception {
        // Get the maritalStatus
        restMaritalStatusMockMvc.perform(get("/api/marital-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusService.save(maritalStatus);

        int databaseSizeBeforeUpdate = maritalStatusRepository.findAll().size();

        // Update the maritalStatus
        MaritalStatus updatedMaritalStatus = maritalStatusRepository.findOne(maritalStatus.getId());
        updatedMaritalStatus
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE);

        restMaritalStatusMockMvc.perform(put("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaritalStatus)))
            .andExpect(status().isOk());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate);
        MaritalStatus testMaritalStatus = maritalStatusList.get(maritalStatusList.size() - 1);
        assertThat(testMaritalStatus.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMaritalStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaritalStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaritalStatus.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testMaritalStatus.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testMaritalStatus.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);

        // Validate the MaritalStatus in Elasticsearch
        MaritalStatus maritalStatusEs = maritalStatusSearchRepository.findOne(testMaritalStatus.getId());
        assertThat(maritalStatusEs).isEqualToComparingFieldByField(testMaritalStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingMaritalStatus() throws Exception {
        int databaseSizeBeforeUpdate = maritalStatusRepository.findAll().size();

        // Create the MaritalStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMaritalStatusMockMvc.perform(put("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isCreated());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusService.save(maritalStatus);

        int databaseSizeBeforeDelete = maritalStatusRepository.findAll().size();

        // Get the maritalStatus
        restMaritalStatusMockMvc.perform(delete("/api/marital-statuses/{id}", maritalStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean maritalStatusExistsInEs = maritalStatusSearchRepository.exists(maritalStatus.getId());
        assertThat(maritalStatusExistsInEs).isFalse();

        // Validate the database is empty
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusService.save(maritalStatus);

        // Search the maritalStatus
        restMaritalStatusMockMvc.perform(get("/api/_search/marital-statuses?query=id:" + maritalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maritalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaritalStatus.class);
        MaritalStatus maritalStatus1 = new MaritalStatus();
        maritalStatus1.setId(1L);
        MaritalStatus maritalStatus2 = new MaritalStatus();
        maritalStatus2.setId(maritalStatus1.getId());
        assertThat(maritalStatus1).isEqualTo(maritalStatus2);
        maritalStatus2.setId(2L);
        assertThat(maritalStatus1).isNotEqualTo(maritalStatus2);
        maritalStatus1.setId(null);
        assertThat(maritalStatus1).isNotEqualTo(maritalStatus2);
    }
}
