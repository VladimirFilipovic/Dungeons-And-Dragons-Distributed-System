package dnd.microservices.statsservice.services;

import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.google.gson.Gson;
import com.google.gson.reflect.*;

import dnd.microservices.core.api.event.Event;
import dnd.microservices.core.api.stats.Statistic;
import dnd.microservices.core.utils.exceptions.EventProcessingException;

@EnableBinding(Sink.class)
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final BasicStatsService statsService;

    @Autowired
    public MessageProcessor(BasicStatsService statsService) {
        this.statsService = statsService;
    }

    @StreamListener(target = Sink.INPUT)
    public void process(Event<String, ?> event) {
        Gson gson = new Gson();
        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {

            case UPDATE:
                String characterId = event.getKey();
                String eventDataJson = gson.toJson(event.getData()); // Convert LinkedHashMap to JSON string

                // Use TypeToken to specify the list's type
                Type statisticListType = new TypeToken<List<Statistic>>() {}.getType();
                List<Statistic> statistic = gson.fromJson(eventDataJson, statisticListType); // Deserialize JSON string to List<Statistic>
                LOG.info("Assign stats to character with ID: {}", characterId);
                statsService.assignStatsToCharacter(characterId, statistic);
                break;

            case DELETE:
                String characterId2 = event.getKey();
                LOG.info("Delete stats for character with ID: {}", characterId2);
                statsService.deleteCharacterStats(characterId2);
                break;

            default:
                String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                LOG.warn(errorMessage);
                throw new EventProcessingException(errorMessage);
        }

        LOG.info("Message processing done!");
    }
}
