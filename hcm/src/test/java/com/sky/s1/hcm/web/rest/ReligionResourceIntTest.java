package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.Religion;
import com.sky.s1.hcm.repository.ReligionRepository;
import com.sky.s1.hcm.service.ReligionService;
import com.sky.s1.hcm.repository.search.ReligionSearchRepository;
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
 * Test class for the ReligionResource REST controller.
 *
 * @see ReligionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class ReligionResourceIntTest {

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
    private ReligionRepository religionRepository;

    @Autowired
    private ReligionService religionService;

    @Autowired
    private ReligionSearchRepository religionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReligionMockMvc;

    private Religion religion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReligionResource religionResource = new ReligionResource(religionService);
        this.restReligionMockMvc = MockMvcBuilders.standaloneSetup(religionResource)
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
    public static Religion createEntity(EntityManager em) {
        Religion religion = new Religion()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE);
        return religion;
    }

    @Before
    public void initTest() {
        religionSearchRepository.deleteAll();
        religion = createEntity(em);
    }

    @Test
    @Transactional
    public void createReligion() throws Exception {
        int databaseSizeBeforeCreate = religionRepository.findAll().size();

        // Create the Religion
        restReligionMockMvc.perform(post("/api/religions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religion)))
            .andExpect(status().isCreated());

        // Validate the Religion in the database
        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeCreate + 1);
        Religion testReligion = religionList.get(religionList.size() - 1);
        assertThat(testReligion.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testReligion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReligion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReligion.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testReligion.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testReligion.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);

        // Validate the Religion in Elasticsearch
        Religion religionEs = religionSearchRepository.findOne(testReligion.getId());
        assertThat(religionEs).isEqualToComparingFieldByField(testReligion);
    }

    @Test
    @Transactional
    public void createReligionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = religionRepository.findAll().size();

        // Create the Religion with an existing ID
        religion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReligionMockMvc.perform(post("/api/religions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = religionRepository.findAll().size();
        // set the field null
        religion.setCode(null);

        // Create the Religion, which fails.

        restReligionMockMvc.perform(post("/api/religions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religion)))
            .andExpect(status().isBadRequest());

        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = religionRepository.findAll().size();
        // set the field null
        religion.setActiveFlag(null);

        // Create the Religion, which fails.

        restReligionMockMvc.perform(post("/api/religions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religion)))
            .andExpect(status().isBadRequest());

        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReligions() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);

        // Get all the religionList
        restReligionMockMvc.perform(get("/api/religions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(religion.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void getReligion() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);

        // Get the religion
        restReligionMockMvc.perform(get("/api/religions/{id}", religion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(religion.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReligion() throws Exception {
        // Get the religion
        restReligionMockMvc.perform(get("/api/religions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReligion() throws Exception {
        // Initialize the database
        religionService.save(religion);

        int databaseSizeBeforeUpdate = religionRepository.findAll().size();

        // Update the religion
        Religion updatedReligion = religionRepository.findOne(religion.getId());
        updatedReligion
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE);

        restReligionMockMvc.perform(put("/api/religions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReligion)))
            .andExpect(status().isOk());

        // Validate the Religion in the database
        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeUpdate);
        Religion testReligion = religionList.get(religionList.size() - 1);
        assertThat(testReligion.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testReligion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReligion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReligion.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testReligion.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testReligion.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);

        // Validate the Religion in Elasticsearch
        Religion religionEs = religionSearchRepository.findOne(testReligion.getId());
        assertThat(religionEs).isEqualToComparingFieldByField(testReligion);
    }

    @Test
    @Transactional
    public void updateNonExistingReligion() throws Exception {
        int databaseSizeBeforeUpdate = religionRepository.findAll().size();

        // Create the Religion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReligionMockMvc.perform(put("/api/religions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religion)))
            .andExpect(status().isCreated());

        // Validate the Religion in the database
        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReligion() throws Exception {
        // Initialize the database
        religionService.save(religion);

        int databaseSizeBeforeDelete = religionRepository.findAll().size();

        // Get the religion
        restReligionMockMvc.perform(delete("/api/religions/{id}", religion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean religionExistsInEs = religionSearchRepository.exists(religion.getId());
        assertThat(religionExistsInEs).isFalse();

        // Validate the database is empty
        List<Religion> religionList = religionRepository.findAll();
        assertThat(religionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReligion() throws Exception {
        // Initialize the database
        religionService.save(religion);

        // Search the religion
        restReligionMockMvc.perform(get("/api/_search/religions?query=id:" + religion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(religion.getId().intValue())))
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
        TestUtil.equalsVerifier(Religion.class);
        Religion religion1 = new Religion();
        religion1.setId(1L);
        Religion religion2 = new Religion();
        religion2.setId(religion1.getId());
        assertThat(religion1).isEqualTo(religion2);
        religion2.setId(2L);
        assertThat(religion1).isNotEqualTo(religion2);
        religion1.setId(null);
        assertThat(religion1).isNotEqualTo(religion2);
    }
}
