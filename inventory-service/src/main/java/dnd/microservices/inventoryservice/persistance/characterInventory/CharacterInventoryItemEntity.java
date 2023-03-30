package dnd.microservices.inventoryservice.persistance.characterInventory;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;

public class CharacterInventoryItemEntity {
    
    @EmbeddedId
    CharacterInventoryItemKey id;
    
    @ManyToOne
    @MapsId("characterInventoryId")
    @JoinColumn(name = "character_inventory_id")
    public CharacterInventoryEntity characterInventory;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    public ItemEntity item;

    public int quantity;

    public CharacterInventoryItemEntity() {
    }

    public CharacterInventoryItemEntity(CharacterInventoryEntity characterInventory, ItemEntity item, int quantity) {
        this.characterInventory = characterInventory;
        this.item = item;
        this.quantity = quantity;
    }
}