package dnd.microservices.spellsservice.persistance;

import java.util.HashMap;

import javax.persistence.*;


@Entity
@Table(
    name = "spells", 
    indexes = { @Index(name = "spells-name-index", unique = true, columnList = "name") },
    uniqueConstraints = { @UniqueConstraint(name = "spells-name-unique", columnNames = "name") }
    )
public class SpellEntity {
    
    @Id @GeneratedValue
    private int id;

    @Version
    private int version;

    private String name;
    private String description;
    private int range;
    private String duration;
    private String castingTime;
    private int level;
    private HashMap<String, Integer> damageAtLevel;
    private String serviceAdress;

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public void setCastingTime(String castingTime) {
        this.castingTime = castingTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public HashMap<String, Integer> getDamageAtLevel() {
        return damageAtLevel;
    }

    public void setDamageAtLevel(HashMap<String, Integer> damageAtLevel) {
        this.damageAtLevel = damageAtLevel;
    }

    public String getServiceAdress() {
        return serviceAdress;
    }

    public SpellEntity() {
    }

    public SpellEntity(
            String name,
            String description,
            int range,
            String duration,
            String castingTime,
            int level,
            HashMap<String, Integer> damageAtLevel,
            String serviceAdress
    ) {
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
