package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.CourseOfActionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CourseOfAction and its DTO CourseOfActionDTO.
 */
@Mapper(componentModel = "spring", uses = {NetLinkMapper.class})
public interface CourseOfActionMapper extends EntityMapper<CourseOfActionDTO, CourseOfAction> {

    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    CourseOfActionDTO toDto(CourseOfAction courseOfAction);

    @Mapping(source = "linkOfId", target = "linkOf")
    CourseOfAction toEntity(CourseOfActionDTO courseOfActionDTO);

    default CourseOfAction fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseOfAction courseOfAction = new CourseOfAction();
        courseOfAction.setId(id);
        return courseOfAction;
    }
}
