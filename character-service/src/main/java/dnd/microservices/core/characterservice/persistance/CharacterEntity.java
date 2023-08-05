package dnd.microservices.core.characterservice.persistance;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="characters")
public class CharacterEntity {
    @Id 
    public String id;
    
    @Version
    public int version;
    
    @Indexed(unique = true)
    public String name;

    public String race;
    public String religion;
    public String serviceAddress;

    public CharacterEntity(String id, int version, String name, String race, String religion, String serviceAddress) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public CharacterEntity() {
    }

    public CharacterEntity(String name, String race, String religion, String serviceAddress) {
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
    }
}
