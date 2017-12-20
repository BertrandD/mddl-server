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
public final class Module extends SlotItem {
    public Module(StatsSet set, Requirement req) {
        super(StructureSlotType.MODULE, set, req);
    }
}
