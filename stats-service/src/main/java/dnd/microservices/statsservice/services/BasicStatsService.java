package dnd.microservices.statsservice.services;

import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsAssignmentDto;
import dnd.microservices.core.api.stats.StatsService;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.ServiceUtil;
import dnd.microservices.statsservice.persistance.Stats.StatsEntity;
import dnd.microservices.statsservice.persistance.Stats.StatsKey;
import dnd.microservices.statsservice.persistance.Stats.StatsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public List<Statistic> getStats(String characterId, String statName) {
        Stream<StatsEntity> statsEntities = null;
        if (statName == null) {
            statsEntities = StreamSupport
                    .stream(repository.findById_CharacterId(characterId).get().spliterator(), false);
        } else {
            statsEntities = Stream.of(repository.findById(new StatsKey(characterId, statName)).get());
        }

        return statsEntities
                .map(statsMapper::entityToApi)
                .collect(Collectors.toList());
    }

    @Override
    public void assignStatsToCharacter(String characterId, List<Statistic> statistics) {
        List<StatsEntity> statsEntities = new ArrayList<StatsEntity>();
        for (Statistic stat : statistics) {
            if (repository.existsById(new StatsKey(characterId, stat.name.name()))) {
                StatsEntity statsEntity = repository.findById(new StatsKey(characterId, stat.name.name())).get();
                statsEntity.setValue(stat.value);
                statsEntities.add(statsEntity);
            } else {
                statsEntities.add(statsMapper.apiToEntity(characterId, stat));
            }
        }
        repository.saveAll(statsEntities);
    }

    @Override
    public void deleteCharacterStats(String characterId) {
       HashSet<StatsEntity> statsEntities = this.repository
              .findById_CharacterId(characterId)
              .orElseThrow(() -> new NotFoundException("No stats found for character " + characterId));

         this.repository.deleteAll(statsEntities);
    }   

}
