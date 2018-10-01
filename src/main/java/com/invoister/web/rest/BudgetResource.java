package com.invoister.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.invoister.service.BudgetService;
import com.invoister.web.rest.errors.BadRequestAlertException;
import com.invoister.web.rest.util.HeaderUtil;
import com.invoister.web.rest.util.PaginationUtil;
import com.invoister.service.dto.BudgetDTO;
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
 * REST controller for managing Budget.
 */
@RestController
@RequestMapping("/api")
public class BudgetResource {

    private final Logger log = LoggerFactory.getLogger(BudgetResource.class);

    private static final String ENTITY_NAME = "budget";

    private final BudgetService budgetService;

    public BudgetResource(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    /**
     * POST  /budgets : Create a new budget.
     *
     * @param budgetDTO the budgetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new budgetDTO, or with status 400 (Bad Request) if the budget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/budgets")
    @Timed
    public ResponseEntity<BudgetDTO> createBudget(@Valid @RequestBody BudgetDTO budgetDTO) throws URISyntaxException {
        log.debug("REST request to save Budget : {}", budgetDTO);
        if (budgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new budget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BudgetDTO result = budgetService.save(budgetDTO);
        return ResponseEntity.created(new URI("/api/budgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgets : Updates an existing budget.
     *
     * @param budgetDTO the budgetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated budgetDTO,
     * or with status 400 (Bad Request) if the budgetDTO is not valid,
     * or with status 500 (Internal Server Error) if the budgetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/budgets")
    @Timed
    public ResponseEntity<BudgetDTO> updateBudget(@Valid @RequestBody BudgetDTO budgetDTO) throws URISyntaxException {
        log.debug("REST request to update Budget : {}", budgetDTO);
        if (budgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BudgetDTO result = budgetService.save(budgetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, budgetDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgets : get all the budgets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of budgets in body
     */
    @GetMapping("/budgets")
    @Timed
    public ResponseEntity<List<BudgetDTO>> getAllBudgets(Pageable pageable) {
        log.debug("REST request to get a page of Budgets");
        Page<BudgetDTO> page = budgetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/budgets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /budgets/:id : get the "id" budget.
     *
     * @param id the id of the budgetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the budgetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/budgets/{id}")
    @Timed
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable Long id) {
        log.debug("REST request to get Budget : {}", id);
        Optional<BudgetDTO> budgetDTO = budgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetDTO);
    }

    /**
     * DELETE  /budgets/:id : delete the "id" budget.
     *
     * @param id the id of the budgetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/budgets/{id}")
    @Timed
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        log.debug("REST request to delete Budget : {}", id);
        budgetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/budgets?query=:query : search for the budget corresponding
     * to the query.
     *
     * @param query the query of the budget search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/budgets")
    @Timed
    public ResponseEntity<List<BudgetDTO>> searchBudgets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Budgets for query {}", query);
        Page<BudgetDTO> page = budgetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/budgets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
