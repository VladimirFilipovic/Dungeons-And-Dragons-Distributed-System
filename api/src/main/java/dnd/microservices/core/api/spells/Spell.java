package dnd.microservices.core.api.spells;

import java.util.Map;

public class Spell {
    private final String id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRange() {
        return range;
    }

    public String getDuration() {
        return duration;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public int getLevel() {
        return level;
    }

    public Map<String, Integer> getDamageAtLevel() {
        return damageAtLevel;
    }

    public String getServiceAdress() {
        return serviceAdress;
    }

    private final String name;
    private final String description;
    private final int range;
    private final String duration;
    private final String castingTime;
    private final int level;
    private final Map<String, Integer> damageAtLevel;
    private final String serviceAdress;

    public Spell(String id, String name, String description, int range, String duration, String castingTime, int level, Map<String, Integer> damageAtLevel
                 ,String serviceAdress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.range = range;
        this.duration = duration;
        this.castingTime = castingTime;
        this.level = level;
        this.damageAtLevel = damageAtLevel;
        this.serviceAdress = serviceAdress;
    }

    public Spell() {
        this.id =null;
        this.name = null;
        this.description =null;
        this.range = 0;
        this.duration = null;
        this.castingTime = null;
        this.level = 0;
        this.damageAtLevel = null;
        this.serviceAdress = null;

    }
}
