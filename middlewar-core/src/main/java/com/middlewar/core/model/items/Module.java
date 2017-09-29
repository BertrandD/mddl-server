package com.middlewar.core.model.items;

import com.middlewar.core.enums.Slot;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public final class Module extends SlotItem {
    public Module(StatsSet set, Requirement req) {
        super(Slot.MODULE, set, req);
    }
}
