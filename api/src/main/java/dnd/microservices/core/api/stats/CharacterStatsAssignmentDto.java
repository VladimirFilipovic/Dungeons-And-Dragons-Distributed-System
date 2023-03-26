package dnd.microservices.core.api.stats;

import java.util.List;

public class CharacterStatsAssignmentDto {
    
    public String CharacterId;
    public List<StatsAssignmentDto> Stats;

    public CharacterStatsAssignmentDto() {
    }

    public CharacterStatsAssignmentDto(String characterId, List<StatsAssignmentDto> stats) {
        CharacterId = characterId;
        Stats = stats;
    }

}
