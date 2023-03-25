package dnd.microservices.statsservice.persistance;


import javax.persistence.*;

@Entity
@Table(name = "stats", indexes = {@Index(name = "stats_name", unique = true, columnList = "name")})
public class StatsEntity {
    @Id @GeneratedValue
    private int id;

    @Version
    private int version;

    private String name;
    private int value;
    private String serviceAddress;

    public int getId() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public StatsEntity() {
    }

    public StatsEntity(String name, int value, String serviceAddress) {
        this.name = name;
        this.value = value;
        this.serviceAddress = serviceAddress;
    }
}
