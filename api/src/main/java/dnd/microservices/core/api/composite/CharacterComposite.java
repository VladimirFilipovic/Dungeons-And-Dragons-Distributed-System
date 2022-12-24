package dnd.microservices.core.api.composite;

import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.stats.Statistic;

import java.util.List;

public class CharacterComposite {

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

    public List<Item> getItems() {
        return items;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public List<Statistic> getStats() {
        return stats;
    }

    private final String id;
    private final String name;
    private final String race;
    private final String religion;
    private final String serviceAddress;
    private final List<Item> items;
    private final List<Spell> spells;
    private final List<Statistic> stats;


    public CharacterComposite(
            String id,
            String name,
            String race,
            String religion,
            String serviceAddress,
            List<Item> items,
            List<Spell> spells,
            List<Statistic> stats
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
}
