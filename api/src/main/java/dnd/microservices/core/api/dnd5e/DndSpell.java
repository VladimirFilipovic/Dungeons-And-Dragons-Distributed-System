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
}
