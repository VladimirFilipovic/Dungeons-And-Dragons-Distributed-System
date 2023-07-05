package dnd.microservices.charactercompositeservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import dnd.microservices.charactercompositeservice.services.BasicCharacterCompositeService;
import dnd.microservices.charactercompositeservice.services.IntegrationService;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.composite.CharacterComposite;
import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsName;


@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
//skips test suite
@Ignore
public class CharacterCompositeApplicationTests {
    private static final String CHARACTER_ID_OK = "1";
    private static final String CHARACTER_ID_NOT_FOUND = "2";
    private static final String CHARACTER_ID_INVALID = "3";

    public ItemCreateDto itemCreateDto;
    public Character character;
    public String testCharacterId;
    List<Statistic> statsDto;
    public CharacterComposite characterComposite;

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private BasicCharacterCompositeService basicCharacterCompositeService;

    @Test
    public void CreateItem() {
      itemCreateDto = new ItemCreateDto("testItem");
		  integrationService.createItem(itemCreateDto);

      Item item = integrationService.getItem("testItem").block();
      assertNotNull(item);
      assertEquals(item.name, "testItem");
    }

    @Test
    public void CreateSpell() {
      DndSpell spell = integrationService.getSpell("acid-arrow").block();
      assertNotNull(spell);
      assertEquals(spell.name, "Acid Arrow");
    }

    @Test
    public void CreateCharacter() {
      Character createCharacterDto = new Character("testCharacter", "testRace", "testReligion");
      testCharacterId = integrationService.createCharacter(createCharacterDto).id;

      character = integrationService.getCharacter(testCharacterId).block();
      assertNotNull(character);
      assertEquals(character.name, "testCharacter");
    }


    @Test
    public void CreateStats() {
      Character createCharacterDto = new Character("testCharacter2", "testRace", "testReligion");
      testCharacterId = integrationService.createCharacter(createCharacterDto).id;

      List<Statistic> statsDto = new ArrayList<>();
      statsDto.add(new Statistic(StatsName.HP, 10));
      statsDto.add(new Statistic(StatsName.AC, 10));
      integrationService.assignStatsToCharacter(testCharacterId, statsDto);

      List<Statistic> stats = integrationService.getStats(testCharacterId).collectList().block();
      assertNotNull(stats);
      assertEquals(stats.size(), 2);
    }

    @Test
    public void CreateCompositeCharacter() {
      //*items */
      itemCreateDto = new ItemCreateDto("testItem");
      Item item = integrationService.createItem(itemCreateDto);
      InventoryItem inventoryItem = new InventoryItem(item, 1);
      List<InventoryItem> inventoryItems = new ArrayList<>();
      inventoryItems.add(inventoryItem);

      /* spells */ 
      DndSpell spell = integrationService.getSpell("acid-arrow").block();
      CharacterSpell characterSpell = new CharacterSpell(spell, 1);
      List<CharacterSpell> characterSpells = new ArrayList<>();
      characterSpells.add(characterSpell);

      statsDto = new ArrayList<>();
      statsDto.add(new Statistic(StatsName.HP, 10));
      statsDto.add(new Statistic(StatsName.AC, 10));

       characterComposite = new CharacterComposite(
          "testCharacter3",
          "testRace",
          "testReligion",
          inventoryItems,
          characterSpells,
          statsDto
      );

      CharacterComposite composite = basicCharacterCompositeService.createCharacter(characterComposite);
      assertNotNull(composite);

      characterComposite = basicCharacterCompositeService.getCharacterData(composite.id).block();
      assertNotNull(characterComposite);
      assertEquals(characterComposite.name, "testCharacter3");
      assertNotNull(characterComposite.items);
      assertNotNull(characterComposite.spells);
      assertNotNull(characterComposite.stats);
    }

    @After
    public void cleanup(
    ) {
        if(itemCreateDto != null) {
          integrationService.deleteItem(itemCreateDto.name);
        }
        if(testCharacterId != null) {
           integrationService.deleteCharacterStats(testCharacterId);
        }
        if(statsDto != null) {
           integrationService.deleteCharacterStats(testCharacterId);
        }
        if(characterComposite != null) {
          basicCharacterCompositeService.deleteCompositeCharacter(characterComposite.id);
        }
    }
    

}
