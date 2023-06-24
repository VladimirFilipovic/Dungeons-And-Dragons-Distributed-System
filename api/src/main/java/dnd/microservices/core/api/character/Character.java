package dnd.microservices.core.api.character;

public class Character {

    public String id;
    public String name;
    public String race;
    public String religion;
    public String serviceAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

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
            String serviceAddress) {
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
            String serviceAddress) {
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
