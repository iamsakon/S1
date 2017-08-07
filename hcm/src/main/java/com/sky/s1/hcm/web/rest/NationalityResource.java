package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.Nationality;
import com.sky.s1.hcm.service.NationalityService;
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
 * REST controller for managing Nationality.
 */
@RestController
@RequestMapping("/api")
public class NationalityResource {

    private final Logger log = LoggerFactory.getLogger(NationalityResource.class);

    private static final String ENTITY_NAME = "nationality";

    private final NationalityService nationalityService;

    public NationalityResource(NationalityService nationalityService) {
        this.nationalityService = nationalityService;
    }

    /**
     * POST  /nationalities : Create a new nationality.
     *
     * @param nationality the nationality to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nationality, or with status 400 (Bad Request) if the nationality has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nationalities")
    @Timed
    public ResponseEntity<Nationality> createNationality(@Valid @RequestBody Nationality nationality) throws URISyntaxException {
        log.debug("REST request to save Nationality : {}", nationality);
        if (nationality.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new nationality cannot already have an ID")).body(null);
        }
        Nationality result = nationalityService.save(nationality);
        return ResponseEntity.created(new URI("/api/nationalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /nationalities : Updates an existing nationality.
     *
     * @param nationality the nationality to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nationality,
     * or with status 400 (Bad Request) if the nationality is not valid,
     * or with status 500 (Internal Server Error) if the nationality couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nationalities")
    @Timed
    public ResponseEntity<Nationality> updateNationality(@Valid @RequestBody Nationality nationality) throws URISyntaxException {
        log.debug("REST request to update Nationality : {}", nationality);
        if (nationality.getId() == null) {
            return createNationality(nationality);
        }
        Nationality result = nationalityService.save(nationality);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nationality.getId().toString()))
            .body(result);
    }

    /**
     * GET  /nationalities : get all the nationalities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of nationalities in body
     */
    @GetMapping("/nationalities")
    @Timed
    public ResponseEntity<List<Nationality>> getAllNationalities(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Nationalities");
        Page<Nationality> page = nationalityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nationalities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /nationalities/:id : get the "id" nationality.
     *
     * @param id the id of the nationality to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nationality, or with status 404 (Not Found)
     */
    @GetMapping("/nationalities/{id}")
    @Timed
    public ResponseEntity<Nationality> getNationality(@PathVariable Long id) {
        log.debug("REST request to get Nationality : {}", id);
        Nationality nationality = nationalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nationality));
    }

    /**
     * DELETE  /nationalities/:id : delete the "id" nationality.
     *
     * @param id the id of the nationality to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nationalities/{id}")
    @Timed
    public ResponseEntity<Void> deleteNationality(@PathVariable Long id) {
        log.debug("REST request to delete Nationality : {}", id);
        nationalityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/nationalities?query=:query : search for the nationality corresponding
     * to the query.
     *
     * @param query the query of the nationality search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/nationalities")
    @Timed
    public ResponseEntity<List<Nationality>> searchNationalities(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Nationalities for query {}", query);
        Page<Nationality> page = nationalityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/nationalities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
