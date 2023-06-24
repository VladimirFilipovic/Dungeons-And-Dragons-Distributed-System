package dnd.microservices.core.api.spells.characterSpells;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface CharacterSpellsService {

        @GetMapping(
                value = "/characters/{characterId}/spells", 
                produces = "application/json"
                )
        List<CharacterSpell> getCharacterSpells(@PathVariable String characterId);

        @PutMapping(
                        value = "/characters/{characterId}/spells", 
                        consumes = "application/json", 
                        produces = "application/json"
                )
        void assignSpellToCharacter(@PathVariable String characterId, @RequestBody CharacterSpellAssignmentDto body);

        @DeleteMapping(
                value = "/characters/{characterId}/spells/{spellName}", 
                consumes = "application/json",
                 produces = "application/json"
                )
        void removeSpellFromCharacter(@PathVariable String characterId, @PathVariable String spellName);

        @DeleteMapping(value = "/characters/{characterId}/spells")
        void deleteCharacterSpellRecords(@PathVariable String characterId);

}
