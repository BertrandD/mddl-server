package com.middlewar.core.model.items;

import com.middlewar.core.enums.StructureSlotType;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

import static com.middlewar.core.enums.StructureSlotType.CARGO;
import static com.middlewar.core.enums.StructureSlotType.ENGINE;
import static com.middlewar.core.enums.StructureSlotType.MODULE;
import static com.middlewar.core.enums.StructureSlotType.TECHNOLOGY;
import static com.middlewar.core.enums.StructureSlotType.WEAPON;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public final class Structure extends Item {

    private Map<StructureSlotType, Integer> availablesSlots;

    public Structure(StatsSet set, Requirement req) {
        super(set, req);
        setAvailablesSlots(set);
    }

    public Integer getAvailablesSlot(SlotItem slotItem) {
        return availablesSlots.get(slotItem.getSlotUsed());
    }

    public void setAvailablesSlots(StatsSet set) {
        this.availablesSlots = new EnumMap<>(StructureSlotType.class);
        getAvailablesSlots().put(CARGO, set.getInt("slot_cargo", 0));
        getAvailablesSlots().put(ENGINE, set.getInt("slot_engine", 0));
        getAvailablesSlots().put(MODULE, set.getInt("slot_module", 0));
        getAvailablesSlots().put(WEAPON, set.getInt("slot_weapon", 0));
        getAvailablesSlots().put(TECHNOLOGY, set.getInt("slot_technology", 0));
    }
}
