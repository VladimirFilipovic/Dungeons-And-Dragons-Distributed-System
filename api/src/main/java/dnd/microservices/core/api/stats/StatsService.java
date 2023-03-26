package dnd.microservices.core.api.stats;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    List<Statistic> getStats(@RequestParam(name = "characterName", required = true) String characterName);

    @PutMapping(
            value = "/stats",
            consumes = "application/json",
            produces = "application/json"
    ) 
    public void assignStatsToCharacter(String characterName, List<Statistic> stats);
}
