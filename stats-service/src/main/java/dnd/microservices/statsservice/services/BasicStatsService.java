package dnd.microservices.statsservice.services;

import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.api.stats.StatsService;
import dnd.microservices.core.utils.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BasicStatsService implements StatsService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public BasicStatsService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    /**
     * @param statisticName
     * @return
     */
    @Override
    public Statistic getStatistic(String statisticName) {
        return new Statistic("1", "strength", 0, this.serviceUtil.getServiceAddress());
    }

    /**
     * @param characterName
     * @return
     */
    @Override
    public List<Statistic> getStats(String characterName) {
       return new ArrayList<>();
    }


    /**
     * @param characterName
     * @param statisticName
     * @param newValue
     */
    @Override
    public void changeCharacterStats(String characterName, String statisticName, Integer newValue) {

    }
}
