package dnd.microservices.core.api.items.inventory;

public class InventoryCreateDto {
    public String characterId;

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public InventoryCreateDto() {
    }

    public InventoryCreateDto(String characterId) {
        this.characterId = characterId;
    }
}
