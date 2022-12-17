package dnd.microservices.core.api.spells;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SpellsService {
    @GetMapping(
            value = "/spells",
            produces = "application/json"
    )
    List<Spell> getSpells();

    @GetMapping(
            value = "/spells/{spellName}",
            produces = "application/json"
    )
    Spell getSpell(@PathVariable String spellName);

}
