package dnd.microservices.core.characterservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import dnd.microservices.core.characterservice.persistance.CharacterEntity;
import dnd.microservices.core.characterservice.persistance.CharacterRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistenceTests {

    @Autowired
    private CharacterRepository repository;

    private CharacterEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();

        CharacterEntity entity = new CharacterEntity("1", 1, "a", "s", "c", "d");
        savedEntity = repository.save(entity);

        assertEqualsCharacter(entity, savedEntity);
    }


    @Test
   	public void create() {

        CharacterEntity newEntity = new CharacterEntity("01", 1, "Vlad", "s", "c", "d");
        repository.save(newEntity);

        CharacterEntity foundEntity = repository.findByCharacterId(newEntity.getId()).get();
        assertEqualsCharacter(newEntity, foundEntity);

        assertEquals(2, repository.count());
    }

    @Test
   	public void update() {
        savedEntity.setRace("Random");
        repository.save(savedEntity);

        CharacterEntity foundEntity = repository.findByCharacterId(savedEntity.getId()).get();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("Random", foundEntity.getRace());
    }

    // @Test
   	// public void delete() {
    //     repository.delete(savedEntity);
    //     assertFalse(repository.existsById(savedEntity.getId()));
    // }

    // @Test
   	// public void getByProductId() {
    //     List<CharacterEntity> entityList = repository.findByProductId(savedEntity.getProductId());

    //     assertThat(entityList, hasSize(1));
    //     assertEqualsCharacter(savedEntity, entityList.get(0));
    // }

    // @Test(expected = DataIntegrityViolationException.class)
   	// public void duplicateError() {
    //     CharacterEntity entity = new CharacterEntity(1, 2, "a", "s", "c");
    //     repository.save(entity);
    // }

    // @Test
   	// public void optimisticLockError() {

    //     // Store the saved entity in two separate entity objects
    //     CharacterEntity entity1 = repository.findById(savedEntity.getId()).get();
    //     CharacterEntity entity2 = repository.findById(savedEntity.getId()).get();

    //     // Update the entity using the first entity object
    //     entity1.setAuthor("a1");
    //     repository.save(entity1);

    //     //  Update the entity using the second entity object.
    //     // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
    //     try {
    //         entity2.setAuthor("a2");
    //         repository.save(entity2);

    //         fail("Expected an OptimisticLockingFailureException");
    //     } catch (OptimisticLockingFailureException e) {}

    //     // Get the updated entity from the database and verify its new sate
    //     CharacterEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    //     assertEquals(1, (int)updatedEntity.getVersion());
    //     assertEquals("a1", updatedEntity.getAuthor());
    // }

    // Determines if two CharacterEntity objects are equal
    private void assertEqualsCharacter(CharacterEntity expectedEntity, CharacterEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getRace(), actualEntity.getRace());
        assertEquals(expectedEntity.getClass(), actualEntity.getClass());
    }
}