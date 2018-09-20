package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.EquipmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Equipment and its DTO EquipmentDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, OrganisationMapper.class})
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {


    @Mapping(target = "biographics", ignore = true)
    @Mapping(target = "events", ignore = true)
    Equipment toEntity(EquipmentDTO equipmentDTO);

    default Equipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }
}
