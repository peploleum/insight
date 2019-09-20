package com.peploleum.insight.service.mapper;

import com.peploleum.insight.domain.dictionary.Dictionary;
import com.peploleum.insight.service.dto.DictionaryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Dictionary and its DTO DictionaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DictionaryMapper extends EntityMapper<DictionaryDTO, Dictionary> {


    default Dictionary fromId(String id) {
        if (id == null) {
            return null;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        return dictionary;
    }
}
