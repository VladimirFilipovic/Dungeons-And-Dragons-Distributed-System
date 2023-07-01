package dnd.microservices.statsservice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsService;
import dnd.microservices.statsservice.persistance.Stats.StatsEntity;
import dnd.microservices.statsservice.persistance.Stats.StatsKey;
import dnd.microservices.statsservice.persistance.Stats.StatsRepository;
import reactor.core.publisher.Flux;

@RestController
public class BasicStatsService implements StatsService {

    private final StatsRepository repository;
    private final StatsMapper statsMapper;

    @Autowired
    public BasicStatsService(
        StatsRepository repository,
        StatsMapper statsMapper
        ) {
        this.repository = repository;
        this.statsMapper = statsMapper;
    }

    @Override
    public Flux<Statistic> getStats(String characterId, String statName) {
        if (statName == null) {
            return repository
                    .findById_CharacterId(characterId)
                    .map(statsMapper::entityToApi);
        } else {
            return this.repository
                    .findById(new StatsKey(characterId, statName))
                    .flux()
                    .map(statsMapper::entityToApi);
        }
    }

    @Override
    public void assignStatsToCharacter(String characterId, List<Statistic> statistics) {
        List<StatsEntity> statsEntities = new ArrayList<StatsEntity>();
        for (Statistic stat : statistics) {
            if (repository.existsById(new StatsKey(characterId, stat.name.name())).block()) {
                StatsEntity statsEntity = repository.findById(new StatsKey(characterId, stat.name.name())).block();
                statsEntity.setValue(stat.value);
                statsEntities.add(statsEntity);
            } else {
                statsEntities.add(statsMapper.apiToEntity(characterId, stat));
            }
        }
        repository.saveAll(statsEntities).subscribe();
    }

    @Override
    public void deleteCharacterStats(String characterId) {
         this.repository
              .findById_CharacterId(characterId)
              .flatMap(statsEntity -> this.repository.delete(statsEntity))
              .subscribe();
    }   

}
