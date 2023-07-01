package dnd.microservices.core.characterservice;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import dnd.microservices.core.characterservice.persistance.CharacterEntity;
import dnd.microservices.core.characterservice.persistance.CharacterRepository;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private CharacterRepository repository;

    private CharacterEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll().block();
        CharacterEntity entity = new CharacterEntity("Miroljub", "Aaraorca", "None", "Unknown");
          StepVerifier.create(repository.save(entity))
            .expectNextMatches(createdEntity -> {
                savedEntity = createdEntity;
                return areCharactersEqual(entity, savedEntity);
            })
            .verifyComplete();
    }

    @Test
   	public void create() {
        CharacterEntity newEntity = new CharacterEntity("Miroljub 2", "Aaraorca", "None", "Unknown");
        
        CharacterEntity newSavedEntity = repository.save(newEntity).block();
        
        StepVerifier.create(repository.findById(newSavedEntity.id)).expectNextMatches(foundEntity -> areCharactersEqual(newEntity, foundEntity)).verifyComplete();
    }

     @Test
    public void update() {
        savedEntity.setReligion("Random description");

        StepVerifier.create(repository.save(savedEntity))
            .expectNextCount(1)
            .verifyComplete();

        StepVerifier.create(repository.findById(savedEntity.id))
            .expectNextMatches(foundEntity -> areCharactersEqual(foundEntity, savedEntity))
            .verifyComplete();
    }

     @Test
   	 public void delete() {
        StepVerifier.create(repository.delete(savedEntity)).verifyComplete();

        StepVerifier.create(repository.existsById(savedEntity.id))
            .expectNext(false)
            .verifyComplete();
     }

     @Test
   	 public void getById() {
         StepVerifier.create(repository.findById(savedEntity.id))
            .expectNextMatches(entity -> areCharactersEqual(entity, savedEntity))
            .verifyComplete();
     }

     @Test
     public void duplicateError() {
         CharacterEntity entity = new CharacterEntity("Miroljub", "Aaraorca", "None", "Unknown");
         entity.setId(savedEntity.id);
        StepVerifier.create(repository.save(entity)).verifyError();
    }

    @Test
   	public void optimisticLockError() {
        CharacterEntity entity1 = repository.findById(savedEntity.id).block();
        CharacterEntity entity2 = repository.findById(savedEntity.id).block();

        entity1.setRace("Random race");

        StepVerifier.create(repository.save(entity1))
            .expectNextCount(1)
            .verifyComplete();

        StepVerifier.create(repository.save(entity2))
            .expectError(OptimisticLockingFailureException.class)
            .verify();

        StepVerifier.create(repository.findById(savedEntity.id))
            .expectNextMatches(updatedEntity -> updatedEntity.version == 2 && updatedEntity.race.equals("Random race"))
            .verifyComplete();
    }

    private boolean areCharactersEqual(CharacterEntity expectedEntity, CharacterEntity actualEntity) {
       return  expectedEntity.id.equals(actualEntity.id) &&
               expectedEntity.getName().equals(actualEntity.getName()) &&
               expectedEntity.getRace().equals(actualEntity.getRace()) &&
               expectedEntity.getReligion().equals(actualEntity.getReligion());
    }

}