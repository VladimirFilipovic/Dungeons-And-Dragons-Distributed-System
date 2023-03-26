package dnd.microservices.inventoryservice.services;

import dnd.microservices.core.api.items.CharacterInventoryItemDto;
import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.utils.http.ServiceUtil;
import dnd.microservices.inventoryservice.persistance.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class BasicInventoryService implements ItemsService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public BasicInventoryService(ItemRepository itemRepository, ItemMapper itemMapper, ServiceUtil serviceUtil) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.serviceUtil = serviceUtil;
    }
 
    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Item> getItems(String characterName) {
        List<ItemEntity> itemEntities = null;
        if (characterName != null) {
            itemEntities = itemRepository.fi(characterName);
        }
        List<Item> items = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntities) {
            Item item = itemMapper.entityToApi(itemEntity);
            item.setServiceAddress(serviceUtil.getServiceAddress());
            items.add(item);
        }
        return items;
    }

    /**
     * @param itemName
     * @return
     */
    @Override
    public Item getItem(String itemName) {
        return new Item(itemName, itemName, 100, "Fake item dude", this.serviceUtil.getServiceAddress());
    }

    /**
     * @param itemName
     * @param inventoryId
     */
    @Override
    public void removeItemFromInventory(String itemName, String inventoryId) {

    }

    /**
     * @param body
     */
    @Override
    public void addItemToInventory(CharacterInventoryItemDto body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItemToInventory'");
    }
}
