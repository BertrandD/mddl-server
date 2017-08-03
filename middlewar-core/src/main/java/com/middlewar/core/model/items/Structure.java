package com.middlewar.core.model.items;

import com.middlewar.core.enums.StructureSlot;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public final class Structure extends Item {
    private HashMap<StructureSlot, Integer> availablesSlots;

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

    public void setAvailablesSlots(StatsSet set) {
        setAvailablesSlots(new HashMap<>());
        getAvailablesSlots().put(StructureSlot.CARGO, set.getInt("slot_cargo", 0));
        getAvailablesSlots().put(StructureSlot.ENGINE, set.getInt("slot_engine", 0));
        getAvailablesSlots().put(StructureSlot.MODULE, set.getInt("slot_module", 0));
        getAvailablesSlots().put(StructureSlot.WEAPON, set.getInt("slot_weapon", 0));
        getAvailablesSlots().put(StructureSlot.TECHNOLOGY, set.getInt("slot_technology", 0));
    }
}
