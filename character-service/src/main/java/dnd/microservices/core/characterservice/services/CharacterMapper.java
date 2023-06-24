package dnd.microservices.core.characterservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.characterservice.persistance.CharacterEntity;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

     @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)
    })   
    public CharacterEntity apiToEntity(Character body);

    public Character entityToApi(CharacterEntity newCharacterEntity);

}
