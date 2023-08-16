package dnd.microservices.charactercompositeservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import dnd.microservices.charactercompositeservice.services.IntegrationService;
import dnd.microservices.core.api.composite.CharacterComposite;
import dnd.microservices.core.api.composite.CharacterCompositeCreationDto;
import dnd.microservices.core.api.composite.CharacterCompositeCreationResultDto;
import dnd.microservices.core.api.dnd5e.DndAreaOfEffect;
import dnd.microservices.core.api.dnd5e.DndDamage;
import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.event.Event;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemDto;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.ModificationType;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsName;
import static org.springframework.http.HttpStatus.OK;
import static reactor.core.publisher.Mono.just;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;
import static org.hamcrest.Matchers.is;
import dnd.microservices.core.api.character.Character;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static dnd.microservices.charactercompositeservice.IsSameEvent.sameEventExceptCreatedAt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@AutoConfigureWebTestClient(timeout = "36000")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,properties ={"eureka.client.enabled=false"})
public class MessagingTests {
    private static final String CHARACTER_ID_OK = "1";
    private static final String CHARACTER_ID_NOT_FOUND = "2";
    private static final String CHARACTER_ID_INVALID = "3";

    DndSpell dndSpell = new DndSpell(
            "spellIndex",
            "Spell Name",
            "spellUrl",
            List.of("Description 1", "Description 2"),
            List.of("Higher Level 1", "Higher Level 2"),
            "Spell Range",
            List.of("Component 1", "Component 2"),
            "Material",
            true,
            "Duration",
            false,
            "Casting Time",
            1,
            "Attack Type",
            new DndDamage(),
            new DndAreaOfEffect());

    @Autowired
    private WebTestClient client;

    @Autowired
    private IntegrationService.MessageSources channels;

    @Autowired
    private MessageCollector collector;

    BlockingQueue<Message<?>> queueCharacters = null;
    BlockingQueue<Message<?>> queueSpells = null;
    BlockingQueue<Message<?>> queueStats = null;
    BlockingQueue<Message<?>> queueInventory = null;

    @Before
    public void setUp() {
        queueCharacters = getQueue(channels.outputCharacter());
        queueStats = getQueue(channels.outputStats());
        queueSpells = getQueue(channels.outputSpells());
        queueInventory = getQueue(channels.outputInventory());
    }

    @Test
    public void createCompositeCharacter() {
      //*items */
      Item item = new Item(1, "testItem", "testType", "testDescription");
      InventoryItemDto inventoryItem = new InventoryItemDto(item.id, 1);
      List<InventoryItemDto> inventoryItems = new ArrayList<>();
      inventoryItems.add(inventoryItem);

      /* spells */ 
      CharacterSpellAssignmentDto characterSpell = new CharacterSpellAssignmentDto("acid-arrow", 1);
      List<CharacterSpellAssignmentDto> characterSpells = new ArrayList<>();
      characterSpells.add(characterSpell);

     /* stats */
     List<Statistic> statsDto = new ArrayList<>();
     statsDto.add(new Statistic(StatsName.HP, 10));

        CharacterCompositeCreationDto composite = new CharacterCompositeCreationDto(
                "testCharacter3",
                "testRace",
                "testReligion",
                "",
                inventoryItems,
                characterSpells,
                statsDto);

         CharacterCompositeCreationResultDto compositeResult = postAndVerifyCharacter(composite, OK);

        // Assert one create character event queued up
        assertEquals(1, queueCharacters.size());
        Character testCharacter = new Character(compositeResult.character.id, compositeResult.character.name, compositeResult.character.race, compositeResult.character.religion, "");

        Event<String, Character> expectedCharacterEvent = new Event<>(Event.Type.CREATE, testCharacter.name,
                testCharacter);
        assertThat(queueCharacters, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedCharacterEvent))));

        // Assert one create inventory event queued up
        assertEquals(2, queueInventory.size());
        queueInventory.remove();
        InventoryItemModificationDto inventoryItemModificationDto = new InventoryItemModificationDto(item.id, 1,
                ModificationType.ADD);
        Event<String, InventoryItemModificationDto> expectedInventoryEvent = new Event<>(Event.Type.UPDATE_INV, compositeResult.character.id,
                inventoryItemModificationDto);
     

        assertThat(queueInventory, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedInventoryEvent))));


        // Assert one create spell event queued up
        assertEquals(1, queueSpells.size());
        CharacterSpellAssignmentDto spellAssignment = new CharacterSpellAssignmentDto(characterSpell.spellName, characterSpell.spellLevel);

        Event<String, CharacterSpellAssignmentDto> expectedSpellEvent = new Event<>(Event.Type.CREATE, compositeResult.character.id ,spellAssignment);
        assertThat(queueSpells, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedSpellEvent))));

        // Assert one create stats event queued up
        assertEquals(1, queueStats.size());

        Event<String, List<Statistic>> expectedStatsEvent = new Event<>(Event.Type.UPDATE, compositeResult.character.id, composite.stats);
        assertThat(queueStats, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedStatsEvent))));
    }

    @Test
    public void deleteCompositeCharacter() {

        deleteAndVerifyCharacter(CHARACTER_ID_OK, OK);

        assertEquals(1, queueCharacters.size());

        Event<String, Character> expectedCharacterEvent = new Event<>(Event.Type.DELETE, CHARACTER_ID_OK, null);
        assertThat(queueCharacters, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedCharacterEvent))));

        assertEquals(1, queueInventory.size());

        Event<String, InventoryItemModificationDto> expectedInventoryEvent = new Event<>(Event.Type.DELETE_INV, CHARACTER_ID_OK, null);
        assertThat(queueInventory, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedInventoryEvent))));

        assertEquals(1, queueSpells.size());

        Event<String, CharacterSpellAssignmentDto> expectedSpellEvent = new Event<>(Event.Type.DELETE_SPELL_REC, CHARACTER_ID_OK, null);
        assertThat(queueSpells, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedSpellEvent))));

        assertEquals(1, queueStats.size());

        Event<String, List<Statistic>> expectedStatsEvent = new Event<>(Event.Type.DELETE, CHARACTER_ID_OK, null);
        assertThat(queueStats, is(receivesPayloadThat(sameEventExceptCreatedAt(expectedStatsEvent))));
    }

    private BlockingQueue<Message<?>> getQueue(MessageChannel messageChannel) {
        return collector.forChannel(messageChannel);
    }

    private CharacterCompositeCreationResultDto postAndVerifyCharacter(CharacterCompositeCreationDto compositeCharacter,
            HttpStatus expectedStatus) {
        return client.post()
                .uri("/characters")
                .body(just(compositeCharacter), CharacterCompositeCreationDto.class)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .returnResult(CharacterCompositeCreationResultDto.class)
                .getResponseBody()
                .blockLast();
    }

    private void deleteAndVerifyCharacter(String characterId, HttpStatus expectedStatus) {
        client.delete()
                .uri("/characters/" + characterId)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

}
