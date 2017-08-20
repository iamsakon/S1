package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.ProbationDay;
import com.sky.s1.hcm.repository.ProbationDayRepository;
import com.sky.s1.hcm.repository.search.ProbationDaySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProbationDay.
 */
@Service
@Transactional
public class ProbationDayService {

    private final Logger log = LoggerFactory.getLogger(ProbationDayService.class);

    private final ProbationDayRepository probationDayRepository;

    private final ProbationDaySearchRepository probationDaySearchRepository;

    public ProbationDayService(ProbationDayRepository probationDayRepository, ProbationDaySearchRepository probationDaySearchRepository) {
        this.probationDayRepository = probationDayRepository;
        this.probationDaySearchRepository = probationDaySearchRepository;
    }

    /**
     * Save a probationDay.
     *
     * @param probationDay the entity to save
     * @return the persisted entity
     */
    public ProbationDay save(ProbationDay probationDay) {
        log.debug("Request to save ProbationDay : {}", probationDay);
        ProbationDay result = probationDayRepository.save(probationDay);
        probationDaySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the probationDays.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProbationDay> findAll(Pageable pageable) {
        log.debug("Request to get all ProbationDays");
        return probationDayRepository.findAll(pageable);
    }

    /**
     *  Get one probationDay by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProbationDay findOne(Long id) {
        log.debug("Request to get ProbationDay : {}", id);
        return probationDayRepository.findOne(id);
    }

    /**
     *  Delete the  probationDay by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProbationDay : {}", id);
        probationDayRepository.delete(id);
        probationDaySearchRepository.delete(id);
    }

    /**
     * Search for the probationDay corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProbationDay> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProbationDays for query {}", query);
        Page<ProbationDay> result = probationDaySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
