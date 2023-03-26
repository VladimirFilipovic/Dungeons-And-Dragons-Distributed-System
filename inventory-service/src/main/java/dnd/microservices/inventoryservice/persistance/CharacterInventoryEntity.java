package dnd.microservices.inventoryservice.persistance;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "character_inventory")
public class CharacterInventoryEntity {
    
    @Id
    public String characterId;
    
    @Version
    public int version;

    @OneToMany(mappedBy = "characterInventory")
    public Set<CharacterInventoryItemEntity> items;

    public String serviceAddress;
    
    public CharacterInventoryEntity() {
    }

    public CharacterInventoryEntity(String characterId, Set<CharacterInventoryItemEntity> items, String serviceAddress) {
        this.characterId = characterId;
        this.items = items;
        this.serviceAddress = serviceAddress;
    }
    
}
