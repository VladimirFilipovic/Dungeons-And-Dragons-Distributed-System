package dnd.microservices.charactercompositeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.CompositeReactiveHealthIndicator;
import org.springframework.boot.actuate.health.DefaultReactiveHealthIndicatorRegistry;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.ReactiveHealthIndicatorRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import dnd.microservices.charactercompositeservice.services.IntegrationService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.LinkedHashMap;

@SpringBootApplication
@ComponentScan("dnd.microservices")
@EnableSwagger2
@EnableEurekaClient 
public class CharacterCompositeServiceApplication {

	@Value("${api.common.version}")
	String apiVersion;
	@Value("${api.common.title}")
	String apiTitle;
	@Value("${api.common.description}")
	String apiDescription;
	@Value("${api.common.termsOfServiceUrl}")
	String apiTermsOfServiceUrl;
	@Value("${api.common.license}")
	String apiLicense;
	@Value("${api.common.licenseUrl}")
	String apiLicenseUrl;
	@Value("${api.common.contact.name}")
	String apiContactName;
	@Value("${api.common.contact.url}")
	String apiContactUrl;
	@Value("${api.common.contact.email}")
	String apiContactEmail;

	@Bean
	public Docket apiDocumentation() {
		return new Docket(SWAGGER_2)
				.select()
				.apis(basePackage("dnd.microservices"))
				.paths(PathSelectors.any())
				.build()
				.globalResponseMessage(GET, emptyList())
				.apiInfo(new ApiInfo(
						apiTitle,
						apiDescription,
						apiVersion,
						apiTermsOfServiceUrl,
						new Contact(apiContactName, apiContactUrl,
								apiContactEmail),
						apiLicense,
						apiLicenseUrl,
						emptyList()));
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		final WebClient.Builder builder = WebClient.builder();
		return builder;
	}

	@Autowired
	HealthAggregator healthAggregator;

	@Autowired
	IntegrationService integration;
	public static void main(String[] args) {
		SpringApplication.run(CharacterCompositeServiceApplication.class, args);
	}

}
