package dnd.microservices.statsservice.persistance;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "stats", indexes = {@Index(name = "stats_name", unique = true, columnList = "id")})
public class StatsEntity {
    private String id;
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

    public String getServiceAddress() {
        return serviceAddress;
    }

    public StatsEntity() {
    }

    public StatsEntity(String id, String name, int value, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.serviceAddress = serviceAddress;
    }
}
