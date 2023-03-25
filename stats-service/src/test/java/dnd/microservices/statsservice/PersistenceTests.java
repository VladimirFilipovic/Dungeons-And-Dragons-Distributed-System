package dnd.microservices.statsservice;


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

import dnd.microservices.statsservice.persistance.StatsEntity;
import dnd.microservices.statsservice.persistance.StatsRepository;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistenceTests {

    @Autowired
    private StatsRepository repository;

    private StatsEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();
        StatsEntity entity = new StatsEntity("Stat 1", 0, null);
        savedEntity = repository.save(entity);
    }

    @Test
    public void create() {
        StatsEntity newEntity =  new StatsEntity("Amazing new statistic", 0, null);

        StatsEntity newSavedEntity = repository.save(newEntity);

        Optional<StatsEntity> foundEntity = repository.findById(newSavedEntity.getId());

        assertEqualsStats(newSavedEntity, foundEntity.get());
        assertEquals(2, repository.count());
    }

    @Test
    public void update() {
        savedEntity.setName("Stat name update");
        repository.save(savedEntity);

        StatsEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, foundEntity.getVersion());
        assertEquals(savedEntity.getName(), foundEntity.getName());
    }

    @Test
    public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    public void getById() {
        Optional<StatsEntity> entity = repository.findById(savedEntity.getId());
        assertTrue(entity.isPresent());
        assertEqualsStats(savedEntity, entity.get());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateError() {
        StatsEntity entity = new StatsEntity("Stat 1", 0, null);
        repository.save(entity);
    }

    @Test
    public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        StatsEntity entity1 = repository.findById(savedEntity.getId()).get();
        StatsEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update the entity using the first entity object
        entity1.setServiceAddress("1000");
        repository.save(entity1);

        // Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e.
        // a Optimistic Lock Error
        try {
            entity2.setServiceAddress("1000");
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {
        }

        // Get the updated entity from the database and verify its new sate
        StatsEntity updatedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (int) updatedEntity.getVersion());
        assertEquals("1000", updatedEntity.getServiceAddress());
    }

    private void assertEqualsStats(StatsEntity expectedEntity, StatsEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getServiceAddress(), actualEntity.getServiceAddress());
    }

}