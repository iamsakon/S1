package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.Gender;
import com.sky.s1.hcm.repository.GenderRepository;
import com.sky.s1.hcm.repository.search.GenderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Gender.
 */
@Service
@Transactional
public class GenderService {

    private final Logger log = LoggerFactory.getLogger(GenderService.class);

    private final GenderRepository genderRepository;

    private final GenderSearchRepository genderSearchRepository;

    public GenderService(GenderRepository genderRepository, GenderSearchRepository genderSearchRepository) {
        this.genderRepository = genderRepository;
        this.genderSearchRepository = genderSearchRepository;
    }

    /**
     * Save a gender.
     *
     * @param gender the entity to save
     * @return the persisted entity
     */
    public Gender save(Gender gender) {
        log.debug("Request to save Gender : {}", gender);
        Gender result = genderRepository.save(gender);
        genderSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the genders.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Gender> findAll(Pageable pageable) {
        log.debug("Request to get all Genders");
        return genderRepository.findAll(pageable);
    }

    /**
     *  Get one gender by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Gender findOne(Long id) {
        log.debug("Request to get Gender : {}", id);
        return genderRepository.findOne(id);
    }

    /**
     *  Delete the  gender by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Gender : {}", id);
        genderRepository.delete(id);
        genderSearchRepository.delete(id);
    }

    /**
     * Search for the gender corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Gender> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Genders for query {}", query);
        Page<Gender> result = genderSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
