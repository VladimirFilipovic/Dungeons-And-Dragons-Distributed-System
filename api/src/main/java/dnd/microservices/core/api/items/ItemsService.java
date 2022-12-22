package dnd.microservices.core.api.items;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


public interface ItemsService {
    @GetMapping(
            value = "/items/{itemName}",
            produces = "application/json"
    )
    Item getItem(@PathVariable String itemName);

    void addItemToInventory(String itemName, String inventoryId);
    void removeItemFromInventory(String itemName, String inventoryId);
}