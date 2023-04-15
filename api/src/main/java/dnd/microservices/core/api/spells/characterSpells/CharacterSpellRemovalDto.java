package dnd.microservices.core.api.spells.characterSpells;

public class CharacterSpellRemovalDto {
    public String spellName;

    public CharacterSpellRemovalDto() {
    }

    public CharacterSpellRemovalDto(String spellName) {
        this.spellName = spellName;
    }
}
