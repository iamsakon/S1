package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.ProbationPeriod;
import com.sky.s1.hcm.repository.ProbationPeriodRepository;
import com.sky.s1.hcm.service.ProbationPeriodService;
import com.sky.s1.hcm.repository.search.ProbationPeriodSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProbationPeriodResource REST controller.
 *
 * @see ProbationPeriodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class ProbationPeriodResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_COMPANY_CODE = 1L;
    private static final Long UPDATED_COMPANY_CODE = 2L;

    private static final LocalDate DEFAULT_PERIOD_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EVALUATE_PERIOD_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATE_PERIOD_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EVALUATE_PERIOD_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVALUATE_PERIOD_END = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ProbationPeriodRepository probationPeriodRepository;

    @Autowired
    private ProbationPeriodService probationPeriodService;

    @Autowired
    private ProbationPeriodSearchRepository probationPeriodSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProbationPeriodMockMvc;

    private ProbationPeriod probationPeriod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProbationPeriodResource probationPeriodResource = new ProbationPeriodResource(probationPeriodService);
        this.restProbationPeriodMockMvc = MockMvcBuilders.standaloneSetup(probationPeriodResource)
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
    public static ProbationPeriod createEntity(EntityManager em) {
        ProbationPeriod probationPeriod = new ProbationPeriod()
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE)
            .periodStart(DEFAULT_PERIOD_START)
            .periodEnd(DEFAULT_PERIOD_END)
            .evaluatePeriodStart(DEFAULT_EVALUATE_PERIOD_START)
            .evaluatePeriodEnd(DEFAULT_EVALUATE_PERIOD_END);
        return probationPeriod;
    }

    @Before
    public void initTest() {
        probationPeriodSearchRepository.deleteAll();
        probationPeriod = createEntity(em);
    }

    @Test
    @Transactional
    public void createProbationPeriod() throws Exception {
        int databaseSizeBeforeCreate = probationPeriodRepository.findAll().size();

        // Create the ProbationPeriod
        restProbationPeriodMockMvc.perform(post("/api/probation-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(probationPeriod)))
            .andExpect(status().isCreated());

        // Validate the ProbationPeriod in the database
        List<ProbationPeriod> probationPeriodList = probationPeriodRepository.findAll();
        assertThat(probationPeriodList).hasSize(databaseSizeBeforeCreate + 1);
        ProbationPeriod testProbationPeriod = probationPeriodList.get(probationPeriodList.size() - 1);
        assertThat(testProbationPeriod.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testProbationPeriod.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);
        assertThat(testProbationPeriod.getPeriodStart()).isEqualTo(DEFAULT_PERIOD_START);
        assertThat(testProbationPeriod.getPeriodEnd()).isEqualTo(DEFAULT_PERIOD_END);
        assertThat(testProbationPeriod.getEvaluatePeriodStart()).isEqualTo(DEFAULT_EVALUATE_PERIOD_START);
        assertThat(testProbationPeriod.getEvaluatePeriodEnd()).isEqualTo(DEFAULT_EVALUATE_PERIOD_END);

        // Validate the ProbationPeriod in Elasticsearch
        ProbationPeriod probationPeriodEs = probationPeriodSearchRepository.findOne(testProbationPeriod.getId());
        assertThat(probationPeriodEs).isEqualToComparingFieldByField(testProbationPeriod);
    }

    @Test
    @Transactional
    public void createProbationPeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = probationPeriodRepository.findAll().size();

        // Create the ProbationPeriod with an existing ID
        probationPeriod.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProbationPeriodMockMvc.perform(post("/api/probation-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(probationPeriod)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProbationPeriod> probationPeriodList = probationPeriodRepository.findAll();
        assertThat(probationPeriodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProbationPeriods() throws Exception {
        // Initialize the database
        probationPeriodRepository.saveAndFlush(probationPeriod);

        // Get all the probationPeriodList
        restProbationPeriodMockMvc.perform(get("/api/probation-periods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(probationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())))
            .andExpect(jsonPath("$.[*].periodStart").value(hasItem(DEFAULT_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].evaluatePeriodStart").value(hasItem(DEFAULT_EVALUATE_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].evaluatePeriodEnd").value(hasItem(DEFAULT_EVALUATE_PERIOD_END.toString())));
    }

    @Test
    @Transactional
    public void getProbationPeriod() throws Exception {
        // Initialize the database
        probationPeriodRepository.saveAndFlush(probationPeriod);

        // Get the probationPeriod
        restProbationPeriodMockMvc.perform(get("/api/probation-periods/{id}", probationPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(probationPeriod.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()))
            .andExpect(jsonPath("$.periodStart").value(DEFAULT_PERIOD_START.toString()))
            .andExpect(jsonPath("$.periodEnd").value(DEFAULT_PERIOD_END.toString()))
            .andExpect(jsonPath("$.evaluatePeriodStart").value(DEFAULT_EVALUATE_PERIOD_START.toString()))
            .andExpect(jsonPath("$.evaluatePeriodEnd").value(DEFAULT_EVALUATE_PERIOD_END.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProbationPeriod() throws Exception {
        // Get the probationPeriod
        restProbationPeriodMockMvc.perform(get("/api/probation-periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProbationPeriod() throws Exception {
        // Initialize the database
        probationPeriodService.save(probationPeriod);

        int databaseSizeBeforeUpdate = probationPeriodRepository.findAll().size();

        // Update the probationPeriod
        ProbationPeriod updatedProbationPeriod = probationPeriodRepository.findOne(probationPeriod.getId());
        updatedProbationPeriod
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE)
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .evaluatePeriodStart(UPDATED_EVALUATE_PERIOD_START)
            .evaluatePeriodEnd(UPDATED_EVALUATE_PERIOD_END);

        restProbationPeriodMockMvc.perform(put("/api/probation-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProbationPeriod)))
            .andExpect(status().isOk());

        // Validate the ProbationPeriod in the database
        List<ProbationPeriod> probationPeriodList = probationPeriodRepository.findAll();
        assertThat(probationPeriodList).hasSize(databaseSizeBeforeUpdate);
        ProbationPeriod testProbationPeriod = probationPeriodList.get(probationPeriodList.size() - 1);
        assertThat(testProbationPeriod.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testProbationPeriod.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);
        assertThat(testProbationPeriod.getPeriodStart()).isEqualTo(UPDATED_PERIOD_START);
        assertThat(testProbationPeriod.getPeriodEnd()).isEqualTo(UPDATED_PERIOD_END);
        assertThat(testProbationPeriod.getEvaluatePeriodStart()).isEqualTo(UPDATED_EVALUATE_PERIOD_START);
        assertThat(testProbationPeriod.getEvaluatePeriodEnd()).isEqualTo(UPDATED_EVALUATE_PERIOD_END);

        // Validate the ProbationPeriod in Elasticsearch
        ProbationPeriod probationPeriodEs = probationPeriodSearchRepository.findOne(testProbationPeriod.getId());
        assertThat(probationPeriodEs).isEqualToComparingFieldByField(testProbationPeriod);
    }

    @Test
    @Transactional
    public void updateNonExistingProbationPeriod() throws Exception {
        int databaseSizeBeforeUpdate = probationPeriodRepository.findAll().size();

        // Create the ProbationPeriod

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProbationPeriodMockMvc.perform(put("/api/probation-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(probationPeriod)))
            .andExpect(status().isCreated());

        // Validate the ProbationPeriod in the database
        List<ProbationPeriod> probationPeriodList = probationPeriodRepository.findAll();
        assertThat(probationPeriodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProbationPeriod() throws Exception {
        // Initialize the database
        probationPeriodService.save(probationPeriod);

        int databaseSizeBeforeDelete = probationPeriodRepository.findAll().size();

        // Get the probationPeriod
        restProbationPeriodMockMvc.perform(delete("/api/probation-periods/{id}", probationPeriod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean probationPeriodExistsInEs = probationPeriodSearchRepository.exists(probationPeriod.getId());
        assertThat(probationPeriodExistsInEs).isFalse();

        // Validate the database is empty
        List<ProbationPeriod> probationPeriodList = probationPeriodRepository.findAll();
        assertThat(probationPeriodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProbationPeriod() throws Exception {
        // Initialize the database
        probationPeriodService.save(probationPeriod);

        // Search the probationPeriod
        restProbationPeriodMockMvc.perform(get("/api/_search/probation-periods?query=id:" + probationPeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(probationPeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())))
            .andExpect(jsonPath("$.[*].periodStart").value(hasItem(DEFAULT_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].evaluatePeriodStart").value(hasItem(DEFAULT_EVALUATE_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].evaluatePeriodEnd").value(hasItem(DEFAULT_EVALUATE_PERIOD_END.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProbationPeriod.class);
        ProbationPeriod probationPeriod1 = new ProbationPeriod();
        probationPeriod1.setId(1L);
        ProbationPeriod probationPeriod2 = new ProbationPeriod();
        probationPeriod2.setId(probationPeriod1.getId());
        assertThat(probationPeriod1).isEqualTo(probationPeriod2);
        probationPeriod2.setId(2L);
        assertThat(probationPeriod1).isNotEqualTo(probationPeriod2);
        probationPeriod1.setId(null);
        assertThat(probationPeriod1).isNotEqualTo(probationPeriod2);
    }
}
