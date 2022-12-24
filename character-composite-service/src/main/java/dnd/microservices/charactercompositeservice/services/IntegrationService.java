package dnd.microservices.charactercompositeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        return null;
    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Item> getItems(String characterName) {
        return null;
    }

    /**
     * @param itemName
     * @return
     */
    @Override
    public Item getItem(String itemName) {
        return null;
    }

    /**
     * @param itemName
     * @param inventoryId
     */
    @Override
    public void addItemToInventory(String itemName, String inventoryId) {

    }

    /**
     * @param itemName
     * @param inventoryId
     */
    @Override
    public void removeItemFromInventory(String itemName, String inventoryId) {

    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Spell> getSpells(String characterName) {
        return null;
    }

    /**
     * @param spellName
     * @return
     */
    @Override
    public Spell getSpell(String spellName) {
        return null;
    }

    /**
     * @param spellName
     * @param characterName
     */
    @Override
    public void assignSpellToCharacter(String spellName, String characterName) {

    }

    /**
     * @param spellName
     * @param characterName
     */
    @Override
    public void unAssignSpellFromCharacter(String spellName, String characterName) {

    }

    /**
     * @param statisticName
     * @return
     */
    @Override
    public Statistic getStatistic(String statisticName) {
        return null;
    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Statistic> getStats(String characterName) {
        return null;
    }

    /**
     * @param characterName
     * @param statisticName
     * @param newValue
     */
    @Override
    public void changeCharacterStats(String characterName, String statisticName, Integer newValue) {

    }

    private String getServiceUrl(String host, int port, String serviceName) {
        return "http://" + host + ":" + port + "/product/";
    }
}
