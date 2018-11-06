package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.ObservedDataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ObservedData and its DTO ObservedDataDTO.
 */
@Mapper(componentModel = "spring", uses = {NetLinkMapper.class})
public interface ObservedDataMapper extends EntityMapper<ObservedDataDTO, ObservedData> {

    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    ObservedDataDTO toDto(ObservedData observedData);

    @Mapping(source = "linkOfId", target = "linkOf")
    ObservedData toEntity(ObservedDataDTO observedDataDTO);

    default ObservedData fromId(Long id) {
        if (id == null) {
            return null;
        }
        ObservedData observedData = new ObservedData();
        observedData.setId(id);
        return observedData;
    }
}
