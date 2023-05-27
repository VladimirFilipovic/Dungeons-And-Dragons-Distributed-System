package dnd.microservices.core.api.stats;

import java.util.List;

public class StatsAssignmentDto {
    
    public String characterId;
    public List<Statistic> stats;

    public StatsAssignmentDto() {
    }

    public StatsAssignmentDto(String characterId, List<Statistic> stats) {
        this.characterId = characterId;
        this.stats = stats;
    }
}
