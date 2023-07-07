package dnd.microservices.core.characterservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DuplicateKeyException;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.characterservice.persistance.CharacterEntity;
import dnd.microservices.core.characterservice.persistance.CharacterRepository;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.ServiceUtil;
import reactor.core.publisher.Mono;
import static reactor.core.publisher.Mono.error;


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
    public Mono<Character> getCharacter(String characterId) {
        return characterRepository.findById(characterId)
                .switchIfEmpty(error(new NotFoundException("No character found for id: " + characterId)))
                .doOnNext(characterEntity -> characterEntity.serviceAddress = serviceUtil.getServiceAddress())
                .map(characterMapper::entityToApi);
    }

    @Override
    public Character createCharacter(Character body) {
      try {
        CharacterEntity characterEntity = characterMapper.apiToEntity(body);
        Mono<Character> character =  characterRepository.save(characterEntity).map(characterMapper::entityToApi);
        return character.block();
      } catch (DuplicateKeyException dke) {
        throw new IllegalArgumentException("Duplicate key, Character Name: " + body.name);
      }
    }

    @Override
    public void deleteCharacter(String characterId) {
      characterRepository.findById(characterId).blockOptional().ifPresent(characterRepository::delete);
    }
}
