package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.ActorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Actor and its DTO ActorDTO.
 */
@Mapper(componentModel = "spring", uses = {NetLinkMapper.class})
public interface ActorMapper extends EntityMapper<ActorDTO, Actor> {

    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    ActorDTO toDto(Actor actor);

    @Mapping(target = "targetsActorToIntrusionSets", ignore = true)
    @Mapping(target = "targetsActorToMalwares", ignore = true)
    @Mapping(source = "linkOfId", target = "linkOf")
    Actor toEntity(ActorDTO actorDTO);

    default Actor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Actor actor = new Actor();
        actor.setId(id);
        return actor;
    }
}
