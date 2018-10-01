package com.invoister.service;

import com.invoister.service.dto.CostumerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Costumer.
 */
public interface CostumerService {

    /**
     * Save a costumer.
     *
     * @param costumerDTO the entity to save
     * @return the persisted entity
     */
    CostumerDTO save(CostumerDTO costumerDTO);

    /**
     * Get all the costumers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CostumerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" costumer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CostumerDTO> findOne(Long id);

    /**
     * Delete the "id" costumer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the costumer corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CostumerDTO> search(String query, Pageable pageable);
}
