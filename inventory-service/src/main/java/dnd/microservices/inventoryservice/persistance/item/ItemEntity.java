package dnd.microservices.inventoryservice.persistance.item;

import java.util.Set;

import javax.persistence.*;


import dnd.microservices.inventoryservice.persistance.characterInventory.*;

@Entity
@Table(name = "items", indexes = { @Index(name = "items_name_index", unique = true, columnList = "name") })
public class ItemEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    
    @Version
    public int version;

    public String name;
    public String description;
    public String serviceAddress;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    public Set<CharacterInventoryItemEntity> characterInventories;
    
    public ItemEntity() {
    }

    public ItemEntity(String name, String description, String serviceAddress) {
        this.name = name;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }

}
