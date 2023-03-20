package dnd.microservices.statsservice.persistance;


import javax.persistence.*;

@Entity
@Table(name = "stats", indexes = {@Index(name = "stats_name", unique = true, columnList = "id")})
public class StatsEntity {
    @Id
    private String id;
    @Version
    private int version;
    private String name;
    private int value;
    private String serviceAddress;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getVersion() {
        return version;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public StatsEntity() {
    }

    public StatsEntity(String id, int version, String name, int value, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.serviceAddress = serviceAddress;
        this.version = version;
    }
}
