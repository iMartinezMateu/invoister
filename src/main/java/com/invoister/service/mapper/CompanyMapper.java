package com.invoister.service.mapper;

import com.invoister.domain.*;
import com.invoister.service.dto.CompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Company and its DTO CompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {


    @Mapping(target = "notificators", ignore = true)
    @Mapping(target = "costumers", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    @Mapping(target = "reports", ignore = true)
    Company toEntity(CompanyDTO companyDTO);

    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
