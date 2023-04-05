package dnd.microservices.core.api.items.inventory;

public class InventoryItemModificationDto {

    public String itemName;
   
    public int amount;

    public ModificationType modificationType;
   
    public InventoryItemModificationDto() {
    }

    public InventoryItemModificationDto(String itemName, int amount, ModificationType modificationType) {
        this.itemName = itemName;
        this.amount = amount;
        this.modificationType = modificationType;
    }
}