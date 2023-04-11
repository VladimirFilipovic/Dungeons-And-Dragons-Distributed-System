package dnd.microservices.inventoryservice.persistance.characterInventory;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


public interface CharacterInventoryItemEntityRepository extends CrudRepository<CharacterInventoryItemEntity, CharacterInventoryItemKey>{
    @Transactional(readOnly = true) Optional<HashSet<CharacterInventoryItemEntity>> findById_CharacterInventoryId(String characterInventoryId);
    @Transactional(readOnly = true) Optional<HashSet<CharacterInventoryItemEntity>> findById_ItemId(int itemId);
    @Transactional(readOnly = true) Optional<CharacterInventoryItemEntity> findById(CharacterInventoryItemKey id);
}
