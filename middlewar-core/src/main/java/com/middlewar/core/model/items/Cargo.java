package com.middlewar.core.model.items;

import com.middlewar.core.enums.StructureSlotType;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.Stats;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public final class Cargo extends SlotItem {

    private long capacity;

    public Cargo(StatsSet set, Requirement req) {
        super(StructureSlotType.CARGO, set, req);
        setCapacity(set.getLong("capacity", 0));
        getStats().put(Stats.CARGO, new StatHolder(Stats.CARGO, getCapacity(), StatOp.DIFF));
    }
}
