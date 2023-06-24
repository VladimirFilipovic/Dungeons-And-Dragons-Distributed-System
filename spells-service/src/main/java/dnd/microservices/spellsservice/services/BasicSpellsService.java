package dnd.microservices.spellsservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.utils.http.ServiceUtil;



@RestController
public class BasicSpellsService implements SpellsService {

    private final ServiceUtil serviceUtil;
    private final RestTemplate restTemplate;
    private static final String API_BASE_URL = "https://www.dnd5eapi.co/api";


    @Autowired
    public BasicSpellsService(ServiceUtil serviceUtil, RestTemplate restTemplate) {
        this.serviceUtil = serviceUtil;
        this.restTemplate = restTemplate;
    }
    
    /**
     * @param spellName
     * @return
     */
    @Override
    public DndSpell getSpell(String spellName) {
        DndSpell dndSpell = this.restTemplate.getForObject(
            API_BASE_URL + "/spells/" + spellName,
            DndSpell.class
        );
        return dndSpell;
    }

   
}
