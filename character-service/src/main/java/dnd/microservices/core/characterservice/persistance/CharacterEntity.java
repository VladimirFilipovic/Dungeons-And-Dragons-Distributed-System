package dnd.microservices.core.characterservice.persistance;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="characters")
public class CharacterEntity {
    @Id 
    private String id;
    
    @Version
    private int version;
    
    @Indexed(unique = true)
    private String name;

    private String race;
    private String religion;
    private String serviceAddress;
    private List<String> items;
    private List<String> spells;
    private List<String> stats;

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setSpells(List<String> spells) {
        this.spells = spells;
    }

    public void setStats(List<String> stats) {
        this.stats = stats;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
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

    public CharacterEntity(String name, String race, String religion, String serviceAddress) {
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
    }
}
