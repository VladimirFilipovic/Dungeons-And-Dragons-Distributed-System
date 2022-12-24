package dnd.microservices.core.api.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CharacterCompositeService {

    @GetMapping(
            value = "/full-character-info/{characterName}",
            produces = "application/json"
    )
    public CharacterComposite getCharacterData(@PathVariable String characterName);
}
