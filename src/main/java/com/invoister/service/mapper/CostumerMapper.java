package com.invoister.service.mapper;

import com.invoister.domain.*;
import com.invoister.service.dto.CostumerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Costumer and its DTO CostumerDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface CostumerMapper extends EntityMapper<CostumerDTO, Costumer> {

    @Mapping(source = "company.id", target = "companyId")
    CostumerDTO toDto(Costumer costumer);

    @Mapping(source = "companyId", target = "company")
    Costumer toEntity(CostumerDTO costumerDTO);

    default Costumer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Costumer costumer = new Costumer();
        costumer.setId(id);
        return costumer;
    }
}
