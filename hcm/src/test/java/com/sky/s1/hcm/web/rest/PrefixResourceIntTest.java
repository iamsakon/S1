package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.Prefix;
import com.sky.s1.hcm.repository.PrefixRepository;
import com.sky.s1.hcm.service.PrefixService;
import com.sky.s1.hcm.repository.search.PrefixSearchRepository;
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
 * Test class for the PrefixResource REST controller.
 *
 * @see PrefixResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class PrefixResourceIntTest {

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
    private PrefixRepository prefixRepository;

    @Autowired
    private PrefixService prefixService;

    @Autowired
    private PrefixSearchRepository prefixSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrefixMockMvc;

    private Prefix prefix;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrefixResource prefixResource = new PrefixResource(prefixService);
        this.restPrefixMockMvc = MockMvcBuilders.standaloneSetup(prefixResource)
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
    public static Prefix createEntity(EntityManager em) {
        Prefix prefix = new Prefix()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .activeFlag(DEFAULT_ACTIVE_FLAG)
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE);
        return prefix;
    }

    @Before
    public void initTest() {
        prefixSearchRepository.deleteAll();
        prefix = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrefix() throws Exception {
        int databaseSizeBeforeCreate = prefixRepository.findAll().size();

        // Create the Prefix
        restPrefixMockMvc.perform(post("/api/prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prefix)))
            .andExpect(status().isCreated());

        // Validate the Prefix in the database
        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeCreate + 1);
        Prefix testPrefix = prefixList.get(prefixList.size() - 1);
        assertThat(testPrefix.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPrefix.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrefix.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPrefix.isActiveFlag()).isEqualTo(DEFAULT_ACTIVE_FLAG);
        assertThat(testPrefix.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testPrefix.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);

        // Validate the Prefix in Elasticsearch
        Prefix prefixEs = prefixSearchRepository.findOne(testPrefix.getId());
        assertThat(prefixEs).isEqualToComparingFieldByField(testPrefix);
    }

    @Test
    @Transactional
    public void createPrefixWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prefixRepository.findAll().size();

        // Create the Prefix with an existing ID
        prefix.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrefixMockMvc.perform(post("/api/prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prefix)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = prefixRepository.findAll().size();
        // set the field null
        prefix.setCode(null);

        // Create the Prefix, which fails.

        restPrefixMockMvc.perform(post("/api/prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prefix)))
            .andExpect(status().isBadRequest());

        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = prefixRepository.findAll().size();
        // set the field null
        prefix.setActiveFlag(null);

        // Create the Prefix, which fails.

        restPrefixMockMvc.perform(post("/api/prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prefix)))
            .andExpect(status().isBadRequest());

        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrefixes() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get all the prefixList
        restPrefixMockMvc.perform(get("/api/prefixes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prefix.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].activeFlag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())));
    }

    @Test
    @Transactional
    public void getPrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get the prefix
        restPrefixMockMvc.perform(get("/api/prefixes/{id}", prefix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prefix.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.activeFlag").value(DEFAULT_ACTIVE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrefix() throws Exception {
        // Get the prefix
        restPrefixMockMvc.perform(get("/api/prefixes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrefix() throws Exception {
        // Initialize the database
        prefixService.save(prefix);

        int databaseSizeBeforeUpdate = prefixRepository.findAll().size();

        // Update the prefix
        Prefix updatedPrefix = prefixRepository.findOne(prefix.getId());
        updatedPrefix
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .activeFlag(UPDATED_ACTIVE_FLAG)
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE);

        restPrefixMockMvc.perform(put("/api/prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrefix)))
            .andExpect(status().isOk());

        // Validate the Prefix in the database
        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeUpdate);
        Prefix testPrefix = prefixList.get(prefixList.size() - 1);
        assertThat(testPrefix.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPrefix.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrefix.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPrefix.isActiveFlag()).isEqualTo(UPDATED_ACTIVE_FLAG);
        assertThat(testPrefix.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testPrefix.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);

        // Validate the Prefix in Elasticsearch
        Prefix prefixEs = prefixSearchRepository.findOne(testPrefix.getId());
        assertThat(prefixEs).isEqualToComparingFieldByField(testPrefix);
    }

    @Test
    @Transactional
    public void updateNonExistingPrefix() throws Exception {
        int databaseSizeBeforeUpdate = prefixRepository.findAll().size();

        // Create the Prefix

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrefixMockMvc.perform(put("/api/prefixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prefix)))
            .andExpect(status().isCreated());

        // Validate the Prefix in the database
        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrefix() throws Exception {
        // Initialize the database
        prefixService.save(prefix);

        int databaseSizeBeforeDelete = prefixRepository.findAll().size();

        // Get the prefix
        restPrefixMockMvc.perform(delete("/api/prefixes/{id}", prefix.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean prefixExistsInEs = prefixSearchRepository.exists(prefix.getId());
        assertThat(prefixExistsInEs).isFalse();

        // Validate the database is empty
        List<Prefix> prefixList = prefixRepository.findAll();
        assertThat(prefixList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrefix() throws Exception {
        // Initialize the database
        prefixService.save(prefix);

        // Search the prefix
        restPrefixMockMvc.perform(get("/api/_search/prefixes?query=id:" + prefix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prefix.getId().intValue())))
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
        TestUtil.equalsVerifier(Prefix.class);
        Prefix prefix1 = new Prefix();
        prefix1.setId(1L);
        Prefix prefix2 = new Prefix();
        prefix2.setId(prefix1.getId());
        assertThat(prefix1).isEqualTo(prefix2);
        prefix2.setId(2L);
        assertThat(prefix1).isNotEqualTo(prefix2);
        prefix1.setId(null);
        assertThat(prefix1).isNotEqualTo(prefix2);
    }
}
