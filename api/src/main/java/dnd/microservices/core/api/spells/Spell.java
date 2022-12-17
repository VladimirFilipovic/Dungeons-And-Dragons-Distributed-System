package dnd.microservices.core.api.spells;

import java.util.Map;

public class Spell {
    private final String id;
    private final String name;
    private final String description;
    private final int range;
    private final String duration;
    private final String castingTime;
    private final int level;
    private final Map<String, Integer> damageAtLevel;
    public Spell(String id, String name, String description, int range, String duration, String castingTime, int level, Map<String, Integer> damageAtLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.range = range;
        this.duration = duration;
        this.castingTime = castingTime;
        this.level = level;
        this.damageAtLevel = damageAtLevel;
    }


}
