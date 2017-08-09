package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.BloodType;
import com.sky.s1.hcm.repository.BloodTypeRepository;
import com.sky.s1.hcm.service.BloodTypeService;
import com.sky.s1.hcm.repository.search.BloodTypeSearchRepository;
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
 * Test class for the BloodTypeResource REST controller.
 *
 * @see BloodTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class BloodTypeResourceIntTest {

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
    private BloodTypeRepository bloodTypeRepository;

    @Autowired
    private BloodTypeService bloodTypeService;

    @Autowired
    private BloodTypeSearchRepository bloodTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBloodTypeMockMvc;

    private BloodType bloodType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodTypeResource bloodTypeResource = new BloodTypeResource(bloodTypeService);
        this.restBloodTypeMockMvc = MockMvcBuilders.standaloneSetup(bloodTypeResource)
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
    public static BloodType createEntity(EntityManager em) {
        BloodType bloodType = new BloodType()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE);
        return bloodType;
    }

    @Before
    public void initTest() {
        bloodTypeSearchRepository.deleteAll();
        bloodType = createEntity(em);
    }

    @Test
    @Transactional
    public void createBloodType() throws Exception {
        int databaseSizeBeforeCreate = bloodTypeRepository.findAll().size();

        // Create the BloodType
        restBloodTypeMockMvc.perform(post("/api/blood-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodType)))
            .andExpect(status().isCreated());

        // Validate the BloodType in the database
        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BloodType testBloodType = bloodTypeList.get(bloodTypeList.size() - 1);
        assertThat(testBloodType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBloodType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBloodType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBloodType.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testBloodType.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testBloodType.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);

        // Validate the BloodType in Elasticsearch
        BloodType bloodTypeEs = bloodTypeSearchRepository.findOne(testBloodType.getId());
        assertThat(bloodTypeEs).isEqualToComparingFieldByField(testBloodType);
    }

    @Test
    @Transactional
    public void createBloodTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bloodTypeRepository.findAll().size();

        // Create the BloodType with an existing ID
        bloodType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloodTypeMockMvc.perform(post("/api/blood-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodTypeRepository.findAll().size();
        // set the field null
        bloodType.setCode(null);

        // Create the BloodType, which fails.

        restBloodTypeMockMvc.perform(post("/api/blood-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodType)))
            .andExpect(status().isBadRequest());

        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodTypeRepository.findAll().size();
        // set the field null
        bloodType.setActiveFlag(null);

        // Create the BloodType, which fails.

        restBloodTypeMockMvc.perform(post("/api/blood-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodType)))
            .andExpect(status().isBadRequest());

        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBloodTypes() throws Exception {
        // Initialize the database
        bloodTypeRepository.saveAndFlush(bloodType);

        // Get all the bloodTypeList
        restBloodTypeMockMvc.perform(get("/api/blood-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void getBloodType() throws Exception {
        // Initialize the database
        bloodTypeRepository.saveAndFlush(bloodType);

        // Get the bloodType
        restBloodTypeMockMvc.perform(get("/api/blood-types/{id}", bloodType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bloodType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBloodType() throws Exception {
        // Get the bloodType
        restBloodTypeMockMvc.perform(get("/api/blood-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBloodType() throws Exception {
        // Initialize the database
        bloodTypeService.save(bloodType);

        int databaseSizeBeforeUpdate = bloodTypeRepository.findAll().size();

        // Update the bloodType
        BloodType updatedBloodType = bloodTypeRepository.findOne(bloodType.getId());
        updatedBloodType
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE);

        restBloodTypeMockMvc.perform(put("/api/blood-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBloodType)))
            .andExpect(status().isOk());

        // Validate the BloodType in the database
        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeUpdate);
        BloodType testBloodType = bloodTypeList.get(bloodTypeList.size() - 1);
        assertThat(testBloodType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBloodType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBloodType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBloodType.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testBloodType.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testBloodType.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);

        // Validate the BloodType in Elasticsearch
        BloodType bloodTypeEs = bloodTypeSearchRepository.findOne(testBloodType.getId());
        assertThat(bloodTypeEs).isEqualToComparingFieldByField(testBloodType);
    }

    @Test
    @Transactional
    public void updateNonExistingBloodType() throws Exception {
        int databaseSizeBeforeUpdate = bloodTypeRepository.findAll().size();

        // Create the BloodType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBloodTypeMockMvc.perform(put("/api/blood-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodType)))
            .andExpect(status().isCreated());

        // Validate the BloodType in the database
        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBloodType() throws Exception {
        // Initialize the database
        bloodTypeService.save(bloodType);

        int databaseSizeBeforeDelete = bloodTypeRepository.findAll().size();

        // Get the bloodType
        restBloodTypeMockMvc.perform(delete("/api/blood-types/{id}", bloodType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bloodTypeExistsInEs = bloodTypeSearchRepository.exists(bloodType.getId());
        assertThat(bloodTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<BloodType> bloodTypeList = bloodTypeRepository.findAll();
        assertThat(bloodTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBloodType() throws Exception {
        // Initialize the database
        bloodTypeService.save(bloodType);

        // Search the bloodType
        restBloodTypeMockMvc.perform(get("/api/_search/blood-types?query=id:" + bloodType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodType.getId().intValue())))
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
        TestUtil.equalsVerifier(BloodType.class);
        BloodType bloodType1 = new BloodType();
        bloodType1.setId(1L);
        BloodType bloodType2 = new BloodType();
        bloodType2.setId(bloodType1.getId());
        assertThat(bloodType1).isEqualTo(bloodType2);
        bloodType2.setId(2L);
        assertThat(bloodType1).isNotEqualTo(bloodType2);
        bloodType1.setId(null);
        assertThat(bloodType1).isNotEqualTo(bloodType2);
    }
}
