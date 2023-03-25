package dnd.microservices.spellsservice;

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

import dnd.microservices.spellsservice.persistance.SpellEntity;
import dnd.microservices.spellsservice.persistance.SpellRepository;

import java.io.Console;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistenceTests {

    @Autowired
    private SpellRepository repository;

    private SpellEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();
        SpellEntity entity = new SpellEntity(
            "Test", 
            "Test desc", 
            0, 
            "Duration", 
            "Casting time", 
            0, 
            new HashMap<String, Integer>(), 
            "adress"
            );
        savedEntity = repository.save(entity);
    }

    @Test
    public void create() {
        SpellEntity newEntity =  new SpellEntity(
            "Test 2", 
            "Test desc 2", 
            0, 
            "Duration 2", 
            "Casting time 2 ", 
            0, 
            new HashMap<String, Integer>(), 
            "address"
            );

        SpellEntity newSavedEntity = repository.save(newEntity);

        Optional<SpellEntity> foundEntity = repository.findById(newSavedEntity.getId());

        assertEqualsSpell(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }

    @Test
    public void update() {
        savedEntity.setDescription("Random description");
        repository.save(savedEntity);

        SpellEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, foundEntity.getVersion());
        assertEquals(savedEntity.getDescription(), foundEntity.getDescription());
    }

    @Test
    public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    public void getById() {
        Optional<SpellEntity> entity = repository.findById(savedEntity.getId());
        assertTrue(entity.isPresent());
        assertEqualsSpell(savedEntity, entity.get());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateError() {
        SpellEntity entity = new SpellEntity(
            "Test", 
            "Test desc", 
            0, 
            "Duration", 
            "Casting time", 
            0, 
            null, 
            "address"
            );
        repository.save(entity);
    }

    @Test
    public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        SpellEntity entity1 = repository.findById(savedEntity.getId()).get();
        SpellEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update the entity using the first entity object
        entity1.setLevel(1000);
        repository.save(entity1);

        // Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e.
        // a Optimistic Lock Error
        try {
            entity2.setLevel(1000);
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {
        }

        // Get the updated entity from the database and verify its new sate
        SpellEntity updatedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (int) updatedEntity.getVersion());
        assertEquals(1000, updatedEntity.getLevel());
    }

    private void assertEqualsSpell(SpellEntity expectedEntity, SpellEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getCastingTime(), actualEntity.getCastingTime());
        assertEquals(expectedEntity.getDamageAtLevel(), actualEntity.getDamageAtLevel());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getDuration(), actualEntity.getDuration());
        assertEquals(expectedEntity.getRange(), actualEntity.getRange());
        assertEquals(expectedEntity.getServiceAdress(), actualEntity.getServiceAdress());
        assertEquals(expectedEntity.getLevel(), actualEntity.getLevel());
    }
}