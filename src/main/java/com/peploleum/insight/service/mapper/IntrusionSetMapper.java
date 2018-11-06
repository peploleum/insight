package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.IntrusionSetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IntrusionSet and its DTO IntrusionSetDTO.
 */
@Mapper(componentModel = "spring", uses = {ActorMapper.class, NetLinkMapper.class})
public interface IntrusionSetMapper extends EntityMapper<IntrusionSetDTO, IntrusionSet> {

    @Mapping(source = "isTargetsIntrusionSetToActor.id", target = "isTargetsIntrusionSetToActorId")
    @Mapping(source = "isTargetsIntrusionSetToActor.nom", target = "isTargetsIntrusionSetToActorNom")
    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    IntrusionSetDTO toDto(IntrusionSet intrusionSet);

    @Mapping(target = "isUsesIntrusionSetToTools", ignore = true)
    @Mapping(source = "isTargetsIntrusionSetToActorId", target = "isTargetsIntrusionSetToActor")
    @Mapping(source = "linkOfId", target = "linkOf")
    IntrusionSet toEntity(IntrusionSetDTO intrusionSetDTO);

    default IntrusionSet fromId(Long id) {
        if (id == null) {
            return null;
        }
        IntrusionSet intrusionSet = new IntrusionSet();
        intrusionSet.setId(id);
        return intrusionSet;
    }
}
