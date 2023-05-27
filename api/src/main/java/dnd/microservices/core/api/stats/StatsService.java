package dnd.microservices.core.api.stats;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface StatsService {
    @GetMapping(
            value = "/characters/{characterid}/stats",
            produces = "application/json"
    )
    List<Statistic> getStats(
        @PathVariable(name = "characterId") String characterId,
        @RequestParam(name = "statName", required = false) String statName
        );

    @PutMapping(
            value = "/characters/{characterid}/stats",
            consumes = "application/json",
            produces = "application/json"
    ) 
    public void assignStatsToCharacter(
        @PathVariable(name = "characterId") String characterId,
        @RequestBody StatsAssignmentDto body);

    @DeleteMapping (
            value = "/{characterId}/stats"
    )
    public void deleteCharacterStats(
        @PathVariable(name = "characterId") String characterId
    );
}
