package dnd.microservices.charactercompositeservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
class CharacterCompositeServiceApplicationTests {
	@Autowired
	private WebTestClient client;

	@Test
	void contextLoads() {
	}

}
