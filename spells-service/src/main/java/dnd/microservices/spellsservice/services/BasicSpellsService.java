package dnd.microservices.spellsservice.services;

import dnd.microservices.core.api.spells.Spell;
import dnd.microservices.core.api.spells.SpellsService;
import dnd.microservices.core.utils.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
     * @param characterName
     * @return
     */
    @Override
    public List<Spell> getSpells(String characterName) {
        return new ArrayList<>();
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

    @Override
    public void assignSpellToCharacter(String spellName, String characterName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignSpellToCharacter'");
    }

    @Override
    public void unAssignSpellFromCharacter(String characterName, String spellName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unAssignSpellFromCharacter'");
    }

   
}
