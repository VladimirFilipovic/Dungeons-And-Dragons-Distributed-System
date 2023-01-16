package dnd.microservices.inventoryservice.services;

import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.ItemsService;
import dnd.microservices.core.utils.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class BasicInventoryService implements ItemsService {


    private final ServiceUtil serviceUtil;

    @Autowired
    public BasicInventoryService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }
    
    /**
     * @param itemName
     * @param inventoryId
     */
    @Override
    public void addItemToInventory(String itemName, String inventoryId) {

    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Item> getItems(String characterName) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("itemName", "itemName", 100, "Fake item dude", this.serviceUtil.getServiceAddress()));
        items.add(new Item("itemName", "itemName", 100, "Fake item dude", this.serviceUtil.getServiceAddress()));

        return  items;
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
}
