package dnd.microservices.core.characterservice.services;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.characterservice.persistance.CharacterEntity;
import dnd.microservices.core.characterservice.persistance.CharacterRepository;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DuplicateKeyException;

import java.util.ArrayList;

@RestController
public class BasicCharacterService implements CharacterService {

    private final ServiceUtil serviceUtil;
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    @Autowired
    public BasicCharacterService(ServiceUtil serviceUtil, CharacterRepository characterRepository,
            CharacterMapper characterMapper) {
        this.serviceUtil = serviceUtil;
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
    }

    //TODO: consider changing id from string to int https://stackoverflow.com/questions/3298814/how-does-performance-of-guids-compare-to-strings-in-sql
    //TODO: picture of the character
    @Override
    public Character getCharacter(String characterId) {
        CharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(() -> new NotFoundException("No character found for id: " + characterId));
        Character character = characterMapper.entityToApi(characterEntity);
        character.setServiceAddress(serviceUtil.getServiceAddress());
        return character;
    }

    @Override
    public Character createCharacter(Character body) {
      try {
        CharacterEntity characterEntity = characterMapper.apiToEntity(body);
        CharacterEntity newCharacterEntity = characterRepository.save(characterEntity);
        return characterMapper.entityToApi(newCharacterEntity);
      } catch (DuplicateKeyException dke) {
        throw new IllegalArgumentException("Duplicate key, Character Name: " + body.name);
      }
    }

    @Override
    public void deleteCharacter(String characterId) {
      characterRepository.findById(characterId)
      .ifPresent(e -> characterRepository.delete(e));
    }
}
