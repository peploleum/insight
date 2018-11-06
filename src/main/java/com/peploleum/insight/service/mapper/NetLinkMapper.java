package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.NetLinkDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NetLink and its DTO NetLinkDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NetLinkMapper extends EntityMapper<NetLinkDTO, NetLink> {


    @Mapping(target = "isLinkOfAttackPatterns", ignore = true)
    @Mapping(target = "isLinkOfCampaigns", ignore = true)
    @Mapping(target = "isLinkOfCourseOfActions", ignore = true)
    @Mapping(target = "isLinkOfActors", ignore = true)
    @Mapping(target = "isLinkOfActivityPatterns", ignore = true)
    @Mapping(target = "isLinkOfIntrusionSets", ignore = true)
    @Mapping(target = "isLinkOfMalwares", ignore = true)
    @Mapping(target = "isLinkOfObservedData", ignore = true)
    @Mapping(target = "isLinkOfReports", ignore = true)
    @Mapping(target = "isLinkOfThreatActors", ignore = true)
    @Mapping(target = "isLinkOfTools", ignore = true)
    @Mapping(target = "isLinkOfVulnerabilities", ignore = true)
    NetLink toEntity(NetLinkDTO netLinkDTO);

    default NetLink fromId(Long id) {
        if (id == null) {
            return null;
        }
        NetLink netLink = new NetLink();
        netLink.setId(id);
        return netLink;
    }
}
