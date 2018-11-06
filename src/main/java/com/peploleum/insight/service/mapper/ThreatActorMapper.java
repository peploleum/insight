package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.ThreatActorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ThreatActor and its DTO ThreatActorDTO.
 */
@Mapper(componentModel = "spring", uses = {MalwareMapper.class, NetLinkMapper.class})
public interface ThreatActorMapper extends EntityMapper<ThreatActorDTO, ThreatActor> {

    @Mapping(source = "isUsesThreatActorToMalware.id", target = "isUsesThreatActorToMalwareId")
    @Mapping(source = "isUsesThreatActorToMalware.nom", target = "isUsesThreatActorToMalwareNom")
    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    ThreatActorDTO toDto(ThreatActor threatActor);

    @Mapping(target = "isTargetsThreatActorToVulnerabilities", ignore = true)
    @Mapping(target = "isUsesThreatActorToTools", ignore = true)
    @Mapping(source = "isUsesThreatActorToMalwareId", target = "isUsesThreatActorToMalware")
    @Mapping(source = "linkOfId", target = "linkOf")
    ThreatActor toEntity(ThreatActorDTO threatActorDTO);

    default ThreatActor fromId(Long id) {
        if (id == null) {
            return null;
        }
        ThreatActor threatActor = new ThreatActor();
        threatActor.setId(id);
        return threatActor;
    }
}
