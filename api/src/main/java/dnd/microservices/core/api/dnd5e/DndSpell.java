package dnd.microservices.core.api.dnd5e;

import java.util.List;

public class DndSpell {
    public String index;
    public String name;
    public String url;
    public List<String> desc;
    public List<String> higherLevel;
    public String range;
    public List<String> components;
    public String material;
    public boolean ritual;
    public String duration;
    public boolean concentration;
    public String castingTime;
    public int level;
    public String attackType;
    public DndDamage damage;
    public DndAreaOfEffect areaOfEffect;

    public DndSpell() {
    }


    public DndSpell(String index, String name, String url, List<String> desc, List<String> higherLevel, String range,
            List<String> components, String material, boolean ritual, String duration, boolean concentration,
            String castingTime, int level, String attackType, DndDamage damage, DndAreaOfEffect areaOfEffect) {
        this.index = index;
        this.name = name;
        this.url = url;
        this.desc = desc;
        this.higherLevel = higherLevel;
        this.range = range;
        this.components = components;
        this.material = material;
        this.ritual = ritual;
        this.duration = duration;
        this.concentration = concentration;
        this.castingTime = castingTime;
        this.level = level;
        this.attackType = attackType;
        this.damage = damage;
        this.areaOfEffect = areaOfEffect;
    }
}
