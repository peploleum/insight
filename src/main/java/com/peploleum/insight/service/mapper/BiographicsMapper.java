package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.BiographicsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Biographics and its DTO BiographicsDTO.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, EquipmentMapper.class, LocationMapper.class, OrganisationMapper.class})
public interface BiographicsMapper extends EntityMapper<BiographicsDTO, Biographics> {



    default Biographics fromId(Long id) {
        if (id == null) {
            return null;
        }
        Biographics biographics = new Biographics();
        biographics.setId(id);
        return biographics;
    }
}
