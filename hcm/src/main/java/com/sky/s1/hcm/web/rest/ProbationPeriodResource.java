package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.ProbationPeriod;
import com.sky.s1.hcm.service.ProbationPeriodService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProbationPeriod.
 */
@RestController
@RequestMapping("/api")
public class ProbationPeriodResource {

    private final Logger log = LoggerFactory.getLogger(ProbationPeriodResource.class);

    private static final String ENTITY_NAME = "probationPeriod";

    private final ProbationPeriodService probationPeriodService;

    public ProbationPeriodResource(ProbationPeriodService probationPeriodService) {
        this.probationPeriodService = probationPeriodService;
    }

    /**
     * POST  /probation-periods : Create a new probationPeriod.
     *
     * @param probationPeriod the probationPeriod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new probationPeriod, or with status 400 (Bad Request) if the probationPeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/probation-periods")
    @Timed
    public ResponseEntity<ProbationPeriod> createProbationPeriod(@RequestBody ProbationPeriod probationPeriod) throws URISyntaxException {
        log.debug("REST request to save ProbationPeriod : {}", probationPeriod);
        if (probationPeriod.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new probationPeriod cannot already have an ID")).body(null);
        }
        ProbationPeriod result = probationPeriodService.save(probationPeriod);
        return ResponseEntity.created(new URI("/api/probation-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /probation-periods : Updates an existing probationPeriod.
     *
     * @param probationPeriod the probationPeriod to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated probationPeriod,
     * or with status 400 (Bad Request) if the probationPeriod is not valid,
     * or with status 500 (Internal Server Error) if the probationPeriod couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/probation-periods")
    @Timed
    public ResponseEntity<ProbationPeriod> updateProbationPeriod(@RequestBody ProbationPeriod probationPeriod) throws URISyntaxException {
        log.debug("REST request to update ProbationPeriod : {}", probationPeriod);
        if (probationPeriod.getId() == null) {
            return createProbationPeriod(probationPeriod);
        }
        ProbationPeriod result = probationPeriodService.save(probationPeriod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, probationPeriod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /probation-periods : get all the probationPeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of probationPeriods in body
     */
    @GetMapping("/probation-periods")
    @Timed
    public ResponseEntity<List<ProbationPeriod>> getAllProbationPeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProbationPeriods");
        Page<ProbationPeriod> page = probationPeriodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/probation-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /probation-periods/:id : get the "id" probationPeriod.
     *
     * @param id the id of the probationPeriod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the probationPeriod, or with status 404 (Not Found)
     */
    @GetMapping("/probation-periods/{id}")
    @Timed
    public ResponseEntity<ProbationPeriod> getProbationPeriod(@PathVariable Long id) {
        log.debug("REST request to get ProbationPeriod : {}", id);
        ProbationPeriod probationPeriod = probationPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(probationPeriod));
    }

    /**
     * DELETE  /probation-periods/:id : delete the "id" probationPeriod.
     *
     * @param id the id of the probationPeriod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/probation-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteProbationPeriod(@PathVariable Long id) {
        log.debug("REST request to delete ProbationPeriod : {}", id);
        probationPeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/probation-periods?query=:query : search for the probationPeriod corresponding
     * to the query.
     *
     * @param query the query of the probationPeriod search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/probation-periods")
    @Timed
    public ResponseEntity<List<ProbationPeriod>> searchProbationPeriods(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ProbationPeriods for query {}", query);
        Page<ProbationPeriod> page = probationPeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/probation-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
