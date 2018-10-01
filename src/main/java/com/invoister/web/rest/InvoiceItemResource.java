package com.invoister.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.invoister.service.InvoiceItemService;
import com.invoister.web.rest.errors.BadRequestAlertException;
import com.invoister.web.rest.util.HeaderUtil;
import com.invoister.service.dto.InvoiceItemDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing InvoiceItem.
 */
@RestController
@RequestMapping("/api")
public class InvoiceItemResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceItemResource.class);

    private static final String ENTITY_NAME = "invoiceItem";

    private final InvoiceItemService invoiceItemService;

    public InvoiceItemResource(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    /**
     * POST  /invoice-items : Create a new invoiceItem.
     *
     * @param invoiceItemDTO the invoiceItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceItemDTO, or with status 400 (Bad Request) if the invoiceItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-items")
    @Timed
    public ResponseEntity<InvoiceItemDTO> createInvoiceItem(@Valid @RequestBody InvoiceItemDTO invoiceItemDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceItem : {}", invoiceItemDTO);
        if (invoiceItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceItemDTO result = invoiceItemService.save(invoiceItemDTO);
        return ResponseEntity.created(new URI("/api/invoice-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-items : Updates an existing invoiceItem.
     *
     * @param invoiceItemDTO the invoiceItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceItemDTO,
     * or with status 400 (Bad Request) if the invoiceItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-items")
    @Timed
    public ResponseEntity<InvoiceItemDTO> updateInvoiceItem(@Valid @RequestBody InvoiceItemDTO invoiceItemDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceItem : {}", invoiceItemDTO);
        if (invoiceItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceItemDTO result = invoiceItemService.save(invoiceItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-items : get all the invoiceItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceItems in body
     */
    @GetMapping("/invoice-items")
    @Timed
    public List<InvoiceItemDTO> getAllInvoiceItems(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all InvoiceItems");
        return invoiceItemService.findAll();
    }

    /**
     * GET  /invoice-items/:id : get the "id" invoiceItem.
     *
     * @param id the id of the invoiceItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-items/{id}")
    @Timed
    public ResponseEntity<InvoiceItemDTO> getInvoiceItem(@PathVariable Long id) {
        log.debug("REST request to get InvoiceItem : {}", id);
        Optional<InvoiceItemDTO> invoiceItemDTO = invoiceItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceItemDTO);
    }

    /**
     * DELETE  /invoice-items/:id : delete the "id" invoiceItem.
     *
     * @param id the id of the invoiceItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceItem : {}", id);
        invoiceItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/invoice-items?query=:query : search for the invoiceItem corresponding
     * to the query.
     *
     * @param query the query of the invoiceItem search
     * @return the result of the search
     */
    @GetMapping("/_search/invoice-items")
    @Timed
    public List<InvoiceItemDTO> searchInvoiceItems(@RequestParam String query) {
        log.debug("REST request to search InvoiceItems for query {}", query);
        return invoiceItemService.search(query);
    }

}
