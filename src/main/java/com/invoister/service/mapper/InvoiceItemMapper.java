package com.invoister.service.mapper;

import com.invoister.domain.*;
import com.invoister.service.dto.InvoiceItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceItem and its DTO InvoiceItemDTO.
 */
@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface InvoiceItemMapper extends EntityMapper<InvoiceItemDTO, InvoiceItem> {



    default InvoiceItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setId(id);
        return invoiceItem;
    }
}
