package com.invoister.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.invoister.service.NotificatorService;
import com.invoister.web.rest.errors.BadRequestAlertException;
import com.invoister.web.rest.util.HeaderUtil;
import com.invoister.service.dto.NotificatorDTO;
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
 * REST controller for managing Notificator.
 */
@RestController
@RequestMapping("/api")
public class NotificatorResource {

    private final Logger log = LoggerFactory.getLogger(NotificatorResource.class);

    private static final String ENTITY_NAME = "notificator";

    private final NotificatorService notificatorService;

    public NotificatorResource(NotificatorService notificatorService) {
        this.notificatorService = notificatorService;
    }

    /**
     * POST  /notificators : Create a new notificator.
     *
     * @param notificatorDTO the notificatorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificatorDTO, or with status 400 (Bad Request) if the notificator has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notificators")
    @Timed
    public ResponseEntity<NotificatorDTO> createNotificator(@Valid @RequestBody NotificatorDTO notificatorDTO) throws URISyntaxException {
        log.debug("REST request to save Notificator : {}", notificatorDTO);
        if (notificatorDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificatorDTO result = notificatorService.save(notificatorDTO);
        return ResponseEntity.created(new URI("/api/notificators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notificators : Updates an existing notificator.
     *
     * @param notificatorDTO the notificatorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificatorDTO,
     * or with status 400 (Bad Request) if the notificatorDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificatorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notificators")
    @Timed
    public ResponseEntity<NotificatorDTO> updateNotificator(@Valid @RequestBody NotificatorDTO notificatorDTO) throws URISyntaxException {
        log.debug("REST request to update Notificator : {}", notificatorDTO);
        if (notificatorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificatorDTO result = notificatorService.save(notificatorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificatorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notificators : get all the notificators.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notificators in body
     */
    @GetMapping("/notificators")
    @Timed
    public List<NotificatorDTO> getAllNotificators() {
        log.debug("REST request to get all Notificators");
        return notificatorService.findAll();
    }

    /**
     * GET  /notificators/:id : get the "id" notificator.
     *
     * @param id the id of the notificatorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificatorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notificators/{id}")
    @Timed
    public ResponseEntity<NotificatorDTO> getNotificator(@PathVariable Long id) {
        log.debug("REST request to get Notificator : {}", id);
        Optional<NotificatorDTO> notificatorDTO = notificatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificatorDTO);
    }

    /**
     * DELETE  /notificators/:id : delete the "id" notificator.
     *
     * @param id the id of the notificatorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notificators/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotificator(@PathVariable Long id) {
        log.debug("REST request to delete Notificator : {}", id);
        notificatorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/notificators?query=:query : search for the notificator corresponding
     * to the query.
     *
     * @param query the query of the notificator search
     * @return the result of the search
     */
    @GetMapping("/_search/notificators")
    @Timed
    public List<NotificatorDTO> searchNotificators(@RequestParam String query) {
        log.debug("REST request to search Notificators for query {}", query);
        return notificatorService.search(query);
    }

}
