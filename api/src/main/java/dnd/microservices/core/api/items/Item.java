package dnd.microservices.core.api.items;

public class Item {
    private final String id;
    private final String name;
    private final int amount;
    private final String description;
    private final String serviceAdress;

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

    public String getServiceAdress() {
        return serviceAdress;
    }

    public Item(String id, String name, int amount, String description, String serviceAdress) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.serviceAdress = serviceAdress;
    }

    public Item() {
        this.id = null;
        this.name = null;
        this.amount = 0;
        this.description = null;
        this.serviceAdress = null;
    }
}
