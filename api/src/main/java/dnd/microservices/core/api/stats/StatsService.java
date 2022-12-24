package dnd.microservices.core.api.stats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface StatsService {
    @GetMapping(
            value = "/stats/{statisticName}",
            produces = "application/json"
    )
    Statistic getStatistic(@PathVariable String statisticName);

    @GetMapping(
            value = "/stats",
            produces = "application/json"
    )
    List<Statistic> getStats();

    public void changeCharacterStats(String characterName, String statisticName, Integer newValue);
}
