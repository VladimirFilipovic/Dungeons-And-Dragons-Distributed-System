package dnd.microservices.core.api.composite;

import java.util.List;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.items.inventory.InventoryItemDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.stats.Statistic;

public class CharacterCompositeCreationResultDto {
    public Character character;
    public List<InventoryItemDto> items;
    public List<CharacterSpellAssignmentDto> spells;
    public List<Statistic> stats;




    public CharacterCompositeCreationResultDto() {
    }

        public CharacterCompositeCreationResultDto(Character character, List<InventoryItemDto> items,
            List<CharacterSpellAssignmentDto> spells, List<Statistic> stats) {
        this.character = character;
        this.items = items;
        this.spells = spells;
        this.stats = stats;
    }


    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
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
