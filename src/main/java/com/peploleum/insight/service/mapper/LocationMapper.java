package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.service.dto.LocationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {


    default Location fromId(String id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
