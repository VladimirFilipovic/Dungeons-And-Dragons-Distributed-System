package dnd.microservices.statsservice;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import dnd.microservices.statsservice.persistance.Stats.StatsEntity;
import dnd.microservices.statsservice.persistance.Stats.StatsKey;
import dnd.microservices.statsservice.persistance.Stats.StatsRepository;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {

    @Autowired
    private StatsRepository repository;

    private StatsEntity savedEntity;

    @Before
    public void setupDb() {
        repository.deleteAll().block();
        StatsEntity entity = new StatsEntity(new StatsKey("character 1", "stat 1"), 0, "service");
       
        StepVerifier.create(
            repository.save(entity)
        )
        .expectNextMatches(createdEntity -> {
            savedEntity = createdEntity;
            return areStatsEqual(entity, savedEntity);
        })
        .verifyComplete();
    }


    @Test
    public void create() {
        StatsEntity newEntity = new StatsEntity(new StatsKey("character 2", "stat 2"), 0, "service");

        StatsEntity newSavedEntity = repository.save(newEntity).block();

        StepVerifier
        .create(repository.findById(newSavedEntity.id))
        .expectNextMatches(foundEntity -> areStatsEqual(newEntity, foundEntity))
                .verifyComplete();
    }


    @Test
    public void update() {
        savedEntity.value = 52;

        StepVerifier.create(repository.save(savedEntity))
        .expectNextCount(1)
        .verifyComplete();


        StepVerifier.create(repository.findById(savedEntity.id))
                .expectNextMatches(updatedEntity -> {
                    return updatedEntity.version == 2 && updatedEntity.value == 52;
                })
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
                .expectNextMatches(entity -> areStatsEqual(entity, savedEntity))
                .verifyComplete();
    }

    @Test
    public void duplicateError() {
        StatsEntity entity = new StatsEntity(new StatsKey("character 1", "stat 1"), 0, "service");
        StepVerifier.create(repository.save(entity)).verifyError();
    }
 
    @Test
    public void optimisticLockError() {
        // Store the saved entity in two separate entity objects
        StatsEntity entity1 = repository.findById(savedEntity.getId()).block();
        StatsEntity entity2 = repository.findById(savedEntity.getId()).block();

        // Update the entity using the first entity object
        entity1.value = 15;
        repository.save(entity1).block();

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        StepVerifier.create(repository.save(entity2)).expectError().verify();

        // Get the updated entity from the database and verify its new sate
        StepVerifier.create(repository.findById(savedEntity.getId()))
            .expectNextMatches(foundEntity ->
                foundEntity.getVersion() == 2 &&
                foundEntity.getValue() == 15
            )
            .verifyComplete();
    }

    private boolean areStatsEqual(StatsEntity expectedEntity, StatsEntity actualEntity) {
        return expectedEntity.id.equals(actualEntity.id) &&
                expectedEntity.value == (actualEntity.value) &&
                expectedEntity.version == (actualEntity.version);
    }
}
