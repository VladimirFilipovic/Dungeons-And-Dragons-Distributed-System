package dnd.microservices.inventoryservice.persistance;

import javax.persistence.*;

@Entity
@Table(name = "items", indexes = { @Index(name = "items_name_index", unique = true, columnList = "name") })
public class ItemEntity {

    @Id @GeneratedValue
    private String id;
    private String name;
    private int amount;
    private String description;
    private String serviceAddress;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public ItemEntity(String id, String name, int amount, String description, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }


}
