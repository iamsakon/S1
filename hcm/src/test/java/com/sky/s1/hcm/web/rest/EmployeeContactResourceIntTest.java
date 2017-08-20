package com.sky.s1.hcm.web.rest;

import com.sky.s1.hcm.HcmApp;

import com.sky.s1.hcm.domain.EmployeeContact;
import com.sky.s1.hcm.repository.EmployeeContactRepository;
import com.sky.s1.hcm.service.EmployeeContactService;
import com.sky.s1.hcm.repository.search.EmployeeContactSearchRepository;
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
 * Test class for the EmployeeContactResource REST controller.
 *
 * @see EmployeeContactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HcmApp.class)
public class EmployeeContactResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Long DEFAULT_COMPANY_CODE = 1L;
    private static final Long UPDATED_COMPANY_CODE = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NO_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NO_EXTENSION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EmployeeContactRepository employeeContactRepository;

    @Autowired
    private EmployeeContactService employeeContactService;

    @Autowired
    private EmployeeContactSearchRepository employeeContactSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeeContactMockMvc;

    private EmployeeContact employeeContact;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeContactResource employeeContactResource = new EmployeeContactResource(employeeContactService);
        this.restEmployeeContactMockMvc = MockMvcBuilders.standaloneSetup(employeeContactResource)
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
    public static EmployeeContact createEntity(EntityManager em) {
        EmployeeContact employeeContact = new EmployeeContact()
            .tenantId(DEFAULT_TENANT_ID)
            .companyCode(DEFAULT_COMPANY_CODE)
            .email(DEFAULT_EMAIL)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .phoneNoExtension(DEFAULT_PHONE_NO_EXTENSION)
            .activeDate(DEFAULT_ACTIVE_DATE);
        return employeeContact;
    }

    @Before
    public void initTest() {
        employeeContactSearchRepository.deleteAll();
        employeeContact = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeContact() throws Exception {
        int databaseSizeBeforeCreate = employeeContactRepository.findAll().size();

        // Create the EmployeeContact
        restEmployeeContactMockMvc.perform(post("/api/employee-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeContact)))
            .andExpect(status().isCreated());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeContact testEmployeeContact = employeeContactList.get(employeeContactList.size() - 1);
        assertThat(testEmployeeContact.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEmployeeContact.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);
        assertThat(testEmployeeContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployeeContact.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testEmployeeContact.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployeeContact.getPhoneNoExtension()).isEqualTo(DEFAULT_PHONE_NO_EXTENSION);
        assertThat(testEmployeeContact.getActiveDate()).isEqualTo(DEFAULT_ACTIVE_DATE);

        // Validate the EmployeeContact in Elasticsearch
        EmployeeContact employeeContactEs = employeeContactSearchRepository.findOne(testEmployeeContact.getId());
        assertThat(employeeContactEs).isEqualToComparingFieldByField(testEmployeeContact);
    }

    @Test
    @Transactional
    public void createEmployeeContactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeContactRepository.findAll().size();

        // Create the EmployeeContact with an existing ID
        employeeContact.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeContactMockMvc.perform(post("/api/employee-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeContact)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmployeeContacts() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList
        restEmployeeContactMockMvc.perform(get("/api/employee-contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].phoneNoExtension").value(hasItem(DEFAULT_PHONE_NO_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].activeDate").value(hasItem(DEFAULT_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get the employeeContact
        restEmployeeContactMockMvc.perform(get("/api/employee-contacts/{id}", employeeContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeContact.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.phoneNoExtension").value(DEFAULT_PHONE_NO_EXTENSION.toString()))
            .andExpect(jsonPath("$.activeDate").value(DEFAULT_ACTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeContact() throws Exception {
        // Get the employeeContact
        restEmployeeContactMockMvc.perform(get("/api/employee-contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactService.save(employeeContact);

        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();

        // Update the employeeContact
        EmployeeContact updatedEmployeeContact = employeeContactRepository.findOne(employeeContact.getId());
        updatedEmployeeContact
            .tenantId(UPDATED_TENANT_ID)
            .companyCode(UPDATED_COMPANY_CODE)
            .email(UPDATED_EMAIL)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .phoneNoExtension(UPDATED_PHONE_NO_EXTENSION)
            .activeDate(UPDATED_ACTIVE_DATE);

        restEmployeeContactMockMvc.perform(put("/api/employee-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeContact)))
            .andExpect(status().isOk());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
        EmployeeContact testEmployeeContact = employeeContactList.get(employeeContactList.size() - 1);
        assertThat(testEmployeeContact.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEmployeeContact.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);
        assertThat(testEmployeeContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployeeContact.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testEmployeeContact.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployeeContact.getPhoneNoExtension()).isEqualTo(UPDATED_PHONE_NO_EXTENSION);
        assertThat(testEmployeeContact.getActiveDate()).isEqualTo(UPDATED_ACTIVE_DATE);

        // Validate the EmployeeContact in Elasticsearch
        EmployeeContact employeeContactEs = employeeContactSearchRepository.findOne(testEmployeeContact.getId());
        assertThat(employeeContactEs).isEqualToComparingFieldByField(testEmployeeContact);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();

        // Create the EmployeeContact

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeeContactMockMvc.perform(put("/api/employee-contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeContact)))
            .andExpect(status().isCreated());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactService.save(employeeContact);

        int databaseSizeBeforeDelete = employeeContactRepository.findAll().size();

        // Get the employeeContact
        restEmployeeContactMockMvc.perform(delete("/api/employee-contacts/{id}", employeeContact.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employeeContactExistsInEs = employeeContactSearchRepository.exists(employeeContact.getId());
        assertThat(employeeContactExistsInEs).isFalse();

        // Validate the database is empty
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactService.save(employeeContact);

        // Search the employeeContact
        restEmployeeContactMockMvc.perform(get("/api/_search/employee-contacts?query=id:" + employeeContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].phoneNoExtension").value(hasItem(DEFAULT_PHONE_NO_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].activeDate").value(hasItem(DEFAULT_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeContact.class);
        EmployeeContact employeeContact1 = new EmployeeContact();
        employeeContact1.setId(1L);
        EmployeeContact employeeContact2 = new EmployeeContact();
        employeeContact2.setId(employeeContact1.getId());
        assertThat(employeeContact1).isEqualTo(employeeContact2);
        employeeContact2.setId(2L);
        assertThat(employeeContact1).isNotEqualTo(employeeContact2);
        employeeContact1.setId(null);
        assertThat(employeeContact1).isNotEqualTo(employeeContact2);
    }
}
