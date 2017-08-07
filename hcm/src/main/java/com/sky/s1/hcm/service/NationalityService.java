package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.Nationality;
import com.sky.s1.hcm.repository.NationalityRepository;
import com.sky.s1.hcm.repository.search.NationalitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Nationality.
 */
@Service
@Transactional
public class NationalityService {

    private final Logger log = LoggerFactory.getLogger(NationalityService.class);

    private final NationalityRepository nationalityRepository;

    private final NationalitySearchRepository nationalitySearchRepository;

    public NationalityService(NationalityRepository nationalityRepository, NationalitySearchRepository nationalitySearchRepository) {
        this.nationalityRepository = nationalityRepository;
        this.nationalitySearchRepository = nationalitySearchRepository;
    }

    /**
     * Save a nationality.
     *
     * @param nationality the entity to save
     * @return the persisted entity
     */
    public Nationality save(Nationality nationality) {
        log.debug("Request to save Nationality : {}", nationality);
        Nationality result = nationalityRepository.save(nationality);
        nationalitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the nationalities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Nationality> findAll(Pageable pageable) {
        log.debug("Request to get all Nationalities");
        return nationalityRepository.findAll(pageable);
    }

    /**
     *  Get one nationality by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Nationality findOne(Long id) {
        log.debug("Request to get Nationality : {}", id);
        return nationalityRepository.findOne(id);
    }

    /**
     *  Delete the  nationality by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Nationality : {}", id);
        nationalityRepository.delete(id);
        nationalitySearchRepository.delete(id);
    }

    /**
     * Search for the nationality corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Nationality> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Nationalities for query {}", query);
        Page<Nationality> result = nationalitySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
