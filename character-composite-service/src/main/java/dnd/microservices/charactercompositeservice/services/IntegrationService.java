package dnd.microservices.charactercompositeservice.services;

import static org.springframework.http.HttpMethod.GET;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.api.items.inventory.InventoryService;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpell;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellsService;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsService;

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

    private String itemsServiceHost;

    private int itemsServicePort;

    private String spellsServiceHost;

    private int spellsServicePort;

    private String statsServiceHost;

    private int statsServicePort;


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
        this.itemsServiceHost = itemsServiceHost;
        this.itemsServicePort = itemsServicePort;
        this.spellsServiceHost = spellsServiceHost;
        this.spellsServicePort = spellsServicePort;
        this.statsServiceHost = statsServiceHost;
        this.statsServicePort = statsServicePort;

        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.characterServiceUrl = this.getServiceUrl(characterServiceHost, characterServicePort, "characters");
        this.itemsServiceUrl = this.getServiceUrl(itemsServiceHost, itemsServicePort, "items");
        this.spellsServiceUrl = this.getServiceUrl(spellsServiceHost, spellsServicePort, "spells");
        this.statsServiceUrl = this.getServiceUrl(statsServiceHost, statsServicePort, "characters");
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
    public Item createItem(ItemCreateDto body) {
        try {
            String requestUrl = itemsServiceUrl;
            LOG.debug("Will call Items API on URL: {}", requestUrl);
            Item item = restTemplate.postForObject(requestUrl, body, Item.class);
            LOG.debug("Created an item with name: {}", body.name);
            return item;
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void deleteItem(String itemName) {
        try {
            String requestUrl = itemsServiceUrl + "/" + itemName;
            LOG.debug("Will call Items API on URL: {}", requestUrl);

            restTemplate.delete(requestUrl);
            LOG.debug("Deleted an item with name: {}", itemName);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            throw ex;
        }
    }

    @Override
    public List<InventoryItem> getCharacterInventory(String characterId) {
        String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId + "/inventory";
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
            String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId + "/inventory";
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
    public void createCharacterInventory(String characterId) {
        try {
            String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId + "/inventory";

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the request entity with empty body and headers
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Make the HTTP request
            LOG.debug("Will call Items API on URL: {}", requestUrl);
            ResponseEntity<Void> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, Void.class);
            LOG.debug("Created inventory for character: {}", characterId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public void deleteCharacterInventory(String characterId) {
        try {
            String requestUrl = "http://" + itemsServiceHost + ":" + itemsServicePort + "/characters/" + characterId + "/inventory";
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
    public DndSpell getSpell(String spellName) {
       try {
            String requestUrl = spellsServiceUrl + "/" + spellName;
            LOG.debug("Will call Spells API on URL: {}", requestUrl);

            DndSpell spell = restTemplate.getForObject(requestUrl, DndSpell.class);
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
            String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/" + characterId + "/spells";
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
             String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/" + characterId + "/spells";
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
             String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/" + characterId + "/spells" + "/" + spellName;
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
             String requestUrl = "http://" + spellsServiceHost + ":" + spellsServicePort + "/characters/" + characterId + "/spells" ;
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
            String requestUrl =statsServiceUrl + "/" + characterId + "/stats";

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
            String requestUrl = statsServiceUrl + "/"  + characterId + "/stats?statName=" +  statName;

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
    public void assignStatsToCharacter(String characterId, List<Statistic> body) {
        try {
            String requestUrl = statsServiceUrl + "/" + characterId + "/stats";
            LOG.debug("Will call Stats API on URL: {}", requestUrl);            
            restTemplate.put(requestUrl, body);
            LOG.debug("Assigned a stats for character with id: {}", characterId);
        } catch (HttpClientErrorException ex) {
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
        }
    }

    @Override
    public void deleteCharacterStats(String characterId) {
        try {
            String requestUrl =statsServiceUrl + "/" + characterId + "/stats";
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
