package dnd.microservices.inventoryservice;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import dnd.microservices.inventoryservice.persistance.ItemEntity;
import dnd.microservices.inventoryservice.persistance.ItemRepository;

import java.io.Console;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistenceTests {

    @Autowired
    private ItemRepository repository;
    private ItemEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();

        ItemEntity entity = new ItemEntity( 1, "item 1", 3, "random", "?");
        savedEntity = repository.save(entity);
    }


    @Test
   	public void create() {
        int newEntityId = 1;
        ItemEntity newEntity = new ItemEntity(newEntityId, "item 2", 3, "random 0", "?");
        
        ItemEntity newSavedEntity = repository.save(newEntity);

        Optional<ItemEntity> foundEntity = repository.findById(newSavedEntity.getId());

        assertEqualsItem(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }

    @Test
   	public void update() {
        savedEntity.setDescription("Random description");
        repository.save(savedEntity);

        ItemEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, foundEntity.getVersion());
        assertEquals(savedEntity.getDescription(), foundEntity.getDescription());
    }

     @Test
   	 public void delete() {
         repository.delete(savedEntity);
         assertFalse(repository.existsById(savedEntity.getId()));
     }

     @Test
   	 public void getByProductId() {
         Optional<ItemEntity> entity = repository.findById(savedEntity.getId());
         assertTrue(entity.isPresent());
         assertEqualsItem(savedEntity, entity.get());
     }

     @Test(expected = DataIntegrityViolationException.class)
     public void duplicateError() {
        ItemEntity entity = new ItemEntity(1, "item 1", 3, "random", "?");
        repository.save(entity);
    }

    @Test
   	public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        ItemEntity entity1 = repository.findById(savedEntity.getId()).get();
        ItemEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update the entity using the first entity object
        entity1.setAmount(1000);
        repository.save(entity1);

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        try {
            entity2.setAmount(1000);
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {}

        // Get the updated entity from the database and verify its new sate
        ItemEntity updatedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (int)updatedEntity.getVersion());
        assertEquals(1000, updatedEntity.getAmount());
    }

    private void assertEqualsItem(ItemEntity expectedEntity, ItemEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getAmount(), actualEntity.getAmount());
        assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    }
}