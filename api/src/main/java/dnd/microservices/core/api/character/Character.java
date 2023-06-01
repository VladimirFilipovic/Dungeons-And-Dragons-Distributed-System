package dnd.microservices.core.api.character;


public class Character {

    public final String id;
    public final String name;
    public final String race;
    public final String religion;

    public String serviceAddress;


    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Character(
            String name,
            String race,
            String religion) {
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = null;
        this.id = null;
    }

    
    public Character(
            String name,
            String race,
            String religion,
            String serviceAddress
    ) {
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
        this.id = null;
    }

    public Character(
            String id,
            String name,
            String race,
            String religion,
            String serviceAddress
    ) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.religion = religion;
        this.serviceAddress = serviceAddress;
    }



    public Character() {
        this.id = null;
        this.name = null;
        this.race = null;
        this.religion = null;
        this.serviceAddress = null;
    }
}
