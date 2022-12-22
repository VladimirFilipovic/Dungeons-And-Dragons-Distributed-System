package dnd.microservices.spellsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dnd.microservices")
public class SpellsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpellsApplication.class, args);
	}

}
