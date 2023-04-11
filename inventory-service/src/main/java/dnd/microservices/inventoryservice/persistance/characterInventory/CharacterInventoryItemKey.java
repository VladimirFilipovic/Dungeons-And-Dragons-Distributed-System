package dnd.microservices.inventoryservice.persistance.characterInventory;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class CharacterInventoryItemKey implements Serializable {
    
    @Column(name = "character_inventory_id")
    public String characterInventoryId;

    @Column(name = "item_id")
    public int itemId;

    @Override
    public int hashCode() {
        return characterInventoryId.hashCode() + itemId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharacterInventoryItemKey)) {
            return false;
        }
        CharacterInventoryItemKey key = (CharacterInventoryItemKey) obj;
        return key.characterInventoryId == characterInventoryId && key.itemId == itemId;
    }


    public CharacterInventoryItemKey() {
    }

    public CharacterInventoryItemKey(String characterInventoryId, int itemId) {
        this.characterInventoryId = characterInventoryId;
        this.itemId = itemId;
    }
}
