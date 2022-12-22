package dnd.microservices.spellsservice.services;

import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.utils.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BasicSpellsService implements SpellsService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public BasicSpellsService(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    /**
     * @return
     */
    @Override
    public List<Spell> getSpells() {
        return null;
    }

    /**
     * @param spellName
     * @return
     */
    @Override
    public Spell getSpell(String spellName) {
        return new Spell(
                spellName,
                spellName,
                "Fake spell",
                20,
                "20",
                "20",
                15,
                new HashMap<>(),
                this.serviceUtil.getServiceAddress()
        );
    }

    /**
     * @param spellName
     * @param characterName
     */
    @Override
    public void assignSpellToCharacter(String spellName, String characterName) {
        //TODO
    }

    /**
     * @param spellName
     * @param characterName
     */
    @Override
    public void unAssignSpellFromCharacter(String spellName, String characterName) {
        //TODO
    }
}
