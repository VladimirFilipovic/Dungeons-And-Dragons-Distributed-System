package dnd.microservices.core.api.stats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface StatsInterface {
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
}
