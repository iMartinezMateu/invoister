package com.invoister.service.mapper;

import com.invoister.domain.*;
import com.invoister.service.dto.NotificatorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Notificator and its DTO NotificatorDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface NotificatorMapper extends EntityMapper<NotificatorDTO, Notificator> {

    @Mapping(source = "company.id", target = "companyId")
    NotificatorDTO toDto(Notificator notificator);

    @Mapping(source = "companyId", target = "company")
    Notificator toEntity(NotificatorDTO notificatorDTO);

    default Notificator fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notificator notificator = new Notificator();
        notificator.setId(id);
        return notificator;
    }
}
