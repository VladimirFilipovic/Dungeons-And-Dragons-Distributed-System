package dnd.microservices.core.characterservice.services;

import org.mapstruct.Mapper;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.characterservice.persistance.CharacterEntity;

@Mapper(componentModel = "spring")
public class CharacterMapper {

    public CharacterEntity apiToEntity(Character body) {
        return null;
    }

    public Character entityToApi(CharacterEntity newCharacterEntity) {
        return null;
    }

}
