package dnd.microservices.core.characterservice.persistance;


import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CharacterRepository extends PagingAndSortingRepository<CharacterEntity, String> {
    Optional<CharacterEntity> findByName(String name);
}