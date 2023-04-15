package dnd.microservices.core.api.spells.characterSpells;

import dnd.microservices.core.api.spells.Spell;

public class CharacterSpell {
   
    public Spell spell;
    public int level;

    public int getDamage() {
        return spell.getDamageAtLevel().get(level);
    }

    public CharacterSpell() {
    }

    public CharacterSpell(Spell spell, int level) {
        this.spell = spell;
        this.level = level;
    }
}
