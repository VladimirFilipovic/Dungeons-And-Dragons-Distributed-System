package dnd.microservices.inventoryservice.persistance.inventory;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.repository.CrudRepository;


public interface CharacterInventoryRepository extends CrudRepository<CharacterInventoryEntity, String> {
   @Transactional(readOnly = true) Optional<CharacterInventoryEntity> findByCharacterId(String characterId);
}
