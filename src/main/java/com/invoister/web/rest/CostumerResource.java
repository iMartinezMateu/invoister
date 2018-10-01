package com.invoister.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.invoister.service.CostumerService;
import com.invoister.web.rest.errors.BadRequestAlertException;
import com.invoister.web.rest.util.HeaderUtil;
import com.invoister.web.rest.util.PaginationUtil;
import com.invoister.service.dto.CostumerDTO;
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
 * REST controller for managing Costumer.
 */
@RestController
@RequestMapping("/api")
public class CostumerResource {

    private final Logger log = LoggerFactory.getLogger(CostumerResource.class);

    private static final String ENTITY_NAME = "costumer";

    private final CostumerService costumerService;

    public CostumerResource(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    /**
     * POST  /costumers : Create a new costumer.
     *
     * @param costumerDTO the costumerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new costumerDTO, or with status 400 (Bad Request) if the costumer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/costumers")
    @Timed
    public ResponseEntity<CostumerDTO> createCostumer(@Valid @RequestBody CostumerDTO costumerDTO) throws URISyntaxException {
        log.debug("REST request to save Costumer : {}", costumerDTO);
        if (costumerDTO.getId() != null) {
            throw new BadRequestAlertException("A new costumer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostumerDTO result = costumerService.save(costumerDTO);
        return ResponseEntity.created(new URI("/api/costumers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /costumers : Updates an existing costumer.
     *
     * @param costumerDTO the costumerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated costumerDTO,
     * or with status 400 (Bad Request) if the costumerDTO is not valid,
     * or with status 500 (Internal Server Error) if the costumerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/costumers")
    @Timed
    public ResponseEntity<CostumerDTO> updateCostumer(@Valid @RequestBody CostumerDTO costumerDTO) throws URISyntaxException {
        log.debug("REST request to update Costumer : {}", costumerDTO);
        if (costumerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CostumerDTO result = costumerService.save(costumerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, costumerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /costumers : get all the costumers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of costumers in body
     */
    @GetMapping("/costumers")
    @Timed
    public ResponseEntity<List<CostumerDTO>> getAllCostumers(Pageable pageable) {
        log.debug("REST request to get a page of Costumers");
        Page<CostumerDTO> page = costumerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/costumers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /costumers/:id : get the "id" costumer.
     *
     * @param id the id of the costumerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the costumerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/costumers/{id}")
    @Timed
    public ResponseEntity<CostumerDTO> getCostumer(@PathVariable Long id) {
        log.debug("REST request to get Costumer : {}", id);
        Optional<CostumerDTO> costumerDTO = costumerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costumerDTO);
    }

    /**
     * DELETE  /costumers/:id : delete the "id" costumer.
     *
     * @param id the id of the costumerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/costumers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCostumer(@PathVariable Long id) {
        log.debug("REST request to delete Costumer : {}", id);
        costumerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/costumers?query=:query : search for the costumer corresponding
     * to the query.
     *
     * @param query the query of the costumer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/costumers")
    @Timed
    public ResponseEntity<List<CostumerDTO>> searchCostumers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Costumers for query {}", query);
        Page<CostumerDTO> page = costumerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/costumers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
