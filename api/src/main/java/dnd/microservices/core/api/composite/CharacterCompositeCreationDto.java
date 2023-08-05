package dnd.microservices.core.api.composite;

import java.util.List;

import dnd.microservices.core.api.items.inventory.InventoryItemDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.stats.Statistic;

public class CharacterCompositeCreationDto {
    public String name;
    public String race;
    public String religion;
    public String serviceAddress;
    public List<InventoryItemDto> items;
    public List<CharacterSpellAssignmentDto> spells;
    public List<Statistic> stats;

    public CharacterCompositeCreationDto() {
    }

    public CharacterCompositeCreationDto(String name, String race, String religion, String serviceAddress,
            List<InventoryItemDto> items, List<CharacterSpellAssignmentDto> spells, List<Statistic> stats) {
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
        this.items = items;
        this.spells = spells;
        this.stats = stats;
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

    public List<InventoryItemDto> getItems() {
        return items;
    }

    public void setItems(List<InventoryItemDto> items) {
        this.items = items;
    }

    public List<CharacterSpellAssignmentDto> getSpells() {
        return spells;
    }

    public void setSpells(List<CharacterSpellAssignmentDto> spells) {
        this.spells = spells;
    }

    public List<Statistic> getStats() {
        return stats;
    }

    public void setStats(List<Statistic> stats) {
        this.stats = stats;
    }
}
