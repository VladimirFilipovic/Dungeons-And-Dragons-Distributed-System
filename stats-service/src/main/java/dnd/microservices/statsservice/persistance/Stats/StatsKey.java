package dnd.microservices.statsservice.persistance.Stats;


public class StatsKey {
    public String characterId;

    public String statisticName;

    @Override
    public int hashCode() {
        return characterId.hashCode() + statisticName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatsKey) {
            StatsKey other = (StatsKey) obj;
            return characterId.equals(other.characterId) && statisticName.equals(other.statisticName);
        } else {
            return false;
        }
    }


    public StatsKey() {
    }

    public StatsKey(String characterId, String statisticName) {
        this.characterId = characterId;
        this.statisticName = statisticName;
    }
}
