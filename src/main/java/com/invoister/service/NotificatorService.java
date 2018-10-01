package com.invoister.service;

import com.invoister.service.dto.NotificatorDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Notificator.
 */
public interface NotificatorService {

    /**
     * Save a notificator.
     *
     * @param notificatorDTO the entity to save
     * @return the persisted entity
     */
    NotificatorDTO save(NotificatorDTO notificatorDTO);

    /**
     * Get all the notificators.
     *
     * @return the list of entities
     */
    List<NotificatorDTO> findAll();


    /**
     * Get the "id" notificator.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NotificatorDTO> findOne(Long id);

    /**
     * Delete the "id" notificator.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the notificator corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<NotificatorDTO> search(String query);
}
