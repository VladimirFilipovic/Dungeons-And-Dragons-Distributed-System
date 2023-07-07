package dnd.microservices.statsservice.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

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

        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {

            case CREATE:
                String characterId = event.getKey();
                List<Statistic> statistic = (List<Statistic>) event.getData();
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
