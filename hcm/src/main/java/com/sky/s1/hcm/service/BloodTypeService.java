package com.sky.s1.hcm.service;

import com.sky.s1.hcm.domain.BloodType;
import com.sky.s1.hcm.repository.BloodTypeRepository;
import com.sky.s1.hcm.repository.search.BloodTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BloodType.
 */
@Service
@Transactional
public class BloodTypeService {

    private final Logger log = LoggerFactory.getLogger(BloodTypeService.class);

    private final BloodTypeRepository bloodTypeRepository;

    private final BloodTypeSearchRepository bloodTypeSearchRepository;

    public BloodTypeService(BloodTypeRepository bloodTypeRepository, BloodTypeSearchRepository bloodTypeSearchRepository) {
        this.bloodTypeRepository = bloodTypeRepository;
        this.bloodTypeSearchRepository = bloodTypeSearchRepository;
    }

    /**
     * Save a bloodType.
     *
     * @param bloodType the entity to save
     * @return the persisted entity
     */
    public BloodType save(BloodType bloodType) {
        log.debug("Request to save BloodType : {}", bloodType);
        BloodType result = bloodTypeRepository.save(bloodType);
        bloodTypeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the bloodTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BloodType> findAll(Pageable pageable) {
        log.debug("Request to get all BloodTypes");
        return bloodTypeRepository.findAll(pageable);
    }

    /**
     *  Get one bloodType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BloodType findOne(Long id) {
        log.debug("Request to get BloodType : {}", id);
        return bloodTypeRepository.findOne(id);
    }

    /**
     *  Delete the  bloodType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BloodType : {}", id);
        bloodTypeRepository.delete(id);
        bloodTypeSearchRepository.delete(id);
    }

    /**
     * Search for the bloodType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BloodType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BloodTypes for query {}", query);
        Page<BloodType> result = bloodTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
