package dnd.microservices.core.characterservice.services;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.utils.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicCharacterService implements CharacterService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public BasicCharacterService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    //TODO: consider changing id from string to int https://stackoverflow.com/questions/3298814/how-does-performance-of-guids-compare-to-strings-in-sql
    //TODO: picture of the character
    @Override
    public Character getCharacter(String characterName) {
        return new Character(characterName, characterName, "Aaraorca", "none", serviceUtil.getServiceAddress());
    }
}
