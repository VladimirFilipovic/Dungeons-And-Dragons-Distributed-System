package dnd.microservices.spellsservice.persistance;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "spells", indexes = { @Index(name = "spell-name-index", unique = true, columnList = "name") })
public class SpellEntity {
    private long id;
    private String name;
    private String description;
    private int range;
    private String duration;
    private String castingTime;
    private int level;
    private Map<String, Integer> damageAtLevel;
    private String serviceAdress;

    public SpellEntity() {
    }

    public SpellEntity(
            long id,
            String name,
            String description,
            int range,
            String duration,
            String castingTime,
            int level,
            Map<String, Integer> damageAtLevel,
            String serviceAdress
    ) {
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
}
