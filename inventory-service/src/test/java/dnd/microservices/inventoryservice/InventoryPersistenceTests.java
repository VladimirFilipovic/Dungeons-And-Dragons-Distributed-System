package dnd.microservices.inventoryservice;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;
import dnd.microservices.inventoryservice.persistance.characterInventory.CharacterInventoryItemEntity;
import dnd.microservices.inventoryservice.persistance.characterInventory.CharacterInventoryItemEntityRepository;
import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryEntity;
import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryRepository;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemRepository;

import java.io.Console;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class InventoryPersistenceTests {

    @Autowired
    private CharacterInventoryItemEntityRepository characterInventoryItemRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired 
    private CharacterInventoryRepository characterInventoryRepository;
    
    private CharacterInventoryItemEntity savedEntity;
    private ItemEntity itemEntity;
    private CharacterInventoryEntity characterInventoryEntity;

    @Before
   	public void setupDb() {
   		characterInventoryItemRepository.deleteAll();
        itemRepository.deleteAll();
        characterInventoryRepository.deleteAll();

        itemEntity = itemRepository.save(new ItemEntity( "item 1", "random", "?"));
        characterInventoryEntity = characterInventoryRepository.save(new CharacterInventoryEntity("character1"));
    }


    @Test
   	public void create() {
        CharacterInventoryItemEntity entity = new CharacterInventoryItemEntity(characterInventoryEntity, itemEntity, 1);
        savedEntity = characterInventoryItemRepository.save(entity);

        Optional<CharacterInventoryItemEntity> foundEntity = characterInventoryItemRepository.findById(savedEntity.id);

        assertEqualsInventoryItem(savedEntity, foundEntity.get());
        assertEquals(1, characterInventoryItemRepository.count());
    }

    @Test
   	public void update() {
        CharacterInventoryItemEntity entity = new CharacterInventoryItemEntity(characterInventoryEntity, itemEntity, 1);
        savedEntity = characterInventoryItemRepository.save(entity);


        savedEntity.quantity = 2;
        characterInventoryItemRepository.save(savedEntity);

        CharacterInventoryItemEntity foundEntity = characterInventoryItemRepository.findById(savedEntity.id).get();
        assertEquals(2, foundEntity.quantity);
    }

     @Test
   	 public void delete() {
        CharacterInventoryItemEntity entity = new CharacterInventoryItemEntity(characterInventoryEntity, itemEntity, 1);
        savedEntity = characterInventoryItemRepository.save(entity);

         characterInventoryItemRepository.delete(savedEntity);
         assertFalse(characterInventoryItemRepository.existsById(savedEntity.id));
     }

     @Test
   	 public void getByCompositeKey() {
        CharacterInventoryItemEntity entity = new CharacterInventoryItemEntity(characterInventoryEntity, itemEntity, 1);
        savedEntity = characterInventoryItemRepository.save(entity);

        Optional<CharacterInventoryItemEntity> foundEntity = characterInventoryItemRepository.findById(savedEntity.id);
        assertTrue(foundEntity.isPresent());
        assertEqualsInventoryItem(savedEntity, foundEntity.get());
     }
     @Test
   	 public void getByItemId() {
        CharacterInventoryItemEntity entity = new CharacterInventoryItemEntity(characterInventoryEntity, itemEntity, 1);
        savedEntity = characterInventoryItemRepository.save(entity);

        Optional<HashSet<CharacterInventoryItemEntity>> foundEntities = characterInventoryItemRepository.findById_ItemId(savedEntity.item.id);
        assertTrue(foundEntities.isPresent());
     }
     @Test
   	 public void getByCharacterId() {
        CharacterInventoryItemEntity entity = new CharacterInventoryItemEntity(characterInventoryEntity, itemEntity, 1);
        savedEntity = characterInventoryItemRepository.save(entity);

        Optional<HashSet<CharacterInventoryItemEntity>> foundEntity = characterInventoryItemRepository.findById_CharacterInventoryId(savedEntity.characterInventory.characterId);
        assertTrue(foundEntity.isPresent());
     }


    private void assertEqualsInventoryItem(CharacterInventoryItemEntity expectedEntity, CharacterInventoryItemEntity actualEntity) {
        assertEquals(expectedEntity.id, actualEntity.id);
        assertEquals(expectedEntity.item.id, actualEntity.item.id);
        assertEquals(expectedEntity.characterInventory.characterId, actualEntity.characterInventory.characterId);
        assertEquals(expectedEntity.quantity, actualEntity.quantity);
        assertEquals(expectedEntity.version, actualEntity.version);
    }
}