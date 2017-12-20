package com.middlewar.core.model.items;

import com.middlewar.core.enums.StructureSlotType;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public abstract class SlotItem extends Item {

    private StructureSlotType slotUsed;

    public SlotItem(StructureSlotType slotUsed, StatsSet set, Requirement requirement) {
        super(set, requirement);
        setSlotUsed(slotUsed);
    }
}
