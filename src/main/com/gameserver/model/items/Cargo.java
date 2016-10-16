package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

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
