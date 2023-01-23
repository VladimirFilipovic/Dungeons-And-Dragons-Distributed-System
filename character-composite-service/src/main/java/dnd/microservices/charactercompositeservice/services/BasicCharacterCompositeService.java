package dnd.microservices.charactercompositeservice.services;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.composite.CharacterComposite;
import dnd.microservices.core.api.composite.CharacterCompositeService;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.utils.http.ServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Api(description = "REST API for full character information.")
@RestController
@EnableSwagger2
public class BasicCharacterCompositeService implements CharacterCompositeService {
    private final ServiceUtil serviceUtil;
    private  IntegrationService integration;

    @Autowired
    public BasicCharacterCompositeService(
            ServiceUtil serviceUtil,
            IntegrationService integration
    ) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }


    @Override
    public CharacterComposite getCharacterData(String characterName) {
        Character character = this.integration.getCharacter(characterName);
        List<Item> items = this.integration.getItems(characterName);
        List<Spell> spells = this.integration.getSpells(characterName);
        List<Statistic> stats = this.integration.getStats(characterName);

        return createCharacterAggregate(character, items, spells, stats, serviceUtil.getServiceAddress());
    }

    private CharacterComposite createCharacterAggregate(
            Character character,
            List<Item> items,
            List<Spell> spells,
            List<Statistic> stats,
            String serviceAddress
    ) {
        //Character data
        String id = character.getId();
        String name = character.getName();
        String race = character.getRace();
        String religion = character.getReligion();

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
}
