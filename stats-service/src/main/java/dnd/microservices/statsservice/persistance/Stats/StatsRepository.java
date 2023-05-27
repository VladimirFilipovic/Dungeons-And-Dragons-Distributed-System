package dnd.microservices.statsservice.persistance.Stats;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface StatsRepository extends CrudRepository<StatsEntity, StatsKey> {
     Optional<HashSet<StatsEntity>> findById_CharacterId(String characterId);
     Optional<StatsEntity> findById(StatsKey id);
}
