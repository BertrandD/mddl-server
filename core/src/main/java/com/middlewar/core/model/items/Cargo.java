package com.middlewar.core.model.items;

import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public final class Cargo extends Item
{
    private long capacity;

    public Cargo(StatsSet set, Requirement req)
    {
        super(set, req);
        setCapacity(set.getLong("capacity", 0));
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}