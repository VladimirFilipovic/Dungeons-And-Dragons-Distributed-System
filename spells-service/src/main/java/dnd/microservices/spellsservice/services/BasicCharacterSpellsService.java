package dnd.microservices.spellsservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellsService;
import dnd.microservices.spellsservice.persistance.CharacterSpellEntity;
import dnd.microservices.spellsservice.persistance.CharacterSpellKey;
import dnd.microservices.spellsservice.persistance.CharacterSpellRepository;
import reactor.core.publisher.Flux;

@RestController
public class BasicCharacterSpellsService implements CharacterSpellsService {

    private CharacterSpellRepository characterSpellRepository;
    private CharacterSpellsMapper characterSpellsMapper;

    @Autowired
    public BasicCharacterSpellsService(CharacterSpellRepository characterSpellRepository,
            CharacterSpellsMapper characterSpellsMapper) {
        this.characterSpellRepository = characterSpellRepository;
        this.characterSpellsMapper = characterSpellsMapper;
    }

    @Override
    public Flux<CharacterSpell> getCharacterSpells(String characterId) {
        return this.characterSpellRepository
                .findById_CharacterId(characterId)
                .map(characterSpellsMapper::entityToApi);
    }

    @Override
    public void assignSpellToCharacter(String characterId, CharacterSpellAssignmentDto body) {
        this.characterSpellRepository.save(new CharacterSpellEntity(
                new CharacterSpellKey(characterId, body.spellName),
                body.spellLevel)).block();
    }

    @Override
    public void removeSpellFromCharacter(String characterId, String spellName) {
        CharacterSpellEntity characterSpellEntity = this.characterSpellRepository
                .findById(new CharacterSpellKey(characterId, spellName)).block();

        if (characterSpellEntity != null) {
            this.characterSpellRepository.delete(characterSpellEntity).block();
        }
    }

    @Override
    public void deleteCharacterSpellRecords(String characterId) {
        this.characterSpellRepository
                .findById_CharacterId(characterId)
                .flatMap(characterSpellEntity -> this.characterSpellRepository.delete(characterSpellEntity))
                .subscribe();
    }

}
