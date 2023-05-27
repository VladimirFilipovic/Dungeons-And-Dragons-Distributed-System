package dnd.microservices.spellsservice.persistance;

public class CharacterSpellKey {
    
    private String characterId;
    private String spellName;

    public CharacterSpellKey() {
    }

    public CharacterSpellKey(String characterId, String spellName) {
        this.characterId = characterId;
        this.spellName = spellName;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getSpellName() {
        return spellName;
    }

    public void setSpellName(String spellName) {
        this.spellName = spellName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CharacterSpellKey that = (CharacterSpellKey) o;

        if (characterId != null ? !characterId.equals(that.characterId) : that.characterId != null) return false;
        return spellName != null ? spellName.equals(that.spellName) : that.spellName == null;

    }

    @Override
    public int hashCode() {
        int result = characterId != null ? characterId.hashCode() : 0;
        result = 31 * result + (spellName != null ? spellName.hashCode() : 0);
        return result;
    }
}
