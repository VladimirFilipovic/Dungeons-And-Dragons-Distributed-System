package dnd.microservices.core.api.items;

public class Item {
    
    public  int id;
    public  String name;
    public  String description;
    public  String serviceAddress;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Item(int id, String name, String description, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }

    public Item(String name, String description, String serviceAddress) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }

    public Item() {
        this.id = 0;
        this.name = null;
        this.description = null;
        this.serviceAddress = null;
    }
}
