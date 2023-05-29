package dnd.microservices.core.api.items.inventory;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

public interface InventoryService {
    
    @GetMapping(
            value    = "/characters/{characterId}/inventory",
            produces = "application/json"
        )
    List<InventoryItem> getCharacterInventory(@PathVariable String characterId);
   
    @PutMapping(
        value = "/characters/{characterId}/inventory", 
        consumes = "application/json",
        produces = "application/json"
    )
    void modifyCharacterInventory(@PathVariable String characterId, InventoryItemModificationDto body);

    @DeleteMapping(
        value = "/characters/{characterId}/inventory"
    ) 
    void deleteCharacterInventory(@PathVariable String characterId);
}
