package dnd.microservices.spellsservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import dnd.microservices.spellsservice.persistance.CharacterSpellEntity;
import dnd.microservices.spellsservice.persistance.CharacterSpellKey;
import dnd.microservices.spellsservice.persistance.CharacterSpellRepository;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private CharacterSpellRepository repository;

    private CharacterSpellEntity savedEntity;

    @Before
    public void setupDb() {
        repository.deleteAll().block();

        CharacterSpellEntity entity = new CharacterSpellEntity(new CharacterSpellKey("1", "heal"), 1);

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(createdEntity -> {
                    savedEntity = createdEntity;
                    return areCharacterSpellEntitiesEqual(entity, savedEntity);
                })
                .verifyComplete();
    }

    @Test
    public void create() {
        CharacterSpellEntity newEntity = new CharacterSpellEntity(new CharacterSpellKey("2", "heal"), 1);

        CharacterSpellEntity newSavedEntity = repository.save(newEntity).block();

        StepVerifier.create(repository.findById(newSavedEntity.id))
                .expectNextMatches(foundEntity -> areCharacterSpellEntitiesEqual(newEntity, foundEntity))
                .verifyComplete();
    }

    @Test
    public void update() {
        savedEntity.spellLevel = 3;

        StepVerifier.create(repository.save(savedEntity))
                .expectNextCount(1)
                .verifyComplete(); 

        StepVerifier.create(repository.findById(savedEntity.id))
                .expectNextMatches(foundEntity -> areCharacterSpellEntitiesEqual(foundEntity, savedEntity))
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
                .expectNextMatches(entity -> areCharacterSpellEntitiesEqual(savedEntity, entity))
                .verifyComplete();
    }

    @Test
    public void duplicateError() {
        CharacterSpellEntity entity = new CharacterSpellEntity(
                new CharacterSpellKey("1", "heal"), 1);

        StepVerifier.create(repository.save(entity))
                .verifyError(DataIntegrityViolationException.class);
    }

    private boolean areCharacterSpellEntitiesEqual(CharacterSpellEntity expectedEntity, CharacterSpellEntity actualEntity) {
        return expectedEntity.id.equals(actualEntity.id)
               && expectedEntity.spellLevel == actualEntity.spellLevel
               && expectedEntity.version == actualEntity.version;
    }
}
