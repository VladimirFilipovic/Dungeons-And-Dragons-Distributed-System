package dnd.microservices.core.characterservice.persistance;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface CharacterRepository extends ReactiveCrudRepository<CharacterEntity, String> {
    Mono<CharacterEntity> findByName(String name);
}