package com.invoister.service.impl;

import com.invoister.service.BudgetService;
import com.invoister.domain.Budget;
import com.invoister.repository.BudgetRepository;
import com.invoister.repository.search.BudgetSearchRepository;
import com.invoister.service.dto.BudgetDTO;
import com.invoister.service.mapper.BudgetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Budget.
 */
@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private final Logger log = LoggerFactory.getLogger(BudgetServiceImpl.class);

    private final BudgetRepository budgetRepository;

    private final BudgetMapper budgetMapper;

    private final BudgetSearchRepository budgetSearchRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, BudgetMapper budgetMapper, BudgetSearchRepository budgetSearchRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
        this.budgetSearchRepository = budgetSearchRepository;
    }

    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BudgetDTO save(BudgetDTO budgetDTO) {
        log.debug("Request to save Budget : {}", budgetDTO);
        Budget budget = budgetMapper.toEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        BudgetDTO result = budgetMapper.toDto(budget);
        budgetSearchRepository.save(budget);
        return result;
    }

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BudgetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Budgets");
        return budgetRepository.findAll(pageable)
            .map(budgetMapper::toDto);
    }


    /**
     * Get one budget by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BudgetDTO> findOne(Long id) {
        log.debug("Request to get Budget : {}", id);
        return budgetRepository.findById(id)
            .map(budgetMapper::toDto);
    }

    /**
     * Delete the budget by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Budget : {}", id);
        budgetRepository.deleteById(id);
        budgetSearchRepository.deleteById(id);
    }

    /**
     * Search for the budget corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BudgetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Budgets for query {}", query);
        return budgetSearchRepository.search(queryStringQuery(query), pageable)
            .map(budgetMapper::toDto);
    }
}
