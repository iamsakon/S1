package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.BloodType;
import com.sky.s1.hcm.service.BloodTypeService;
import com.sky.s1.hcm.web.rest.util.HeaderUtil;
import com.sky.s1.hcm.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BloodType.
 */
@RestController
@RequestMapping("/api")
public class BloodTypeResource {

    private final Logger log = LoggerFactory.getLogger(BloodTypeResource.class);

    private static final String ENTITY_NAME = "bloodType";

    private final BloodTypeService bloodTypeService;

    public BloodTypeResource(BloodTypeService bloodTypeService) {
        this.bloodTypeService = bloodTypeService;
    }

    /**
     * POST  /blood-types : Create a new bloodType.
     *
     * @param bloodType the bloodType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bloodType, or with status 400 (Bad Request) if the bloodType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blood-types")
    @Timed
    public ResponseEntity<BloodType> createBloodType(@Valid @RequestBody BloodType bloodType) throws URISyntaxException {
        log.debug("REST request to save BloodType : {}", bloodType);
        if (bloodType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bloodType cannot already have an ID")).body(null);
        }
        BloodType result = bloodTypeService.save(bloodType);
        return ResponseEntity.created(new URI("/api/blood-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blood-types : Updates an existing bloodType.
     *
     * @param bloodType the bloodType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bloodType,
     * or with status 400 (Bad Request) if the bloodType is not valid,
     * or with status 500 (Internal Server Error) if the bloodType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blood-types")
    @Timed
    public ResponseEntity<BloodType> updateBloodType(@Valid @RequestBody BloodType bloodType) throws URISyntaxException {
        log.debug("REST request to update BloodType : {}", bloodType);
        if (bloodType.getId() == null) {
            return createBloodType(bloodType);
        }
        BloodType result = bloodTypeService.save(bloodType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bloodType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blood-types : get all the bloodTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bloodTypes in body
     */
    @GetMapping("/blood-types")
    @Timed
    public ResponseEntity<List<BloodType>> getAllBloodTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BloodTypes");
        Page<BloodType> page = bloodTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blood-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blood-types/:id : get the "id" bloodType.
     *
     * @param id the id of the bloodType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bloodType, or with status 404 (Not Found)
     */
    @GetMapping("/blood-types/{id}")
    @Timed
    public ResponseEntity<BloodType> getBloodType(@PathVariable Long id) {
        log.debug("REST request to get BloodType : {}", id);
        BloodType bloodType = bloodTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bloodType));
    }

    /**
     * DELETE  /blood-types/:id : delete the "id" bloodType.
     *
     * @param id the id of the bloodType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blood-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteBloodType(@PathVariable Long id) {
        log.debug("REST request to delete BloodType : {}", id);
        bloodTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/blood-types?query=:query : search for the bloodType corresponding
     * to the query.
     *
     * @param query the query of the bloodType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/blood-types")
    @Timed
    public ResponseEntity<List<BloodType>> searchBloodTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of BloodTypes for query {}", query);
        Page<BloodType> page = bloodTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/blood-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
