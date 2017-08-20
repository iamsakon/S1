package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.Candidate;
import com.sky.s1.hcm.repository.CandidateRepository;
import com.sky.s1.hcm.repository.search.CandidateSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Candidate.
 */
@Service
@Transactional
public class CandidateService {

    private final Logger log = LoggerFactory.getLogger(CandidateService.class);

    private final CandidateRepository candidateRepository;

    private final CandidateSearchRepository candidateSearchRepository;

    public CandidateService(CandidateRepository candidateRepository, CandidateSearchRepository candidateSearchRepository) {
        this.candidateRepository = candidateRepository;
        this.candidateSearchRepository = candidateSearchRepository;
    }

    /**
     * Save a candidate.
     *
     * @param candidate the entity to save
     * @return the persisted entity
     */
    public Candidate save(Candidate candidate) {
        log.debug("Request to save Candidate : {}", candidate);
        Candidate result = candidateRepository.save(candidate);
        candidateSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the candidates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Candidate> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll(pageable);
    }

    /**
     *  Get one candidate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Candidate findOne(Long id) {
        log.debug("Request to get Candidate : {}", id);
        return candidateRepository.findOne(id);
    }

    /**
     *  Delete the  candidate by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Candidate : {}", id);
        candidateRepository.delete(id);
        candidateSearchRepository.delete(id);
    }

    /**
     * Search for the candidate corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Candidate> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Candidates for query {}", query);
        Page<Candidate> result = candidateSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
