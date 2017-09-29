package com.middlewar.core.model.items;

import com.middlewar.core.enums.Slot;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public final class Structure extends Item {
    private HashMap<Slot, Integer> availablesSlots;

    public Structure(StatsSet set, Requirement req) {
        super(set, req);
        setAvailablesSlots(set);
    }

    public Integer getAvailablesSlotForItem(SlotItem slotItem) {
        return availablesSlots.get(slotItem.getSlotUsed());
    }

    public HashMap<Slot, Integer> getAvailablesSlots() {
        return availablesSlots;
    }

    public void setAvailablesSlots(HashMap<Slot, Integer> availablesSlots) {
        this.availablesSlots = availablesSlots;
    }

    public void setAvailablesSlots(StatsSet set) {
        setAvailablesSlots(new HashMap<>());
        getAvailablesSlots().put(Slot.CARGO, set.getInt("slot_cargo", 0));
        getAvailablesSlots().put(Slot.ENGINE, set.getInt("slot_engine", 0));
        getAvailablesSlots().put(Slot.MODULE, set.getInt("slot_module", 0));
        getAvailablesSlots().put(Slot.WEAPON, set.getInt("slot_weapon", 0));
        getAvailablesSlots().put(Slot.TECHNOLOGY, set.getInt("slot_technology", 0));
    }
}
