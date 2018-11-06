package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.ToolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tool and its DTO ToolDTO.
 */
@Mapper(componentModel = "spring", uses = {IntrusionSetMapper.class, MalwareMapper.class, NetLinkMapper.class, ThreatActorMapper.class})
public interface ToolMapper extends EntityMapper<ToolDTO, Tool> {

    @Mapping(source = "usesToolToIntrusionSet.id", target = "usesToolToIntrusionSetId")
    @Mapping(source = "usesToolToIntrusionSet.nom", target = "usesToolToIntrusionSetNom")
    @Mapping(source = "isUsesToolToMalware.id", target = "isUsesToolToMalwareId")
    @Mapping(source = "isUsesToolToMalware.nom", target = "isUsesToolToMalwareNom")
    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    @Mapping(source = "usesToolToThreatActor.id", target = "usesToolToThreatActorId")
    @Mapping(source = "usesToolToThreatActor.nom", target = "usesToolToThreatActorNom")
    ToolDTO toDto(Tool tool);

    @Mapping(target = "isTargetsToolToVulnerabilities", ignore = true)
    @Mapping(source = "usesToolToIntrusionSetId", target = "usesToolToIntrusionSet")
    @Mapping(source = "isUsesToolToMalwareId", target = "isUsesToolToMalware")
    @Mapping(source = "linkOfId", target = "linkOf")
    @Mapping(source = "usesToolToThreatActorId", target = "usesToolToThreatActor")
    Tool toEntity(ToolDTO toolDTO);

    default Tool fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tool tool = new Tool();
        tool.setId(id);
        return tool;
    }
}
