package dnd.microservices.statsservice.persistance.Stats;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface StatsRepository extends ReactiveCrudRepository<StatsEntity, StatsKey> {
     Flux<StatsEntity> findById_CharacterId(String characterId);
     Mono<StatsEntity> findById(StatsKey id);
}
