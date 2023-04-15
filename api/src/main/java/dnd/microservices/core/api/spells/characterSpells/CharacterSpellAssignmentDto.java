package dnd.microservices.core.api.spells.characterSpells;


public class CharacterSpellAssignmentDto {
    
    public String spellName;
    public int spellLevel;

    public CharacterSpellAssignmentDto() {
    }

    public CharacterSpellAssignmentDto(String spellName, int spellLevel ) {
        this.spellName = spellName;
        this.spellLevel = spellLevel;
    }
}