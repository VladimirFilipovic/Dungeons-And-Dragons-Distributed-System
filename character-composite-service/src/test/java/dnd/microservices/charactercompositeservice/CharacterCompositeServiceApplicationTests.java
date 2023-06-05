package dnd.microservices.charactercompositeservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import dnd.microservices.core.api.composite.CharacterComposite;
import static org.springframework.http.HttpStatus.*;


@SpringBootTest
class CharacterCompositeServiceApplicationTests {
	@Autowired
	private WebTestClient client;

	@Test
	void contextLoads() {
	}

// 	@Test
// public void createCharacterComposite1() {
//     CharacterComposite characterComposite = new CharacterComposite(
//             "1", "name", "race", "religion", "serviceAddress",
//             null, null, null
//     );

//     postAndVerifyCharacterComposite(characterComposite, OK);
// }

// @Test
// public void createCharacterComposite2() {
//     CharacterComposite characterComposite = new CharacterComposite(
//             "1", "name", "race", "religion", "serviceAddress",
//             singletonList(new InventoryItem(null, 1)),
//             singletonList(new CharacterSpell(null, 1)),
//             singletonList(new Statistic(null, 1))
//     );

//     postAndVerifyCharacterComposite(characterComposite, OK);
// }

// @Test
// public void deleteCharacterComposite() {
//     CharacterComposite characterComposite = new CharacterComposite(
//             "1", "name", "race", "religion", "serviceAddress",
//             singletonList(new InventoryItem(null, 1)),
//             singletonList(new CharacterSpell(null, 1)),
//             singletonList(new Statistic(null, 1))
//     );

//     postAndVerifyCharacterComposite(characterComposite, OK);

//     deleteAndVerifyCharacterComposite(characterComposite.id, OK);
//     deleteAndVerifyCharacterComposite(characterComposite.id, OK);
// }

// @Test
// public void getCharacterCompositeById() {
//     getAndVerifyCharacterComposite("1", OK)
//         .jsonPath("$.id").isEqualTo("1")
//         .jsonPath("$.items.length()").isEqualTo(0)
//         .jsonPath("$.spells.length()").isEqualTo(0)
//         .jsonPath("$.stats.length()").isEqualTo(0);
// }

// @Test
// public void getCharacterCompositeNotFound() {
//     getAndVerifyCharacterComposite("999", NOT_FOUND)
//         .jsonPath("$.path").isEqualTo("/characters/999")
//         .jsonPath("$.message").isEqualTo("NOT FOUND: 999");
// }

// @Test
// public void getCharacterCompositeInvalidInput() {
//     getAndVerifyCharacterComposite("invalid", UNPROCESSABLE_ENTITY)
//         .jsonPath("$.path").isEqualTo("/characters/invalid")
//         .jsonPath("$.message").isEqualTo("INVALID: invalid");
// }

// private WebTestClient.BodyContentSpec getAndVerifyCharacterComposite(String characterId, HttpStatus expectedStatus) {
//     return client.get()
//         .uri("/characters/" + characterId)
//         .accept(APPLICATION_JSON)
//         .exchange()
//         .expectStatus().isEqualTo(expectedStatus)
//         .expectHeader().contentType(APPLICATION_JSON)
//         .expectBody();
// }

// private void postAndVerifyCharacterComposite(CharacterComposite characterComposite, HttpStatus expectedStatus) {
//     client.post()
//         .uri("/characters")
//         .body(just(characterComposite), CharacterComposite.class)
//         .exchange()
//         .expectStatus().isEqualTo(expectedStatus);
// }

// private void deleteAndVerifyCharacterComposite(String characterId, HttpStatus expectedStatus) {
//     client.delete()
//         .uri("/characters/" + characterId)
//         .exchange()
//         .expectStatus().isEqualTo(expectedStatus);
// }


}
