package com.middlewar.core.model.items;

import com.middlewar.core.enums.Slot;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.Stats;

/**
 * @author LEBOC Philippe
 */
public final class Cargo extends SlotItem {
    private long capacity;

    public Cargo(StatsSet set, Requirement req) {
        super(Slot.CARGO, set, req);
        setCapacity(set.getLong("capacity", 0));
        getStats().put(Stats.CARGO, new StatHolder(Stats.CARGO, getCapacity(), StatOp.DIFF));
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}
