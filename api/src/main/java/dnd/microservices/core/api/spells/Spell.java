package dnd.microservices.core.api.spells;

import java.util.Map;

public class Spell {
    public final String id;
    public final String name;
    public final String description;
    public final int range;
    public final String duration;
    public final String castingTime;
    public final int level;
    public final Map<Integer, Integer> damageAtLevel;
    public final String serviceAdress;

    public Spell(String id, String name, String description, int range, String duration, String castingTime, int level, Map<Integer, Integer> damageAtLevel
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
