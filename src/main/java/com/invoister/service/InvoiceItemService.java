package com.invoister.service;

import com.invoister.service.dto.InvoiceItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing InvoiceItem.
 */
public interface InvoiceItemService {

    /**
     * Save a invoiceItem.
     *
     * @param invoiceItemDTO the entity to save
     * @return the persisted entity
     */
    InvoiceItemDTO save(InvoiceItemDTO invoiceItemDTO);

    /**
     * Get all the invoiceItems.
     *
     * @return the list of entities
     */
    List<InvoiceItemDTO> findAll();

    /**
     * Get all the InvoiceItem with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<InvoiceItemDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" invoiceItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InvoiceItemDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the invoiceItem corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<InvoiceItemDTO> search(String query);
}
