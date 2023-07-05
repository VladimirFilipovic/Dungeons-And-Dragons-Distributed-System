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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    public List<CharacterSpell> getSpells() {
        return spells;
    }

    public void setSpells(List<CharacterSpell> spells) {
        this.spells = spells;
    }

    public List<Statistic> getStats() {
        return stats;
    }

    public void setStats(List<Statistic> stats) {
        this.stats = stats;
    }

    public CharacterComposite() {
        
    }
    
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
