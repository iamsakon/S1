package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.EmployeeContact;
import com.sky.s1.hcm.repository.EmployeeContactRepository;
import com.sky.s1.hcm.repository.search.EmployeeContactSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EmployeeContact.
 */
@Service
@Transactional
public class EmployeeContactService {

    private final Logger log = LoggerFactory.getLogger(EmployeeContactService.class);

    private final EmployeeContactRepository employeeContactRepository;

    private final EmployeeContactSearchRepository employeeContactSearchRepository;

    public EmployeeContactService(EmployeeContactRepository employeeContactRepository, EmployeeContactSearchRepository employeeContactSearchRepository) {
        this.employeeContactRepository = employeeContactRepository;
        this.employeeContactSearchRepository = employeeContactSearchRepository;
    }

    /**
     * Save a employeeContact.
     *
     * @param employeeContact the entity to save
     * @return the persisted entity
     */
    public EmployeeContact save(EmployeeContact employeeContact) {
        log.debug("Request to save EmployeeContact : {}", employeeContact);
        EmployeeContact result = employeeContactRepository.save(employeeContact);
        employeeContactSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the employeeContacts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmployeeContact> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeContacts");
        return employeeContactRepository.findAll(pageable);
    }

    /**
     *  Get one employeeContact by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EmployeeContact findOne(Long id) {
        log.debug("Request to get EmployeeContact : {}", id);
        return employeeContactRepository.findOne(id);
    }

    /**
     *  Delete the  employeeContact by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeContact : {}", id);
        employeeContactRepository.delete(id);
        employeeContactSearchRepository.delete(id);
    }

    /**
     * Search for the employeeContact corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmployeeContact> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EmployeeContacts for query {}", query);
        Page<EmployeeContact> result = employeeContactSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
