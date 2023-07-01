package dnd.microservices.spellsservice.persistance;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CharacterSpellRepository extends ReactiveCrudRepository<CharacterSpellEntity, CharacterSpellKey> {
    Flux<CharacterSpellEntity> findById_CharacterId(String characterId);
    Flux<CharacterSpellEntity> findById_SpellName(String spellName);
    Mono<CharacterSpellEntity> findById(CharacterSpellKey id);
}