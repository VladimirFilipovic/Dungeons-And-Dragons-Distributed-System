package dnd.microservices.core.api.spells;


public class CharacterSpellDto {
    
    public String CharacterId;
    public String SpellId;

    public CharacterSpellDto() {
    }

    public CharacterSpellDto(String characterId, String spellId) {
        CharacterId = characterId;
        SpellId = spellId;
    }
}