package dnd.microservices.statsservice.persistance;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface StatsRepository extends CrudRepository<StatsEntity, Integer> {
    @Transactional(readOnly = true) Optional<StatsEntity> findByName(String name);
    @Transactional(readOnly = true) Optional<StatsEntity> findById(String id);
}
