package com.gameserver.model.items;

import com.gameserver.enums.StructureSlot;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public final class Structure extends Item
{
    private HashMap<StructureSlot, Integer> availablesSlots;
    // private List<Technology> technologies;

    public Structure(StatsSet set, Requirement req) {
        super(set, req);
        setAvailablesSlots(set);
    }

    public HashMap<StructureSlot, Integer> getAvailablesSlots() {
        return availablesSlots;
    }

    public void setAvailablesSlots(HashMap<StructureSlot, Integer> availablesSlots) {
        this.availablesSlots = availablesSlots;
    }

    public void setAvailablesSlots(StatsSet set){
        setAvailablesSlots(new HashMap<>());
        getAvailablesSlots().put(StructureSlot.CARGO, set.getInt("SLOT_CARGO_COUNT", 0));
        getAvailablesSlots().put(StructureSlot.ENGINE, set.getInt("SLOT_ENGINE_COUNT", 0));
        getAvailablesSlots().put(StructureSlot.MODULE, set.getInt("SLOT_MODULE_COUNT", 0));
        getAvailablesSlots().put(StructureSlot.WEAPON, set.getInt("SLOT_WEAPON_COUNT", 0));
        getAvailablesSlots().put(StructureSlot.TECHNOLOGY, set.getInt("SLOT_TECHNOLOGY_COUNT", 0));
    }
}
