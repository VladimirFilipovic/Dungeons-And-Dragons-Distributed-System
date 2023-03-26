package dnd.microservices.core.api.items;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface ItemsService {

    /**
     * Sample usage: curl $HOST:$PORT/items?characterName="Miroljub"
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

    @PostMapping(
            value = "/items",
            consumes = "application/json",
            produces = "application/json"
    )
    void addItemToInventory(CharacterInventoryItemDto body);

    @DeleteMapping(value = "/items")
    void removeItemFromInventory(@RequestParam(value = "characterName", required = true) String characterName,
                    @RequestParam(value = "itemName", required = true) String itemName);
}