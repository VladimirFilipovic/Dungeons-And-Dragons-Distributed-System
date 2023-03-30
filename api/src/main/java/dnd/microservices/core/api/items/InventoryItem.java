package dnd.microservices.core.api.items;

public class InventoryItemDto {

     public String itemName;
   
    public int amount;

    public ModificationType modificationType;
   
    public InventoryItemDto() {
    }

    public InventoryItemDto(String itemName, int amount, ModificationType modificationType) {
        this.itemName = itemName;
        this.amount = amount;
        this.modificationType = modificationType;
    }
}