package dnd.microservices.charactercompositeservice.services;

import java.util.List;

import javax.swing.event.DocumentEvent.EventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.event.Event;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.InventoryService;
import dnd.microservices.core.api.items.inventory.ModificationType;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellsService;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsService;
import reactor.core.publisher.Flux;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Mono;
import static reactor.core.publisher.Flux.empty;

@EnableBinding(IntegrationService.MessageSources.class)
@Component
public class IntegrationService implements CharacterService, ItemsService, InventoryService, SpellsService,
                CharacterSpellsService, StatsService {

        private static final Logger LOG = LoggerFactory.getLogger(IntegrationService.class);

        private final WebClient webClient;
        private final ObjectMapper mapper;

        private final String characterServiceUrl;
        private final String itemsServiceUrl;
        private final String spellsServiceUrl;
        private final String statsServiceUrl;

        private String itemsServiceHost;
        private int itemsServicePort;
        private String spellsServiceHost;
        private int spellsServicePort;
        private String statsServiceHost;
        private int statsServicePort;
        private MessageSources messageSources;

        public interface MessageSources {

                String OUTPUT_CHARACTER = "output-character";
                String OUTPUT_INVENTORY = "output-inventory";
                String OUTPUT_STATS = "output-stats";
                String OUTPUT_SPELLS = "output-spells";

                @Output(OUTPUT_CHARACTER)
                MessageChannel outputCharacter();

                @Output(OUTPUT_INVENTORY)
                MessageChannel outputInventory();

                @Output(OUTPUT_STATS)
                MessageChannel outputStats();

                @Output(OUTPUT_SPELLS)
                MessageChannel outputSpells();
        }

        @Autowired
        public IntegrationService(
                        WebClient.Builder webClientBuilder,
                        ObjectMapper objectMapper,
                        @Value("${app.character-service.host}") String characterServiceHost,
                        @Value("${app.character-service.port}") int characterServicePort,
                        @Value("${app.items-service.host}") String itemsServiceHost,
                        @Value("${app.items-service.port}") int itemsServicePort,
                        @Value("${app.spells-service.host}") String spellsServiceHost,
                        @Value("${app.spells-service.port}") int spellsServicePort,
                        @Value("${app.stats-service.host}") String statsServiceHost,
                        @Value("${app.stats-service.port}") int statsServicePort,
                        MessageSources messageSources) {
                this.webClient = webClientBuilder.build();
                this.mapper = objectMapper;
                this.itemsServiceHost = itemsServiceHost;
                this.itemsServicePort = itemsServicePort;
                this.spellsServiceHost = spellsServiceHost;
                this.spellsServicePort = spellsServicePort;
                this.statsServiceHost = statsServiceHost;
                this.statsServicePort = statsServicePort;
                this.messageSources = messageSources;

                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                this.characterServiceUrl = this.getServiceUrl(characterServiceHost, characterServicePort, "characters");
                this.itemsServiceUrl = this.getServiceUrl(itemsServiceHost, itemsServicePort, "items");
                this.spellsServiceUrl = this.getServiceUrl(spellsServiceHost, spellsServicePort, "spells");
                this.statsServiceUrl = this.getServiceUrl(statsServiceHost, statsServicePort, "characters");
        }

        // #region character

        /**
         * @param characterName
         * @return
         */
        @Override
        public Mono<Character> getCharacter(String characterName) {
                String requestUrl = characterServiceUrl + "/" + characterName;
                LOG.debug("Will call Character API on URL: {}", requestUrl);

                return webClient.get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToMono(Character.class)
                                .doOnSuccess(character -> LOG.debug("Found a product with id: {}", character.getId()))
                                .onErrorMap(this::handleError);
        }

        @Override
        public Character createCharacter(Character body) {
                String requestUrl = characterServiceUrl;
                LOG.debug("Will call Character API on URL: {}", requestUrl);

                Message<Event> message = MessageBuilder
                                .withPayload(new Event(Event.Type.CREATE, body.name, body))
                                .build();

                messageSources.outputCharacter().send(message);

                return body;
        }

        @Override
        public void deleteCharacter(String characterId) {
                String requestUrl = characterServiceUrl + "/" + characterId;
                LOG.debug("Will call Character API on URL: {}", requestUrl);

                Message<Event> message = MessageBuilder
                                .withPayload(new Event(Event.Type.DELETE, characterId, null))
                                .build();

                messageSources.outputCharacter().send(message);
        }

        // #endregion

        // #region items service

        @Override
        public Flux<Item> getItems() {
                return webClient.get()
                                .uri(itemsServiceUrl)
                                .retrieve()
                                .bodyToFlux(Item.class)
                                .doOnNext(item -> LOG.debug("Retrieved item: {}", item.getName()))
                                .onErrorMap(this::handleError);
        }

        @Override
        public Mono<Item> getItem(String itemName) {
                String requestUrl = itemsServiceUrl + "/" + itemName;
                return webClient.get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToMono(Item.class)
                                .doOnSuccess(item -> LOG.debug("Retrieved item: {}", item.getName()))
                                .onErrorMap(this::handleError);
        }

        @Override
        public Item createItem(ItemCreateDto body) {
                String requestUrl = itemsServiceUrl;
                LOG.debug("Will call Item API on URL: {}", requestUrl);

                Event event = new Event(Event.Type.CREATE, body.name, body);
                Message<Event> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputInventory().send(message);

                // Perform any other necessary operations
                // ...

                return new Item(body.name, body.description, null); // Modify the return type as per your requirement
        }

        @Override
        public void deleteItem(String itemName) {
                String requestUrl = itemsServiceUrl + "/" + itemName;
                LOG.debug("Will call Item API on URL: {}", requestUrl);

                Event<String, Void> event = new Event<>(Event.Type.DELETE, itemName, null);
                Message<Event<String, Void>> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputInventory().send(message);
        }

        @Override
        public Flux<InventoryItem> getCharacterInventory(String characterId) {
                String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId
                                + "/inventory";
                return WebClient.create()
                                .get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToFlux(InventoryItem.class)
                                .doOnNext(inventoryItem -> LOG.debug("Retrieved inventory item with id: {}",
                                                inventoryItem.item.name))
                                .onErrorResume(error -> empty());
        }

        @Override
        public void modifyCharacterInventory(String characterId, InventoryItemModificationDto body) {
                String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId
                                + "/inventory";
                LOG.debug("Will call Item API on URL: {}", requestUrl);

                // TODO: whatch out for the type of the event
                Event event = new Event<>(Event.Type.CREATE, characterId, body);
                Message<Event> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputInventory().send(message);

                // Perform any other necessary operations
                // ...
        }

        @Override
        public void createCharacterInventory(String characterId) {
                String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId
                                + "/inventory";
                LOG.debug("Will call Item API on URL: {}", requestUrl);

                Event<String, Void> event = new Event<>(Event.Type.CREATE, characterId, null);
                Message<Event<String, Void>> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputInventory().send(message);

        }

        @Override
        public void deleteCharacterInventory(String characterId) {
                String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId
                                + "/inventory";
                LOG.debug("Will call Item API on URL: {}", requestUrl);

                Event<String, Void> event = new Event<>(Event.Type.DELETE, characterId, null);
                Message<Event<String, Void>> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputInventory().send(message);

        }
        // #endregion items service

        // #region spells service
        @Override
        public Mono<DndSpell> getSpell(String spellName) {
                String requestUrl = spellsServiceUrl + "/" + spellName;
                return WebClient.create()
                                .get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToMono(DndSpell.class)
                                .doOnSuccess(spell -> LOG.debug("Found a spell with name: {}", spell.name))
                                .onErrorMap(this::handleError);
        }

        @Override
        public Flux<CharacterSpell> getCharacterSpells(String characterId) {
                String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/"
                                + characterId
                                + "/spells";
                return WebClient.create()
                                .get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToFlux(CharacterSpell.class)
                                .doOnNext(characterSpell -> LOG.debug("Retrieved character spell: {}",
                                                characterSpell.spell.name))
                                .onErrorResume(error -> empty());

        }

        @Override
        public void assignSpellToCharacter(String characterId, CharacterSpellAssignmentDto body) {
                String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/"
                                + characterId + "/spells";
                LOG.debug("Will call Spell API on URL: {}", requestUrl);

                Event event = new Event<>(Event.Type.CREATE, characterId, body);
                Message<Event> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputSpells().send(message);

                // Perform any other necessary operations
                // ...
        }

        @Override
        public void removeSpellFromCharacter(String characterId, String spellName) {
                String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/"
                                + characterId + "/spells/" + spellName;
                LOG.debug("Will call Spell API on URL: {}", requestUrl);

                Event event = new Event<>(Event.Type.DELETE, characterId, spellName);
                Message<Event> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputSpells().send(message);

                // Perform any other necessary operations
                // ...
        }

        @Override
        public void deleteCharacterSpellRecords(String characterId) {
                String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/"
                                + characterId + "/spells";
                LOG.debug("Will call Spell API on URL: {}", requestUrl);

                Event<String, Void> event = new Event<>(Event.Type.DELETE, characterId, null);
                Message<Event<String, Void>> message = MessageBuilder
                                .withPayload(event)
                                .build();

                messageSources.outputSpells().send(message);

                // Perform any other necessary operations
                // ...
        }

        // #endregion

        // #region stats service
        public Flux<Statistic> getStats(String characterId) {
                String requestUrl = statsServiceUrl + "/" + characterId + "/stats";
                return WebClient.create()
                                .get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToFlux(Statistic.class)
                                .doOnNext(stats -> LOG.debug("Retrieved stats for character with id: {}", characterId))
                                .onErrorMap(this::handleError)
                                .onErrorResume(error -> empty());
        }

        @Override
        public Flux<Statistic> getStats(String characterId, String statName) {
                String requestUrl = statsServiceUrl + "/" + characterId + "/stats?statName=" + statName;
                return WebClient.create()
                                .get()
                                .uri(requestUrl)
                                .retrieve()
                                .bodyToFlux(Statistic.class)
                                .doOnNext(stats -> LOG.debug("Retrieved stats for character with id: {}", characterId))
                                .onErrorResume(error -> empty());
        }

          @Override
    public void assignStatsToCharacter(String characterId, List<Statistic> body) {
        String requestUrl = statsServiceUrl + "/" + characterId + "/stats";
        LOG.debug("Will call Stats API on URL: {}", requestUrl);

        Event event = new Event<>(Event.Type.UPDATE, characterId, body);
        Message<Event> message = MessageBuilder
                .withPayload(event)
                .build();

        messageSources.outputStats().send(message);

        // Perform any other necessary operations
        // ...
    }

    @Override
    public void deleteCharacterStats(String characterId) {
        String requestUrl = statsServiceUrl + "/" + characterId + "/stats";
        LOG.debug("Will call Stats API on URL: {}", requestUrl);

        Event<String, Void> event = new Event<>(Event.Type.DELETE, characterId, null);
        Message<Event<String, Void>> message = MessageBuilder
                .withPayload(event)
                .build();

        messageSources.outputStats().send(message);

        // Perform any other necessary operations
        // ...
    }

        // #endregion

        private String getServiceUrl(String host, int port, String serviceName) {
                return "http://" + host + ":" + port + "/" + serviceName;
        }

        private Throwable handleError(Throwable ex) {
                if (ex instanceof WebClientResponseException) {
                        WebClientResponseException responseException = (WebClientResponseException) ex;
                        LOG.warn("Got an unexpected HTTP error: {}, will rethrow it",
                                        responseException.getStatusCode());
                        LOG.warn("Error body: {}", responseException.getResponseBodyAsString());
                } else {
                        LOG.error("An unexpected error occurred during the web client request", ex);
                }
                return ex;
        }

}
