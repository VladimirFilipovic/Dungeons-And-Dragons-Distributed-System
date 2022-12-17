package dnd.microservices.core.api.items;

public class Item {
    private final String id;
    private final String name;
    private final int amount;
    private final String description;

    public Item(String id, String name, int amount, String description) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
    }

}
