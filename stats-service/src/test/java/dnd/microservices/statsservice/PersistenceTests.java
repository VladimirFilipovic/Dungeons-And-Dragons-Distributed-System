package dnd.microservices.statsservice;


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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import dnd.microservices.statsservice.persistance.Stats.StatsEntity;
import dnd.microservices.statsservice.persistance.Stats.StatsKey;
import dnd.microservices.statsservice.persistance.Stats.StatsRepository;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private StatsRepository repository;

    private StatsEntity savedEntity;

    @Before
   	public void setupDb() {
        repository.deleteAll();
        StatsEntity entity = new StatsEntity(new StatsKey("character 1", "stat 1"), 0, "service");
        savedEntity = repository.save(entity);
    }

    @Test
    public void create() {
        StatsEntity newEntity = new StatsEntity(new StatsKey("character 2", "stat 2"), 0, "service");

        StatsEntity newSavedEntity = repository.save(newEntity);

        Optional<StatsEntity> foundEntity = repository.findById(newSavedEntity.id);

        assertEqualsStats(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }

    @Test
    public void update() {
        savedEntity.value = 52;
        repository.save(savedEntity);

        StatsEntity foundEntity = repository.findById(savedEntity.id).get();
        assertEquals(2, foundEntity.version);
        assertEquals(savedEntity.value, foundEntity.value);
    }

    @Test
    public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.id));
    }

    @Test
    public void getById() {
        Optional<StatsEntity> entity = repository.findById(savedEntity.id);
        assertTrue(entity.isPresent());
        assertEqualsStats(savedEntity, entity.get());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateError() {
        StatsEntity entity = new StatsEntity(new StatsKey("character 1", "stat 1"), 0, "service");
        repository.save(entity);
    }

    @Test
    public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        StatsEntity entity1 = repository.findById(savedEntity.id).get();
        StatsEntity entity2 = repository.findById(savedEntity.id).get();

        // Update the entity using the first entity object
        entity1.serviceAddress = "1000";
        repository.save(entity1);

        // Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e.
        // a Optimistic Lock Error
        try {
            entity2.serviceAddress = "1000";
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {
        }

        // Get the updated entity from the database and verify its new sate
        StatsEntity updatedEntity = repository.findById(savedEntity.id).get();
        assertEquals(2, (int) updatedEntity.version);
        assertEquals("1000", updatedEntity.serviceAddress);
    }

    private void assertEqualsStats(StatsEntity expectedEntity, StatsEntity actualEntity) {
        assertEquals(expectedEntity.id, actualEntity.id);
        assertEquals(expectedEntity.value, actualEntity.value);
        assertEquals(expectedEntity.version, actualEntity.version);
        assertEquals(expectedEntity.serviceAddress, actualEntity.serviceAddress);
    }

}