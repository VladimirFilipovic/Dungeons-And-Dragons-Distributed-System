package dnd.microservices.spellsservice.services;

import org.mapstruct.Mapper;

import dnd.microservices.core.api.dnd5e.DndSpell;
import dnd.microservices.core.api.spells.Spell;

@Mapper(componentModel = "spring")
public interface SpellMapper {
    Spell mapDndSpellToSpell(DndSpell dndSpell);
}
