package com.invoister.service.mapper;

import com.invoister.domain.*;
import com.invoister.service.dto.InvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Invoice and its DTO InvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mapping(source = "company.id", target = "companyId")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "companyId", target = "company")
    @Mapping(target = "invoiceItems", ignore = true)
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(id);
        return invoice;
    }
}
