package dnd.microservices.core.characterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dnd.microservices")
public class CharacterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CharacterServiceApplication.class, args);
	}

}
