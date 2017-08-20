package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.ProbationDay;
import com.sky.s1.hcm.repository.ProbationDayRepository;
import com.sky.s1.hcm.service.ProbationDayService;
import com.sky.s1.hcm.repository.search.ProbationDaySearchRepository;
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
 * Test class for the ProbationDayResource REST controller.
 *
 * @see ProbationDayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class ProbationDayResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_COMPANY_CODE = 1L;
    private static final Long UPDATED_COMPANY_CODE = 2L;

    private static final Integer DEFAULT_AMOUNT_DATE = 1;
    private static final Integer UPDATED_AMOUNT_DATE = 2;

    @Autowired
    private ProbationDayRepository probationDayRepository;

    @Autowired
    private ProbationDayService probationDayService;

    @Autowired
    private ProbationDaySearchRepository probationDaySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProbationDayMockMvc;

    private ProbationDay probationDay;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProbationDayResource probationDayResource = new ProbationDayResource(probationDayService);
        this.restProbationDayMockMvc = MockMvcBuilders.standaloneSetup(probationDayResource)
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
    public static ProbationDay createEntity(EntityManager em) {
        ProbationDay probationDay = new ProbationDay()
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE)
            .amountDate(DEFAULT_AMOUNT_DATE);
        return probationDay;
    }

    @Before
    public void initTest() {
        probationDaySearchRepository.deleteAll();
        probationDay = createEntity(em);
    }

    @Test
    @Transactional
    public void createProbationDay() throws Exception {
        int databaseSizeBeforeCreate = probationDayRepository.findAll().size();

        // Create the ProbationDay
        restProbationDayMockMvc.perform(post("/api/probation-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(probationDay)))
            .andExpect(status().isCreated());

        // Validate the ProbationDay in the database
        List<ProbationDay> probationDayList = probationDayRepository.findAll();
        assertThat(probationDayList).hasSize(databaseSizeBeforeCreate + 1);
        ProbationDay testProbationDay = probationDayList.get(probationDayList.size() - 1);
        assertThat(testProbationDay.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testProbationDay.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);
        assertThat(testProbationDay.getAmountDate()).isEqualTo(DEFAULT_AMOUNT_DATE);

        // Validate the ProbationDay in Elasticsearch
        ProbationDay probationDayEs = probationDaySearchRepository.findOne(testProbationDay.getId());
        assertThat(probationDayEs).isEqualToComparingFieldByField(testProbationDay);
    }

    @Test
    @Transactional
    public void createProbationDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = probationDayRepository.findAll().size();

        // Create the ProbationDay with an existing ID
        probationDay.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProbationDayMockMvc.perform(post("/api/probation-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(probationDay)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProbationDay> probationDayList = probationDayRepository.findAll();
        assertThat(probationDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProbationDays() throws Exception {
        // Initialize the database
        probationDayRepository.saveAndFlush(probationDay);

        // Get all the probationDayList
        restProbationDayMockMvc.perform(get("/api/probation-days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(probationDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())))
            .andExpect(jsonPath("$.[*].amountDate").value(hasItem(DEFAULT_AMOUNT_DATE)));
    }

    @Test
    @Transactional
    public void getProbationDay() throws Exception {
        // Initialize the database
        probationDayRepository.saveAndFlush(probationDay);

        // Get the probationDay
        restProbationDayMockMvc.perform(get("/api/probation-days/{id}", probationDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(probationDay.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()))
            .andExpect(jsonPath("$.amountDate").value(DEFAULT_AMOUNT_DATE));
    }

    @Test
    @Transactional
    public void getNonExistingProbationDay() throws Exception {
        // Get the probationDay
        restProbationDayMockMvc.perform(get("/api/probation-days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProbationDay() throws Exception {
        // Initialize the database
        probationDayService.save(probationDay);

        int databaseSizeBeforeUpdate = probationDayRepository.findAll().size();

        // Update the probationDay
        ProbationDay updatedProbationDay = probationDayRepository.findOne(probationDay.getId());
        updatedProbationDay
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE)
            .amountDate(UPDATED_AMOUNT_DATE);

        restProbationDayMockMvc.perform(put("/api/probation-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProbationDay)))
            .andExpect(status().isOk());

        // Validate the ProbationDay in the database
        List<ProbationDay> probationDayList = probationDayRepository.findAll();
        assertThat(probationDayList).hasSize(databaseSizeBeforeUpdate);
        ProbationDay testProbationDay = probationDayList.get(probationDayList.size() - 1);
        assertThat(testProbationDay.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testProbationDay.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);
        assertThat(testProbationDay.getAmountDate()).isEqualTo(UPDATED_AMOUNT_DATE);

        // Validate the ProbationDay in Elasticsearch
        ProbationDay probationDayEs = probationDaySearchRepository.findOne(testProbationDay.getId());
        assertThat(probationDayEs).isEqualToComparingFieldByField(testProbationDay);
    }

    @Test
    @Transactional
    public void updateNonExistingProbationDay() throws Exception {
        int databaseSizeBeforeUpdate = probationDayRepository.findAll().size();

        // Create the ProbationDay

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProbationDayMockMvc.perform(put("/api/probation-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(probationDay)))
            .andExpect(status().isCreated());

        // Validate the ProbationDay in the database
        List<ProbationDay> probationDayList = probationDayRepository.findAll();
        assertThat(probationDayList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProbationDay() throws Exception {
        // Initialize the database
        probationDayService.save(probationDay);

        int databaseSizeBeforeDelete = probationDayRepository.findAll().size();

        // Get the probationDay
        restProbationDayMockMvc.perform(delete("/api/probation-days/{id}", probationDay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean probationDayExistsInEs = probationDaySearchRepository.exists(probationDay.getId());
        assertThat(probationDayExistsInEs).isFalse();

        // Validate the database is empty
        List<ProbationDay> probationDayList = probationDayRepository.findAll();
        assertThat(probationDayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProbationDay() throws Exception {
        // Initialize the database
        probationDayService.save(probationDay);

        // Search the probationDay
        restProbationDayMockMvc.perform(get("/api/_search/probation-days?query=id:" + probationDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(probationDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())))
            .andExpect(jsonPath("$.[*].amountDate").value(hasItem(DEFAULT_AMOUNT_DATE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProbationDay.class);
        ProbationDay probationDay1 = new ProbationDay();
        probationDay1.setId(1L);
        ProbationDay probationDay2 = new ProbationDay();
        probationDay2.setId(probationDay1.getId());
        assertThat(probationDay1).isEqualTo(probationDay2);
        probationDay2.setId(2L);
        assertThat(probationDay1).isNotEqualTo(probationDay2);
        probationDay1.setId(null);
        assertThat(probationDay1).isNotEqualTo(probationDay2);
    }
}
