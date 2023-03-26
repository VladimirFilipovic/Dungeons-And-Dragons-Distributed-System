package dnd.microservices.inventoryservice.persistance;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CharacterInventoryItemKey implements Serializable {
    
    @Column(name = "character_inventory_id")
    public int characterInventoryId;

    @Column(name = "item_id")
    public int itemId;

    @Override
    public int hashCode() {
        return characterInventoryId + itemId;
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
}
