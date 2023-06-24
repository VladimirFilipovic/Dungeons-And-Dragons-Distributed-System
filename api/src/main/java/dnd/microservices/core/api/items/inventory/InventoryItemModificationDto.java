package dnd.microservices.core.api.items.inventory;

public class InventoryItemModificationDto {

    public int itemId;
   
    public int amount;

    public ModificationType modificationType;
   
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ModificationType getModificationType() {
        return modificationType;
    }

    public void setModificationType(ModificationType modificationType) {
        this.modificationType = modificationType;
    }

    public InventoryItemModificationDto() {
    }

    public InventoryItemModificationDto(int itemId, int amount, ModificationType modificationType) {
        this.itemId = itemId;
        this.amount = amount;
        this.modificationType = modificationType;
    }
}