package dnd.microservices.charactercompositeservice.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.api.stats.Statistic;
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
public class IntegrationService implements CharacterService, ItemsService, SpellsService, StatsService {
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
            LOG.debug("Found a product with id: {}", character.getId());

            return character;


        } catch (HttpClientErrorException ex) {

//            HttpStatusCode statusCode = ex.getStatusCode();
//            if (statusCode.equals(NOT_FOUND)) {
//                throw new NotFoundException(getErrorMessage(ex));
//            } else if (statusCode.equals(UNPROCESSABLE_ENTITY)) {
//                throw new InvalidInputException(getErrorMessage(ex));
//            }
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Item> getItems(String characterName) {
        String requestUrl = itemsServiceUrl + "?characterName=" + characterName;
        List<Item> items = restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Item>>() {}
        ).getBody();

        return  items;
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

    /**
     * @param itemName
     * @param inventoryId
     */
    @Override
    public void addItemToInventory(String itemName, String inventoryId) {
        throw new Error("not implemented");
    }

    /**
     * @param itemName
     * @param inventoryId
     */
    @Override
    public void removeItemFromInventory(String itemName, String inventoryId) {
        throw new Error("not implemented");
    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Spell> getSpells(String characterName) {
        String requestUrl = spellsServiceUrl + "?characterName=" + characterName;
        List<Spell> spells = this.restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Spell>>() {}
        ).getBody();

        return spells;
    }

    /**
     * @param spellName
     * @return
     */
    @Override
    public Spell getSpell(String spellName) {
        String requestUrl = spellsServiceUrl + "/" + spellName;
        Spell spell = this.restTemplate.getForObject(requestUrl, Spell.class);

        return  spell;
    }

    /**
     * @param spellName
     * @param characterName
     */
    @Override
    public void assignSpellToCharacter(String spellName, String characterName) {
        throw new Error("not implemented");
    }

    /**
     * @param spellName
     * @param characterName
     */
    @Override
    public void unAssignSpellFromCharacter(String spellName, String characterName) {
        throw new Error("not implemented");
    }

    /**
     * @param statisticName
     * @return
     */
    @Override
    public Statistic getStatistic(String statisticName) {
        String requestUrl = this.statsServiceUrl + "/" + statisticName;
        Statistic statistic = this.restTemplate.getForObject(requestUrl, Statistic.class);

        return  statistic;
    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Statistic> getStats(String characterName) {
        String requestUrl = this.statsServiceUrl + "?characterName=" + characterName;
        List<Statistic> statistics = this.restTemplate.exchange(
                requestUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Statistic>>(){}
        ).getBody();

        return  statistics;
    }

    /**
     * @param characterName
     * @param statisticName
     * @param newValue
     */
    @Override
    public void changeCharacterStats(String characterName, String statisticName, Integer newValue) {
        throw new Error("not implemented");
    }

    private String getServiceUrl(String host, int port, String serviceName) {
        return "http://" + host + ":" + port + "/" + serviceName;
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
