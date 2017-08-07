package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.MaritalStatus;
import com.sky.s1.hcm.repository.MaritalStatusRepository;
import com.sky.s1.hcm.repository.search.MaritalStatusSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MaritalStatus.
 */
@Service
@Transactional
public class MaritalStatusService {

    private final Logger log = LoggerFactory.getLogger(MaritalStatusService.class);

    private final MaritalStatusRepository maritalStatusRepository;

    private final MaritalStatusSearchRepository maritalStatusSearchRepository;

    public MaritalStatusService(MaritalStatusRepository maritalStatusRepository, MaritalStatusSearchRepository maritalStatusSearchRepository) {
        this.maritalStatusRepository = maritalStatusRepository;
        this.maritalStatusSearchRepository = maritalStatusSearchRepository;
    }

    /**
     * Save a maritalStatus.
     *
     * @param maritalStatus the entity to save
     * @return the persisted entity
     */
    public MaritalStatus save(MaritalStatus maritalStatus) {
        log.debug("Request to save MaritalStatus : {}", maritalStatus);
        MaritalStatus result = maritalStatusRepository.save(maritalStatus);
        maritalStatusSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the maritalStatuses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MaritalStatus> findAll(Pageable pageable) {
        log.debug("Request to get all MaritalStatuses");
        return maritalStatusRepository.findAll(pageable);
    }

    /**
     *  Get one maritalStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MaritalStatus findOne(Long id) {
        log.debug("Request to get MaritalStatus : {}", id);
        return maritalStatusRepository.findOne(id);
    }

    /**
     *  Delete the  maritalStatus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MaritalStatus : {}", id);
        maritalStatusRepository.delete(id);
        maritalStatusSearchRepository.delete(id);
    }

    /**
     * Search for the maritalStatus corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MaritalStatus> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MaritalStatuses for query {}", query);
        Page<MaritalStatus> result = maritalStatusSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
