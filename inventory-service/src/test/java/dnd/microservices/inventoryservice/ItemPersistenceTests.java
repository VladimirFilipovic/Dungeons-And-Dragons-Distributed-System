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

import dnd.microservices.inventoryservice.persistance.item.ItemEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemRepository;

import java.io.Console;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class ItemPersistenceTests {

    @Autowired
    private ItemRepository repository;

    private ItemEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();

        ItemEntity entity = new ItemEntity( "item 1", "random", "?");
        savedEntity = repository.save(entity);
    }


    @Test
   	public void create() {
        ItemEntity newEntity = new ItemEntity( "item 2", "random", "?");
        
        ItemEntity newSavedEntity = repository.save(newEntity);

        Optional<ItemEntity> foundEntity = repository.findById(newSavedEntity.id);

        assertEqualsItem(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }

    @Test
   	public void update() {
        savedEntity.description = "Random description";
        repository.save(savedEntity);

        ItemEntity foundEntity = repository.findById(savedEntity.id).get();
        assertEquals(1, foundEntity.version);
        assertEquals(savedEntity.description, foundEntity.description);
    }

     @Test
   	 public void delete() {
         repository.delete(savedEntity);
         assertFalse(repository.existsById(savedEntity.id));
     }

     @Test
   	 public void getByProductId() {
         Optional<ItemEntity> entity = repository.findById(savedEntity.id);
         assertTrue(entity.isPresent());
         assertEqualsItem(savedEntity, entity.get());
     }

     @Test(expected = DataIntegrityViolationException.class)
     public void duplicateError() {
        ItemEntity entity = new ItemEntity( "item 1", "random", "?");
        repository.save(entity);
    }

    @Test
   	public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        ItemEntity entity1 = repository.findById(savedEntity.id).get();
        ItemEntity entity2 = repository.findById(savedEntity.id).get();

        // Update the entity using the first entity object
        entity1.description = "blah blah blah";
        repository.save(entity1);

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        try {
            entity2.description = "blah blah blah";
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {}

        // Get the updated entity from the database and verify its new sate
        ItemEntity updatedEntity = repository.findById(savedEntity.id).get();
        assertEquals(1, (int)updatedEntity.version);
        assertEquals("blah blah blah", updatedEntity.description);
    }

    private void assertEqualsItem(ItemEntity expectedEntity, ItemEntity actualEntity) {
        assertEquals(expectedEntity.id, actualEntity.id);
        assertEquals(expectedEntity.name, actualEntity.name);
        assertEquals(expectedEntity.description, actualEntity.description);
        assertEquals(expectedEntity.version, actualEntity.version);
    }
}