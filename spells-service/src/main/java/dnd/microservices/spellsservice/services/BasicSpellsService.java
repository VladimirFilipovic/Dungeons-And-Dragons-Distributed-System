package dnd.microservices.spellsservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.utils.http.ServiceUtil;
import reactor.core.publisher.Mono;



@RestController
public class BasicSpellsService implements SpellsService {

    private final ServiceUtil serviceUtil;
    private static final String API_BASE_URL = "https://www.dnd5eapi.co/api";
    private final WebClient webClient;


    @Autowired
    public BasicSpellsService(ServiceUtil serviceUtil, WebClient webClient) {
        this.serviceUtil = serviceUtil;
        this.webClient = webClient;
    }
    
    /**
     * @param spellName
     * @return
     */
    @Override
    public Mono<DndSpell> getSpell(String spellName) {
        return webClient.get()
                .uri(API_BASE_URL + "/spells/" + spellName)
                .retrieve()
                .bodyToMono(DndSpell.class);
    }

   
}
