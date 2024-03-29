package dnd.microservices.core.api.items.inventory;


public class InventoryItemDto {
    public  int id;
    
    public int amount;

    public InventoryItemDto() {
    }

    public InventoryItemDto(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
