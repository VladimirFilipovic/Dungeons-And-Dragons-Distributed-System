package dnd.microservices.inventoryservice.persistance.inventory;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;

import dnd.microservices.inventoryservice.persistance.characterInventory.*;


@Entity
@Table(name = "character_inventory")
public class CharacterInventoryEntity {
    
    @Id
    public String characterId;
    
    @Version
    public int version;

    @OneToMany(mappedBy = "characterInventory", fetch = FetchType.EAGER)
    public Set<CharacterInventoryItemEntity> items;

    public String serviceAddress;
    
    public CharacterInventoryEntity() {
    }

    public CharacterInventoryEntity(String characterId) {
        this.characterId = characterId;
    }
    
}
