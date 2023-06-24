package dnd.microservices.core.api.composite;

import java.util.List;

import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.stats.Statistic;

public class CharacterComposite {
    public String id;
    public String name;
    public String race;
    public String religion;
    public String serviceAddress;
    public List<InventoryItem> items;
    public List<CharacterSpell> spells;
    public List<Statistic> stats;

    
    public CharacterComposite(
            String name,
            String race,
            String religion,
            List<InventoryItem> items,
            List<CharacterSpell> spells,
            List<Statistic> stats
    ) {
        this.id = null;
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.items = items;
        this.spells = spells;
        this.stats = stats;
        this.serviceAddress = null;
    }


    public CharacterComposite(
            String id,
            String name,
            String race,
            String religion,
            String serviceAddress,
            List<InventoryItem> items,
            List<CharacterSpell> spells,
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
