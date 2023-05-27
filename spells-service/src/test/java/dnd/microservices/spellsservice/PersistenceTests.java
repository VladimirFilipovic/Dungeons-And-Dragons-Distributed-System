package dnd.microservices.spellsservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import dnd.microservices.spellsservice.persistance.CharacterSpellEntity;
import dnd.microservices.spellsservice.persistance.CharacterSpellKey;
import dnd.microservices.spellsservice.persistance.CharacterSpellRepository;

import java.io.Console;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private CharacterSpellRepository repository;
    private CharacterSpellEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();
        CharacterSpellEntity entity  = new CharacterSpellEntity(
            new CharacterSpellKey("1", "heal"), 1);
   
        savedEntity = repository.save(entity);
    }

    @Test
    public void create() {
        CharacterSpellEntity newEntity  = new CharacterSpellEntity(
            new CharacterSpellKey("2", "heal"), 1);

        CharacterSpellEntity newSavedEntity = repository.save(newEntity);

        Optional<CharacterSpellEntity> foundEntity = repository.findById(newSavedEntity.id);

        assertEqualsSpell(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }

    @Test
    public void update() {
        savedEntity.spellLevel = 3;
        repository.save(savedEntity);

        CharacterSpellEntity foundEntity = repository.findById(savedEntity.id).get();
        assertEquals(2, foundEntity.version);
        assertEquals(savedEntity.spellLevel, foundEntity.spellLevel);
    }

    @Test
    public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.id));
    }

    @Test
    public void getById() {
        Optional<CharacterSpellEntity> entity = repository.findById(savedEntity.id);
        assertTrue(entity.isPresent());
        assertEqualsSpell(savedEntity, entity.get());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateError() {
        CharacterSpellEntity entity  = new CharacterSpellEntity(
            new CharacterSpellKey("1", "heal"), 1);
        repository.save(entity);
    }

    private void assertEqualsSpell(CharacterSpellEntity expectedEntity, CharacterSpellEntity actualEntity) {
        assertEquals(expectedEntity.id, actualEntity.id);
        assertEquals(expectedEntity.spellLevel, actualEntity.spellLevel);
        assertEquals(expectedEntity.version, actualEntity.version);
    }
}