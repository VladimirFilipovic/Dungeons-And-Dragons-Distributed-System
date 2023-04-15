package dnd.microservices.spellsservice.persistance;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="character-spells")
public class CharacterSpellEntity {
   
    @Id
    public CharacterSpellKey id;
    
    @Version
    public int version;

    public int spellLevel;

    public CharacterSpellEntity() {
    }

    public CharacterSpellEntity(CharacterSpellKey id, int spellLevel) {
        this.id = id;
        this.spellLevel = spellLevel;
    }


}
