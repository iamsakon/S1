package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.Race;
import com.sky.s1.hcm.repository.RaceRepository;
import com.sky.s1.hcm.repository.search.RaceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Race.
 */
@Service
@Transactional
public class RaceService {

    private final Logger log = LoggerFactory.getLogger(RaceService.class);

    private final RaceRepository raceRepository;

    private final RaceSearchRepository raceSearchRepository;

    public RaceService(RaceRepository raceRepository, RaceSearchRepository raceSearchRepository) {
        this.raceRepository = raceRepository;
        this.raceSearchRepository = raceSearchRepository;
    }

    /**
     * Save a race.
     *
     * @param race the entity to save
     * @return the persisted entity
     */
    public Race save(Race race) {
        log.debug("Request to save Race : {}", race);
        Race result = raceRepository.save(race);
        raceSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the races.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Race> findAll(Pageable pageable) {
        log.debug("Request to get all Races");
        return raceRepository.findAll(pageable);
    }

    /**
     *  Get one race by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Race findOne(Long id) {
        log.debug("Request to get Race : {}", id);
        return raceRepository.findOne(id);
    }

    /**
     *  Delete the  race by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Race : {}", id);
        raceRepository.delete(id);
        raceSearchRepository.delete(id);
    }

    /**
     * Search for the race corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Race> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Races for query {}", query);
        Page<Race> result = raceSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
