package dnd.microservices.core.api.items.inventory;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import reactor.core.publisher.Flux;

public interface InventoryService {
    
    @GetMapping(
            value    = "/characters/{characterId}/inventory",
            produces = "application/json"
        )
    Flux<InventoryItem> getCharacterInventory(@PathVariable String characterId);
   
    @PutMapping(
        value = "/characters/{characterId}/inventory", 
        consumes = "application/json",
        produces = "application/json"
    )
    void modifyCharacterInventory(@PathVariable String characterId, @RequestBody InventoryItemModificationDto body);

    @PostMapping(
        value = "/characters/{characterId}/inventory", 
        consumes = "application/json",
        produces = "application/json"
    )
    void createCharacterInventory(@PathVariable String characterId);

    @DeleteMapping(
        value = "/characters/{characterId}/inventory"
    ) 
    void deleteCharacterInventory(@PathVariable String characterId);
}
