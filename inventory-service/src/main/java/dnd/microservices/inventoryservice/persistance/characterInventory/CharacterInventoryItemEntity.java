package dnd.microservices.inventoryservice.persistance.characterInventory;

import javax.persistence.*;

import dnd.microservices.inventoryservice.persistance.inventory.CharacterInventoryEntity;
import dnd.microservices.inventoryservice.persistance.item.ItemEntity;

@Entity
public class CharacterInventoryItemEntity {
    
    @EmbeddedId
    public CharacterInventoryItemKey id;

    @Version
    public int version;
    
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
        this.id = new CharacterInventoryItemKey(characterInventory.characterId, item.id);
        this.characterInventory = characterInventory;
        this.item = item;
        this.quantity = quantity;
    }
}
