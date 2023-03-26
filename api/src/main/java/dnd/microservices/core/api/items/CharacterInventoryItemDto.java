package dnd.microservices.core.api.items;

public class CharacterInventoryItemDto {
   public String characterName;
   public String itemName;

    public CharacterInventoryItemDto() {
    }

    public CharacterInventoryItemDto(String characterName, String itemName) {
        this.characterName = characterName;
        this.itemName = itemName;
    }
}