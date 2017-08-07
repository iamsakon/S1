package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.MilitaryStatus;
import com.sky.s1.hcm.repository.MilitaryStatusRepository;
import com.sky.s1.hcm.service.MilitaryStatusService;
import com.sky.s1.hcm.repository.search.MilitaryStatusSearchRepository;
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
 * Test class for the MilitaryStatusResource REST controller.
 *
 * @see MilitaryStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class MilitaryStatusResourceIntTest {

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
    private MilitaryStatusRepository militaryStatusRepository;

    @Autowired
    private MilitaryStatusService militaryStatusService;

    @Autowired
    private MilitaryStatusSearchRepository militaryStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMilitaryStatusMockMvc;

    private MilitaryStatus militaryStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MilitaryStatusResource militaryStatusResource = new MilitaryStatusResource(militaryStatusService);
        this.restMilitaryStatusMockMvc = MockMvcBuilders.standaloneSetup(militaryStatusResource)
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
    public static MilitaryStatus createEntity(EntityManager em) {
        MilitaryStatus militaryStatus = new MilitaryStatus()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE);
        return militaryStatus;
    }

    @Before
    public void initTest() {
        militaryStatusSearchRepository.deleteAll();
        militaryStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createMilitaryStatus() throws Exception {
        int databaseSizeBeforeCreate = militaryStatusRepository.findAll().size();

        // Create the MilitaryStatus
        restMilitaryStatusMockMvc.perform(post("/api/military-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(militaryStatus)))
            .andExpect(status().isCreated());

        // Validate the MilitaryStatus in the database
        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeCreate + 1);
        MilitaryStatus testMilitaryStatus = militaryStatusList.get(militaryStatusList.size() - 1);
        assertThat(testMilitaryStatus.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMilitaryStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMilitaryStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMilitaryStatus.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testMilitaryStatus.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testMilitaryStatus.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);

        // Validate the MilitaryStatus in Elasticsearch
        MilitaryStatus militaryStatusEs = militaryStatusSearchRepository.findOne(testMilitaryStatus.getId());
        assertThat(militaryStatusEs).isEqualToComparingFieldByField(testMilitaryStatus);
    }

    @Test
    @Transactional
    public void createMilitaryStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = militaryStatusRepository.findAll().size();

        // Create the MilitaryStatus with an existing ID
        militaryStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilitaryStatusMockMvc.perform(post("/api/military-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(militaryStatus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = militaryStatusRepository.findAll().size();
        // set the field null
        militaryStatus.setCode(null);

        // Create the MilitaryStatus, which fails.

        restMilitaryStatusMockMvc.perform(post("/api/military-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(militaryStatus)))
            .andExpect(status().isBadRequest());

        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = militaryStatusRepository.findAll().size();
        // set the field null
        militaryStatus.setActiveFlag(null);

        // Create the MilitaryStatus, which fails.

        restMilitaryStatusMockMvc.perform(post("/api/military-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(militaryStatus)))
            .andExpect(status().isBadRequest());

        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMilitaryStatuses() throws Exception {
        // Initialize the database
        militaryStatusRepository.saveAndFlush(militaryStatus);

        // Get all the militaryStatusList
        restMilitaryStatusMockMvc.perform(get("/api/military-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(militaryStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void getMilitaryStatus() throws Exception {
        // Initialize the database
        militaryStatusRepository.saveAndFlush(militaryStatus);

        // Get the militaryStatus
        restMilitaryStatusMockMvc.perform(get("/api/military-statuses/{id}", militaryStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(militaryStatus.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMilitaryStatus() throws Exception {
        // Get the militaryStatus
        restMilitaryStatusMockMvc.perform(get("/api/military-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMilitaryStatus() throws Exception {
        // Initialize the database
        militaryStatusService.save(militaryStatus);

        int databaseSizeBeforeUpdate = militaryStatusRepository.findAll().size();

        // Update the militaryStatus
        MilitaryStatus updatedMilitaryStatus = militaryStatusRepository.findOne(militaryStatus.getId());
        updatedMilitaryStatus
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE);

        restMilitaryStatusMockMvc.perform(put("/api/military-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMilitaryStatus)))
            .andExpect(status().isOk());

        // Validate the MilitaryStatus in the database
        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeUpdate);
        MilitaryStatus testMilitaryStatus = militaryStatusList.get(militaryStatusList.size() - 1);
        assertThat(testMilitaryStatus.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMilitaryStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilitaryStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilitaryStatus.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testMilitaryStatus.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testMilitaryStatus.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);

        // Validate the MilitaryStatus in Elasticsearch
        MilitaryStatus militaryStatusEs = militaryStatusSearchRepository.findOne(testMilitaryStatus.getId());
        assertThat(militaryStatusEs).isEqualToComparingFieldByField(testMilitaryStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingMilitaryStatus() throws Exception {
        int databaseSizeBeforeUpdate = militaryStatusRepository.findAll().size();

        // Create the MilitaryStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMilitaryStatusMockMvc.perform(put("/api/military-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(militaryStatus)))
            .andExpect(status().isCreated());

        // Validate the MilitaryStatus in the database
        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMilitaryStatus() throws Exception {
        // Initialize the database
        militaryStatusService.save(militaryStatus);

        int databaseSizeBeforeDelete = militaryStatusRepository.findAll().size();

        // Get the militaryStatus
        restMilitaryStatusMockMvc.perform(delete("/api/military-statuses/{id}", militaryStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean militaryStatusExistsInEs = militaryStatusSearchRepository.exists(militaryStatus.getId());
        assertThat(militaryStatusExistsInEs).isFalse();

        // Validate the database is empty
        List<MilitaryStatus> militaryStatusList = militaryStatusRepository.findAll();
        assertThat(militaryStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMilitaryStatus() throws Exception {
        // Initialize the database
        militaryStatusService.save(militaryStatus);

        // Search the militaryStatus
        restMilitaryStatusMockMvc.perform(get("/api/_search/military-statuses?query=id:" + militaryStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(militaryStatus.getId().intValue())))
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
        TestUtil.equalsVerifier(MilitaryStatus.class);
        MilitaryStatus militaryStatus1 = new MilitaryStatus();
        militaryStatus1.setId(1L);
        MilitaryStatus militaryStatus2 = new MilitaryStatus();
        militaryStatus2.setId(militaryStatus1.getId());
        assertThat(militaryStatus1).isEqualTo(militaryStatus2);
        militaryStatus2.setId(2L);
        assertThat(militaryStatus1).isNotEqualTo(militaryStatus2);
        militaryStatus1.setId(null);
        assertThat(militaryStatus1).isNotEqualTo(militaryStatus2);
    }
}
