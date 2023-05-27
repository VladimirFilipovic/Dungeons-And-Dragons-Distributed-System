package dnd.microservices.statsservice.persistance.Stats;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="character-stats")
public class StatsEntity {
    
    @Id
    public StatsKey id;

    @Version
    public int version;

    public int value;

    public String serviceAddress;


    public StatsEntity() {
    }

    public StatsEntity(StatsKey id, int value, String serviceAddress) {
        this.id = id;
        this.value = value;
        this.serviceAddress = serviceAddress;
    }
}
