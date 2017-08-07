package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.Gender;
import com.sky.s1.hcm.service.GenderService;
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
 * REST controller for managing Gender.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

    private final Logger log = LoggerFactory.getLogger(GenderResource.class);

    private static final String ENTITY_NAME = "gender";

    private final GenderService genderService;

    public GenderResource(GenderService genderService) {
        this.genderService = genderService;
    }

    /**
     * POST  /genders : Create a new gender.
     *
     * @param gender the gender to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gender, or with status 400 (Bad Request) if the gender has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/genders")
    @Timed
    public ResponseEntity<Gender> createGender(@Valid @RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to save Gender : {}", gender);
        if (gender.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gender cannot already have an ID")).body(null);
        }
        Gender result = genderService.save(gender);
        return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genders : Updates an existing gender.
     *
     * @param gender the gender to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gender,
     * or with status 400 (Bad Request) if the gender is not valid,
     * or with status 500 (Internal Server Error) if the gender couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/genders")
    @Timed
    public ResponseEntity<Gender> updateGender(@Valid @RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to update Gender : {}", gender);
        if (gender.getId() == null) {
            return createGender(gender);
        }
        Gender result = genderService.save(gender);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genders : get all the genders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of genders in body
     */
    @GetMapping("/genders")
    @Timed
    public ResponseEntity<List<Gender>> getAllGenders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Genders");
        Page<Gender> page = genderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genders/:id : get the "id" gender.
     *
     * @param id the id of the gender to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gender, or with status 404 (Not Found)
     */
    @GetMapping("/genders/{id}")
    @Timed
    public ResponseEntity<Gender> getGender(@PathVariable Long id) {
        log.debug("REST request to get Gender : {}", id);
        Gender gender = genderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gender));
    }

    /**
     * DELETE  /genders/:id : delete the "id" gender.
     *
     * @param id the id of the gender to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/genders/{id}")
    @Timed
    public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
        log.debug("REST request to delete Gender : {}", id);
        genderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/genders?query=:query : search for the gender corresponding
     * to the query.
     *
     * @param query the query of the gender search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/genders")
    @Timed
    public ResponseEntity<List<Gender>> searchGenders(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Genders for query {}", query);
        Page<Gender> page = genderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/genders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
