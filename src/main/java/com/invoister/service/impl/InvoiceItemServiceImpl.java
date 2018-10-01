package com.invoister.service.impl;

import com.invoister.service.InvoiceItemService;
import com.invoister.domain.InvoiceItem;
import com.invoister.repository.InvoiceItemRepository;
import com.invoister.repository.search.InvoiceItemSearchRepository;
import com.invoister.service.dto.InvoiceItemDTO;
import com.invoister.service.mapper.InvoiceItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InvoiceItem.
 */
@Service
@Transactional
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final Logger log = LoggerFactory.getLogger(InvoiceItemServiceImpl.class);

    private final InvoiceItemRepository invoiceItemRepository;

    private final InvoiceItemMapper invoiceItemMapper;

    private final InvoiceItemSearchRepository invoiceItemSearchRepository;

    public InvoiceItemServiceImpl(InvoiceItemRepository invoiceItemRepository, InvoiceItemMapper invoiceItemMapper, InvoiceItemSearchRepository invoiceItemSearchRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemMapper = invoiceItemMapper;
        this.invoiceItemSearchRepository = invoiceItemSearchRepository;
    }

    /**
     * Save a invoiceItem.
     *
     * @param invoiceItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceItemDTO save(InvoiceItemDTO invoiceItemDTO) {
        log.debug("Request to save InvoiceItem : {}", invoiceItemDTO);
        InvoiceItem invoiceItem = invoiceItemMapper.toEntity(invoiceItemDTO);
        invoiceItem = invoiceItemRepository.save(invoiceItem);
        InvoiceItemDTO result = invoiceItemMapper.toDto(invoiceItem);
        invoiceItemSearchRepository.save(invoiceItem);
        return result;
    }

    /**
     * Get all the invoiceItems.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceItemDTO> findAll() {
        log.debug("Request to get all InvoiceItems");
        return invoiceItemRepository.findAllWithEagerRelationships().stream()
            .map(invoiceItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the InvoiceItem with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<InvoiceItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceItemRepository.findAllWithEagerRelationships(pageable).map(invoiceItemMapper::toDto);
    }
    

    /**
     * Get one invoiceItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceItemDTO> findOne(Long id) {
        log.debug("Request to get InvoiceItem : {}", id);
        return invoiceItemRepository.findOneWithEagerRelationships(id)
            .map(invoiceItemMapper::toDto);
    }

    /**
     * Delete the invoiceItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceItem : {}", id);
        invoiceItemRepository.deleteById(id);
        invoiceItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the invoiceItem corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvoiceItemDTO> search(String query) {
        log.debug("Request to search InvoiceItems for query {}", query);
        return StreamSupport
            .stream(invoiceItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(invoiceItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
