package dnd.microservices.spellsservice.persistance;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SpellRepository extends CrudRepository<SpellEntity, Integer> {
    @Transactional(readOnly = true) Optional<SpellEntity> findByName(String name);
    @Transactional(readOnly = true) Optional<SpellEntity> findById(String id);
}
