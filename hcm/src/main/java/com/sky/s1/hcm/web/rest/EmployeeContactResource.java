package com.sky.s1.hcm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sky.s1.hcm.domain.EmployeeContact;
import com.sky.s1.hcm.service.EmployeeContactService;
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
 * REST controller for managing EmployeeContact.
 */
@RestController
@RequestMapping("/api")
public class EmployeeContactResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeContactResource.class);

    private static final String ENTITY_NAME = "employeeContact";

    private final EmployeeContactService employeeContactService;

    public EmployeeContactResource(EmployeeContactService employeeContactService) {
        this.employeeContactService = employeeContactService;
    }

    /**
     * POST  /employee-contacts : Create a new employeeContact.
     *
     * @param employeeContact the employeeContact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeContact, or with status 400 (Bad Request) if the employeeContact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employee-contacts")
    @Timed
    public ResponseEntity<EmployeeContact> createEmployeeContact(@Valid @RequestBody EmployeeContact employeeContact) throws URISyntaxException {
        log.debug("REST request to save EmployeeContact : {}", employeeContact);
        if (employeeContact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new employeeContact cannot already have an ID")).body(null);
        }
        EmployeeContact result = employeeContactService.save(employeeContact);
        return ResponseEntity.created(new URI("/api/employee-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-contacts : Updates an existing employeeContact.
     *
     * @param employeeContact the employeeContact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeContact,
     * or with status 400 (Bad Request) if the employeeContact is not valid,
     * or with status 500 (Internal Server Error) if the employeeContact couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employee-contacts")
    @Timed
    public ResponseEntity<EmployeeContact> updateEmployeeContact(@Valid @RequestBody EmployeeContact employeeContact) throws URISyntaxException {
        log.debug("REST request to update EmployeeContact : {}", employeeContact);
        if (employeeContact.getId() == null) {
            return createEmployeeContact(employeeContact);
        }
        EmployeeContact result = employeeContactService.save(employeeContact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employeeContact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-contacts : get all the employeeContacts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employeeContacts in body
     */
    @GetMapping("/employee-contacts")
    @Timed
    public ResponseEntity<List<EmployeeContact>> getAllEmployeeContacts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of EmployeeContacts");
        Page<EmployeeContact> page = employeeContactService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employee-contacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employee-contacts/:id : get the "id" employeeContact.
     *
     * @param id the id of the employeeContact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeContact, or with status 404 (Not Found)
     */
    @GetMapping("/employee-contacts/{id}")
    @Timed
    public ResponseEntity<EmployeeContact> getEmployeeContact(@PathVariable Long id) {
        log.debug("REST request to get EmployeeContact : {}", id);
        EmployeeContact employeeContact = employeeContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employeeContact));
    }

    /**
     * DELETE  /employee-contacts/:id : delete the "id" employeeContact.
     *
     * @param id the id of the employeeContact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employee-contacts/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmployeeContact(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeContact : {}", id);
        employeeContactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/employee-contacts?query=:query : search for the employeeContact corresponding
     * to the query.
     *
     * @param query the query of the employeeContact search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/employee-contacts")
    @Timed
    public ResponseEntity<List<EmployeeContact>> searchEmployeeContacts(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of EmployeeContacts for query {}", query);
        Page<EmployeeContact> page = employeeContactService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/employee-contacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
