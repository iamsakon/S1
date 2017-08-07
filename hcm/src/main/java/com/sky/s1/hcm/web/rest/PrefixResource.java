package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.Prefix;
import com.sky.s1.hcm.service.PrefixService;
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
 * REST controller for managing Prefix.
 */
@RestController
@RequestMapping("/api")
public class PrefixResource {

    private final Logger log = LoggerFactory.getLogger(PrefixResource.class);

    private static final String ENTITY_NAME = "prefix";

    private final PrefixService prefixService;

    public PrefixResource(PrefixService prefixService) {
        this.prefixService = prefixService;
    }

    /**
     * POST  /prefixes : Create a new prefix.
     *
     * @param prefix the prefix to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prefix, or with status 400 (Bad Request) if the prefix has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prefixes")
    @Timed
    public ResponseEntity<Prefix> createPrefix(@Valid @RequestBody Prefix prefix) throws URISyntaxException {
        log.debug("REST request to save Prefix : {}", prefix);
        if (prefix.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new prefix cannot already have an ID")).body(null);
        }
        Prefix result = prefixService.save(prefix);
        return ResponseEntity.created(new URI("/api/prefixes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prefixes : Updates an existing prefix.
     *
     * @param prefix the prefix to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prefix,
     * or with status 400 (Bad Request) if the prefix is not valid,
     * or with status 500 (Internal Server Error) if the prefix couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prefixes")
    @Timed
    public ResponseEntity<Prefix> updatePrefix(@Valid @RequestBody Prefix prefix) throws URISyntaxException {
        log.debug("REST request to update Prefix : {}", prefix);
        if (prefix.getId() == null) {
            return createPrefix(prefix);
        }
        Prefix result = prefixService.save(prefix);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prefix.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prefixes : get all the prefixes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prefixes in body
     */
    @GetMapping("/prefixes")
    @Timed
    public ResponseEntity<List<Prefix>> getAllPrefixes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Prefixes");
        Page<Prefix> page = prefixService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prefixes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prefixes/:id : get the "id" prefix.
     *
     * @param id the id of the prefix to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prefix, or with status 404 (Not Found)
     */
    @GetMapping("/prefixes/{id}")
    @Timed
    public ResponseEntity<Prefix> getPrefix(@PathVariable Long id) {
        log.debug("REST request to get Prefix : {}", id);
        Prefix prefix = prefixService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(prefix));
    }

    /**
     * DELETE  /prefixes/:id : delete the "id" prefix.
     *
     * @param id the id of the prefix to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prefixes/{id}")
    @Timed
    public ResponseEntity<Void> deletePrefix(@PathVariable Long id) {
        log.debug("REST request to delete Prefix : {}", id);
        prefixService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/prefixes?query=:query : search for the prefix corresponding
     * to the query.
     *
     * @param query the query of the prefix search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/prefixes")
    @Timed
    public ResponseEntity<List<Prefix>> searchPrefixes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Prefixes for query {}", query);
        Page<Prefix> page = prefixService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prefixes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
