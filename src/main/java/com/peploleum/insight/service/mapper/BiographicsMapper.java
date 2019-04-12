package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.service.dto.BiographicsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Biographics and its DTO BiographicsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BiographicsMapper extends EntityMapper<BiographicsDTO, Biographics> {


    default Biographics fromId(String id) {
        if (id == null) {
            return null;
        }
        Biographics biographics = new Biographics();
        biographics.setId(id);
        return biographics;
    }

}
