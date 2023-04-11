package dnd.microservices.core.api.items.inventory;

import dnd.microservices.core.api.items.Item;

public class InventoryItem {
  
    //TODO: ideally item dto without id
    public Item item;
   
    public int amount;

    public InventoryItem() {
    }

    public InventoryItem(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }
}
