package dnd.microservices.core.api.items;

public class CharacterInventoryItemDto {
   public String CharacterId;
   public String ItemId;

    public CharacterInventoryItemDto() {
    }

    public CharacterInventoryItemDto(String characterId, String itemId) {
        CharacterId = characterId;
        ItemId = itemId;
    }
}