package dnd.microservices.inventoryservice.services;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.inventoryservice.persistance.characterInventory.CharacterInventoryItemEntity;
import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryEntity;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    List<InventoryItem> entityToApi(Set<CharacterInventoryItemEntity> characterInventoryItemEntities);

    @Mappings({
        @Mapping(target = "characterId", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    CharacterInventoryEntity apiToEntity(InventoryItem body);
}