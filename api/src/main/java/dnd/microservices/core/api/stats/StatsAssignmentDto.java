package dnd.microservices.core.api.stats;

import java.util.List;

public class StatsAssignmentDto {
    
    public List<Statistic> stats;

    public StatsAssignmentDto() {
    }

    public StatsAssignmentDto(List<Statistic> stats) {
        this.stats = stats;
    }
}
