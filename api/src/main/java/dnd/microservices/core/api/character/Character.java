package dnd.microservices.core.api.character;

public class Character {

    private final String id;
    private final String name;
    private final String race;
    private final String religion;
    private final String serviceAddress;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public String getReligion() {
        return religion;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public Character(String id, String name, String race, String religion, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
    }

}
