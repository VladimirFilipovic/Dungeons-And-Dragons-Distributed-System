package dnd.microservices.core.api.stats;

public class Statistic {
    public  StatsName name;
    public  int value;

    public StatsName getName() {
        return name;
    }
    public void setName(StatsName name) {
        this.name = name;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
       
    public Statistic() {}

    public Statistic(StatsName name, int value) {
        this.name = name;
        this.value = value;
    }
}
