package dnd.microservices.core.api.stats;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface StatsService {
    @GetMapping(
            value = "/stats",
            produces = "application/json"
    )
    List<Statistic> getStats(
        @RequestParam(name = "characterId", required = true) String characterId,
        @RequestParam(name = "statName", required = false) String statName
        );

    @PutMapping(
            value = "/stats",
            consumes = "application/json",
            produces = "application/json"
    ) 
    public void assignStatsToCharacter(@RequestBody StatsAssignmentDto body);
}
