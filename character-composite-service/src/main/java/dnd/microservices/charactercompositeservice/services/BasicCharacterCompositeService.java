package dnd.microservices.charactercompositeservice.services;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.composite.CharacterComposite;
import dnd.microservices.core.api.composite.CharacterCompositeCreationDto;
import dnd.microservices.core.api.composite.CharacterCompositeCreationResultDto;
import dnd.microservices.core.api.composite.CharacterCompositeService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemDto;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.ModificationType;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.utils.http.ServiceUtil;
import io.swagger.annotations.Api;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.bson.types.ObjectId;
import org.slf4j.Logger;

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
    public CharacterCompositeCreationResultDto createCharacter(CharacterCompositeCreationDto characterComposite) {
        ObjectId id = new ObjectId();
        Character character = new Character(id.toString(), characterComposite.name, characterComposite.race, characterComposite.religion, "");
        Character createdCharacter = this.integration.createCharacter(character);
        this.integration.createCharacterInventory(createdCharacter.id);

        LOG.warn("create character: {}", createdCharacter.toString());

        if (characterComposite.items != null) {
            List<InventoryItemDto> inventoryItems = characterComposite.items;
            for (InventoryItemDto invItem : inventoryItems) {
                InventoryItemModificationDto inventoryItem = new InventoryItemModificationDto(invItem.id, invItem.amount, ModificationType.ADD);
                this.integration.modifyCharacterInventory(createdCharacter.id, inventoryItem);
            }
        }

        if (characterComposite.spells != null) {
            List<CharacterSpellAssignmentDto> characterSpells = characterComposite.spells;
            for (CharacterSpellAssignmentDto characterSpell : characterSpells) {
                this.integration.assignSpellToCharacter(createdCharacter.id, characterSpell);
            }
        }

        if (characterComposite.stats != null) {
            this.integration.assignStatsToCharacter(createdCharacter.id, characterComposite.stats);
        }

        return new CharacterCompositeCreationResultDto(
            createdCharacter,
            characterComposite.items,
            characterComposite.spells,
            characterComposite.stats
        );

    }


    @Override
    public void deleteCompositeCharacter(String characterId) {
        this.integration.deleteCharacter(characterId);
        this.integration.deleteCharacterInventory(characterId);
        this.integration.deleteCharacterSpellRecords(characterId);
        this.integration.deleteCharacterStats(characterId);
    }
}
