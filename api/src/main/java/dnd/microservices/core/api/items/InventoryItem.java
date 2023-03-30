package dnd.microservices.core.api.items;

public class InventoryItem {

     public String itemName;
   
    public int amount;

    public ModificationType modificationType;
   
    public InventoryItem() {
    }

    public InventoryItem(String itemName, int amount, ModificationType modificationType) {
        this.itemName = itemName;
        this.amount = amount;
        this.modificationType = modificationType;
    }
}