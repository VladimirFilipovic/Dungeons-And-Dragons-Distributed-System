package dnd.microservices.core.api.spells;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpellsService {
    @GetMapping(
            value = "/spells/{spellName}",
            produces = "application/json"
    )
    Spell getSpell(@PathVariable String spellName);
}
