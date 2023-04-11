package dnd.microservices.core.api.items.inventory;

public class InventoryItemModificationDto {

    public int itemId;
   
    public int amount;

    public ModificationType modificationType;
   
    public InventoryItemModificationDto() {
    }

    public InventoryItemModificationDto(int itemId, int amount, ModificationType modificationType) {
        this.itemId = itemId;
        this.amount = amount;
        this.modificationType = modificationType;
    }
}