package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.MilitaryStatus;
import com.sky.s1.hcm.repository.MilitaryStatusRepository;
import com.sky.s1.hcm.repository.search.MilitaryStatusSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MilitaryStatus.
 */
@Service
@Transactional
public class MilitaryStatusService {

    private final Logger log = LoggerFactory.getLogger(MilitaryStatusService.class);

    private final MilitaryStatusRepository militaryStatusRepository;

    private final MilitaryStatusSearchRepository militaryStatusSearchRepository;

    public MilitaryStatusService(MilitaryStatusRepository militaryStatusRepository, MilitaryStatusSearchRepository militaryStatusSearchRepository) {
        this.militaryStatusRepository = militaryStatusRepository;
        this.militaryStatusSearchRepository = militaryStatusSearchRepository;
    }

    /**
     * Save a militaryStatus.
     *
     * @param militaryStatus the entity to save
     * @return the persisted entity
     */
    public MilitaryStatus save(MilitaryStatus militaryStatus) {
        log.debug("Request to save MilitaryStatus : {}", militaryStatus);
        MilitaryStatus result = militaryStatusRepository.save(militaryStatus);
        militaryStatusSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the militaryStatuses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MilitaryStatus> findAll(Pageable pageable) {
        log.debug("Request to get all MilitaryStatuses");
        return militaryStatusRepository.findAll(pageable);
    }

    /**
     *  Get one militaryStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MilitaryStatus findOne(Long id) {
        log.debug("Request to get MilitaryStatus : {}", id);
        return militaryStatusRepository.findOne(id);
    }

    /**
     *  Delete the  militaryStatus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MilitaryStatus : {}", id);
        militaryStatusRepository.delete(id);
        militaryStatusSearchRepository.delete(id);
    }

    /**
     * Search for the militaryStatus corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MilitaryStatus> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MilitaryStatuses for query {}", query);
        Page<MilitaryStatus> result = militaryStatusSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
