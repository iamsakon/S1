package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.ProbationDay;
import com.sky.s1.hcm.service.ProbationDayService;
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
 * REST controller for managing ProbationDay.
 */
@RestController
@RequestMapping("/api")
public class ProbationDayResource {

    private final Logger log = LoggerFactory.getLogger(ProbationDayResource.class);

    private static final String ENTITY_NAME = "probationDay";

    private final ProbationDayService probationDayService;

    public ProbationDayResource(ProbationDayService probationDayService) {
        this.probationDayService = probationDayService;
    }

    /**
     * POST  /probation-days : Create a new probationDay.
     *
     * @param probationDay the probationDay to create
     * @return the ResponseEntity with status 201 (Created) and with body the new probationDay, or with status 400 (Bad Request) if the probationDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/probation-days")
    @Timed
    public ResponseEntity<ProbationDay> createProbationDay(@RequestBody ProbationDay probationDay) throws URISyntaxException {
        log.debug("REST request to save ProbationDay : {}", probationDay);
        if (probationDay.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new probationDay cannot already have an ID")).body(null);
        }
        ProbationDay result = probationDayService.save(probationDay);
        return ResponseEntity.created(new URI("/api/probation-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /probation-days : Updates an existing probationDay.
     *
     * @param probationDay the probationDay to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated probationDay,
     * or with status 400 (Bad Request) if the probationDay is not valid,
     * or with status 500 (Internal Server Error) if the probationDay couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/probation-days")
    @Timed
    public ResponseEntity<ProbationDay> updateProbationDay(@RequestBody ProbationDay probationDay) throws URISyntaxException {
        log.debug("REST request to update ProbationDay : {}", probationDay);
        if (probationDay.getId() == null) {
            return createProbationDay(probationDay);
        }
        ProbationDay result = probationDayService.save(probationDay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, probationDay.getId().toString()))
            .body(result);
    }

    /**
     * GET  /probation-days : get all the probationDays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of probationDays in body
     */
    @GetMapping("/probation-days")
    @Timed
    public ResponseEntity<List<ProbationDay>> getAllProbationDays(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProbationDays");
        Page<ProbationDay> page = probationDayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/probation-days");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /probation-days/:id : get the "id" probationDay.
     *
     * @param id the id of the probationDay to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the probationDay, or with status 404 (Not Found)
     */
    @GetMapping("/probation-days/{id}")
    @Timed
    public ResponseEntity<ProbationDay> getProbationDay(@PathVariable Long id) {
        log.debug("REST request to get ProbationDay : {}", id);
        ProbationDay probationDay = probationDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(probationDay));
    }

    /**
     * DELETE  /probation-days/:id : delete the "id" probationDay.
     *
     * @param id the id of the probationDay to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/probation-days/{id}")
    @Timed
    public ResponseEntity<Void> deleteProbationDay(@PathVariable Long id) {
        log.debug("REST request to delete ProbationDay : {}", id);
        probationDayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/probation-days?query=:query : search for the probationDay corresponding
     * to the query.
     *
     * @param query the query of the probationDay search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/probation-days")
    @Timed
    public ResponseEntity<List<ProbationDay>> searchProbationDays(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ProbationDays for query {}", query);
        Page<ProbationDay> page = probationDayService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/probation-days");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
