package dnd.microservices.core.api.items;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface ItemsService {

    /**
     * Sample usage: curl $HOST:$PORT/items?characterName="Milorad"
     *
     * @param characterName
     * @return
     */
    @GetMapping(
            value    = "/items",
            produces = "application/json")
    List<Item> getItems(@RequestParam(value = "characterName", required = false) String characterName);

    @GetMapping(
            value = "/items/{itemName}",
            produces = "application/json"
    )
    Item getItem(@PathVariable String itemName);

    void addItemToInventory(String itemName, String inventoryId);
    void removeItemFromInventory(String itemName, String inventoryId);
}