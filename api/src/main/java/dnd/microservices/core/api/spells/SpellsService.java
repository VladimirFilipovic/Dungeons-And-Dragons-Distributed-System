package dnd.microservices.core.api.spells;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import dnd.microservices.core.api.dnd5e.DndSpell;

public interface SpellsService {
    @GetMapping(
            value = "/spells/{spellName}",
            produces = "application/json"
    )
    DndSpell getSpell(@PathVariable String spellName);
}
