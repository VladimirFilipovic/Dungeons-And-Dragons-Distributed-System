package dnd.microservices.core.api.character;

import java.util.List;

public class Character {

    private final String id;
    private final String name;
    private final String race;
    private final String religion;
    private final List<String> items;
    private final List<String> spells;
    private final List<String> stats;

    private String serviceAddress;


    public String getId() {
        return id;
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
    public List<String> getItems() {
        return items;
    }

    public List<String> getSpells() {
        return spells;
    }

    public List<String> getStats() {
        return stats;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Character(
            String id,
            String name,
            String race,
            String religion,
            String serviceAddress,
            List<String> items,
            List<String> spells,
            List<String> stats
    ) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
        this.items = items;
        this.spells = spells;
        this.stats = stats;
    }



    public Character() {
        this.id = null;
        this.name = null;
        this.race = null;
        this.religion = null;
        this.serviceAddress = null;
        this.spells = null;
        this.items = null;
        this.stats = null;
    }
}
