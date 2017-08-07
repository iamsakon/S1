package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.MilitaryStatus;
import com.sky.s1.hcm.service.MilitaryStatusService;
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
 * REST controller for managing MilitaryStatus.
 */
@RestController
@RequestMapping("/api")
public class MilitaryStatusResource {

    private final Logger log = LoggerFactory.getLogger(MilitaryStatusResource.class);

    private static final String ENTITY_NAME = "militaryStatus";

    private final MilitaryStatusService militaryStatusService;

    public MilitaryStatusResource(MilitaryStatusService militaryStatusService) {
        this.militaryStatusService = militaryStatusService;
    }

    /**
     * POST  /military-statuses : Create a new militaryStatus.
     *
     * @param militaryStatus the militaryStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new militaryStatus, or with status 400 (Bad Request) if the militaryStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/military-statuses")
    @Timed
    public ResponseEntity<MilitaryStatus> createMilitaryStatus(@Valid @RequestBody MilitaryStatus militaryStatus) throws URISyntaxException {
        log.debug("REST request to save MilitaryStatus : {}", militaryStatus);
        if (militaryStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new militaryStatus cannot already have an ID")).body(null);
        }
        MilitaryStatus result = militaryStatusService.save(militaryStatus);
        return ResponseEntity.created(new URI("/api/military-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /military-statuses : Updates an existing militaryStatus.
     *
     * @param militaryStatus the militaryStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated militaryStatus,
     * or with status 400 (Bad Request) if the militaryStatus is not valid,
     * or with status 500 (Internal Server Error) if the militaryStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/military-statuses")
    @Timed
    public ResponseEntity<MilitaryStatus> updateMilitaryStatus(@Valid @RequestBody MilitaryStatus militaryStatus) throws URISyntaxException {
        log.debug("REST request to update MilitaryStatus : {}", militaryStatus);
        if (militaryStatus.getId() == null) {
            return createMilitaryStatus(militaryStatus);
        }
        MilitaryStatus result = militaryStatusService.save(militaryStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, militaryStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /military-statuses : get all the militaryStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of militaryStatuses in body
     */
    @GetMapping("/military-statuses")
    @Timed
    public ResponseEntity<List<MilitaryStatus>> getAllMilitaryStatuses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MilitaryStatuses");
        Page<MilitaryStatus> page = militaryStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/military-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /military-statuses/:id : get the "id" militaryStatus.
     *
     * @param id the id of the militaryStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the militaryStatus, or with status 404 (Not Found)
     */
    @GetMapping("/military-statuses/{id}")
    @Timed
    public ResponseEntity<MilitaryStatus> getMilitaryStatus(@PathVariable Long id) {
        log.debug("REST request to get MilitaryStatus : {}", id);
        MilitaryStatus militaryStatus = militaryStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(militaryStatus));
    }

    /**
     * DELETE  /military-statuses/:id : delete the "id" militaryStatus.
     *
     * @param id the id of the militaryStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/military-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteMilitaryStatus(@PathVariable Long id) {
        log.debug("REST request to delete MilitaryStatus : {}", id);
        militaryStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/military-statuses?query=:query : search for the militaryStatus corresponding
     * to the query.
     *
     * @param query the query of the militaryStatus search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/military-statuses")
    @Timed
    public ResponseEntity<List<MilitaryStatus>> searchMilitaryStatuses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MilitaryStatuses for query {}", query);
        Page<MilitaryStatus> page = militaryStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/military-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
