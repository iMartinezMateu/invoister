package com.invoister.service.impl;

import com.invoister.service.CostumerService;
import com.invoister.domain.Costumer;
import com.invoister.repository.CostumerRepository;
import com.invoister.repository.search.CostumerSearchRepository;
import com.invoister.service.dto.CostumerDTO;
import com.invoister.service.mapper.CostumerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Costumer.
 */
@Service
@Transactional
public class CostumerServiceImpl implements CostumerService {

    private final Logger log = LoggerFactory.getLogger(CostumerServiceImpl.class);

    private final CostumerRepository costumerRepository;

    private final CostumerMapper costumerMapper;

    private final CostumerSearchRepository costumerSearchRepository;

    public CostumerServiceImpl(CostumerRepository costumerRepository, CostumerMapper costumerMapper, CostumerSearchRepository costumerSearchRepository) {
        this.costumerRepository = costumerRepository;
        this.costumerMapper = costumerMapper;
        this.costumerSearchRepository = costumerSearchRepository;
    }

    /**
     * Save a costumer.
     *
     * @param costumerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CostumerDTO save(CostumerDTO costumerDTO) {
        log.debug("Request to save Costumer : {}", costumerDTO);
        Costumer costumer = costumerMapper.toEntity(costumerDTO);
        costumer = costumerRepository.save(costumer);
        CostumerDTO result = costumerMapper.toDto(costumer);
        costumerSearchRepository.save(costumer);
        return result;
    }

    /**
     * Get all the costumers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CostumerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Costumers");
        return costumerRepository.findAll(pageable)
            .map(costumerMapper::toDto);
    }


    /**
     * Get one costumer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CostumerDTO> findOne(Long id) {
        log.debug("Request to get Costumer : {}", id);
        return costumerRepository.findById(id)
            .map(costumerMapper::toDto);
    }

    /**
     * Delete the costumer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Costumer : {}", id);
        costumerRepository.deleteById(id);
        costumerSearchRepository.deleteById(id);
    }

    /**
     * Search for the costumer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CostumerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Costumers for query {}", query);
        return costumerSearchRepository.search(queryStringQuery(query), pageable)
            .map(costumerMapper::toDto);
    }
}
