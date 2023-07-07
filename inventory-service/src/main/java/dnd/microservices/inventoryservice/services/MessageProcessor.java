package dnd.microservices.inventoryservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.event.Event;
import dnd.microservices.core.api.items.ItemCreateDto;
import dnd.microservices.core.api.items.inventory.InventoryItemModificationDto;
import dnd.microservices.core.utils.exceptions.EventProcessingException;


@EnableBinding(Sink.class)
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final BasicInventoryService basicInventoryService;
    private final BasicItemsService basicItemsService;

    @Autowired
    public MessageProcessor(BasicInventoryService basicInventoryService, BasicItemsService basicItemsService) {
        this.basicInventoryService = basicInventoryService;
        this.basicItemsService = basicItemsService;
    }
    

    @StreamListener(target = Sink.INPUT)
    public void process(Event<String, ?> event) {

        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {
            case CREATE:
                ItemCreateDto item = (ItemCreateDto) event.getData();
                LOG.info("Create item with name: {}", item.name);
                basicItemsService.createItem(item);
                break;
            case DELETE:
                String itemName = event.getKey().toString();
                LOG.info("Delete item with Name: {}", itemName);
                basicItemsService.deleteItem(itemName);
                break;
            case CREATE_INV:
                String characterId = event.getKey().toString();
                LOG.info("Create inventory for character with id: {}",characterId);
                basicInventoryService.createCharacterInventory(characterId);
                break;
            case DELETE_INV:
                String characterId2 = event.getKey().toString();
                LOG.info("Delete inventory for character with id: {}",characterId2);
                basicInventoryService.deleteCharacterInventory(characterId2);
                break;
            case UPDATE_INV:
                String characterId3 = event.getKey().toString();
                InventoryItemModificationDto invModification = (InventoryItemModificationDto) event.getData();
                LOG.info("Update inventory for character with id: {}",characterId3);
                basicInventoryService.modifyCharacterInventory(characterId3, invModification);
                break;
            default:
                String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                LOG.warn(errorMessage);
                throw new EventProcessingException(errorMessage);
        }

        LOG.info("Message processing done!");
    }
}
