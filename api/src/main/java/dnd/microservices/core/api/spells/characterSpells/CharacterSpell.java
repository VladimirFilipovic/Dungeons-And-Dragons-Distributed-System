package dnd.microservices.core.api.spells.characterSpells;

import dnd.microservices.core.api.dnd5e.DndSpell;

public class CharacterSpell {
   
    public DndSpell spell;
    public int level;

    public CharacterSpell() {
    }

    public CharacterSpell(DndSpell spell, int level) {
        this.spell = spell;
        this.level = level;
    }
}
