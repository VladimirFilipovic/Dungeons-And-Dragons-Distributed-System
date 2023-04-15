package dnd.microservices.spellsservice.persistance;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CharacterSpellRepository extends CrudRepository<CharacterSpellEntity, CharacterSpellKey> {
    Optional<HashSet<CharacterSpellEntity>> findById_CharacterId(String characterId);
    Optional<HashSet<CharacterSpellEntity>> findById_SpellName(String spellName);
}