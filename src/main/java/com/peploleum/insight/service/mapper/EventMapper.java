package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventMapper extends EntityMapper<EventDTO, Event> {



    default Event fromId(String id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
