package dnd.microservices.charactercompositeservice.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.composite.CharacterComposite;
import dnd.microservices.core.api.composite.CharacterCompositeService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.ModificationType;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsAssignmentDto;
import dnd.microservices.core.utils.http.ServiceUtil;
import io.swagger.annotations.Api;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(description = "REST API for full character information.")
@RestController
@EnableSwagger2
public class BasicCharacterCompositeService implements CharacterCompositeService {
    private final ServiceUtil serviceUtil;
    private  IntegrationService integration;
    private static final Logger LOG = LoggerFactory.getLogger(BasicCharacterCompositeService.class);

    @Autowired
    public BasicCharacterCompositeService(
            ServiceUtil serviceUtil,
            IntegrationService integration
    ) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }


    @Override
    public Mono<CharacterComposite> getCharacterData(String characterId) {
        return Mono.zip(
                values -> createCharacterAggregate(
                        (Character) values[0],
                        (List<InventoryItem>) values[1],
                        (List<CharacterSpell>) values[2],
                        (List<Statistic>) values[3],
                        serviceUtil.getServiceAddress()
                ),
                this.integration.getCharacter(characterId),
                this.integration.getCharacterInventory(characterId).collectList(),
                this.integration.getCharacterSpells(characterId).collectList(),
                this.integration.getStats(characterId).collectList()
        ).doOnError(ex -> {
            LOG.warn("getCharacterData() failed: {}", ex.toString());
        })
        .log();
    }

    private CharacterComposite createCharacterAggregate(
            Character character,
            List<InventoryItem>  items,
            List<CharacterSpell>spells,
            List<Statistic> stats,
            String serviceAddress
    ) {
        //Character data
        String id = character.id;
        String name = character.name;
        String race = character.race;
        String religion = character.religion;

        return new CharacterComposite(
                id,
                name,
                race,
                religion,
                serviceAddress,
                items,
                spells,
                stats
        );
    }

    @Override
    public CharacterComposite createCharacter(CharacterComposite characterComposite) {
        Character character = new Character(characterComposite.name, characterComposite.race, characterComposite.religion);
        Character createdCharacter = this.integration.createCharacter(character);
        
        if (characterComposite.items != null) {
            this.integration.createCharacterInventory(createdCharacter.id);

            List<InventoryItem> inventoryItems = characterComposite.items;
            for (InventoryItem invItem : inventoryItems) {
                InventoryItemModificationDto inventoryItem = new InventoryItemModificationDto(invItem.item.getId(), invItem.amount, ModificationType.ADD);
                this.integration.modifyCharacterInventory(createdCharacter.id, inventoryItem);
            }
        }

        if (characterComposite.spells != null) {
            List<CharacterSpell> characterSpells = characterComposite.spells;
            for (CharacterSpell characterSpell : characterSpells) {
                CharacterSpellAssignmentDto spellAssignment = new CharacterSpellAssignmentDto(characterSpell.spell.name, characterSpell.level);
                this.integration.assignSpellToCharacter(createdCharacter.id, spellAssignment);
            }
        }

        if (characterComposite.stats != null) {
            this.integration.assignStatsToCharacter(createdCharacter.id, characterComposite.stats);
        }

        characterComposite.id = createdCharacter.id;

        return characterComposite;
    }


    @Override
    public void deleteCompositeCharacter(String characterId) {
        this.integration.deleteCharacter(characterId);
        this.integration.deleteCharacterInventory(characterId);
        this.integration.deleteCharacterSpellRecords(characterId);
        this.integration.deleteCharacterStats(characterId);
    }
}
