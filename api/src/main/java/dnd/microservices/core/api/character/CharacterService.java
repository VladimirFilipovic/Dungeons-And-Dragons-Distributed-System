package dnd.microservices.core.api.character;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CharacterService {
    @GetMapping(
            value = "/character/{characterName}",
            produces = "application/json")
    Character getCharacter(@PathVariable String characterName);
}
