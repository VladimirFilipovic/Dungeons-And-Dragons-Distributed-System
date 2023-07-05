package dnd.microservices.charactercompositeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.microservices.core.api.character.Character;
import dnd.microservices.core.api.event.Event;
import static dnd.microservices.charactercompositeservice.IsSameEvent.sameEventExceptCreatedAt;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class IsSameEventTests {

	ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEventObjectCompare() throws JsonProcessingException {

    	// Event #1 and #2 are the same event, but occurs as different times
		// Event #3 and #4 are different events
		Event<Integer, Character> event1 = new Event<Integer, Character> (Event.Type.CREATE, 1, new Character("name", "race", "religion"));
		Event<Integer, Character> event2 = new Event<Integer, Character> (Event.Type.CREATE, 1, new Character("name", "race", "religion"));
		Event<Integer, Character> event3 = new Event<>(Event.Type.DELETE, 1, null);
		Event<Integer, Character> event4 = new Event<>(Event.Type.CREATE, 43, new Character("name", "race12", "religion21"));

		String event1JSon = mapper.writeValueAsString(event1);

		assertThat(event1JSon, is(sameEventExceptCreatedAt(event2)));
		assertThat(event1JSon, not(sameEventExceptCreatedAt(event3)));
		assertThat(event1JSon, not(sameEventExceptCreatedAt(event4)));
    }
}
