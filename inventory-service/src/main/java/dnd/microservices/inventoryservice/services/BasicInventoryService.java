package dnd.microservices.inventoryservice.services;

import java.util.List;
import java.util.stream.StreamSupport;

import dnd.microservices.core.api.items.inventorys.InventoryItem;
import dnd.microservices.core.api.items.inventorys.InventoryService;

public class BasicInventoryService implements InventoryService {

    // private 

    @Override
    public List<InventoryItem> getCharacterInventory(String characterName) {
        
        Stream<InventoryItem> inventoryEntitiesStream = StreamSupport.stream(inventoryRepository.findAll().spliterator(), false);

        List<Item> inventorys = inventoryEntitiesStream
                .map(inventoryMapper::entityToApi)
                .peek(inventory -> inventory.setServiceAddress(serviceUtil.getServiceAddress()))
                .collect(Collectors.toList());
    
        return inventorys;
    }

    @Override
    public void modifyCharacterInventory(String characterName, InventoryItem body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyCharacterInventory'");
    }
    // @Override
    // public void addItemToInventory(InventoryItem body) {
    //         inventoryRepository.findByName(body.inventoryName).ifPresent(inventoryEntity -> {
    //             inventoryEntity.getCharacterNames().add(body.characterName);
    //             inventoryRepository.save(inventoryEntity);
    //           });
    // }

    // @Override
    // public void removeItemFromInventory(String characterName, String inventoryName) {
    //   inventoryRepository.findByName(inventoryName).ifPresent(inventoryEntity -> {
    //     inventoryEntity.getCharacterNames().remove(characterName);
    //     inventoryRepository.save(inventoryEntity);
    //   });
}
