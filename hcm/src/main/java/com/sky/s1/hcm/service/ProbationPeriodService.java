package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.ProbationPeriod;
import com.sky.s1.hcm.repository.ProbationPeriodRepository;
import com.sky.s1.hcm.repository.search.ProbationPeriodSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProbationPeriod.
 */
@Service
@Transactional
public class ProbationPeriodService {

    private final Logger log = LoggerFactory.getLogger(ProbationPeriodService.class);

    private final ProbationPeriodRepository probationPeriodRepository;

    private final ProbationPeriodSearchRepository probationPeriodSearchRepository;

    public ProbationPeriodService(ProbationPeriodRepository probationPeriodRepository, ProbationPeriodSearchRepository probationPeriodSearchRepository) {
        this.probationPeriodRepository = probationPeriodRepository;
        this.probationPeriodSearchRepository = probationPeriodSearchRepository;
    }

    /**
     * Save a probationPeriod.
     *
     * @param probationPeriod the entity to save
     * @return the persisted entity
     */
    public ProbationPeriod save(ProbationPeriod probationPeriod) {
        log.debug("Request to save ProbationPeriod : {}", probationPeriod);
        ProbationPeriod result = probationPeriodRepository.save(probationPeriod);
        probationPeriodSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the probationPeriods.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProbationPeriod> findAll(Pageable pageable) {
        log.debug("Request to get all ProbationPeriods");
        return probationPeriodRepository.findAll(pageable);
    }

    /**
     *  Get one probationPeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProbationPeriod findOne(Long id) {
        log.debug("Request to get ProbationPeriod : {}", id);
        return probationPeriodRepository.findOne(id);
    }

    /**
     *  Delete the  probationPeriod by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProbationPeriod : {}", id);
        probationPeriodRepository.delete(id);
        probationPeriodSearchRepository.delete(id);
    }

    /**
     * Search for the probationPeriod corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProbationPeriod> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProbationPeriods for query {}", query);
        Page<ProbationPeriod> result = probationPeriodSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
