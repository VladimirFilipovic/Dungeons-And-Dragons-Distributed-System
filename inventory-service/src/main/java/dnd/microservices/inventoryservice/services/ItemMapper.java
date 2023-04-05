package dnd.microservices.inventoryservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import dnd.microservices.core.api.items.Item;
import dnd.microservices.core.api.items.inventory.InventoryItem;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mappings({
        @Mapping(target = "serviceAddress", ignore = true)
    })
    Item entityToApi(ItemEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    ItemEntity apiToEntity(InventoryItem body);
}