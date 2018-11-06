package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.ActivityPatternDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityPattern and its DTO ActivityPatternDTO.
 */
@Mapper(componentModel = "spring", uses = {NetLinkMapper.class})
public interface ActivityPatternMapper extends EntityMapper<ActivityPatternDTO, ActivityPattern> {

    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    ActivityPatternDTO toDto(ActivityPattern activityPattern);

    @Mapping(source = "linkOfId", target = "linkOf")
    ActivityPattern toEntity(ActivityPatternDTO activityPatternDTO);

    default ActivityPattern fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityPattern activityPattern = new ActivityPattern();
        activityPattern.setId(id);
        return activityPattern;
    }
}
