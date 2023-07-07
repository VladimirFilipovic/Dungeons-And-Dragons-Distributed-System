package dnd.microservices.core.characterservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.event.Event;
import dnd.microservices.core.utils.exceptions.EventProcessingException;


@EnableBinding(Sink.class)
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final BasicCharacterService characterService;

    @Autowired
    public MessageProcessor(BasicCharacterService characterService) {
        this.characterService = characterService;
    }

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, Character> event) {

        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {

        case CREATE:
            dnd.microservices.core.api.character.Character character = event.getData();
            LOG.info("Create character with ID: {}", character.id);
            characterService.createCharacter(character);
            break;

        case DELETE:
            String characterId = event.getKey().toString();
            LOG.info("Delete character with ID: {}", characterId);
            characterService.deleteCharacter(characterId);
            break;

        default:
            String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
            LOG.warn(errorMessage);
            throw new EventProcessingException(errorMessage);
        }

        LOG.info("Message processing done!");
    }
}
