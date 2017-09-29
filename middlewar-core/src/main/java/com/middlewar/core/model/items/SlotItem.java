package com.middlewar.core.model.items;

import com.middlewar.core.enums.Slot;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public abstract class SlotItem extends Item {
    private Slot slotUsed;

    public SlotItem(Slot slotUsed, StatsSet set, Requirement requirement) {
        super(set, requirement);
        setSlotUsed(slotUsed);
    }
}