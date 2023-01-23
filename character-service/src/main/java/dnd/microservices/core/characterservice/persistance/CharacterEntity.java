package dnd.microservices.core.characterservice.persistance;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "characters")
public class CharacterEntity {

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

    @Id
    private String id;
    @Version
    private Integer version;
    @Indexed(unique = true)
    private String name;
    private String race;
    private String religion;
    private String serviceAddress;

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
