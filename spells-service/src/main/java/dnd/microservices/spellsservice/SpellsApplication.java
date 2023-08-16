package dnd.microservices.spellsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan("dnd.microservices")
@EnableEurekaClient
public class SpellsApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SpellsApplication.class);


	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(SpellsApplication.class, args);

		String mysqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		LOG.info("Connected to MySQL: " + mysqlUri);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
