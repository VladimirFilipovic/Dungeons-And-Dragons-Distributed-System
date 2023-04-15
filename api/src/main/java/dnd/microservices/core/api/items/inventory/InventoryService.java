package dnd.microservices.core.api.items.inventory;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface InventoryService {
    
    @GetMapping(
            value    = "/characters/{character-name}/inventory",
            produces = "application/json"
        )
    List<InventoryItem> getCharacterInventory(@PathVariable String characterName);
   
    @PostMapping(
        value = "/characters/{character-name}/inventory", 
        consumes = "application/json",
        produces = "application/json"
    )
    void modifyCharacterInventory(@PathVariable String characterName, InventoryItemModificationDto body);
}
