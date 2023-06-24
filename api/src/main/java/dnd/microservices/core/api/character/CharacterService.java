package dnd.microservices.core.api.character;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CharacterService {
    @GetMapping(
            value = "/characters/{characterId}",
            produces = "application/json")
    Character getCharacter(@PathVariable String characterId);

    @PostMapping(
            value = "/characters",
            consumes = "application/json",
            produces = "application/json"
        )
    Character createCharacter(@RequestBody Character body);

    @DeleteMapping(value = "/characters/{characterId}")
    void deleteCharacter(@PathVariable String characterId);
}
