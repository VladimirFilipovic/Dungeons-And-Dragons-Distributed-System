package dnd.microservices.inventoryservice.persistance.item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<ItemEntity, Integer> {
    @Transactional(readOnly = true) Optional<ItemEntity> findByName(String name);
    @Transactional(readOnly = true) Optional<ItemEntity> findById(int id);
    @Transactional(readOnly = true) void deleteByName(String name);
}