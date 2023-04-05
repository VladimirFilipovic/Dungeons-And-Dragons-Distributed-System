package dnd.microservices.core.api.items.inventory;

public class InventoryItem {
  
    public String itemName;
   
    public int amount;

    public InventoryItem() {
    }

    public InventoryItem(String itemName, int amount) {
        this.itemName = itemName;
        this.amount = amount;
    }
}
