package com.invoister.service.mapper;

import com.invoister.domain.*;
import com.invoister.service.dto.BudgetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Budget and its DTO BudgetDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {

    @Mapping(source = "company.id", target = "companyId")
    BudgetDTO toDto(Budget budget);

    @Mapping(source = "companyId", target = "company")
    Budget toEntity(BudgetDTO budgetDTO);

    default Budget fromId(Long id) {
        if (id == null) {
            return null;
        }
        Budget budget = new Budget();
        budget.setId(id);
        return budget;
    }
}
