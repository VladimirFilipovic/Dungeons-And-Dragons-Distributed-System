package dnd.microservices.spellsservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellsService;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.spellsservice.persistance.CharacterSpellEntity;
import dnd.microservices.spellsservice.persistance.CharacterSpellKey;
import dnd.microservices.spellsservice.persistance.CharacterSpellRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BasicCharacterSpellsService implements CharacterSpellsService {

    private CharacterSpellRepository characterSpellRepository;
    private CharacterSpellsMapper characterSpellsMapper;
    private BasicSpellsService basicSpellsService;

    @Autowired
    public BasicCharacterSpellsService(CharacterSpellRepository characterSpellRepository,
            CharacterSpellsMapper characterSpellsMapper, BasicSpellsService basicSpellsService) {
        this.characterSpellRepository = characterSpellRepository;
        this.characterSpellsMapper = characterSpellsMapper;
        this.basicSpellsService = basicSpellsService;
    }

    @Override
    public Flux<CharacterSpell> getCharacterSpells(String characterId) {
         return this.characterSpellRepository.findById_CharacterId(characterId)
            .flatMap(characterSpell -> {
                // Step 1: Fetch the DnsSpell based on the name from the web service
                String spellName = characterSpell.id.getSpellName();
                return this.basicSpellsService.getSpell(spellName)
                        .map(dnsSpell -> {
                            return new CharacterSpell(dnsSpell, characterSpell.spellLevel);
                        })
                        .switchIfEmpty(
                                Mono.error(new NotFoundException("Character spell not found with characterId: " + characterId))
                        );
                    }
            )
            .switchIfEmpty(Mono.error(new NotFoundException("Character spell not found with characterId: " + characterId)));
    }

    @Override
    public void assignSpellToCharacter(String characterId, CharacterSpellAssignmentDto body) {
        Mono<CharacterSpellEntity> saveRes = this.characterSpellRepository.save(new CharacterSpellEntity(
                new CharacterSpellKey(characterId, body.spellName),
                body.spellLevel));
        saveRes.block();
    }

    @Override
    public void removeSpellFromCharacter(String characterId, String spellName) {
        CharacterSpellEntity characterSpellEntity = this.characterSpellRepository
                .findById(new CharacterSpellKey(characterId, spellName)).block();

        if (characterSpellEntity != null) {
           Mono<Void> deleteRes = this.characterSpellRepository.delete(characterSpellEntity);
              deleteRes.block();
        }
    }

    @Override
    public void deleteCharacterSpellRecords(String characterId) {
        Flux<Object> deleteRes = this.characterSpellRepository
                .findById_CharacterId(characterId)
                .flatMap(characterSpellEntity -> this.characterSpellRepository.delete(characterSpellEntity));
        deleteRes.collectList().block();
    }

}
