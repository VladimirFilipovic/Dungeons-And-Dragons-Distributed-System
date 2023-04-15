package dnd.microservices.core.api.spells.characterSpells;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface CharacterSpellsService {

        @GetMapping(
                value = "/character/{character-id}/spells", 
                produces = "application/json"
                )
        List<CharacterSpell> getCharacterSpells(@PathVariable String characterId);

        @PutMapping(
                        value = "/character/{character-id}/spells", 
                        consumes = "application/json", 
                        produces = "application/json"
                )
        void assignSpellToCharacter(@PathVariable String characterId, @RequestBody CharacterSpellAssignmentDto body);

        @DeleteMapping(
                value = "/character/{character-id}/spells", 
                consumes = "application/json",
                 produces = "application/json"
                )
        void removeSpellFromCharacter(@PathVariable String characterId, @RequestBody CharacterSpellRemovalDto body);

}
