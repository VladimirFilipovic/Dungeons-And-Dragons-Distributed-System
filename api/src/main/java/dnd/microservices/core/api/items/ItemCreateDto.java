package dnd.microservices.core.api.items;

public class ItemCreateDto {
    public String name;
    public String description;

    public ItemCreateDto() {
    }

    public ItemCreateDto(String name) {
        this.name = name;
        this.description = "";
    }

    public ItemCreateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
