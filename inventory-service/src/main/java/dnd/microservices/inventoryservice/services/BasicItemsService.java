package dnd.microservices.inventoryservice.services;

import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.core.utils.exceptions.NotFoundException;
import dnd.microservices.core.utils.http.ServiceUtil;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class BasicItemsService implements ItemsService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public BasicItemsService(ItemRepository itemRepository, ItemMapper itemMapper, ServiceUtil serviceUtil) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.serviceUtil = serviceUtil;
    }
 
    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Item> getItems() {
        Stream<ItemEntity> itemEntitiesStream = StreamSupport.stream(itemRepository.findAll().spliterator(), false);

        List<Item> items = itemEntitiesStream
                .map(itemMapper::entityToApi)
                .peek(item -> item.setServiceAddress(serviceUtil.getServiceAddress()))
                .collect(Collectors.toList());
    
        return items;
    }

    /**
     * @param itemName
     * @return
     */
    @Override
    public Item getItem(String itemName) {
        ItemEntity itemEntity = itemRepository.findByName(itemName)
          .orElseThrow(() -> new NotFoundException("No item found for name: " + itemName));
        
        Item item = itemMapper.entityToApi(itemEntity);
        item.setServiceAddress(serviceUtil.getServiceAddress());
        
        return item;
    }

 

}
