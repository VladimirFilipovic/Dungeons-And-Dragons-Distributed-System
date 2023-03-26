package dnd.microservices.core.api.spells;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpellsService {
    @GetMapping(
            value = "/spells",
            produces = "application/json"
    )
    List<Spell> getSpells(@RequestParam(value = "characterName", required = false) String characterName);

    @GetMapping(
            value = "/spells/{spellName}",
            produces = "application/json"
    )
    Spell getSpell(@PathVariable String spellName);
    
    @PostMapping(
            value = "/spells",
            consumes = "application/json",
            produces = "application/json"
    )
    void assignSpellToCharacter(String spellName, String characterName);

    @DeleteMapping(value = "/spells")
    void unAssignSpellFromCharacter(@RequestParam(value = "characterName", required = true) String characterName,
                    @RequestParam(value = "spellName", required = true) String spellName);

}
