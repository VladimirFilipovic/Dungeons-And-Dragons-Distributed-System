package dnd.microservices.spellsservice.persistance;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SpellRepository extends CrudRepository<SpellEntity, Integer> {
    @Transactional(readOnly = true) Optional<SpellEntity> findById(Integer id);
    @Transactional(readOnly = true) Optional<SpellEntity> findByName(String name);
}
