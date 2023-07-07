package dnd.microservices.spellsservice.services;   

import org.bson.BasicBSONCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.character.CharacterService;
import dnd.microservices.core.api.event.Event;
import dnd.microservices.core.api.spells.characterSpells.CharacterSpellAssignmentDto;
import dnd.microservices.core.utils.exceptions.EventProcessingException;


@EnableBinding(Sink.class)
public class MessageProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final BasicCharacterSpellsService basicCharacterSpellsService;

    @Autowired
    public MessageProcessor(BasicCharacterSpellsService basicCharacterSpellsService) {
        this.basicCharacterSpellsService = basicCharacterSpellsService;
    }

    @StreamListener(target = Sink.INPUT)
    public void process(Event<Integer, ?> event) {

        LOG.info("Process message created at {}...", event.getEventCreatedAt());

        switch (event.getEventType()) {

        case CREATE:
            String characterId = event.getKey().toString();
            CharacterSpellAssignmentDto characterSpellAssignmentDto = (CharacterSpellAssignmentDto) event.getData();
            LOG.info("Assign character spell for character with ID: {}", characterId);
            basicCharacterSpellsService.assignSpellToCharacter(characterId, characterSpellAssignmentDto);
            break;

        case DELETE:
            String characterId2 = event.getKey().toString();
            String spellName = (String) event.getData();
            LOG.info("Delete character spell for character with ID: {}", characterId2);
            basicCharacterSpellsService.removeSpellFromCharacter(characterId2, spellName);
            break;

        
        case DELETE_SPELL_REC:
            String characterId3 = event.getKey().toString();
            LOG.info("Delete spell records for character with ID: {}", characterId3);
            basicCharacterSpellsService.deleteCharacterSpellRecords(characterId3);
            break;

        default:
            String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE DELETE or DELETE SPELL REC event";
            LOG.warn(errorMessage);
            throw new EventProcessingException(errorMessage);
        }

        LOG.info("Message processing done!");
    }
}
