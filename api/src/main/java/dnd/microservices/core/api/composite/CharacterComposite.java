package dnd.microservices.core.api.composite;

import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.stats.Statistic;

import java.util.List;

public class CharacterComposite {


    public final String id;
    public final String name;
    public final String race;
    public final String religion;
    public final String serviceAddress;
    public final List<InventoryItem> items;
    public final List<CharacterSpell> spells;
    public final List<Statistic> stats;


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
