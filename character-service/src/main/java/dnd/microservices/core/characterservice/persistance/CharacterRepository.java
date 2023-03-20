package dnd.microservices.core.characterservice.persistance;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CharacterRepository extends PagingAndSortingRepository<CharacterEntity, String> {
    Optional<CharacterEntity> findByCharacterId(String characterId);
    Optional<CharacterEntity> findByCharacterName(String characterName);
}