
package dnd.microservices.charactercompositeservice;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
			.authorizeExchange()
				.pathMatchers("/actuator/**").permitAll()
				.pathMatchers(POST, "/character-composite/**").hasAuthority("SCOPE_character:write")
				.pathMatchers(DELETE, "/character-composite/**").hasAuthority("SCOPE_character:write")
				.pathMatchers(GET, "/character-composite/**").hasAuthority("SCOPE_character:read")
				.anyExchange().authenticated()
				.and()
			.oauth2ResourceServer()
				.jwt();
		return http.build();
	}
}