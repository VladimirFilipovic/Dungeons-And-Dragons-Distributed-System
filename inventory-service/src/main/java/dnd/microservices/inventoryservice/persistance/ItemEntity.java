package dnd.microservices.inventoryservice.persistance;

import javax.persistence.*;

@Entity
@Table(name = "items", indexes = { @Index(name = "items_name_index", unique = true, columnList = "name") })
public class ItemEntity {

    @Id @GeneratedValue
    private int id;
    
    @Version
    private int version;

    private String name;
    private int amount;
    private String description;
    private String serviceAddress;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getId() {
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

    public int getVersion() {
        return version;
    }

    public ItemEntity() {
    }

    public ItemEntity(int id, String name, int amount, String description, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }


}
