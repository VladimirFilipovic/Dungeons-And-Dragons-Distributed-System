package dnd.microservices.core.characterservice;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import dnd.microservices.core.characterservice.persistance.CharacterEntity;
import dnd.microservices.core.characterservice.persistance.CharacterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.rangeClosed;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private CharacterRepository repository;

    private CharacterEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();

        CharacterEntity entity = new CharacterEntity("Miroljub", "Aaraorca", "None", "Unknown");
        savedEntity = repository.save(entity);
    }

    @Test
   	public void create() {
        CharacterEntity newEntity = new CharacterEntity("Miroljub 2", "Aaraorca", "None", "Unknown");
        
        CharacterEntity newSavedEntity = repository.save(newEntity);

        Optional<CharacterEntity> foundEntity = repository.findById(newSavedEntity.getId());

        assertEqualsCharacter(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }


    @Test
   	public void update() {
        savedEntity.setReligion("Random description");
        repository.save(savedEntity);

        CharacterEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(2, foundEntity.getVersion());
        assertEquals(savedEntity.getReligion(), foundEntity.getReligion());
    }

     @Test
   	 public void delete() {
         repository.delete(savedEntity);
         assertFalse(repository.existsById(savedEntity.getId()));
     }

     @Test
   	 public void getById() {
         Optional<CharacterEntity> entity = repository.findById(savedEntity.getId());
         assertTrue(entity.isPresent());
         assertEqualsCharacter(savedEntity, entity.get());
     }

     @Test(expected = Exception.class)
     public void duplicateError() {
        CharacterEntity entity = new CharacterEntity("Miroljub", "Aaraorca", "None", "Unknown");
        entity.setId(savedEntity.getId());
        repository.save(entity);
    }

    @Test
   	public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        CharacterEntity entity1 = repository.findById(savedEntity.getId()).get();
        CharacterEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update the entity using the first entity object
        entity1.setRace("Random race");
        repository.save(entity1);

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        try {
            entity2.setRace("Random race");
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {}

        // Get the updated entity from the database and verify its new sate
        CharacterEntity updatedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(2, (int)updatedEntity.getVersion());
        assertEquals("Random race", updatedEntity.getRace());
    }

    private void assertEqualsCharacter(CharacterEntity expectedEntity, CharacterEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getRace(), actualEntity.getRace());
        assertEquals(expectedEntity.getReligion(), actualEntity.getReligion());
    }

}