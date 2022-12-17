package dnd.microservices.core.api.stats;

public class Statistic {
    private final String id;
    private final String name;
    private final int value;

    public Statistic(String id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
