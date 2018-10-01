package com.invoister.repository;

import com.invoister.domain.InvoiceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the InvoiceItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query(value = "select distinct invoice_item from InvoiceItem invoice_item left join fetch invoice_item.invoices",
        countQuery = "select count(distinct invoice_item) from InvoiceItem invoice_item")
    Page<InvoiceItem> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct invoice_item from InvoiceItem invoice_item left join fetch invoice_item.invoices")
    List<InvoiceItem> findAllWithEagerRelationships();

    @Query("select invoice_item from InvoiceItem invoice_item left join fetch invoice_item.invoices where invoice_item.id =:id")
    Optional<InvoiceItem> findOneWithEagerRelationships(@Param("id") Long id);

}
