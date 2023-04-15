package dnd.microservices.spellsservice.services;

import org.mapstruct.Mapper;

import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.spellsservice.persistance.CharacterSpellEntity;

@Mapper(componentModel = "spring")
public interface CharacterSpellsMapper {

    CharacterSpell entityToApi(CharacterSpellEntity characterSpellEntity);

    CharacterSpellEntity apiToEntity(CharacterSpell characterSpell);
    
}
