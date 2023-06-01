package dnd.microservices.charactercompositeservice.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.InventoryService;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellRemovalDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellsService;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsAssignmentDto;
import dnd.microservices.core.api.stats.StatsService;
import dnd.microservices.core.utils.exceptions.InvalidInputException;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.io.IOException;
import java.util.List;

@Component
public class IntegrationService implements CharacterService, ItemsService, InventoryService, SpellsService,
        CharacterSpellsService, StatsService {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    //service urls:
    private final String characterServiceUrl;
    private final String itemsServiceUrl;
    private final String spellsServiceUrl;
    private final String statsServiceUrl;


    @Autowired
    public IntegrationService(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${app.character-service.host}") String characterServiceHost,
            @Value("${app.character-service.port}") int characterServicePort,
            @Value("${app.items-service.host}") String itemsServiceHost,
            @Value("${app.items-service.port}") int itemsServicePort,
            @Value("${app.spells-service.host}") String spellsServiceHost,
            @Value("${app.spells-service.port}") int spellsServicePort,
            @Value("${app.stats-service.host}") String statsServiceHost,
            @Value("${app.stats-service.port}") int statsServicePort
    ) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.characterServiceUrl = this.getServiceUrl(characterServiceHost, characterServicePort, "character");
        this.itemsServiceUrl = this.getServiceUrl(itemsServiceHost, itemsServicePort, "items");
        this.spellsServiceUrl = this.getServiceUrl(spellsServiceHost, spellsServicePort, "spells");
        this.statsServiceUrl = this.getServiceUrl(statsServiceHost, statsServicePort, "stats");
    }

    //#region character

    /**
     * @param characterName
     * @return
     */
    @Override
    public Character getCharacter(String characterName) {
        try {
            String requestUrl = characterServiceUrl + "/" + characterName;
            LOG.debug("Will call Character API on URL: {}", requestUrl);

            Character character = restTemplate.getForObject(requestUrl, Character.class);
            LOG.debug("Found a product with id: {}", character.id);

            return character;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public Character createCharacter(Character body) {
        try {
            String requestUrl = characterServiceUrl;
            LOG.debug("Will call Character API on URL: {}", requestUrl);

            Character character = restTemplate.postForObject(requestUrl, body, Character.class);
            LOG.debug("Created a product with id: {}", character.id);

            return character;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void deleteCharacter(String characterId) {
        try {
            String requestUrl = characterServiceUrl + "/" + characterId;
            LOG.debug("Will call Character API on URL: {}", requestUrl);

            restTemplate.delete(requestUrl);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }
    //#endregion
    
    //#region items service

    @Override
    public List<Item> getItems() {
        List<Item> items = restTemplate.exchange(
                itemsServiceUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Item>>() {
                }).getBody();

        return items;
    }

    /**
     * @param itemName
     * @return
     */
    @Override
    public Item getItem(String itemName) {
        String requestUrl = itemsServiceUrl + "/" + itemName;
        Item item = restTemplate.getForObject(requestUrl, Item.class);

        return item;
    }

    @Override
    public void createItem(ItemCreateDto body) {
        try {
            String requestUrl = itemsServiceUrl;
            LOG.debug("Will call Items API on URL: {}", requestUrl);

            restTemplate.postForObject(requestUrl, body, Item.class);
            LOG.debug("Created an item with name: {}", body.name);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public List<InventoryItem> getCharacterInventory(String characterId) {
        String requestUrl = "/characters/" + characterId + "/inventory";
        List<InventoryItem> inventoryItems = restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<InventoryItem>>() {
                }).getBody();

        return inventoryItems;
    }

    @Override
    public void modifyCharacterInventory(String characterId, InventoryItemModificationDto body) {
        try {
            String requestUrl = "/characters/" + characterId + "/inventory";
            LOG.debug("Will call Items API on URL: {}", requestUrl);

            restTemplate.put(requestUrl, body);
            LOG.debug("Modified an item with id: {}", body.itemId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void deleteCharacterInventory(String characterId) {
        try {
            String requestUrl = "/characters/" + characterId + "/inventory";
            LOG.debug("Will call Items API on URL: {}", requestUrl);

            restTemplate.delete(requestUrl);
            LOG.debug("Deleted all items for character: {}", characterId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    //#endregion items service
    
    //#region spells service
    /**
     * @param spellName
     * @return
     */   @Override
    public Spell getSpell(String spellName) {
       try {
            String requestUrl = spellsServiceUrl + "/" + spellName;
            LOG.debug("Will call Spells API on URL: {}", requestUrl);

            Spell spell = restTemplate.getForObject(requestUrl, Spell.class);
            LOG.debug("Found a spell with name: {}", spell.name);

            return spell;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public List<CharacterSpell> getCharacterSpells(String characterId) {
       try {
            String requestUrl = "/character/" + characterId + "/spells";
            LOG.debug("Will call Spells API on URL: {}", requestUrl);

            List<CharacterSpell> characterSpells = restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<CharacterSpell>>() {
                }).getBody();

            return characterSpells;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void assignSpellToCharacter(String characterId, CharacterSpellAssignmentDto body) {
        try {
            String requestUrl = "/character/" +  characterId + "/spells";
            LOG.debug("Will call Spells API on URL: {}", requestUrl);

            restTemplate.put(requestUrl, body, CharacterSpell.class);
            LOG.debug("Assigned a spell with name: {}", body.spellName);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void removeSpellFromCharacter(String characterId, String spellName) {
        try {
            String requestUrl = "/character/" +  characterId + "/spells/" + spellName;
            LOG.debug("Will call Spells API on URL: {}", requestUrl);

            restTemplate.delete(requestUrl);
            LOG.debug("Removed a spell with name: {}", spellName);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void deleteCharacterSpellRecords(String characterId) {
        try {
            String requestUrl = "/character/" +  characterId + "/spells/";
            LOG.debug("Will call Spells API on URL: {}", requestUrl);

            restTemplate.delete(requestUrl);
            LOG.debug("Deleted all spells for character with id: {}", characterId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }
    //#endregion

    //#region stats service
    public List<Statistic> getStats(String characterId) {
       try {
            String requestUrl = "character/" + characterId + "/stats";

            LOG.debug("Will call Stats API on URL: {}", requestUrl);

            List<Statistic> stats = restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Statistic>>() {
                }).getBody();

            return stats;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public List<Statistic> getStats(String characterId, String statName) {
       try {
            String requestUrl = "character/" + characterId + "/stats?statName=" +  statName;


            LOG.debug("Will call Stats API on URL: {}", requestUrl);

            List<Statistic> stats = restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Statistic>>() {
                }).getBody();

            return stats;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void assignStatsToCharacter(String characterId, StatsAssignmentDto body) {
        try {
            String requestUrl = "character/" + characterId + "/stats";
            LOG.debug("Will call Stats API on URL: {}", requestUrl);

            restTemplate.put(requestUrl, body, Statistic.class);
            LOG.debug("Assigned a stats for character with id: {}", characterId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
        }
    }

    @Override
    public void deleteCharacterStats(String characterId) {
        try {
            String requestUrl = "character/" + characterId + "/stats";
            LOG.debug("Will call Stats API on URL: {}", requestUrl);

            restTemplate.delete(requestUrl);
            LOG.debug("Deleted all stats for character with id: {}", characterId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
        }
    }
    //#endregion

    private String getServiceUrl(String host, int port, String serviceName) {
        return "http://" + host + ":" + port + "/" + serviceName;
    }
}
