package dnd.microservices.spellsservice.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellRemovalDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellsService;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.spellsservice.persistance.CharacterSpellEntity;
import dnd.microservices.spellsservice.persistance.CharacterSpellKey;
import dnd.microservices.spellsservice.persistance.CharacterSpellRepository;

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
    public List<CharacterSpell> getCharacterSpells(String characterId) {
        HashSet<CharacterSpellEntity> characterSpellEntities = this.characterSpellRepository
            .findById_CharacterId(characterId)
            .orElseThrow(() -> new NotFoundException("No spells found for character " + characterId));

        List<CharacterSpell> characterSpells = new ArrayList<CharacterSpell>();

        for (CharacterSpellEntity characterSpellEntity : characterSpellEntities) {
            characterSpells.add(characterSpellsMapper.entityToApi(characterSpellEntity));
        }

        return characterSpells;
    }

    @Override
    public void assignSpellToCharacter(String characterId, CharacterSpellAssignmentDto body) {
       this.characterSpellRepository.save(new CharacterSpellEntity(
              new CharacterSpellKey(characterId, body.spellName),
              body.spellLevel
       ));
    }

    @Override
    public void removeSpellFromCharacter(String characterId, String spellName) {
       CharacterSpellEntity characterSpellEntity = this.characterSpellRepository
              .findById(new CharacterSpellKey(characterId, spellName))
              .orElseThrow(() -> new NotFoundException("No spell found for character " + characterId + " with name " + spellName));
        
         this.characterSpellRepository.delete(characterSpellEntity);
    }

    @Override
    public void deleteCharacterSpellRecords(String characterId) {
        HashSet<CharacterSpellEntity> characterSpellEntities = this.characterSpellRepository
            .findById_CharacterId(characterId)
            .orElseThrow(() -> new NotFoundException("No spells found for character " + characterId));

        this.characterSpellRepository.deleteAll(characterSpellEntities);
    }

   
}
