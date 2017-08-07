package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.Prefix;
import com.sky.s1.hcm.repository.PrefixRepository;
import com.sky.s1.hcm.repository.search.PrefixSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Prefix.
 */
@Service
@Transactional
public class PrefixService {

    private final Logger log = LoggerFactory.getLogger(PrefixService.class);

    private final PrefixRepository prefixRepository;

    private final PrefixSearchRepository prefixSearchRepository;

    public PrefixService(PrefixRepository prefixRepository, PrefixSearchRepository prefixSearchRepository) {
        this.prefixRepository = prefixRepository;
        this.prefixSearchRepository = prefixSearchRepository;
    }

    /**
     * Save a prefix.
     *
     * @param prefix the entity to save
     * @return the persisted entity
     */
    public Prefix save(Prefix prefix) {
        log.debug("Request to save Prefix : {}", prefix);
        Prefix result = prefixRepository.save(prefix);
        prefixSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the prefixes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Prefix> findAll(Pageable pageable) {
        log.debug("Request to get all Prefixes");
        return prefixRepository.findAll(pageable);
    }

    /**
     *  Get one prefix by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Prefix findOne(Long id) {
        log.debug("Request to get Prefix : {}", id);
        return prefixRepository.findOne(id);
    }

    /**
     *  Delete the  prefix by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Prefix : {}", id);
        prefixRepository.delete(id);
        prefixSearchRepository.delete(id);
    }

    /**
     * Search for the prefix corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Prefix> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Prefixes for query {}", query);
        Page<Prefix> result = prefixSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
