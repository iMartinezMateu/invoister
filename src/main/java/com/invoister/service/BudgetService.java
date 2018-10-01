package com.invoister.service;

import com.invoister.service.dto.BudgetDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Budget.
 */
public interface BudgetService {

    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save
     * @return the persisted entity
     */
    BudgetDTO save(BudgetDTO budgetDTO);

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BudgetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" budget.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BudgetDTO> findOne(Long id);

    /**
     * Delete the "id" budget.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the budget corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BudgetDTO> search(String query, Pageable pageable);
}
