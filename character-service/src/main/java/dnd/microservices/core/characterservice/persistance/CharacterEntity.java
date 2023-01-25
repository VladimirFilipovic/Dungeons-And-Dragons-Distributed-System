package dnd.microservices.core.characterservice.persistance;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="characters")
public class CharacterEntity {
    @Id
    private String id;
    @Version
    private Integer version;
    private String name;
    private String race;
    private String religion;
    private String serviceAddress;
    private List<String> items;
    private List<String> spells;
    private List<String> stats;

    public String getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public String getReligion() {
        return religion;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public CharacterEntity() {
    }

    public CharacterEntity(String id, Integer version, String name, String race, String religion, String serviceAddress) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
    }
}
