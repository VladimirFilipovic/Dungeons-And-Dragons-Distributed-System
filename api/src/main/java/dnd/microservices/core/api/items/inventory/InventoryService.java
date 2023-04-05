package dnd.microservices.core.api.items.inventory;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface InventoryService {
    
    @GetMapping(
            value    = "/character-inventory/{character-name}",
            produces = "application/json"
        )
    List<InventoryItem> getCharacterInventory(@PathVariable String characterName);
   
    @PostMapping(
        value = "/character-inventory/{character-name}", 
        consumes = "application/json",
        produces = "application/json"
    )
    void modifyCharacterInventory(@PathVariable String characterName, InventoryItemModificationDto body);
}
