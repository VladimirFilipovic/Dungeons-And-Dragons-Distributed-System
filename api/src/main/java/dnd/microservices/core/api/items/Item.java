package dnd.microservices.core.api.items;

public class Item {
    private final String id;
    private final String name;
    private final String description;
    
    private  String serviceAddress;

    public String getId() {
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

    public Item(String id, String name, String description, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }

    public Item() {
        this.id = null;
        this.name = null;
        this.description = null;
        this.serviceAddress = null;
    }
}
