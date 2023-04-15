package dnd.microservices.spellsservice.services;

import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.utils.http.ServiceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
public class BasicSpellsService implements SpellsService {

    private final ServiceUtil serviceUtil;
    private final RestTemplate restTemplate;
    private final SpellMapper mapper;
    private static final String API_BASE_URL = "https://www.dnd5eapi.co/api";


    @Autowired
    public BasicSpellsService(ServiceUtil serviceUtil, RestTemplate restTemplate, SpellMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }
    
    /**
     * @param spellName
     * @return
     */
    @Override
    public Spell getSpell(String spellName) {
        DndSpell dndSpell = this.restTemplate.getForObject(
            API_BASE_URL + "/spells/" + spellName,
            DndSpell.class
        );

        return this.mapper.mapDndSpellToSpell(dndSpell);
    }

   
}
