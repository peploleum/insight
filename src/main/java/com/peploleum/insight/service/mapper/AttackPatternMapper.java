package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.service.dto.AttackPatternDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AttackPattern and its DTO AttackPatternDTO.
 */
@Mapper(componentModel = "spring", uses = {NetLinkMapper.class})
public interface AttackPatternMapper extends EntityMapper<AttackPatternDTO, AttackPattern> {

    @Mapping(source = "linkOf.id", target = "linkOfId")
    @Mapping(source = "linkOf.nom", target = "linkOfNom")
    AttackPatternDTO toDto(AttackPattern attackPattern);

    @Mapping(target = "usesAttackPatternToMalwares", ignore = true)
    @Mapping(source = "linkOfId", target = "linkOf")
    AttackPattern toEntity(AttackPatternDTO attackPatternDTO);

    default AttackPattern fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttackPattern attackPattern = new AttackPattern();
        attackPattern.setId(id);
        return attackPattern;
    }
}
