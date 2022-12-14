package dnd.microservices.core.api.stats;

public class Statistic {

    public String getServiceAddress() {
        return serviceAddress;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    private final String id;
    private final String name;
    private final int value;
    private final String serviceAddress;


    public Statistic(String id, String name, int value, String serviceAddress) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.serviceAddress = serviceAddress;
    }
}
