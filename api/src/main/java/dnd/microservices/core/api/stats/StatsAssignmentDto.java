package dnd.microservices.core.api.stats;

public class StatsAssignmentDto {
    
    public String name;
    public int value;

    public StatsAssignmentDto() {
    }

    public StatsAssignmentDto(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
