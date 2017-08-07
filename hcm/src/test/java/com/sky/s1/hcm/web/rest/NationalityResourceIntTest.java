package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.Nationality;
import com.sky.s1.hcm.repository.NationalityRepository;
import com.sky.s1.hcm.service.NationalityService;
import com.sky.s1.hcm.repository.search.NationalitySearchRepository;
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
 * Test class for the NationalityResource REST controller.
 *
 * @see NationalityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class NationalityResourceIntTest {

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
    private NationalityRepository nationalityRepository;

    @Autowired
    private NationalityService nationalityService;

    @Autowired
    private NationalitySearchRepository nationalitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNationalityMockMvc;

    private Nationality nationality;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NationalityResource nationalityResource = new NationalityResource(nationalityService);
        this.restNationalityMockMvc = MockMvcBuilders.standaloneSetup(nationalityResource)
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
    public static Nationality createEntity(EntityManager em) {
        Nationality nationality = new Nationality()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE);
        return nationality;
    }

    @Before
    public void initTest() {
        nationalitySearchRepository.deleteAll();
        nationality = createEntity(em);
    }

    @Test
    @Transactional
    public void createNationality() throws Exception {
        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

        // Create the Nationality
        restNationalityMockMvc.perform(post("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isCreated());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate + 1);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testNationality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNationality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNationality.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testNationality.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testNationality.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);

        // Validate the Nationality in Elasticsearch
        Nationality nationalityEs = nationalitySearchRepository.findOne(testNationality.getId());
        assertThat(nationalityEs).isEqualToComparingFieldByField(testNationality);
    }

    @Test
    @Transactional
    public void createNationalityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

        // Create the Nationality with an existing ID
        nationality.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNationalityMockMvc.perform(post("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalityRepository.findAll().size();
        // set the field null
        nationality.setCode(null);

        // Create the Nationality, which fails.

        restNationalityMockMvc.perform(post("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalityRepository.findAll().size();
        // set the field null
        nationality.setActiveFlag(null);

        // Create the Nationality, which fails.

        restNationalityMockMvc.perform(post("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNationalities() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get all the nationalityList
        restNationalityMockMvc.perform(get("/api/nationalities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void getNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get the nationality
        restNationalityMockMvc.perform(get("/api/nationalities/{id}", nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nationality.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNationality() throws Exception {
        // Get the nationality
        restNationalityMockMvc.perform(get("/api/nationalities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNationality() throws Exception {
        // Initialize the database
        nationalityService.save(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality
        Nationality updatedNationality = nationalityRepository.findOne(nationality.getId());
        updatedNationality
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE);

        restNationalityMockMvc.perform(put("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNationality)))
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNationality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNationality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNationality.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testNationality.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testNationality.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);

        // Validate the Nationality in Elasticsearch
        Nationality nationalityEs = nationalitySearchRepository.findOne(testNationality.getId());
        assertThat(nationalityEs).isEqualToComparingFieldByField(testNationality);
    }

    @Test
    @Transactional
    public void updateNonExistingNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Create the Nationality

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNationalityMockMvc.perform(put("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isCreated());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNationality() throws Exception {
        // Initialize the database
        nationalityService.save(nationality);

        int databaseSizeBeforeDelete = nationalityRepository.findAll().size();

        // Get the nationality
        restNationalityMockMvc.perform(delete("/api/nationalities/{id}", nationality.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean nationalityExistsInEs = nationalitySearchRepository.exists(nationality.getId());
        assertThat(nationalityExistsInEs).isFalse();

        // Validate the database is empty
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNationality() throws Exception {
        // Initialize the database
        nationalityService.save(nationality);

        // Search the nationality
        restNationalityMockMvc.perform(get("/api/_search/nationalities?query=id:" + nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
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
        TestUtil.equalsVerifier(Nationality.class);
        Nationality nationality1 = new Nationality();
        nationality1.setId(1L);
        Nationality nationality2 = new Nationality();
        nationality2.setId(nationality1.getId());
        assertThat(nationality1).isEqualTo(nationality2);
        nationality2.setId(2L);
        assertThat(nationality1).isNotEqualTo(nationality2);
        nationality1.setId(null);
        assertThat(nationality1).isNotEqualTo(nationality2);
    }
}
